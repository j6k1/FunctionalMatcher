package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfOneOrZero<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected IMatcher<T> matcher;

	public MatcherOfOneOrZero(IOnMatch<T> callback, IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, matcher);
	}

	public MatcherOfOneOrZero(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		init(null, matcher);
	}

	protected void init(IOnMatch<T> callback, IMatcher<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcherOfOneOrZero<T> of(IOnMatch<T> callback, IMatcher<T> matcher)
	{
		return new MatcherOfOneOrZero<T>(callback, matcher);
	}

	public static <T> MatcherOfOneOrZero<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfOneOrZero<T>(matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
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
			if(callback == null || temporary)
			{
				return Optional.of(MatchResult.of(new Range(start, start), Optional.empty()));
			}
			else
			{
				return Optional.of(
						MatchResult.of(
								new Range(start, start),
									Optional.of(
										callback.onmatch(str, start, start, Optional.empty()))));
			}
		}
		else if(callback == null || temporary)
		{
			return Optional.of(
					MatchResult.of(new Range(start, result.get().range.end), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
						new Range(start, result.get().range.end),
							Optional.of(
								callback.onmatch(
									str,
									start, start + result.get().range.end, Optional.empty()))));
		}
	}
}
