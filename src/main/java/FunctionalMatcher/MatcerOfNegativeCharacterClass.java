package FunctionalMatcher;

import java.util.Optional;

public class MatcerOfNegativeCharacterClass<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final IMatcherOfCharacterClass<T> matcher;

	protected MatcerOfNegativeCharacterClass(IMatcherOfCharacterClass<T> matcher, IOnMatch<T> callback)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcerOfNegativeCharacterClass<T> of(IMatcherOfCharacterClass<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcerOfNegativeCharacterClass<T>(matcher, callback);
	}

	public static MatcerOfNegativeCharacterClass<Nothing> of(IMatcherOfCharacterClass<Nothing> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcerOfNegativeCharacterClass<Nothing>(matcher, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

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
								Optional.of(
									callback.onmatch(
											str, new Range(start, start + 1), Optional.empty()))));
		}
	}
}
