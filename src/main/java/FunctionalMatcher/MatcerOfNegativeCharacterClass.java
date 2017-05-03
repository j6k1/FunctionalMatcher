package FunctionalMatcher;

import java.util.Optional;

public class MatcerOfNegativeCharacterClass<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final IMatcerOfCharacterClass<T> matcher;

	protected MatcerOfNegativeCharacterClass(IMatcerOfCharacterClass<T> matcher, IOnMatch<T> callback)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcerOfNegativeCharacterClass<T> of(IMatcerOfCharacterClass<T> matcher, IOnMatch<T> callback)
	{
		return new MatcerOfNegativeCharacterClass<T>(matcher, callback);
	}

	public static MatcerOfNegativeCharacterClass<Nothing> of(IMatcerOfCharacterClass<Nothing> matcher)
	{
		return new MatcerOfNegativeCharacterClass<Nothing>(matcher, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		Optional<MatchResult<T>> result = matcher.match(str, start, true);

		if(result.isPresent()) return Optional.empty();
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
