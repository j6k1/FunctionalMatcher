package FunctionalMatcher;

import java.util.Optional;

public interface IMatchFinder<T> {
	public Optional<MatchResult<T>> next();
}
