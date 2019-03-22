package my.app.vdo.filter;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;

public class FilterMapper<T> {

  protected <X> Specification<T> buildFilterSpecification(Filter<X> filter, SingularAttribute<? super T, X> field) {
    return buildFilterSpecification(filter, root -> root.get(field));
  }

  protected <X> Specification<T> buildFilterSpecification(Filter<X> filter, ExpressionSupplier<T, X> supplier) {
    if (filter.getEqualsTo() != null) {
      return getEqualsSpecification(supplier, filter.getEqualsTo());
    } else if (filter.getIn() != null) {
      return getValueInSpecification(supplier, filter.getIn());
    } else if (filter.isEmpty() != null) {
      return getEmptySpecification(supplier, filter.isEmpty());
    }
    return null;
  }

  public Specification<T> buildStringFilterSpecification(StringFilter filter,
      SingularAttribute<? super T, String> field) {
    return buildStringFilterSpecification(filter, root -> root.get(field));
  }

  public Specification<T> buildStringFilterSpecification(StringFilter filter, ExpressionSupplier<T, String> supplier) {
    if (filter.getEqualsTo() != null) {
      return getEqualsSpecification(supplier, filter.getEqualsTo());
    } else if (filter.getIn() != null) {
      return getValueInSpecification(supplier, filter.getIn());
    } else if (filter.getContains() != null) {
      return getLikeUpperSpecification(supplier, filter.getContains());
    } else if (filter.isEmpty() != null) {
      return getEmptySpecification(supplier, filter.isEmpty());
    }
    return null;
  }

  protected <X extends Comparable<? super X>> Specification<T> buildComparableFilterSpecification(
      ComparableFilter<X> filter, SingularAttribute<? super T, X> field) {
    return buildComparableFilterSpecification(filter, root -> root.get(field));
  }

  protected <X extends Comparable<? super X>> Specification<T> buildComparableFilterSpecification(
      ComparableFilter<X> filter, ExpressionSupplier<T, X> supplier) {
    if (filter.getEqualsTo() != null) {
      return getEqualsSpecification(supplier, filter.getEqualsTo());
    } else if (filter.getIn() != null) {
      return getValueInSpecification(supplier, filter.getIn());
    }

    Specification<T> result = Specification.where(null);
    if (filter.isEmpty() != null) {
      result = result.and(getEmptySpecification(supplier, filter.isEmpty()));
    }
    if (filter.getGreaterThan() != null) {
      result = result.and(getGreaterThanSpecification(supplier, filter.getGreaterThan()));
    }
    if (filter.getGreaterOrEqualThan() != null) {
      result = result.and(getGreaterThanSpecification(supplier, filter.getGreaterOrEqualThan()));
    }
    if (filter.getLessThan() != null) {
      result = result.and(getLessThanSpecification(supplier, filter.getLessThan()));
    }
    if (filter.getLessOrEqualThan() != null) {
      result = result.and(getLessThanOrEqualSpecification(supplier, filter.getLessOrEqualThan()));
    }
    return result;
  }

  public <X> Specification<T> getEqualsSpecification(ExpressionSupplier<T, X> supplier, X value) {
    return (root, query, builder) -> builder.equal(supplier.apply(root), value);
  }

  public Specification<T> getLikeUpperSpecification(ExpressionSupplier<T, String> supplier, String value) {
    String uppercaseValue = "%" + value.toUpperCase() + "%";
    return (root, query, builder) -> builder.like(builder.upper(supplier.apply(root)), uppercaseValue);
  }

  public Specification<T> getNotLikeUpperSpecification(ExpressionSupplier<T, String> supplier, String value) {
    String uppercaseValue = "%" + value.toUpperCase() + "%";
    return (root, query, builder) -> builder.notLike(builder.upper(supplier.apply(root)), uppercaseValue);
  }

  public <X> Specification<T> getValueInSpecification(ExpressionSupplier<T, X> supplier, Collection<X> values) {
    return (root, query, builder) -> {
      In<X> in = builder.in(supplier.apply(root));
      for (X value : values) {
        in = in.value(value);
      }
      return in;
    };
  }

  public <X> Specification<T> getEmptySpecification(ExpressionSupplier<T, X> supplier, boolean empty) {
    if (empty) {
      return (root, query, builder) -> builder.isNull(supplier.apply(root));
    }
    return (root, query, builder) -> builder.isNotNull(supplier.apply(root));
  }

  public <X extends Comparable<? super X>> Specification<T> getGreaterThanOrEqualSpecification(
      ExpressionSupplier<T, X> supplier, X value) {
    return (root, query, builder) -> builder.greaterThanOrEqualTo(supplier.apply(root), value);
  }

  public <X extends Comparable<? super X>> Specification<T> getGreaterThanSpecification(
      ExpressionSupplier<T, X> supplier, X value) {
    return (root, query, builder) -> builder.greaterThan(supplier.apply(root), value);
  }

  public <X extends Comparable<? super X>> Specification<T> getLessThanOrEqualSpecification(
      ExpressionSupplier<T, X> supplier, X value) {
    return (root, query, builder) -> builder.lessThanOrEqualTo(supplier.apply(root), value);
  }

  public <X extends Comparable<? super X>> Specification<T> getLessThanSpecification(ExpressionSupplier<T, X> supplier,
      X value) {
    return (root, query, builder) -> builder.lessThan(supplier.apply(root), value);
  }
}
