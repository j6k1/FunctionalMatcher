package FunctionalMatcher;

import java.util.Optional;

public class BacktrackMatcher<T> implements IMatcher<T> {
	protected final IFixedLengthMatcher<T> matcher;

	protected BacktrackMatcher(IFixedLengthMatcher<T> matcher)
	{
		this.matcher = matcher;
	}

	public static <T> BacktrackMatcher<T> of(IFixedLengthMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new BacktrackMatcher<T>(matcher);
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

		if(state.start - matcher.length() < 0) return Optional.empty();
		else return matcher.match(State.of(state.str, state.start - matcher.length(), true)).map(r -> {
			return MatchResult.of(new Range(state.start, state.start), Optional.empty());
		});
	}
}
