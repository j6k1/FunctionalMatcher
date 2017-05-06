package FunctionalMatcher;

public class MatcherOfShortestZeroOrMore<T> extends MatcherOfShortestQuantity<T> {
	public MatcherOfShortestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 0, callback);
	}

	public MatcherOfShortestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 0);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, IOnMatch<T> callback)
	{
		return new MatcherOfShortestZeroOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestZeroOrMore<T>(matcher, anchor, null);
	}
}
