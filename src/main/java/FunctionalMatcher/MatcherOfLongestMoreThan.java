package FunctionalMatcher;

public class MatcherOfLongestMoreThan<T,R> extends MatcherOfLongestQuantity<T,R> {
	protected MatcherOfLongestMoreThan(IOnMatch<T,R> callback,
										IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes)
	{
		super(callback, matcher, anchor, startTimes);
	}


	public static <T,R> MatcherOfLongestMoreThan<T,R> of(IOnMatch<T,R> callback,
														IMatcher<T> matcher,
														IMatcher<T> anchor,	int startTimes)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(matcher == null)
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

		return new MatcherOfLongestMoreThan<T,R>(callback, matcher, anchor, startTimes);
	}

	public static <T> MatcherOfLongestMoreThan<T,T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, int startTimes)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfLongestMoreThan<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor, startTimes);
	}
}
