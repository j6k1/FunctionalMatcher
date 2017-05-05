package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MatcherOfShortestQuantity<T> implements IMatcher<T>, IListMatcher<T> {
	protected final int startTimes;
	protected IOnMatch<T> callback;
	protected final IMatcher<T> matcher;
	protected final IMatcher<T> anchor;

	protected MatcherOfShortestQuantity(IMatcher<T> matcher, IMatcher<T> anchor,
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

				if(i + 1 >= startTimes)
				{
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
												callback.onmatch(
													str, new Range(start, current), Optional.empty()))));
					}
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

		return Optional.empty();
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
										callback.onmatch(
											str, new Range(start, current), Optional.of(m)))));
				}
				else
				{
					resultList.add(m);
				}

				if(anchor.match(str, (m = result.get()).range.end, true).isPresent())
				{
					lastEnd = current;
					break;
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
			return Optional.of(MatchResultList.of(new Range(start, current), resultList));
		}
	}
}