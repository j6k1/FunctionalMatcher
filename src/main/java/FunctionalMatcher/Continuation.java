package FunctionalMatcher;

public class Continuation<T> implements IContinuation<T> {
	protected MatchResult<T> result;

	protected Continuation(MatchResult<T> result)
	{
		this.result = result;
	}

	public static <T> Continuation<T> of(MatchResult<T> result)
	{
		return new Continuation<T>(result);
	}

	public static <T> Continuation<T> of(MatchResultType<T> t, MatchResult<T> result)
	{
		return new Continuation<T>(result);
	}

	@Override
	public MatchResult<T> result() {
		return result;
	}
}
