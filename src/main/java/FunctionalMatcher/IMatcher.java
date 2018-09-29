package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IMatcher<T> {
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary);
	default MatcherOfSequence<T> seq(IMatcher<T> matcher) {
		MatcherOfSequence<T> sequence = MatcherOfSequence.of(this);
		sequence.seq(matcher);
		return sequence;
	}
}
