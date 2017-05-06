package FunctionalMatcher;

public class MatcherOfGreedyZeroOrMore<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyZeroOrMore(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		super(matcher, 0, callback);
	}

	public MatcherOfGreedyZeroOrMore(IMatcher<T> matcher)
	{
		super(matcher, 0);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		return new MatcherOfGreedyZeroOrMore<T>(matcher, callback);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IMatcher<T> matcher)
	{
		return new MatcherOfGreedyZeroOrMore<T>(matcher);
	}
}
