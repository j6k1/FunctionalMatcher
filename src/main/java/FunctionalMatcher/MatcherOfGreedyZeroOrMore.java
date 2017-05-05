package FunctionalMatcher;

public class MatcherOfGreedyZeroOrMore<T> extends MatcherOfGreedyQuantity<T> {
	protected MatcherOfGreedyZeroOrMore(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		super(matcher, 0, callback);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyZeroOrMore<T>(matcher, callback);
	}

	public static <T> MatcherOfGreedyZeroOrMore<T> of(IMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyZeroOrMore<T>(matcher, null);
	}
}
