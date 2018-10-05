package FunctionalMatcher;

@FunctionalInterface
public interface IContinuationCreator<T,R> {
	public IContinuation<R> create(MatchResult<T> result);
}
