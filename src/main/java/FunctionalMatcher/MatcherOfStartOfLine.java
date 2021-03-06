package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfStartOfLine<T,R> implements IMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;

	protected MatcherOfStartOfLine(IOnMatch<T,R> callback)
	{
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfStartOfLine<T,R> of(IOnMatch<T,R> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfStartOfLine<T,R>(callback);
	}

	public static <T> MatcherOfStartOfLine<T,T> of(MatchResultType<T> t)
	{
		return new MatcherOfStartOfLine<T,T>((str, start, end, m) -> Optional.empty());
	}

	public static MatcherOfStartOfLine<Nothing,Nothing> of()
	{
		return new MatcherOfStartOfLine<Nothing,Nothing>((str, start, end, m) -> Optional.empty());
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
		else if(start != 0 && (c = str.charAt(start - 1)) != '\n' && c != '\r')
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
