package my.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import my.app.domain.PublicResource;
import my.app.dto.PublicResourceDto;
import my.app.dto.PublicResourceMapper;
import my.app.repository.PublicResourceRepository;
import my.app.vdo.filter.PublicResourceFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PublicResourceService {

  private final Logger log = LoggerFactory.getLogger(PublicResourceService.class);

  private final PublicResourceRepository resourceRepository;

  private final PublicResourceMapper resourceMapper;

  public PublicResourceService(PublicResourceRepository resourceRepository, PublicResourceMapper resourceMapper) {
    this.resourceRepository = resourceRepository;
    this.resourceMapper = resourceMapper;
  }


  public PublicResourceDto save(PublicResourceDto dto) {
    log.debug("Request to save PublicResource : {}", dto);
    PublicResource resource = resourceMapper.toEntity(dto);
    resource = resourceRepository.save(resource);
    return resourceMapper.toDto(resource);
  }

  @Transactional(readOnly = true)
  public Page<PublicResourceDto> findAll(Pageable pageable) {
    log.debug("Request to get all PublicResources");
    return resourceRepository.findAll(pageable)
      .map(resourceMapper::toDto);
  }

  @Transactional(readOnly = true)
  public Page<PublicResourceDto> findAll(PublicResourceFilter filter, Pageable pageable) {
    log.info("Request to filter PublicResources: {}", filter);
    Specification<PublicResource> specification = filter.toSpecification();
    return resourceRepository.findAll(specification, pageable)
      .map(resourceMapper::toDto);
  }


  @Transactional(readOnly = true)
  public Optional<PublicResourceDto> findOne(Long id) {
    log.debug("Request to get PublicResource : {}", id);
    return resourceRepository.findById(id)
      .map(resourceMapper::toDto);
  }

  public void delete(Long id) {
    log.debug("Request to delete PublicResource : {}", id);
    resourceRepository.deleteById(id);
  }
}
