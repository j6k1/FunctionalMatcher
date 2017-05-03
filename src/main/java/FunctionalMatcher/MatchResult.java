package FunctionalMatcher;

import java.util.Optional;

public class MatchResult<T> {
	public final Range range;
	public final Optional<T> value;

	protected MatchResult(Range range, Optional<T> value)
	{
		this.range = range;
		this.value = value;
	}

	public static <T> MatchResult<T> of(Range range, Optional<T> value)
	{
		return new MatchResult<T>(range, value);
	}
}
