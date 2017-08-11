package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfTerminateWithNewLine<T,R> implements IMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;

	protected MatcherOfTerminateWithNewLine(IOnMatch<T,R> callback)
	{
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfTerminateWithNewLine<T,R> of(IOnMatch<T,R> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfTerminateWithNewLine<T,R>(callback);
	}

	public static <T> MatcherOfTerminateWithNewLine<T,T> of(MatchResultType<T> t)
	{
		return new MatcherOfTerminateWithNewLine<T,T>((str, start, end, m) -> Optional.empty());
	}

	public static MatcherOfTerminateWithNewLine<Nothing,Nothing> of()
	{
		return new MatcherOfTerminateWithNewLine<Nothing,Nothing>((str, start, end, m) -> Optional.empty());
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int l = str.length();
		char c;

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start != l &&
				!(start + 2 == l && (str.charAt(start) == '\r' && str.charAt(start + 1) == '\n')) &&
				!(start + 1 == l && ((c = str.charAt(start)) == '\r' || c == '\n')))
		{
			return Optional.empty();
		}
		else if(temporary)
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
}
