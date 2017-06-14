package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfFold<T> implements IMatcher<T> {
	protected IOnListMatch<T> callback;
	protected IListMatcher<T> matcher;

	public MatcherOfFold(IOnListMatch<T> callback, IListMatcher<T> matcher)
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

	protected void init(IOnListMatch<T> callback, IListMatcher<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
	}

	public static <T> MatcherOfFold<T> of(IOnListMatch<T> callback, IListMatcher<T> matcher)
	{
		return new MatcherOfFold<T>(callback, matcher);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
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
		else
		{
			Optional<MatchResultList<T>> result = matcher.matchl(str, start, temporary);

			if(!result.isPresent())
			{
				return Optional.empty();
			}
			else
			{
				MatchResultList<T> m = result.get();

				return Optional.of(MatchResult.of(m.range, Optional.of(callback.onmatch(str, m.range.start, m.range.end, m))));
			}
		}
	}
}
