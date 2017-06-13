package FunctionalMatcher;

public class MatcherOfGreedyMoreThan<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyMoreThan(IContinuationMatcher<T> matcher, int startTimes, IOnMatch<T> callback)
	{
		super(matcher, startTimes, callback);
	}

	public MatcherOfGreedyMoreThan(IContinuationMatcher<T> matcher, int startTimes)
	{
		super(matcher, startTimes);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IContinuationMatcher<T> matcher,
														int startTimes, IOnMatch<T> callback)
	{
		return new MatcherOfGreedyMoreThan<T>(matcher, startTimes, callback);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyMoreThan<T>(matcher, startTimes);
	}
}
