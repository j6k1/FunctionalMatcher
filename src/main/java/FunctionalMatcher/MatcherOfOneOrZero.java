package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfOneOrZero<T,R> implements IMatcher<R> {
	protected IOnMatch<T,R> callback;
	protected IOnMatch<T,R> emptyCallback;
	protected IMatcher<T> matcher;

	protected MatcherOfOneOrZero(IOnMatch<T,R> callback, IMatcher<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfOneOrZero<T,R> of(IOnMatch<T,R> callback, IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfOneOrZero<T,R>(callback, matcher);
	}

	public static <T> MatcherOfOneOrZero<T,T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfOneOrZero<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher);
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		Optional<MatchResult<T>> result = matcher.match(str, start, temporary);

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= str.length() + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}

		if(!result.isPresent())
		{
			if(temporary)
			{
				return Optional.of(
						MatchResult.of(
								new Range(start, start),
									emptyCallback.onmatch(str, start, start, Optional.empty())));
			}
			else
			{
				return Optional.of(
						MatchResult.of(
								new Range(start, start),
									callback.onmatch(str, start, start, Optional.empty())));
			}
		}
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
						new Range(start, result.get().range.end),
							emptyCallback.onmatch(
									str,
									start, start + result.get().range.end, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
						new Range(start, result.get().range.end),
							callback.onmatch(
									str,
									start, start + result.get().range.end, Optional.empty())));
		}
	}
}
