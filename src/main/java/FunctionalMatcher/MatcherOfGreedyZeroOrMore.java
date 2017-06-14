package FunctionalMatcher;

public class MatcherOfGreedyZeroOrMore<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyZeroOrMore(IOnMatch<T> callback, IContinuationMatcher<T> matcher)
	{
		super(callback, matcher, 0);
	}

	public MatcherOfGreedyZeroOrMore(IContinuationMatcher<T> matcher)
	{
		super(matcher, 0);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IOnMatch<T> callback, IContinuationMatcher<T> matcher)
	{
		return new MatcherOfGreedyZeroOrMore<T>(callback, matcher);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IContinuationMatcher<T> matcher)
	{
		return new MatcherOfGreedyZeroOrMore<T>(matcher);
	}
}
