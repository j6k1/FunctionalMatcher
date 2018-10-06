package FunctionalMatcher;

import java.util.function.UnaryOperator;

public class RecusiveMatcherCreator<T> {
	public static <T> IMatcher<T> of(UnaryOperator<IMatcher<T>> f)
	{
		if(f == null) {
			throw new NullReferenceNotAllowedException("A NULL value was passed as a reference to IMacher object.");
		}

		IRecusive<IMatcher<T>> r = rr -> {
			return f.apply((state) -> rr.apply(rr).match(state));
		};

		return r.apply(r);
	}

	public static <T> IListMatcher<T> ofl(UnaryOperator<IListMatcher<T>> f)
	{
		if(f == null) {
			throw new NullReferenceNotAllowedException("A NULL value was passed as a reference to IListMacher object.");
		}

		IRecusive<IListMatcher<T>> r = rr -> {
			return f.apply((state) -> rr.apply(rr).matchl(state));
		};

		return r.apply(r);
	}

	public static <T> IContinuationMatcher<T> ofc(UnaryOperator<IContinuationMatcher<T>> f)
	{
		if(f == null) {
			throw new NullReferenceNotAllowedException("A NULL value was passed as a reference to IContinuationMatcher object.");
		}

		IRecusive<IContinuationMatcher<T>> r = rr -> {
			return f.apply((state) -> rr.apply(rr).matchc(state));
		};

		return r.apply(r);
	}
}
