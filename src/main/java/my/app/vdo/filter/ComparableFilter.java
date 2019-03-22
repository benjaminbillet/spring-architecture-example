package my.app.vdo.filter;

import java.util.Objects;

public class ComparableFilter<T extends Comparable<? super T>> extends Filter<T> {

  private static final long serialVersionUID = -3023952855305991004L;

  private T greaterThan;
  private T lessThan;
  private T greaterOrEqualThan;
  private T lessOrEqualThan;

  public T getGreaterThan() {
    return greaterThan;
  }

  public void setGreaterThan(T greaterThan) {
    this.greaterThan = greaterThan;
  }

  public T getGreaterOrEqualThan() {
    return greaterOrEqualThan;
  }

  public void setGreaterOrEqualThan(T greaterOrEqualThan) {
    this.greaterOrEqualThan = greaterOrEqualThan;
  }

  public T getLessThan() {
    return lessThan;
  }

  public void setLessThan(T lessThan) {
    this.lessThan = lessThan;
  }

  public T getLessOrEqualThan() {
    return lessOrEqualThan;
  }

  public void setLessOrEqualThan(T lessOrEqualThan) {
    this.lessOrEqualThan = lessOrEqualThan;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ComparableFilter)) {
      return false;
    }
    ComparableFilter<?> comparableFilter = (ComparableFilter<?>) o;
    return Objects.equals(greaterThan, comparableFilter.greaterThan) && Objects.equals(lessThan, comparableFilter.lessThan) && Objects.equals(greaterOrEqualThan, comparableFilter.greaterOrEqualThan) && Objects.equals(lessOrEqualThan, comparableFilter.lessOrEqualThan);
  }

  @Override
  public int hashCode() {
    return Objects.hash(greaterThan, lessThan, greaterOrEqualThan, lessOrEqualThan);
  }

  @Override
  public String toString() {
    return getFilterName() + "{" +
      " greaterThan='" + getGreaterThan() + "'" +
      ", lessThan='" + getLessThan() + "'" +
      ", greaterOrEqualThan='" + getGreaterOrEqualThan() + "'" +
      ", lessOrEqualThan='" + getLessOrEqualThan() + "'" +
      "}";
  }

}
