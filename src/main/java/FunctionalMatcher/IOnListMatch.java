package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IOnListMatch<T,R> {
	public Optional<R> onmatch(String str, int start, int end, MatchResultList<T> lst);
}
