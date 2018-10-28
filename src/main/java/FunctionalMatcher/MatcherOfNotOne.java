package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfNotOne<T> implements IMatcher<T> {
	protected final IMatcher<T> matcher;

	protected MatcherOfNotOne(IMatcher<T> matcher) {
		this.matcher = matcher;
	}

	public static <T> MatcherOfNotOne<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfNotOne<T>(matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(State state) {

		if(matcher.match(State.of(state.str, state.start, state.temporary)).isPresent())
		{
			return Optional.empty();
		} else {
			return Optional.of(MatchResult.of(new Range(state.start, state.start + 1), Optional.empty()));
		}
	}
}
