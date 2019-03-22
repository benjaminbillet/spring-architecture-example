package my.app.dto;

import java.io.Serializable;
import java.util.Objects;

public class PublicResourceDto implements Serializable {

  private static final long serialVersionUID = -4155145456448846329L;

  public static final String ENTITY_NAME = "public-resource";

  private Long id;

  private String name;

  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PublicResourceDto dto = (PublicResourceDto) o;
    if (dto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), dto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "PublicResourceDto{" + "id=" + getId() + ", name='" + getName() + "'" + ", description='"
      + getDescription() + "'" + "}";
  }
}
