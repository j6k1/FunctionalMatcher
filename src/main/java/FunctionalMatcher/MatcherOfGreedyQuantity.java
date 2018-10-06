package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MatcherOfGreedyQuantity<T,R> implements IMatcher<R>, IListMatcher<R> {
	protected final int startTimes;
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final IContinuationMatcher<T> matcher;

	protected MatcherOfGreedyQuantity(IOnMatch<T,R> callback, IContinuationMatcher<T> matcher, int startTimes)
	{
		this.matcher = matcher;
		this.startTimes = startTimes;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
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
			Optional<IContinuation<T>> result = matcher.matchc(State.of(str, current, temporary));

			if(result.isPresent())
			{
				IContinuation<T> r = result.get();
				MatchResult<T> m = r.result();

				if(current == m.range.end)
				{
					break;
				}

				current = m.range.end;

				if(r instanceof ITermination)
				{
					if(i < startTimes) return Optional.empty();
					else break;
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

		if(temporary)
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

		for(int i=0; current <= l; i++)
		{
			Optional<IContinuation<T>> result = matcher.matchc(State.of(str, current, temporary));

			if(result.isPresent())
			{
				IContinuation<T> r = result.get();
				MatchResult<T> m = r.result();

				if(temporary)
				{
					resultList.add(MatchResult.of(
									m.range, emptyCallback.onmatch(str, start, m.range.end, Optional.of(m))));
				}
				else
				{
					resultList.add(MatchResult.of(
							m.range, callback.onmatch(str, start, m.range.end, Optional.of(m))));
				}

				if(current == m.range.end)
				{
					break;
				}

				current = m.range.end;

				if(r instanceof ITermination)
				{
					if(i < startTimes) return Optional.empty();
					else break;
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

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
	}
}
