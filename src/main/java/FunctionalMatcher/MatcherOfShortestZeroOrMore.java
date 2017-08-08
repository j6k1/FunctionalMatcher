package FunctionalMatcher;

public class MatcherOfShortestZeroOrMore<T,R> extends MatcherOfShortestQuantity<T,R> {
	protected MatcherOfShortestZeroOrMore(IOnMatch<T,R> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 0);
	}

	public static <T,R> MatcherOfShortestZeroOrMore<T,R> of(IOnMatch<T,R> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestZeroOrMore<T,R>(callback, matcher, anchor);
	}

	public static <T> MatcherOfShortestZeroOrMore<T,T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestZeroOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor);
	}
}
