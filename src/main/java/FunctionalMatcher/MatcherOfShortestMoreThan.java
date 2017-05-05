package FunctionalMatcher;

public class MatcherOfShortestMoreThan<T> extends MatcherOfShortestQuantity<T> {
	protected MatcherOfShortestMoreThan(IMatcher<T> matcher,
			IMatcher<T> anchor, int startTimes, IOnMatch<T> callback)
	{
		super(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor,
														int startTimes, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfShortestMoreThan<T>(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfShortestMoreThan<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfShortestMoreThan<T>(matcher, anchor, startTimes, null);
	}
}
