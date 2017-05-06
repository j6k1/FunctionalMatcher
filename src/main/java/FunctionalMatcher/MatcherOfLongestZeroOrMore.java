package FunctionalMatcher;

public class MatcherOfLongestZeroOrMore<T> extends MatcherOfLongestQuantity<T> {
	public MatcherOfLongestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 0, callback);
	}

	public MatcherOfLongestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 0);
	}

	public static <T> MatcherOfLongestZeroOrMore<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, IOnMatch<T> callback)
	{
		return new MatcherOfLongestZeroOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfLongestZeroOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestZeroOrMore<T>(matcher, anchor);
	}
}
