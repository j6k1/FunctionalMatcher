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
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		if(start - matcher.length() < 0) return Optional.empty();
		else return matcher.match(str, start - matcher.length(), true).map(r -> {
			return MatchResult.of(new Range(start, start), Optional.empty());
		});
	}
}
