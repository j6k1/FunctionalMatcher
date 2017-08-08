package FunctionalMatcher;

public class MatcherOfGreedyMoreThan<T,R> extends MatcherOfGreedyQuantity<T,R> {
	protected MatcherOfGreedyMoreThan(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher, int startTimes)
	{
		super(callback, matcher, startTimes);
	}

	public static <T,R> MatcherOfGreedyMoreThan<T,R> of(IOnMatch<T,R> callback,
													IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyMoreThan<T,R>(callback, matcher, startTimes);
	}

	public static <T> MatcherOfGreedyMoreThan<T,T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyMoreThan<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, startTimes);
	}
}
