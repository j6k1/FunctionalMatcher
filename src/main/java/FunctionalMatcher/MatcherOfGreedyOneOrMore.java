package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T,R> extends MatcherOfGreedyQuantity<T,R> {
	protected MatcherOfGreedyOneOrMore(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		super(callback, matcher, 1);
	}

	public static <T,R> MatcherOfGreedyOneOrMore<T,R> of(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyOneOrMore<T,R>(callback, matcher);
	}

	public static <T> MatcherOfGreedyOneOrMore<T,T> of(IContinuationMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyOneOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher);
	}
}
