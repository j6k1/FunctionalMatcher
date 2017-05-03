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
		return new MatcherOfOneOrZero<T>(matcher, callback);
	}

	public static <T> MatcherOfOneOrZero<T> of(IMatcher<T> matcher)
	{
		return new MatcherOfOneOrZero<T>(matcher, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		Optional<MatchResult<T>> result = matcher.match(str, start, temporary);

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
									Optional.of(callback.onmatch(str, MatchResult.of(
										new Range(start, start), Optional.empty())))));
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
								Optional.of(callback.onmatch(str, MatchResult.of(
									new Range(start, start + result.get().range.end), Optional.empty())))));
		}
	}
}
