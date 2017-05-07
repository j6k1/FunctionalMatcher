package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MatcherOfGreedyQuantity<T> implements IMatcher<T>, IListMatcher<T> {
	protected int startTimes;
	protected IOnMatch<T> callback;
	protected IMatcher<T> matcher;

	public MatcherOfGreedyQuantity(IMatcher<T> matcher, int startTimes, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(matcher, startTimes, callback);
	}

	public MatcherOfGreedyQuantity(IMatcher<T> matcher, int startTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		init(matcher, startTimes, null);
	}

	protected void init(IMatcher<T> matcher, int startTimes, IOnMatch<T> callback)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		this.matcher = matcher;
		this.startTimes = startTimes;
		this.callback = callback;
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary) {
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

		for(int i=0; current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, true);

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				current = m.range.end;
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
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary) {
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

		for(int i=0; current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(result.isPresent())
			{
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

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
	}
}
