package FunctionalMatcher;

public class MatcherOfShortestOneOrMore<T> extends MatcherOfShortestQuantity<T> {
	protected MatcherOfShortestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 1, callback);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IMatcher<T> matcher,
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

		return new MatcherOfShortestOneOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfShortestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfShortestOneOrMore<T>(matcher, anchor, null);
	}
}
