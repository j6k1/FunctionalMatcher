package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IListMatcher<T> {
	public Optional<MatchResultList<T>> matchl(State state);
}
