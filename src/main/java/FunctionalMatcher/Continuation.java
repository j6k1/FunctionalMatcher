package FunctionalMatcher;

public class Continuation<T> implements IContinuation<T> {
	protected final MatchResult<T> result;

	protected Continuation(MatchResult<T> result)
	{
		this.result = result;
	}

	public static <T> Continuation<T> of(MatchResult<T> result)
	{
		if(result == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the MatchResult.");
		}

		return new Continuation<T>(result);
	}

	@Override
	public MatchResult<T> result() {
		return result;
	}
}
