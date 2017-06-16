package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfAnyChar<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected boolean multiline;

	public MatcherOfAnyChar(IOnMatch<T> callback, boolean multiline)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, multiline);
	}

	public MatcherOfAnyChar(boolean multiline)
	{
		init(null, multiline);
	}

	protected void init(IOnMatch<T> callback, boolean multiline)
	{
		this.multiline = multiline;
		this.callback = callback;
	}

	public static <T> MatcherOfAnyChar<T> of(IOnMatch<T> callback, boolean multiline)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfAnyChar<T>(callback, multiline);
	}

	public static <T> MatcherOfAnyChar<T> of(MatchResultType<T> t, boolean multiline)
	{
		return new MatcherOfAnyChar<T>(multiline);
	}

	public static MatcherOfAnyChar<Nothing> of(boolean multiline)
	{
		return new MatcherOfAnyChar<Nothing>(multiline);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
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
		else if(multiline && l > start && callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + 1), Optional.empty()));
		}
		else if(multiline && l > start)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								Optional.of(
									callback.onmatch(str, start, start + 1, Optional.empty()))));
		}
		else if(!multiline && l > start && str.charAt(start) != '\n' && str.charAt(start) != '\r')
		{
			if(callback == null || temporary)
			{
				return Optional.of(MatchResult.of(new Range(start, start + 1), Optional.empty()));
			}
			else
			{
				return Optional.of(
						MatchResult.of(
								new Range(start, start + 1),
									Optional.of(
										callback.onmatch(
												str, start, start + 1, Optional.empty()))));
			}
		}
		else
		{
			return Optional.empty();
		}
	}
}
