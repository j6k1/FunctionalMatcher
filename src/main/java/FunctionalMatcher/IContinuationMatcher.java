package FunctionalMatcher;

import java.util.Optional;

@FunctionalInterface
public interface IContinuationMatcher<T> {
	public Optional<IContinuation<T>> matchc(String str, int start, boolean temporary);
	public static <T> IContinuationMatcher<T> of(IMatcher<T> matcher) {
		return ((String str, int start, boolean temporary) -> {
			return matcher.match(str, start, temporary).map(r -> Continuation.of(r));
		});
	}
}
