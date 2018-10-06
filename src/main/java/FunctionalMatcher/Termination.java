package FunctionalMatcher;

public class Termination<T> implements ITermination<T> {
	protected final MatchResult<T> result;

	protected Termination(MatchResult<T> result)
	{
		this.result = result;
	}

	public static <T> Termination<T> of(MatchResult<T> result)
	{
		if(result == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the MatchResult.");
		}

		return new Termination<T>(result);
	}

	@Override
	public MatchResult<T> result() {
		return result;
	}
}
