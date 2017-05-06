package FunctionalMatcher;

public class MatcherOfGreedyOneOrMore<T> extends MatcherOfGreedyQuantity<T> {
	public MatcherOfGreedyOneOrMore(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		super(matcher, 1, callback);
	}

	public MatcherOfGreedyOneOrMore(IMatcher<T> matcher)
	{
		super(matcher, 1);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		return new MatcherOfGreedyOneOrMore<T>(matcher, callback);
	}

	public static <T> MatcherOfGreedyOneOrMore<T> of(IMatcher<T> matcher, int startTimes)
	{
		return new MatcherOfGreedyOneOrMore<T>(matcher);
	}
}
