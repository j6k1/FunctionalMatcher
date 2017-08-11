package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfRepetition<T,R> implements IMatcher<R>, IListMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final IMatcher<T> matcher;
	protected final int times;

	protected MatcherOfRepetition(IOnMatch<T,R> callback, IMatcher<T> matcher, int times)
	{
		this.matcher = matcher;
		this.times = times;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfRepetition<T,R> of(IOnMatch<T,R> callback, IMatcher<T> matcher, int times)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}
		else if(times < 1)
		{
			throw new InvalidMatchConditionException("A value less than 1 was specified for the number of matches.");
		}

		return new MatcherOfRepetition<T,R>(callback, matcher, times);
	}

	public static <T> MatcherOfRepetition<T,T> of(IMatcher<T> matcher, int times)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(times < 1)
		{
			throw new InvalidMatchConditionException("A value less than 1 was specified for the number of matches.");
		}

		return new MatcherOfRepetition<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, times);
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int l = str.length();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}

		for(int i=0; i < times && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(!result.isPresent()) return Optional.empty();

			MatchResult<T> m = result.get();

			current = m.range.end;
		}

		if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, current),
								emptyCallback.onmatch(str, start, current, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, current),
								callback.onmatch(str, start, current, Optional.empty())));
		}
	}

	@Override
	public Optional<MatchResultList<R>> matchl(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int l = str.length();
		ArrayList<MatchResult<R>> resultList = new ArrayList<>();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}

		for(int i=0; i < times && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(!result.isPresent()) return Optional.empty();

			MatchResult<T> m = result.get();

			current = m.range.end;

			if(temporary)
			{
				resultList.add(MatchResult.of(
								m.range, emptyCallback.onmatch(str, start, current, Optional.of(m))));
			}
			else
			{
				resultList.add(MatchResult.of(
						m.range, callback.onmatch(str, start, current, Optional.of(m))));
			}
		}

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
	}
}
