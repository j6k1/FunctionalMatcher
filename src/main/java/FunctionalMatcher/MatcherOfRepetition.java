package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfRepetition<T> implements IMatcher<T>, IListMatcher<T> {
	protected IOnMatch<T> callback;
	protected final IMatcher<T> matcher;
	protected final int times;

	protected MatcherOfRepetition(IMatcher<T> matcher, int times, IOnMatch<T> callback)
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
		return new MatcherOfRepetition<T>(matcher, times, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		int current = start;
		int l = str.length();

		for(int i=0; i < times && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, true);

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
								Optional.of(callback.onmatch(str, MatchResult.of(
									new Range(start, current), Optional.empty())))));
		}
	}

	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary)
	{
		int current = start;
		int l = str.length();
		ArrayList<MatchResult<T>> resultList = new ArrayList<>();

		for(int i=0; i < times && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(!result.isPresent()) return Optional.empty();

			MatchResult<T> m = result.get();

			current = m.range.end;

			if(callback != null && !temporary)
			{
				resultList.add(MatchResult.of(m.range, Optional.of(callback.onmatch(str, m))));
			}
			else
			{
				resultList.add(m);
			}
		}

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
	}
}
