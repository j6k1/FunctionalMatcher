package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfJust<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final String value;

	protected MatcherOfJust(String value, IOnMatch<T> callback)
	{
		this.value = value;
		this.callback = callback;
	}

	public static <T> MatcherOfJust<T> of(String value, IOnMatch<T> callback)
	{
		return new MatcherOfJust<T>(value, callback);
	}

	public static MatcherOfJust<Nothing> of(String value)
	{
		return new MatcherOfJust<Nothing>(value, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(start >= str.length() || !str.startsWith(value, start)) return Optional.empty();
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + value.length()), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + value.length()),
								Optional.of(callback.onmatch(str, MatchResult.of(
									new Range(start, start + value.length()), Optional.empty())))));
		}
	}
}
