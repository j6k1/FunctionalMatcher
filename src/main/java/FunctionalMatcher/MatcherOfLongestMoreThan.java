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

		return new MatcherOfLongestMoreThan<T>(matcher, anchor, startTimes, callback);
	}

	public static <T> MatcherOfLongestMoreThan<T> of(IMatcher<T> matcher,
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

		return new MatcherOfLongestMoreThan<T>(matcher, anchor, startTimes, null);
	}
}
