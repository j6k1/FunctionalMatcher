package FunctionalMatcher;

import java.util.Arrays;
import java.util.Optional;

public class MatcherOfAsciiCharacterClass<T> implements IMatcherOfCharacterClass<T> {
	protected IOnMatch<T> callback;
	protected final boolean[] characterMap;

	{
		characterMap = new boolean[128];
		Arrays.fill(characterMap, false);
	}

	public MatcherOfAsciiCharacterClass(IOnMatch<T> callback, String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, characters);
	}

	protected MatcherOfAsciiCharacterClass(String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}

		init(null, characters);
	}

	protected void init(IOnMatch<T> callback, String characters)
	{
		char[] chars = characters.toCharArray();

		for(char c: chars)
		{
			if(c >= 0x80)
			{
				throw new InvalidCharacterRangeException(
							"Character codes outside the range of ascii characters can not be specified.");
			}
			characterMap[c] = true;
		}
		this.callback = callback;
	}

	public static <T> MatcherOfAsciiCharacterClass<T> of(IOnMatch<T> callback, String characters)
	{
		return new MatcherOfAsciiCharacterClass<T>(callback, characters);
	}

	public static <T> MatcherOfAsciiCharacterClass<T> of(MatchResultType<T> t, String characters)
	{
		return new MatcherOfAsciiCharacterClass<T>(characters);
	}

	public static MatcherOfAsciiCharacterClass<Nothing> of(String characters)
	{
		return new MatcherOfAsciiCharacterClass<Nothing>(characters);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
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
		else if(start == l || (c = str.charAt(start)) >= 0x80 || !characterMap[c]) return Optional.empty();
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start + 1), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								Optional.of(callback.onmatch(str,
											start, start + 1, Optional.empty()))));
		}
	}
}
