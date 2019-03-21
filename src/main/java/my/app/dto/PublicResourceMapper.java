package my.app.dto;

import org.springframework.stereotype.Component;

import my.app.domain.PublicResource;

// make the mapper injectable
@Component
public class PublicResourceMapper {
  public PublicResourceDto toDto(PublicResource resource) {
    PublicResourceDto dto = new PublicResourceDto();
    dto.setId(resource.getId());
    dto.setName(resource.getName());
    dto.setDescription(resource.getDescription());
    return dto;
  }

  public PublicResource toEntity(PublicResourceDto dto) {
    PublicResource resource = new PublicResource();
    resource.setId(dto.getId());
    resource.setName(dto.getName());
    resource.setDescription(dto.getDescription());
    return resource;
  }
}
