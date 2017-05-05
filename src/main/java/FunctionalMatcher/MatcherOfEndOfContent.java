package FunctionalMatcher;

import java.util.Optional;

public class MatcherOfEndOfContent<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;

	protected MatcherOfEndOfContent(IOnMatch<T> callback)
	{
		this.callback = callback;
	}

	public static <T> MatcherOfEndOfContent<T> of(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfEndOfContent<T>(callback);
	}

	public static MatcherOfEndOfContent<Nothing> of()
	{
		return new MatcherOfEndOfContent<Nothing>(null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int l = str.length();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
		else if(start != l)
		{
			return Optional.empty();
		}
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, start), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, start),
								Optional.of(
									callback.onmatch(str, new Range(start, start), Optional.empty()))));
		}
	}
}