package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfAnyChar<T,R> implements IFixedLengthMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final boolean multiline;

	protected MatcherOfAnyChar(IOnMatch<T,R> callback, boolean multiline)
	{
		this.multiline = multiline;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfAnyChar<T,R> of(IOnMatch<T,R> callback, boolean multiline)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfAnyChar<T,R>(callback, multiline);
	}

	public static <T,R> MatcherOfAnyChar<T,T> of(MatchResultType<T> t, boolean multiline)
	{
		return new MatcherOfAnyChar<T,T>((str, start, end, m) -> Optional.empty(), multiline);
	}

	public static MatcherOfAnyChar<Nothing,Nothing> of(boolean multiline)
	{
		return new MatcherOfAnyChar<Nothing,Nothing>(
									(str, start, end, m) -> Optional.empty() , multiline);
	}

	@Override
	public int length()
	{
		return 1;
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary) {
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
		else if(multiline && l > start && temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								emptyCallback.onmatch(str, start, start + 1, Optional.empty())));
		}
		else if(multiline && l > start)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								callback.onmatch(str, start, start + 1, Optional.empty())));
		}
		else if(!multiline && l > start && str.charAt(start) != '\n' && str.charAt(start) != '\r')
		{
			if(temporary)
			{
				return Optional.of(
						MatchResult.of(
							new Range(start, start + 1),
								emptyCallback.onmatch(str, start, start + 1, Optional.empty())));
			}
			else
			{
				return Optional.of(
							MatchResult.of(
								new Range(start, start + 1),
									callback.onmatch(str, start, start + 1, Optional.empty())));
			}
		}
		else
		{
			return Optional.empty();
		}
	}
}
