package FunctionalMatcher;

import java.util.Optional;

public class NegativeMatcher<T> implements IMatcher<T> {
	protected final IMatcher<T> matcher;

	protected NegativeMatcher(IMatcher<T> matcher)
	{
		this.matcher = matcher;
	}

	public static <T> NegativeMatcher<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new NegativeMatcher<T>(matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(State state) {
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		if(state.str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		if(matcher.match(State.of(state.str, state.start, true)).isPresent())
		{
			return Optional.empty();
		} else {
			return Optional.of(MatchResult.of(new Range(state.start, state.start), Optional.empty()));
		}
	}
}
