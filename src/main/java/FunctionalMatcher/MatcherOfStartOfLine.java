package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfStartOfLine<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;

	public MatcherOfStartOfLine(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback);
	}

	public MatcherOfStartOfLine()
	{
		init(null);
	}

	protected void init(IOnMatch<T> callback)
	{
		this.callback = callback;
	}

	public static <T> MatcherOfStartOfLine<T> of(IOnMatch<T> callback)
	{
		return new MatcherOfStartOfLine<T>(callback);
	}

	public static MatcherOfStartOfLine<Nothing> of()
	{
		return new MatcherOfStartOfLine<Nothing>();
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
		else if(start != 0 && (c = str.charAt(start - 1)) != '\n' && c != '\r')
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
									callback.onmatch(str, start, start, Optional.empty()))));
		}
	}
}
