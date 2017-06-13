package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyOneOrMore(IContinuationMatcher<T> matcher, IOnMatch<T> callback)
	{
		super(matcher, 1, callback);
	}

	public MatcherOfGreedyOneOrMore(IContinuationMatcher<T> matcher)
	{
		super(matcher, 1);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IContinuationMatcher<T> matcher, IOnMatch<T> callback)
	{
		return new MatcherOfGreedyOneOrMore<T>(matcher, callback);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyOneOrMore<T>(matcher);
	}
}
