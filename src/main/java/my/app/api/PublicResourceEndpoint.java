package my.app.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import my.app.dto.PublicResourceDto;
import my.app.service.PublicResourceService;
import my.app.util.RestUtil;

@RestController
@RequestMapping("/api/public")
public class PublicResourceEndpoint {
  private final Logger log = LoggerFactory.getLogger(PublicResourceEndpoint.class);

  private final PublicResourceService publicResourceService;

  public PublicResourceEndpoint(PublicResourceService publicResourceService) {
    this.publicResourceService = publicResourceService;
  }

  @PostMapping("/resources")
  public ResponseEntity<PublicResourceDto> createPublicResource(@RequestBody PublicResourceDto dto) throws URISyntaxException {
    log.debug("POST /resources - {}", dto);
    if (dto.getId() != null) {
      throw new IllegalArgumentException("A new resource cannot already have an id");
    }
    PublicResourceDto result = publicResourceService.save(dto);
    return ResponseEntity.created(new URI("/api/public/resources/" + result.getId())).body(result);
  }

  @PutMapping("/resources")
  public ResponseEntity<PublicResourceDto> updatePublicResource(@RequestBody PublicResourceDto dto) throws URISyntaxException {
    log.debug("PUT /resources - {}", dto);
    if (dto.getId() == null) {
      throw new IllegalArgumentException("Invalid resource id");
    }
    PublicResourceDto result = publicResourceService.save(dto);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/resources")
  public ResponseEntity<List<PublicResourceDto>> getAllPublicResources(Pageable pageable) {
    log.debug("GET /resources - {}", pageable);
    Page<PublicResourceDto> page = publicResourceService.findAll(pageable);
    return ResponseEntity.ok(page.getContent());
  }

  @GetMapping("/resources/{id}")
  public ResponseEntity<PublicResourceDto> getPublicResource(@PathVariable Long id) {
    log.debug("GET /resources/{}", id);
    Optional<PublicResourceDto> dto = publicResourceService.findOne(id);
    return RestUtil.toResponseEntity(dto);
  }

  @DeleteMapping("/resources/{id}")
  public ResponseEntity<Void> deletePublicResource(@PathVariable Long id) {
    log.debug("DELETE /resources/{}", id);
    publicResourceService.delete(id);
    return ResponseEntity.ok().build();
  }
}
