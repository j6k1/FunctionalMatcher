package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfJust<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected String value;

	public MatcherOfJust(String value, IOnMatch<T> callback)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(value, callback);
	}

	public MatcherOfJust(String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		init(value, null);
	}

	protected void init(String value, IOnMatch<T> callback)
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
		return new MatcherOfJust<Nothing>(value);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int l = str.length();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start == l || !str.startsWith(value, start)) return Optional.empty();
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + value.length()), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + value.length()),
								Optional.of(
									callback.onmatch(
											str,
											new Range(start, start + value.length()), Optional.empty()))));
		}
	}
}
