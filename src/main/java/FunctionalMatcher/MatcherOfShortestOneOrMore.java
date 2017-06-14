package FunctionalMatcher;

public class MatcherOfShortestOneOrMore<T> extends MatcherOfShortestQuantity<T> {
	public MatcherOfShortestOneOrMore(IOnMatch<T> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 1);
	}

	public MatcherOfShortestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(matcher, anchor, 1);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestOneOrMore<T>(callback, matcher, anchor);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestOneOrMore<T>(matcher, anchor);
	}
}
