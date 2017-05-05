package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfTerminateWithNewLine<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;

	protected MatcherOfTerminateWithNewLine(IOnMatch<T> callback)
	{
		this.callback = callback;
	}

	public static <T> MatcherOfTerminateWithNewLine<T> of(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfTerminateWithNewLine<T>(callback);
	}

	public static MatcherOfTerminateWithNewLine<Nothing> of()
	{
		return new MatcherOfTerminateWithNewLine<Nothing>(null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
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
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start),
								Optional.of(
									callback.onmatch(str, new Range(start, start), Optional.empty()))));
		}
	}
}
