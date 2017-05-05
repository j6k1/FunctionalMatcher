package FunctionalMatcher;

import java.util.Optional;

public class MatcerOfCharacterRange<T> implements IMatcerOfCharacterClass<T> {
	protected IOnMatch<T> callback;
	protected final char codeStart;
	protected final char codeEnd;

	protected MatcerOfCharacterRange(char codeStart, char codeEnd, IOnMatch<T> callback)
	{
		if(codeStart > codeEnd)
		{
			throw new InvalidCharacterRangeException("A value greater than end was specified as the value of start.");
		}
		this.codeStart = codeStart;
		this.codeEnd = codeEnd;
		this.callback = callback;
	}

	public static <T> MatcerOfCharacterRange<T> of(char codeStart, char codeEnd, IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcerOfCharacterRange<T>(codeStart, codeEnd, callback);
	}

	public static MatcerOfCharacterRange<Nothing> of(char codeStart, char codeEnd)
	{
		return new MatcerOfCharacterRange<Nothing>(codeStart, codeEnd, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		if(start >= str.length()) return Optional.empty();

		char c = str.charAt(start);

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= str.length() + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(c < codeStart || c > codeEnd) return Optional.empty();
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + 1), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								Optional.of(
									callback.onmatch(str,
										new Range(start, start + 1), Optional.empty()))));
		}
	}

}
