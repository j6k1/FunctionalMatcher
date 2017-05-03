package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IListMatcher<T> {
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary);
}
