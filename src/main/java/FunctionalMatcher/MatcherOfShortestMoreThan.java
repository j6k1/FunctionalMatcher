package FunctionalMatcher;

public class MatcherOfShortestMoreThan<T> extends MatcherOfShortestQuantity<T> {
	public MatcherOfShortestMoreThan(IMatcher<T> matcher,
			IMatcher<T> anchor, int startTimes, IOnMatch<T> callback)
	{
		super(matcher, anchor, startTimes, callback);
	}

	public MatcherOfShortestMoreThan(IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes)
	{
		super(matcher, anchor, startTimes);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor,
														int startTimes, IOnMatch<T> callback)
	{
		return new MatcherOfShortestMoreThan<T>(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		return new MatcherOfShortestMoreThan<T>(matcher, anchor, startTimes, null);
	}
}
