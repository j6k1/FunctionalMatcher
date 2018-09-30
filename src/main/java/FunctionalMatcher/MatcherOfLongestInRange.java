package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfLongestInRange<T,R> implements IMatcher<R>, IListMatcher<R> {
	protected final int startTimes;
	protected final int endTimes;
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final IMatcher<T> matcher;
	protected final IMatcher<T> anchor;

	protected MatcherOfLongestInRange(IOnMatch<T,R> callback,
										IMatcher<T> matcher,
										IMatcher<T> anchor, int startTimes, int endTimes)
	{
		this.matcher = matcher;
		this.anchor = anchor;
		this.startTimes = startTimes;
		this.endTimes = endTimes;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfLongestInRange<T,R> of(IOnMatch<T,R> callback,
													IMatcher<T> matcher,
													IMatcher<T> anchor,
													int startTimes, int endTimes)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(startTimes > endTimes)
		{
			throw new InvalidRangeException("A value greater than end was specified as the value of start.");
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

		return new MatcherOfLongestInRange<T,R>(callback, matcher, anchor, startTimes, endTimes);
	}

	public static <T> MatcherOfLongestInRange<T,T> of(IMatcher<T> matcher, IMatcher<T> anchor,
												int startTimes, int endTimes)
	{
		if(startTimes < 0)
		{
			throw new InvalidMatchConditionException("A negative value was specified for the number of matches.");
		}
		else if(startTimes > endTimes)
		{
			throw new InvalidRangeException("A value greater than end was specified as the value of start.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(anchor == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument anchor is null.");
		}

		return new MatcherOfLongestInRange<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, anchor, startTimes, endTimes);
	}

	@Override
	public Optional<MatchResult<R>> match(State state) {
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		final String str = state.str;
		final int start = state.start;
		final boolean temporary = state.temporary;

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
			Optional<MatchResult<T>> result = matcher.match(State.of(str, current, temporary));

			MatchResult<T> m = null;

			if(result.isPresent() && anchor.match(State.of(str, (m = result.get()).range.end, true)).isPresent())
			{
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
		else if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, lastEnd),
								emptyCallback.onmatch(str, start, lastEnd, Optional.empty())));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, lastEnd),
								callback.onmatch(str, start, lastEnd, Optional.empty())));
		}
	}

	@Override
	public Optional<MatchResultList<R>> matchl(State state) {
		if(state == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the state.");
		}

		final String str = state.str;
		final int start = state.start;
		final boolean temporary = state.temporary;

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

		for(int i=0; i < endTimes && current <= l; i++)
		{
			Optional<MatchResult<T>> result = matcher.match(State.of(str, current, temporary));

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				tempResultList.add(m);

				if(anchor.match(State.of(str, (m = result.get()).range.end, true)).isPresent())
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
