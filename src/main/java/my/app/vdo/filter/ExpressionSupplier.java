package my.app.vdo.filter;

import java.util.function.Function;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public interface ExpressionSupplier<T, X> extends Function<Root<T>, Expression<X>> {

}

