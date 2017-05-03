package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfAnyChar<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final boolean multiline;

	protected MatcherOfAnyChar(boolean multiline, IOnMatch<T> callback)
	{
		this.multiline = multiline;
		this.callback = callback;
	}

	public static <T> MatcherOfAnyChar<T> of(boolean multiline, IOnMatch<T> callback)
	{
		return new MatcherOfAnyChar<T>(multiline, callback);
	}

	public static MatcherOfAnyChar<Nothing> of(boolean multiline)
	{
		return new MatcherOfAnyChar<Nothing>(multiline, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		int l = str.length();

		if(multiline && l > start && callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + 1), Optional.empty()));
		}
		else if(multiline && l > start)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								Optional.of(callback.onmatch(str, MatchResult.of(
									new Range(start, start + 1), Optional.empty())))));
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
									Optional.of(callback.onmatch(str, MatchResult.of(
										new Range(start, start + 1), Optional.empty())))));
			}
		}
		else
		{
			return Optional.empty();
		}
	}
}
