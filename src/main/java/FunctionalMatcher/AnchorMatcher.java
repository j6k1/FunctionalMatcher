package FunctionalMatcher;

import java.util.Optional;

public class AnchorMatcher<T> implements IMatcher<T> {
	protected final IMatcher<T> matcher;

	protected AnchorMatcher(IMatcher<T> matcher)
	{
		this.matcher = matcher;
	}

	public static <T> AnchorMatcher<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new AnchorMatcher<T>(matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		return matcher.match(str, start, true).map(r -> {
			return MatchResult.of(r.range, Optional.empty());
		});
	}
}
