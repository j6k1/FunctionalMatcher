package FunctionalMatcher;

public class MatcherOfShortestZeroOrMore<T> extends MatcherOfShortestQuantity<T> {
	protected MatcherOfShortestZeroOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 0, callback);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IMatcher<T> matcher,
														IMatcher<T> anchor, IOnMatch<T> callback)
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

		return new MatcherOfShortestZeroOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfShortestZeroOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfShortestZeroOrMore<T>(matcher, anchor, null);
	}
}
