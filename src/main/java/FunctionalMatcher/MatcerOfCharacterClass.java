package FunctionalMatcher;

import java.util.HashSet;
import java.util.Optional;

public class MatcerOfCharacterClass<T> implements IMatcerOfCharacterClass<T> {
	protected IOnMatch<T> callback;
	protected final HashSet<Character> charactersMap;

	protected MatcerOfCharacterClass(String characters, IOnMatch<T> callback)
	{
		charactersMap = new HashSet<>();
		char[] chars = characters.toCharArray();
		for(char c: chars) charactersMap.add(c);
		this.callback = callback;
	}

	public static <T> MatcerOfCharacterClass<T> of(String value, IOnMatch<T> callback)
	{
		return new MatcerOfCharacterClass<T>(value, callback);
	}

	public static MatcerOfCharacterClass<Nothing> of(String value)
	{
		return new MatcerOfCharacterClass<Nothing>(value, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(!charactersMap.contains(str.charAt(start))) return Optional.empty();
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
