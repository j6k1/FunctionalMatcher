package FunctionalMatcher;

import java.util.HashSet;
import java.util.Optional;

public class MatcherOfCharacterClass<T,R> implements IMatcherOfCharacterClass<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final HashSet<Character> charactersSet = new HashSet<>();

	protected MatcherOfCharacterClass(IOnMatch<T,R> callback, String characters)
	{
		char[] chars = characters.toCharArray();
		for(char c: chars) charactersSet.add(c);
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfCharacterClass<T,R> of(IOnMatch<T,R> callback, String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfCharacterClass<T,R>(callback, characters);
	}

	public static <T> MatcherOfCharacterClass<T,T> of(MatchResultType<T> t, String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}

		return new MatcherOfCharacterClass<T,T>((str, start, end, m) -> Optional.empty(), characters);
	}

	public static MatcherOfCharacterClass<Nothing,Nothing> of(String characters)
	{
		if(characters == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument characters is null.");
		}

		return new MatcherOfCharacterClass<Nothing,Nothing>(
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

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= str.length() + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start == str.length() || !charactersSet.contains(str.charAt(start))) return Optional.empty();
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
