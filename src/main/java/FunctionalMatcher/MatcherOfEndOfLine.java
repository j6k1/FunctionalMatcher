package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfEndOfLine<T,R> implements IMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;

	protected MatcherOfEndOfLine(IOnMatch<T,R> callback)
	{
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfEndOfLine<T,R> of(IOnMatch<T,R> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfEndOfLine<T,R>(callback);
	}

	public static <T> MatcherOfEndOfLine<T,T> of(MatchResultType<T> t)
	{
		return new MatcherOfEndOfLine<T,T>((str, start, end, m) -> Optional.empty());
	}

	public static MatcherOfEndOfLine<Nothing,Nothing> of()
	{
		return new MatcherOfEndOfLine<Nothing,Nothing>((str, start, end, m) -> Optional.empty());
	}

	@Override
	public Optional<MatchResult<R>> match(State state) {
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

		int l = str.length();
		char c;

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start < l && (c = str.charAt(start)) != '\r' && c != '\n')
		{
			return Optional.empty();
		}
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start),
								emptyCallback.onmatch(str, start, start, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start),
								callback.onmatch(str, start, start, Optional.empty())));
		}
	}
}
