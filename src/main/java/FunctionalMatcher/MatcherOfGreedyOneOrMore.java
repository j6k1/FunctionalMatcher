package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T,R> extends MatcherOfGreedyQuantity<T,R> {
	protected MatcherOfGreedyOneOrMore(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		super(callback, matcher, 1);
	}

	public static <T,R> MatcherOfGreedyOneOrMore<T,R> of(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher)
	{
		return new MatcherOfGreedyOneOrMore<T,R>(callback, matcher);
	}

	public static <T> MatcherOfGreedyOneOrMore<T,T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyOneOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher);
	}
}
