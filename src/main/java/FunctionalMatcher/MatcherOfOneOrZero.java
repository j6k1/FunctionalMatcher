package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfOneOrZero<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final IMatcher<T> matcher;

	protected MatcherOfOneOrZero(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcherOfOneOrZero<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfOneOrZero<T>(matcher, callback);
	}

	public static <T> MatcherOfOneOrZero<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfOneOrZero<T>(matcher, null);
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
			MatchResult<T> m = result.get();

			if(callback == null || temporary)
			{
				if(m.value.isPresent())
				{
					return Optional.of(MatchResult.of(new Range(start, start), m.value));
				}
				else
				{
					return Optional.of(MatchResult.of(new Range(start, start), Optional.empty()));
				}
			}
			else
			{
				return Optional.of(
						MatchResult.of(
								new Range(start, start),
									Optional.of(
										callback.onmatch(str, new Range(start, start), Optional.of(m)))));
			}
		}
		else if(callback == null || temporary)
		{
			return Optional.of(
					MatchResult.of(new Range(start, start + result.get().range.end), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
						new Range(start, start + result.get().range.end),
							Optional.of(
								callback.onmatch(
									str,
									new Range(start, start + result.get().range.end), Optional.empty()))));
		}
	}
}
