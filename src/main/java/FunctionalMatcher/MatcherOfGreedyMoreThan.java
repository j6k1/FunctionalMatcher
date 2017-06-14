package FunctionalMatcher;

public class MatcherOfGreedyMoreThan<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyMoreThan(IOnMatch<T> callback, IContinuationMatcher<T> matcher, int startTimes)
	{
		super(callback, matcher, startTimes);
	}

	public MatcherOfGreedyMoreThan(IContinuationMatcher<T> matcher, int startTimes)
	{
		super(matcher, startTimes);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IOnMatch<T> callback,
													IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyMoreThan<T>(callback, matcher, startTimes);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyMoreThan<T>(matcher, startTimes);
	}
}
