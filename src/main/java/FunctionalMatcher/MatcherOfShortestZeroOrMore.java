package FunctionalMatcher;

public class MatcherOfShortestZeroOrMore<T> extends MatcherOfShortestQuantity<T> {
	protected MatcherOfShortestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 0, callback);
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
