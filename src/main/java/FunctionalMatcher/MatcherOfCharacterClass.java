package FunctionalMatcher;

import java.util.HashSet;
import java.util.Optional;

public class MatcherOfCharacterClass<T> implements IMatcherOfCharacterClass<T> {
	protected IOnMatch<T> callback;
	protected final HashSet<Character> charactersSet;

	protected MatcherOfCharacterClass(String characters, IOnMatch<T> callback)
	{
		charactersSet = new HashSet<>();
		char[] chars = characters.toCharArray();
		for(char c: chars) charactersSet.add(c);
		this.callback = callback;
	}

	public static <T> MatcherOfCharacterClass<T> of(String characters, IOnMatch<T> callback)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfCharacterClass<T>(characters, callback);
	}

	public static MatcherOfCharacterClass<Nothing> of(String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}

		return new MatcherOfCharacterClass<Nothing>(characters, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= str.length() + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start == str.length() || !charactersSet.contains(str.charAt(start))) return Optional.empty();
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
											new Range(start, start + 1), Optional.empty()))));
		}
	}
}
