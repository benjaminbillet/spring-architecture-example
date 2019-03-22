package my.app.vdo.filter;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

import my.app.domain.PublicResource;
import my.app.domain.PublicResource_; // this class is autogenerated by the Hibernate JPA 2 metamodel generator

public class PublicResourceFilter implements Serializable {
  private static final long serialVersionUID = 7026184814741731302L;

  private StringFilter name;
  private StringFilter description;

  public StringFilter getName() {
    return name;
  }

  public void setName(StringFilter name) {
    this.name = name;
  }

  public StringFilter getDescription() {
    return description;
  }

  public void setDescription(StringFilter description) {
    this.description = description;
  }

  public Specification<PublicResource> toSpecification() {
    Specification<PublicResource> specification = Specification.where(null);
    FilterMapper<PublicResource> mapper = new FilterMapper<>();
    if (getName() != null) {
      specification = specification.and(mapper.buildStringFilterSpecification(getName(), PublicResource_.name));
    }
    if (getDescription() != null) {
      specification = specification.and(mapper.buildStringFilterSpecification(getDescription(), PublicResource_.description));
    }
    return specification;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PublicResourceFilter that = (PublicResourceFilter) o;
    return Objects.equals(name, that.name) && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  @Override
  public String toString() {
    return "PublicResourceFilter{" + (name != null ? "name=" + name + ", " : "")
        + (description != null ? "description=" + description + ", " : "") + "}";
  }
}
