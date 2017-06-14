package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyOneOrMore(IOnMatch<T> callback, IContinuationMatcher<T> matcher)
	{
		super(callback, matcher, 1);
	}

	public MatcherOfGreedyOneOrMore(IContinuationMatcher<T> matcher)
	{
		super(matcher, 1);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IOnMatch<T> callback, IContinuationMatcher<T> matcher)
	{
		return new MatcherOfGreedyOneOrMore<T>(callback, matcher);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyOneOrMore<T>(matcher);
	}
}
