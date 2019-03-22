package my.app.vdo.filter;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class Filter<T> implements Serializable {

  private static final long serialVersionUID = 1772583975688040162L;

  private T equalsTo;
  private Boolean empty;
  private Collection<T> in;

  public T getEqualsTo() {
    return equalsTo;
  }

  public void setEqualsTo(T equals) {
    this.equalsTo = equals;
  }

  public Boolean isEmpty() {
    return this.empty;
  }

  public Boolean getEmpty() {
    return this.empty;
  }

  public void setEmpty(Boolean empty) {
    this.empty = empty;
  }

  public Collection<T> getIn() {
    return in;
  }

  public void setIn(Collection<T> in) {
    this.in = in;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Filter)) {
        return false;
    }
    Filter<?> filter = (Filter<?>) o;
    return Objects.equals(equalsTo, filter.equalsTo) && Objects.equals(empty, filter.empty) && Objects.equals(in, filter.in);
  }

  @Override
  public int hashCode() {
    return Objects.hash(equalsTo, empty, in);
  }

  @Override
  public String toString() {
    return getFilterName() + "{" +
      " equalsTo='" + equalsTo + "'" +
      ", empty='" + empty + "'" +
      ", in='" + in + "'" +
      "}";
  }

  public String getFilterName() {
    return getClass().getSimpleName();
  }
}
