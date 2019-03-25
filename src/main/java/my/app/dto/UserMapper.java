package my.app.dto;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import my.app.domain.Authority;
import my.app.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {

  UserDto toDto(User user);

  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "lastModifiedBy", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "activationKey", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "activated", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  User toEntity(UserDto dto);

  default Set<Authority> authoritiesFromStrings(Set<String> authorities) {
    return authorities.stream().map(Authority::of).collect(Collectors.toSet());
  }

  default Set<String> authoritiesToString(Set<Authority> authorities) {
    return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
  }
}
