package FunctionalMatcher;

public class MatcherOfGreedyMoreThan<T> extends MatcherOfGreedyQuantity<T> {
	protected MatcherOfGreedyMoreThan(IMatcher<T> matcher, int startTimes, IOnMatch<T> callback)
	{
		super(matcher, startTimes, callback);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IMatcher<T> matcher,
														int startTimes, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyMoreThan<T>(matcher, startTimes, callback);
	}

	public static <T> MatcherOfGreedyMoreThan<T> of(IMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyMoreThan<T>(matcher, startTimes, null);
	}
}
