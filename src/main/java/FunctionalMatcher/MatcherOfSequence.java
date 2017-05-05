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
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfSequence<T>(matcherList, callback);
	}

	public static MatcherOfSequence<Nothing> of(ArrayList<IMatcher<Nothing>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcherOfSequence<Nothing>(matcherList, null);
	}


	public static <T> MatcherOfSequence<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		ArrayList<IMatcher<T>> lst = new ArrayList<IMatcher<T>>();
		lst.add(matcher);
		return new MatcherOfSequence<T>(lst, callback);
	}

	public static <T> MatcherOfSequence<T> of(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfSequence<T>(new ArrayList<IMatcher<T>>(), callback);
	}

	public static <T> MatcherOfSequence<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcher<T>> lst = new ArrayList<IMatcher<T>>();
		lst.add(matcher);
		return new MatcherOfSequence<T>(lst, null);
	}

	public MatcherOfSequence<T> add(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		matcherList.add(matcher);
		return this;
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int l = str.length();

		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
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
									Optional.of(
										callback.onmatch(
											str, new Range(start, current), Optional.empty()))));
			}
		}
	}

	@Override
	public Optional<MatchResultList<T>> matchl(String str, int start, boolean temporary)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		int l = str.length();

		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= l + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
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
					resultList.add(MatchResult.of(
									m.range, Optional.of(
										callback.onmatch(str, new Range(start, current), Optional.of(m)))));
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
