package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfNegativeCharacterClass<T,R> implements IMatcher<R> {
	protected IOnMatch<T,R> callback;
	protected IOnMatch<T,R> emptyCallback;
	protected IMatcherOfCharacterClass<T> matcher;

	protected MatcherOfNegativeCharacterClass(IOnMatch<T,R> callback, IMatcherOfCharacterClass<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfNegativeCharacterClass<T,R> of(IOnMatch<T,R> callback, IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfNegativeCharacterClass<T,R>(callback, matcher);
	}

	public static <T> MatcherOfNegativeCharacterClass<T,T> of(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfNegativeCharacterClass<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher);
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		Optional<MatchResult<T>> result = matcher.match(str, start, true);

		if(start == str.length() || result.isPresent()) return Optional.empty();
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								emptyCallback.onmatch(
											str, start, start + 1, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start + 1),
								callback.onmatch(
											str, start, start + 1, Optional.empty())));
		}
	}
}
