package my.app.dto;

import org.mapstruct.Mapper;

import my.app.domain.PublicResource;

@Mapper(componentModel = "spring")
public interface PublicResourceMapper extends EntityMapper<PublicResourceDto, PublicResource> {

  PublicResourceDto toDto(PublicResource resource);

  PublicResource toEntity(PublicResourceDto dto);
}
