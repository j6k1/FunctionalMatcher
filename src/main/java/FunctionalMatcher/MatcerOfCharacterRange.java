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
		return new MatcerOfCharacterRange<T>(codeStart, codeEnd, callback);
	}

	public static MatcerOfCharacterRange<Nothing> of(char codeStart, char codeEnd)
	{
		return new MatcerOfCharacterRange<Nothing>(codeStart, codeEnd, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		char c = str.charAt(start);

		if(c < codeStart || c > codeEnd) return Optional.empty();
		else if(callback == null || temporary)
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

}
