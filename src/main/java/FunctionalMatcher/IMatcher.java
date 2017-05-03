package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IMatcher<T> {
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary);
}