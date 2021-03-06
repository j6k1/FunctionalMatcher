package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IContinuationMatcher<T> {
	public Optional<IContinuation<T>> matchc(State state);
}
