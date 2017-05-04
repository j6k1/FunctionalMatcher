package FunctionalMatcher;

public class MatcherOfLongestMoreThan<T> extends MatcherOfLongestQuantity<T> {
	protected MatcherOfLongestMoreThan(IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes, IOnMatch<T> callback)
	{
		super(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfLongestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor,
														int startTimes, IOnMatch<T> callback)
	{
		return new MatcherOfLongestMoreThan<T>(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfLongestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		return new MatcherOfLongestMoreThan<T>(matcher, anchor, startTimes, null);
	}
}
