package FunctionalMatcher;

import java.util.Optional;

public class MatchFinder<T> implements IMatchFinder<T> {
	protected IMatcher<T> matcher;
	protected String str;
	protected int start;

	protected MatchFinder(IMatcher<T> matcher, String str, int start)
	{
		this.matcher = matcher;
		this.str = str;
		this.start = start;
	}

	public static <T> MatchFinder<T> of(IMatcher<T> matcher, String str, int start)
	{
		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatchFinder<T>(matcher, str, start);
	}

	@Override
	public Optional<MatchResult<T>> next() {
		if(start == -1)
		{
			return Optional.empty();
		}
		else
		{
			int index = start;
			int length = str.length();

			Optional<MatchResult<T>> r = Optional.empty();

			for(; index <= length && !(r = matcher.match(str, index, false)).isPresent(); index++);

			if(index <= length)	start = index;
			else start = -1;

			return r;
		}
	}
}
