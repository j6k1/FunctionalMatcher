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
		if(range == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the range.");
		}
		else if(value == null) {
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the value.");
		}

		return new MatchResult<T>(range, value);
	}

	public <R> Optional<MatchResult<R>> next(State state, IMatcher<R> matcher)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}
		else if(matcher == null) {
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return matcher.match(State.of(state.str, range.end, false));
	}

	public <R> Optional<IContinuation<R>> next(State state, IContinuationMatcher<R> matcher)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}
		else if(matcher == null) {
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return matcher.matchc(State.of(state.str, range.end, false));
	}

	public <R> Optional<MatchResult<T>> skip(State state, IMatcher<R> matcher)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}
		else if(matcher == null) {
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return matcher.match(State.of(state.str, range.end, true)).map(r -> this.compositeOfEnd(r.range.end));
	}

	public <R> Optional<MatchResult<R>> back(State state, IFixedLengthMatcher<R> matcher)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}
		else if(matcher == null) {
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		if(range.start - matcher.length() < 0) return Optional.empty();
		else return matcher.match(State.of(state.str, range.start - matcher.length(), true)).map(r -> {
			return MatchResult.of(new Range(range.start, range.start), Optional.empty());
		});
	}

	public <R> Optional<MatchResult<R>> back(State state, MatcherOfFixedLengthSelect<R> matcher)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}
		else if(matcher == null) {
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		for(IFixedLengthMatcher<R> m: matcher)
		{
			if(range.start - m.length() >= 0)
			{
				Optional<MatchResult<R>> optR = m.match(State.of(state.str, range.start - m.length(), true));

				if(optR.isPresent()) return optR.map(r -> {
					return MatchResult.of(new Range(range.start, range.start), Optional.empty());
				});
			}
		}

		return Optional.empty();
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
		if(range == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the range.");
		}

		return MatchResult.of(range, value);
	}

	public <I> MatchResult<T> compositeOf(MatchResult<I> m)
	{
		if(m == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the MatchResult.");
		}

		return MatchResult.of(m.range, value);
	}

	public MatchResult<T> compositeOfStart(int start)
	{
		return MatchResult.of(new Range(start, range.end), value);
	}

	public MatchResult<T> compositeOfEnd(int end)
	{
		return MatchResult.of(new Range(range.start, end), value);
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
