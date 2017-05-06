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

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof MatchResult)
		{
			@SuppressWarnings("rawtypes")
			MatchResult r = (MatchResult)o;

			return (this.range.equals(r.range) && this.value.equals(r.value));
		}
		else
		{
			return false;
		}
	}
}
