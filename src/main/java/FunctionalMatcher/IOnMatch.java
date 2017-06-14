package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IOnMatch<T> {
	public T onmatch(String str, int start, int end, Optional<MatchResult<T>> m);
}
