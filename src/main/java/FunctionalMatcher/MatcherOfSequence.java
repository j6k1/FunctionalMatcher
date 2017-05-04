package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfSequence<T> implements IMatcher<T>, IListMatcher<T> {
	protected IOnMatch<T> callback;
	protected final ArrayList<IMatcher<T>> matcherList;

	protected MatcherOfSequence(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		this.matcherList = matcherList;
		this.callback = callback;
	}

	public static <T> MatcherOfSequence<T> of(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		return new MatcherOfSequence<T>(matcherList, callback);
	}

	public static MatcherOfSequence<Nothing> of(ArrayList<IMatcher<Nothing>> matcherList)
	{
		return new MatcherOfSequence<Nothing>(matcherList, null);
	}

	public static <T> MatcherOfSequence<T> of(IOnMatch<T> callback)
	{
		return new MatcherOfSequence<T>(new ArrayList<IMatcher<T>>(), callback);
	}

	public MatcherOfSequence<T> add(IMatcher<T> matcher)
	{
		matcherList.add(matcher);
		return this;
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start >= str.length() + 1)
		{
			return Optional.empty();
		}
		else
		{
			int current = start;

			for(IMatcher<T> matcher: matcherList)
			{
				Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

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
	}

	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary)
	{
		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start >= str.length() + 1)
		{
			return Optional.empty();
		}
		else
		{
			int current = start;
			ArrayList<MatchResult<T>> resultList = new ArrayList<>();

			for(IMatcher<T> matcher: matcherList)
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
}
