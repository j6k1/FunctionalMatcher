package FunctionalMatcher;

public class Termination<T> implements ITermination<T> {
	protected MatchResult<T> result;

	protected Termination(MatchResult<T> result)
	{
		this.result = result;
	}

	public static <T> Termination<T> of(MatchResult<T> result)
	{
		return new Termination<T>(result);
	}

	@Override
	public MatchResult<T> result() {
		return result;
	}
}
