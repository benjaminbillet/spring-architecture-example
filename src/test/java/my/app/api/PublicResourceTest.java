package my.app.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import my.app.App;
import my.app.config.ApplicationProperties;
import my.app.config.ExceptionTranslator;
import my.app.domain.PublicResource;
import my.app.dto.PublicResourceDto;
import my.app.repository.PublicResourceRepository;
import my.app.service.PublicResourceService;
import my.app.util.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class PublicResourceTest {

  @Autowired
  private ApplicationProperties config;

  @Autowired
  private PublicResourceRepository publicResourceRepository;

  @Autowired
  private PublicResourceService publicResourceService;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;


  @Test
  @Transactional
  public void testCreate() throws Exception {
    int nbResourcesBefore = publicResourceRepository.findAll().size();

    PublicResourceDto dto = new PublicResourceDto();
    dto.setName("My resource");
    dto.setDescription("Description of my resource");

    getMockEndpoint().perform(post("/api/public/resources")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(JsonUtil.toJsonBytes(dto)))
      .andExpect(status().isCreated());

    List<PublicResource> resources = publicResourceRepository.findAll();
    assertThat(resources).hasSize(nbResourcesBefore + 1);

    PublicResource resource = publicResourceRepository.findAllByName("My resource").get(0);
    assertThat(resource.getName()).isEqualTo(dto.getName());
    assertThat(resource.getDescription()).isEqualTo(dto.getDescription());
  }

  @Test
  @Transactional
  public void testFilterAndPagination() throws Exception {
    List<PublicResource> resources = new ArrayList<>();
    for (int i = 1; i <= 20; i++) {
      PublicResource resource = new PublicResource();
      resource.setName("My resource " + i);
      resource.setDescription("Value " + (i % 2));
      resources.add(resource);
    }
    publicResourceRepository.saveAll(resources);
    publicResourceRepository.flush();

    getMockEndpoint().perform(get("/api/public/resources?size=5&description.contains=0"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
      .andExpect(jsonPath("$.length()").value(5))
      .andExpect(jsonPath("$.[*].description").value(hasItem("Value 0")));
  }

  private MockMvc getMockEndpoint() {
    PublicResourceEndpoint endpoint = new PublicResourceEndpoint(config, publicResourceService);
    return MockMvcBuilders.standaloneSetup(endpoint)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setControllerAdvice(exceptionTranslator)
      .setMessageConverters(jacksonMessageConverter)
      .build();
  }
}
