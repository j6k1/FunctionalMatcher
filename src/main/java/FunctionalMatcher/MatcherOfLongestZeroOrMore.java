package FunctionalMatcher;

public class MatcherOfLongestZeroOrMore<T,R> extends MatcherOfLongestQuantity<T,R> {
	protected MatcherOfLongestZeroOrMore(IOnMatch<T,R> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 0);
	}

	public static <T,R> MatcherOfLongestZeroOrMore<T,R> of(IOnMatch<T,R> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestZeroOrMore<T,R>(callback, matcher, anchor);
	}

	public static <T> MatcherOfLongestZeroOrMore<T,T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestZeroOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor);
	}
}
