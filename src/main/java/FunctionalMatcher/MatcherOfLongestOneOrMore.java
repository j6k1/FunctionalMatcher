package FunctionalMatcher;

public class MatcherOfLongestOneOrMore<T> extends MatcherOfLongestQuantity<T> {
	protected MatcherOfLongestOneOrMore(IMatcher<T> matcher, IMatcher<T> anchor, IOnMatch<T> callback)
	{
		super(matcher, anchor, 1, callback);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IMatcher<T> matcher,
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

		return new MatcherOfLongestOneOrMore<T>(matcher, anchor, callback);
	}

	public static <T> MatcherOfLongestOneOrMore<T> of(IMatcher<T> matcher, IMatcher<T> anchor)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfLongestOneOrMore<T>(matcher, anchor, null);
	}
}
