package FunctionalMatcher;

public class MatcherOfLongestOneOrMore<T,R> extends MatcherOfLongestQuantity<T,R> {
	protected MatcherOfLongestOneOrMore(IOnMatch<T,R> callback, IMatcher<T> matcher, IMatcher<T> anchor)
	{
		super(callback, matcher, anchor, 1);
	}

	public static <T,R> MatcherOfLongestOneOrMore<T,R> of(IOnMatch<T,R> callback,
														IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestOneOrMore<T,R>(callback, matcher, anchor);
	}

	public static <T> MatcherOfLongestOneOrMore<T,T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		return new MatcherOfLongestOneOrMore<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor);
	}
}
