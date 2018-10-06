package FunctionalMatcher;

import java.util.Optional;

public class BacktrackSelectMatcher<T> implements IMatcher<T> {
	protected final MatcherOfFixedLengthSelect<T> matcher;

	protected BacktrackSelectMatcher(MatcherOfFixedLengthSelect<T> matcher) {
		this.matcher = matcher;
	}

	public BacktrackSelectMatcher<T> of(MatcherOfFixedLengthSelect<T> matcher) {
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new BacktrackSelectMatcher<T>(matcher);
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

		for(IFixedLengthMatcher<T> m: matcher)
		{
			if(state.start - m.length() >= 0)
			{
				Optional<MatchResult<T>> optR = m.match(
													State.of(state.str,
															state.start - m.length(),
															state.temporary));

				if(optR.isPresent()) {
					return Optional.of(MatchResult.of(new Range(state.start, state.start), Optional.empty()));
				}
			}
		}

		return Optional.empty();
	}
}
