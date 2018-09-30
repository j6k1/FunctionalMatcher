package FunctionalMatcher;

import java.util.Arrays;
import java.util.Optional;

public class MatcherOfAsciiCharacterClass<T,R> implements IMatcherOfCharacterClass<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final boolean[] characterMap;

	{
		characterMap = new boolean[128];
		Arrays.fill(characterMap, false);
	}

	protected MatcherOfAsciiCharacterClass(IOnMatch<T,R> callback, String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}

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
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfAsciiCharacterClass<T,R> of(IOnMatch<T,R> callback, String characters)
	{
		return new MatcherOfAsciiCharacterClass<T,R>(callback, characters);
	}

	public static <T> MatcherOfAsciiCharacterClass<T,T> of(MatchResultType<T> t, String characters)
	{
		return new MatcherOfAsciiCharacterClass<T,T>((str, start, end, m) -> Optional.empty(), characters);
	}

	public static MatcherOfAsciiCharacterClass<Nothing,Nothing> of(String characters)
	{
		return new MatcherOfAsciiCharacterClass<Nothing,Nothing>(
												(str, start, end, m) -> Optional.empty(), characters);
	}

	@Override
	public Optional<MatchResult<R>> match(State state)
	{
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		final String str = state.str;
		final int start = state.start;
		final boolean temporary = state.temporary;

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
