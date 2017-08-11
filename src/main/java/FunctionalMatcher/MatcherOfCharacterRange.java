package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfCharacterRange<T,R> implements IMatcherOfCharacterClass<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final char codeStart;
	protected final char codeEnd;

	protected MatcherOfCharacterRange(IOnMatch<T,R> callback, char codeStart, char codeEnd)
	{
		this.codeStart = codeStart;
		this.codeEnd = codeEnd;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfCharacterRange<T,R> of(IOnMatch<T,R> callback, char codeStart, char codeEnd)
	{
		if(codeStart > codeEnd)
		{
			throw new InvalidCharacterRangeException("A value greater than end was specified as the value of start.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfCharacterRange<T,R>(callback, codeStart, codeEnd);
	}

	public static <T> MatcherOfCharacterRange<T,T> of(MatchResultType<T> t, char codeStart, char codeEnd)
	{
		return new MatcherOfCharacterRange<T,T>((str, start, end, m) -> Optional.empty(), codeStart, codeEnd);
	}

	public static MatcherOfCharacterRange<Nothing,Nothing> of(char codeStart, char codeEnd)
	{
		return new MatcherOfCharacterRange<Nothing,Nothing>((str, start, end, m) -> Optional.empty(), codeStart, codeEnd);
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		if(start >= str.length()) return Optional.empty();

		char c;
		int l = str.length();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start == l || (c = str.charAt(start)) < codeStart || c > codeEnd) return Optional.empty();
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								emptyCallback.onmatch(str,
									start, start + 1, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								callback.onmatch(str,
									start, start + 1, Optional.empty())));
		}
	}

}
