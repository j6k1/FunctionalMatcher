package FunctionalMatcher;

public class MatcherOfLongestOneOrMore<T> extends MatcherOfLongestQuantity<T> {
	public MatcherOfLongestOneOrMore(IOnMatch<T> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 1);
	}

	public MatcherOfLongestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 1);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestOneOrMore<T>(callback, matcher, anchor);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestOneOrMore<T>(matcher, anchor);
	}
}
