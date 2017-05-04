package FunctionalMatcher;

public class MatcherOfLongestOneOrMore<T> extends MatcherOfLongestQuantity<T> {
	protected MatcherOfLongestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 1, callback);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, IOnMatch<T> callback)
	{
		return new MatcherOfLongestOneOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestOneOrMore<T>(matcher, anchor, null);
	}
}
