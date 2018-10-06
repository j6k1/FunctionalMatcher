package FunctionalMatcher;

public class MatcherOfGreedyZeroOrMore<T,R> extends MatcherOfGreedyQuantity<T,R> {
	protected MatcherOfGreedyZeroOrMore(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		super(callback, matcher, 0);
	}

	public static <T,R> MatcherOfGreedyZeroOrMore<T,R> of(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyZeroOrMore<T,R>(callback, matcher);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T,T> of(IContinuationMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyZeroOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher);
	}
}
