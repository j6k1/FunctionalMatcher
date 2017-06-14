package FunctionalMatcher;

public class MatcherOfShortestZeroOrMore<T> extends MatcherOfShortestQuantity<T> {
	public MatcherOfShortestZeroOrMore(IOnMatch<T> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 0);
	}

	public MatcherOfShortestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 0);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestZeroOrMore<T>(callback, matcher, anchor);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestZeroOrMore<T>(null, matcher, anchor);
	}
}
