package FunctionalMatcher;

public class MatcherOfLongestZeroOrMore<T> extends MatcherOfLongestQuantity<T> {
	public MatcherOfLongestZeroOrMore(IOnMatch<T> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 0);
	}

	public MatcherOfLongestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 0);
	}

	public static <T> MatcherOfLongestZeroOrMore<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestZeroOrMore<T>(callback, matcher, anchor);
	}

	public static <T> MatcherOfLongestZeroOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestZeroOrMore<T>(matcher, anchor);
	}
}
