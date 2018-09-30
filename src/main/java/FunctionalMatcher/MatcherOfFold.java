package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfFold<T,R> implements IMatcher<R> {
	protected final IOnListMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final IListMatcher<T> matcher;

	protected MatcherOfFold(IOnListMatch<T,R> callback, IListMatcher<T> matcher)
	{
		this.matcher = matcher;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfFold<T,R> of(IOnListMatch<T,R> callback, IListMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfFold<T,R>(callback, matcher);
	}

	@Override
	public Optional<MatchResult<R>> match(State state) {
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		final String str = state.str;
		final int start = state.start;

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
			Optional<MatchResultList<T>> result = matcher.matchl(state);

			if(!result.isPresent())
			{
				return Optional.empty();
			}
			else
			{
				MatchResultList<T> m = result.get();

				return Optional.of(
						MatchResult.of(
							m.range, callback.onmatch(str, m.range.start, m.range.end, m)));
			}
		}
	}
}
