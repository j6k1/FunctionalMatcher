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

	public <R> Optional<MatchResult<R>> next(String str, IMatcher<R> matcher)
	{
		return matcher.match(str, range.end, false);
	}

	public <R> Optional<IContinuation<R>> next(String str, IContinuationMatcher<R> matcher)
	{
		return matcher.matchc(str, range.end, false);
	}

	public MatchResult<T> compositeOf(int end)
	{
		return MatchResult.of(new Range(range.start, end), value);
	}

	public MatchResult<T> compositeOf(int start, int end)
	{
		return MatchResult.of(new Range(start, end), value);
	}

	public MatchResult<T> compositeOf(Range range)
	{
		return MatchResult.of(range, value);
	}

	public <I> MatchResult<T> compositeOf(MatchResult<I> m)
	{
		return MatchResult.of(m.range, value);
	}

	public MatchResult<T> compositeOfStart(int start)
	{
		return MatchResult.of(new Range(start, range.end), value);
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

	@Override
	public String toString()
	{
		return "(" + range.start + ", " + range.end + ", " + value.toString() + ")";
	}
}
