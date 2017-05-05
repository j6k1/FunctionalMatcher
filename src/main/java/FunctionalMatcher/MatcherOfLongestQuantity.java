package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MatcherOfLongestQuantity<T> implements IMatcher<T>, IListMatcher<T> {
	protected final int startTimes;
	protected IOnMatch<T> callback;
	protected final IMatcher<T> matcher;
	protected final IMatcher<T> anchor;

	protected MatcherOfLongestQuantity(IMatcher<T> matcher, IMatcher<T> anchor,
										int startTimes, IOnMatch<T> callback)
	{
		this.matcher = matcher;
		this.anchor = anchor;
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

		for(int i=0; current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, true);

			MatchResult<T> m = null;

			if(result.isPresent() && anchor.match(str, (m = result.get()).range.end, true).isPresent())
			{
				current = m.range.end;
				lastEnd = current;
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
								Optional.of(
										callback.onmatch(
												str, new Range(start, lastEnd), Optional.empty()))));
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
		ArrayList<MatchResult<T>> tempResultList = new ArrayList<>();
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

				tempResultList.add(m);

				if(anchor.match(str, (m = result.get()).range.end, true).isPresent())
				{
					lastEnd = current;

					if(callback != null && !temporary)
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(MatchResult.of(
											m.range, Optional.of(
												callback.onmatch(
														str, new Range(start, current), Optional.of(t)))));
						}
					}
					else
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(t);
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