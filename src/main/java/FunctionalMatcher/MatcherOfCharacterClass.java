package FunctionalMatcher;

import java.util.HashSet;
import java.util.Optional;

public class MatcherOfCharacterClass<T> implements IMatcherOfCharacterClass<T> {
	protected IOnMatch<T> callback;
	protected final HashSet<Character> charactersSet = new HashSet<>();

	public MatcherOfCharacterClass(IOnMatch<T> callback, String characters)
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

	public MatcherOfCharacterClass(String characters)
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
		for(char c: chars) charactersSet.add(c);
		this.callback = callback;
	}

	public static <T> MatcherOfCharacterClass<T> of(IOnMatch<T> callback, String characters)
	{
		return new MatcherOfCharacterClass<T>(callback, characters);
	}

	public static <T> MatcherOfCharacterClass<T> of(String characters)
	{
		return new MatcherOfCharacterClass<T>(characters);
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
											start, start + 1, Optional.empty()))));
		}
	}
}
