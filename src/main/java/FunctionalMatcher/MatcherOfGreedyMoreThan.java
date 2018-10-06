package FunctionalMatcher;

public class MatcherOfGreedyMoreThan<T,R> extends MatcherOfGreedyQuantity<T,R> {
	protected MatcherOfGreedyMoreThan(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher, int startTimes)
	{
		super(callback, matcher, startTimes);
	}

	public static <T,R> MatcherOfGreedyMoreThan<T,R> of(IOnMatch<T,R> callback,
													IContinuationMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyMoreThan<T,R>(callback, matcher, startTimes);
	}

	public static <T> MatcherOfGreedyMoreThan<T,T> of(IContinuationMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}

		return new MatcherOfGreedyMoreThan<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, startTimes);
	}
}
