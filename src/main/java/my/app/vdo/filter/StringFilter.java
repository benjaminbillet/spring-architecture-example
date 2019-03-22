package my.app.vdo.filter;

import java.util.Objects;

public class StringFilter extends Filter<String> {

  private static final long serialVersionUID = -1994403528954465646L;

  // TODO also regexes?
  private String contains;
  private String notContains;

  public String getContains() {
    return contains;
  }

  public void setContains(String contains) {
    this.contains = contains;
  }

  public String getNotContains() {
    return this.notContains;
  }

  public void setNotContains(String notContains) {
    this.notContains = notContains;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof StringFilter)) {
      return false;
    }
    StringFilter stringFilter = (StringFilter) o;
    return Objects.equals(contains, stringFilter.contains) && Objects.equals(notContains, stringFilter.notContains);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contains, notContains);
  }

  @Override
  public String toString() {
    return getFilterName() + "{" + " contains='" + getContains() + "'" + " notContains='" + getNotContains()
        + "'" + "}";
  }
}
