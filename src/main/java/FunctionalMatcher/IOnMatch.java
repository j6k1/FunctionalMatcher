package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IOnMatch<T,R> {
	public Optional<R> onmatch(String str, int start, int end, Optional<MatchResult<T>> m);
}
