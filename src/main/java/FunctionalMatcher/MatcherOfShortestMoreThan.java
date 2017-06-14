package FunctionalMatcher;

public class MatcherOfShortestMoreThan<T> extends MatcherOfShortestQuantity<T> {
	public MatcherOfShortestMoreThan(IOnMatch<T> callback,
										IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes)
	{
		super(callback, matcher, anchor, startTimes);
	}

	public MatcherOfShortestMoreThan(IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes)
	{
		super(matcher, anchor, startTimes);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IOnMatch<T> callback,
														IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		return new MatcherOfShortestMoreThan<T>(callback, matcher, anchor, startTimes);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		return new MatcherOfShortestMoreThan<T>(null, matcher, anchor, startTimes);
	}
}
