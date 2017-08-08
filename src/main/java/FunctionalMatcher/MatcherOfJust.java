package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfJust<T,R> implements IMatcher<R> {
	protected IOnMatch<T,R> callback;
	protected IOnMatch<T,R> emptyCallback;
	protected String value;

	protected MatcherOfJust(IOnMatch<T,R> callback, String value)
	{
		this.value = value;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfJust<T,R> of(IOnMatch<T,R> callback, String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfJust<T,R>(callback, value);
	}

	public static <T> MatcherOfJust<T,T> of(MatchResultType<T> t, String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<T,T>((str, start, end, m) -> Optional.empty(), value);
	}

	public static MatcherOfJust<Nothing,Nothing> of(String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<Nothing,Nothing>((str, start, end, m) -> Optional.empty(), value);
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
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
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + value.length()),
								emptyCallback.onmatch(
											str,
											start, start + value.length(), Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + value.length()),
								callback.onmatch(
											str,
											start, start + value.length(), Optional.empty())));
		}
	}
}
