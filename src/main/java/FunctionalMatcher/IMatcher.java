package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IMatcher<T> {
	public Optional<MatchResult<T>> match(State state);
	default MatcherOfSequence<T> seq(IMatcher<T> matcher) {
		MatcherOfSequence<T> sequence = MatcherOfSequence.of(this);
		sequence.seq(matcher);
		return sequence;
	}

	default IContinuationMatcher<T> toContinuation() {
		return (state) -> {
			return this.match(state).map(r -> Continuation.of(r));
		};
	}

	default <R> IContinuationMatcher<R> toContinuation(IContinuationCreator<T,R> creator) {
		return (state) -> {
			return this.match(state).map(r -> creator.create(r));
		};
	}
}
