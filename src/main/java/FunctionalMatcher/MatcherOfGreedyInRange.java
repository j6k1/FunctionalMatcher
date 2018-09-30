package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfGreedyInRange<T,R> implements IMatcher<R>, IListMatcher<R> {
	protected final int startTimes;
	protected final int endTimes;
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final IContinuationMatcher<T> matcher;

	protected MatcherOfGreedyInRange(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher,
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

		this.matcher = matcher;
		this.startTimes = startTimes;
		this.endTimes = endTimes;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfGreedyInRange<T,R> of(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher,
													int startTimes, int endTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfGreedyInRange<T,R>(callback, matcher, startTimes, endTimes);
	}

	public static <T> MatcherOfGreedyInRange<T,T> of(IContinuationMatcher<T> matcher,
													int startTimes, int endTimes)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return new MatcherOfGreedyInRange<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcher, startTimes, endTimes);
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
			Optional<IContinuation<T>> result = matcher.matchc(State.of(str, current, temporary));

			if(result.isPresent())
			{
				IContinuation<T> r = result.get();
				MatchResult<T> m = r.result();

				if(r instanceof ITermination)
				{
					lastEnd = (i < startTimes) ? -1 : m.range.end;
					break;
				}
				else if(current == m.range.end)
				{
					lastEnd = (i < startTimes) ? -1 : current;
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
			Optional<IContinuation<T>> result = matcher.matchc(State.of(str, current, temporary));

			if(result.isPresent())
			{
				IContinuation<T> r = result.get();
				MatchResult<T> m = r.result();

				if(temporary)
				{
					resultList.add(MatchResult.of(m.range,
							emptyCallback.onmatch(str, start, m.range.end, Optional.of(m))));
				}
				else
				{
					resultList.add(MatchResult.of(m.range,
						callback.onmatch(str, start, m.range.end, Optional.of(m))));
				}

				if(r instanceof ITermination)
				{
					lastEnd = (i < startTimes) ? -1 : m.range.end;
					break;
				}
				else if(current == m.range.end)
				{
					lastEnd = (i < startTimes) ? -1 : current;
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
		else
		{
			return Optional.of(MatchResultList.of(new Range(start, current), resultList));
		}
	}
}
