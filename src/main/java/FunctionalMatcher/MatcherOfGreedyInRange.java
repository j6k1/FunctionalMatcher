package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfGreedyInRange<T> implements IMatcher<T>, IListMatcher<T> {
	protected int startTimes;
	protected int endTimes;
	protected IOnMatch<T> callback;
	protected IMatcher<T> matcher;

	public MatcherOfGreedyInRange(IMatcher<T> matcher,
										int startTimes, int endTimes, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(matcher, startTimes, endTimes, callback);
	}

	public MatcherOfGreedyInRange(IMatcher<T> matcher, int startTimes, int endTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		init(matcher, startTimes, endTimes, null);
	}

	protected void init(IMatcher<T> matcher, int startTimes, int endTimes, IOnMatch<T> callback)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(startTimes > endTimes)
		{
			throw new InvalidRangeException("A value greater than end was specified as the value of start.");
		}
		this.matcher = matcher;
		this.startTimes = startTimes;
		this.endTimes = endTimes;
		this.callback = callback;
	}

	public static <T> MatcherOfGreedyInRange<T> of(IMatcher<T> matcher,
													int startTimes, int endTimes, IOnMatch<T> callback)
	{
		return new MatcherOfGreedyInRange<T>(matcher, startTimes, endTimes, callback);
	}

	public static <T> MatcherOfGreedyInRange<T> of(IMatcher<T> matcher,
													int startTimes, int endTimes)
	{
		return new MatcherOfGreedyInRange<T>(matcher, startTimes, endTimes);
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int lastEnd = -1;
		int l = str.length();

		if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}

		for(int i=0; i < endTimes && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, true);

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				current = m.range.end;
				lastEnd = current;
			}
			else if(!result.isPresent() && i < startTimes)
			{
				return Optional.empty();
			}
			else if(!result.isPresent())
			{
				if(i == 0 && start == l) return Optional.empty();
				else if(i == 0) current++;
				break;
			}
		}

		if(lastEnd == -1)
		{
			return Optional.empty();
		}
		else if(callback == null || temporary)
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
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int lastEnd = -1;
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

		for(int i=0; i < endTimes && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				current = m.range.end;
				lastEnd = current;

				if(callback != null && !temporary)
				{
					resultList.add(MatchResult.of(m.range,
									Optional.of(
										callback.onmatch(str, new Range(start, current), Optional.of(m)))));
				}
				else
				{
					resultList.add(m);
				}
			}
			else if(!result.isPresent() && i < startTimes)
			{
				return Optional.empty();
			}
			else if(!result.isPresent())
			{
				if(i == 0 && start == l) return Optional.empty();
				else if(i == 0) current++;
				break;
			}
		}

		if(lastEnd == -1)
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(MatchResultList.of(new Range(start, current), resultList));
		}
	}
}
