package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfJust<T,R> implements IFixedLengthMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final String value;
	protected final boolean ignoreCase;

	protected MatcherOfJust(IOnMatch<T,R> callback, String value, boolean ignoreCase)
	{
		this.value = value;
		this.ignoreCase = ignoreCase;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfJust<T,R> of(IOnMatch<T,R> callback, String value, boolean ignoreCase)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfJust<T,R>(callback, value, ignoreCase);
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

		return new MatcherOfJust<T,R>(callback, value, false);
	}

	public static <T> MatcherOfJust<T,T> of(MatchResultType<T> t, String value, boolean ignoreCase)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<T,T>((str, start, end, m) -> Optional.empty(), value, ignoreCase);
	}

	public static <T> MatcherOfJust<T,T> of(MatchResultType<T> t, String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<T,T>((str, start, end, m) -> Optional.empty(), value, false);
	}

	public static MatcherOfJust<Nothing,Nothing> of(String value, boolean ignoreCase)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<Nothing,Nothing>((str, start, end, m) -> Optional.empty(), value, ignoreCase);
	}

	public static MatcherOfJust<Nothing,Nothing> of(String value)
	{
		if(value == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument value is null.");
		}

		return new MatcherOfJust<Nothing,Nothing>((str, start, end, m) -> Optional.empty(), value, false);
	}

	@Override
	public int length()
	{
		return value.length();
	}

	@Override
	public Optional<MatchResult<R>> match(State state)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		final String str = state.str;
		final int start = state.start;
		final boolean temporary = state.temporary;

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
		else if(start == l || start + value.length() > l ||
				(ignoreCase && str.substring(start, start + value.length()).equalsIgnoreCase(value)) ||
				(!ignoreCase && !str.startsWith(value, start)))
		{
			return Optional.empty();
		}
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
