package FunctionalMatcher;

import java.util.function.UnaryOperator;

public class RecusiveMatcherCreator<T> {
	public static <T> IMatcher<T> of(UnaryOperator<IMatcher<T>> f)
	{
		IRecusive<IMatcher<T>> r = rr -> {
			return f.apply((str, start, temporary) -> rr.apply(rr).match(str, start, temporary));
		};

		return r.apply(r);
	}

	public static <T> IListMatcher<T> ofl(UnaryOperator<IListMatcher<T>> f)
	{
		IRecusive<IListMatcher<T>> r = rr -> {
			return f.apply((str, start, temporary) -> rr.apply(rr).matchl(str, start, temporary));
		};

		return r.apply(r);
	}

	public static <T> IContinuationMatcher<T> ofc(UnaryOperator<IContinuationMatcher<T>> f)
	{
		IRecusive<IContinuationMatcher<T>> r = rr -> {
			return f.apply((str, start, temporary) -> rr.apply(rr).matchc(str, start, temporary));
		};

		return r.apply(r);
	}
}
