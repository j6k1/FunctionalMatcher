package FunctionalMatcher;

public class MatcherOfShortestOneOrMore<T,R> extends MatcherOfShortestQuantity<T,R> {
	protected MatcherOfShortestOneOrMore(IOnMatch<T,R> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 1);
	}

	public static <T,R> MatcherOfShortestOneOrMore<T,R> of(IOnMatch<T,R> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestOneOrMore<T,R>(callback, matcher, anchor);
	}

	public static <T> MatcherOfShortestOneOrMore<T,T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfShortestOneOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor);
	}
}
