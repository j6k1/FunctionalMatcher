package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IOnMatch<T> {
	public T onmatch(String str, Range range, Optional<MatchResult<T>> m);
}
