package FunctionalMatcher;

public class MatcherOfLongestMoreThan<T> extends MatcherOfLongestQuantity<T> {
	public MatcherOfLongestMoreThan(IOnMatch<T> callback,
										IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes)
	{
		super(callback, matcher, anchor, startTimes);
	}

	public MatcherOfLongestMoreThan(IMatcher<T> matcher,
									IMatcher<T> anchor, int startTimes)
	{
		super(matcher, anchor, startTimes);
	}

	public static <T> MatcherOfLongestMoreThan<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher,
														IMatcher<T> anchor,	int startTimes)
	{
		return new MatcherOfLongestMoreThan<T>(callback, matcher, anchor, startTimes);
	}

	public static <T> MatcherOfLongestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		return new MatcherOfLongestMoreThan<T>(matcher, anchor, startTimes);
	}
}
