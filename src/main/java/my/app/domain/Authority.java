package my.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

// authority = a spring security role
@Entity
@Table(name = "authority")
public class Authority implements Serializable {
  private static final long serialVersionUID = 1311098324949590322L;

  @NotNull
  @Size(max = 50)
  @Id
  @Column(length = 50)
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static Authority of(String name) {
    Authority authority = new Authority();
    authority.setName(name);
    return authority;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Authority)) {
      return false;
    }
    Authority authority = (Authority) o;
    return Objects.equals(name, authority.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "{" + " name='" + getName() + "'" + "}";
  }
}
