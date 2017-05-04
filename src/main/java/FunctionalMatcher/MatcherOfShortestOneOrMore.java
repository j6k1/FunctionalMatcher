package FunctionalMatcher;

public class MatcherOfShortestOneOrMore<T> extends MatcherOfShortestQuantity<T> {
	protected MatcherOfShortestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 1, callback);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, IOnMatch<T> callback)
	{
		return new MatcherOfShortestOneOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestOneOrMore<T>(matcher, anchor, null);
	}
}
