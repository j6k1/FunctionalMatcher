package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MatcherOfLongestQuantity<T,R> implements IMatcher<R>, IListMatcher<R> {
	protected int startTimes;
	protected IOnMatch<T,R> callback;
	protected IOnMatch<T,R> emptyCallback;
	protected IMatcher<T> matcher;
	protected IMatcher<T> anchor;

	protected MatcherOfLongestQuantity(IOnMatch<T,R> callback,
										IMatcher<T> matcher, IMatcher<T> anchor, int startTimes)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		this.matcher = matcher;
		this.anchor = anchor;
		this.startTimes = startTimes;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary) {
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
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			MatchResult<T> m = null;

			if(result.isPresent() && anchor.match(str, (m = result.get()).range.end, true).isPresent())
			{
				if(current == m.range.end)
				{
					lastEnd = current;
					break;
				}
				else
				{
					current = m.range.end;
					lastEnd = current;
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
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, lastEnd),
								emptyCallback.onmatch(
												str, start, lastEnd, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, lastEnd),
								callback.onmatch(
												str, start, lastEnd, Optional.empty())));
		}
	}

	@Override
	public Optional<MatchResultList<R>> matchl(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int current = start;
		int lastEnd = -1;
		int l = str.length();
		ArrayList<MatchResult<T>> tempResultList = new ArrayList<>();
		ArrayList<MatchResult<R>> resultList = new ArrayList<>();

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

				tempResultList.add(m);

				if(anchor.match(str, (m = result.get()).range.end, true).isPresent())
				{
					lastEnd = m.range.end;

					if(temporary)
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(MatchResult.of(
											m.range, emptyCallback.onmatch(
														str, start, m.range.end, Optional.of(t))));
						}
					}
					else
					{
						for(MatchResult<T> t: tempResultList)
						{
							resultList.add(MatchResult.of(
									m.range, callback.onmatch(
												str, start, m.range.end, Optional.of(t))));
						}
					}
					tempResultList = new ArrayList<>();
				}

				if(current == m.range.end) break;
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
		else
		{
			return Optional.of(MatchResultList.of(new Range(start, lastEnd), resultList));
		}
	}
}
