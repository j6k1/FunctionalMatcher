package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfLongestInRange<T> implements IMatcher<T>, IListMatcher<T> {
	protected final int startTimes;
	protected final int endTimes;
	protected IOnMatch<T> callback;
	protected final IMatcher<T> matcher;
	protected final IMatcher<T> anchor;

	protected MatcherOfLongestInRange(IMatcher<T> matcher, IMatcher<T> anchor,
										int startTimes, int endTimes, IOnMatch<T> callback)
	{
		if(startTimes > endTimes)
		{
			throw new InvalidRangeException("A value greater than end was specified as the value of start.");
		}
		this.matcher = matcher;
		this.anchor = anchor;
		this.startTimes = startTimes;
		this.endTimes = endTimes;
		this.callback = callback;
	}

	public static <T> MatcherOfLongestInRange<T> of(IMatcher<T> matcher, IMatcher<T> anchor,
												int startTimes, int endTimes, IOnMatch<T> callback)
	{
		return new MatcherOfLongestInRange<T>(matcher, anchor, startTimes, endTimes, callback);
	}

	public static <T> MatcherOfLongestInRange<T> of(IMatcher<T> matcher, IMatcher<T> anchor,
												int startTimes, int endTimes)
	{
		return new MatcherOfLongestInRange<T>(matcher, anchor, startTimes, endTimes, null);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		// TODO 自動生成されたメソッド・スタブ
		int current = start;
		int lastEnd = -1;
		int l = str.length();

		for(int i=0; i < endTimes && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, true);

			MatchResult<T> m = null;

			if(result.isPresent() && anchor.match(str, (m = result.get()).range.end, true).isPresent())
			{
				current = m.range.end;
			}
			else if(!result.isPresent() && i < startTimes)
			{
				return Optional.empty();
			}
			else if(!result.isPresent())
			{
				break;
			}
		}

		if(lastEnd == -1)
		{
			return Optional.empty();
		}
		else if(callback == null || temporary)
		{
			return Optional.of(MatchResult.of(new Range(start, lastEnd), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, lastEnd),
								Optional.of(callback.onmatch(str, MatchResult.of(
									new Range(start, lastEnd), Optional.empty())))));
		}
	}

	@Override
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary) {
		int current = start;
		int lastEnd = -1;
		int l = str.length();
		ArrayList<MatchResult<T>> tempResultList = new ArrayList<>();
		ArrayList<MatchResult<T>> resultList = new ArrayList<>();

		for(int i=0; i < endTimes && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			MatchResult<T> m = null;

			if(result.isPresent())
			{
				current = m.range.end;

				tempResultList.add(m);

				if(anchor.match(str, (m = result.get()).range.end, true).isPresent())
				{
					lastEnd = current;

					if(callback != null && !temporary)
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(MatchResult.of(m.range, Optional.of(callback.onmatch(str, t))));
						}
					}
					else
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(m);
						}
					}
					tempResultList = new ArrayList<>();
				}
			}
			else if(!result.isPresent() && i < startTimes)
			{
				return Optional.empty();
			}
			else if(!result.isPresent())
			{
				break;
			}
		}

		if(lastEnd == -1)
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(MatchResultList.of(new Range(start, lastEnd), resultList));
		}
	}
}
