package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfNegativeCharacterClass<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected IMatcherOfCharacterClass<T> matcher;

	public MatcherOfNegativeCharacterClass(IOnMatch<T> callback, IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, matcher);
	}

	public MatcherOfNegativeCharacterClass(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		init(null, matcher);
	}

	protected void init(IOnMatch<T> callback, IMatcherOfCharacterClass<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcherOfNegativeCharacterClass<T> of(IOnMatch<T> callback, IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfNegativeCharacterClass<T>(callback, matcher);
	}

	public static <T> MatcherOfNegativeCharacterClass<T> of(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfNegativeCharacterClass<T>(matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		Optional<MatchResult<T>> result = matcher.match(str, start, true);

		if(start == str.length() || result.isPresent()) return Optional.empty();
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
											str, start, start + 1, Optional.empty()))));
		}
	}
}
