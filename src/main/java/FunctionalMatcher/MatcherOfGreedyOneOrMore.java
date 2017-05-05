package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T> extends MatcherOfGreedyQuantity<T> {
	protected MatcherOfGreedyOneOrMore(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		super(matcher, 1, callback);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyOneOrMore<T>(matcher, callback);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyOneOrMore<T>(matcher, null);
	}
}
