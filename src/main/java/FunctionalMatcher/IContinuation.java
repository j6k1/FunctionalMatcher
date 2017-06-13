package FunctionalMatcher;

public interface IContinuation<T> {
	public boolean isContinuation();
	public MatchResult<T> result();
}
