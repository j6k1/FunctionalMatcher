package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfRepetition<T> implements IMatcher<T>, IListMatcher<T> {
	protected IOnMatch<T> callback;
	protected IMatcher<T> matcher;
	protected int times;

	public MatcherOfRepetition(IMatcher<T> matcher, int times, IOnMatch<T> callback)
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

		init(matcher, times, callback);
	}

	public MatcherOfRepetition(IMatcher<T> matcher, int times)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		init(matcher, times, null);
	}

	protected void init(IMatcher<T> matcher, int times, IOnMatch<T> callback)
	{
		this.matcher = matcher;
		this.times = times;
		this.callback = callback;
	}

	public static <T> MatcherOfRepetition<T> of(IMatcher<T> matcher, int times, IOnMatch<T> callback)
	{
		return new MatcherOfRepetition<T>(matcher, times, callback);
	}

	public static <T> MatcherOfRepetition<T> of(IMatcher<T> matcher, int times)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfRepetition<T>(matcher, times);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
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

		if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, current), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, current),
								Optional.of(
									callback.onmatch(str, new Range(start, current), Optional.empty()))));
		}
	}

	@Override
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int l = str.length();
		ArrayList<MatchResult<T>> resultList = new ArrayList<>();

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

			if(callback != null && !temporary)
			{
				resultList.add(MatchResult.of(
								m.range, Optional.of(
									callback.onmatch(str, new Range(start, current), Optional.of(m)))));
			}
			else
			{
				resultList.add(m);
			}
		}

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
	}
}
