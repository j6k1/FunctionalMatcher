package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfSequence<T> implements IMatcher<T>, IListMatcher<T> {
	protected final ArrayList<IMatcher<T>> matcherList;

	protected MatcherOfSequence(ArrayList<IMatcher<T>> matcherList)
	{
		this.matcherList = matcherList;
	}

	public static <T> MatcherOfSequence<T> of(ArrayList<IMatcher<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcherOfSequence<T>(matcherList);
	}

	public static <T> MatcherOfSequence<T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcher<T>> lst = new ArrayList<IMatcher<T>>();
		lst.add(matcher);

		return new MatcherOfSequence<T>(lst);
	}

	@Override
	public MatcherOfSequence<T> seq(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		matcherList.add(matcher);

		return this;
	}

	public <R> IMatcher<R> map(IOnMatch<T,R> callback) {
		return (String str, int start, boolean temporary) -> {
			return MatcherOfSequence.this.match(str, start, temporary).map(r -> {
				return MatchResult.of(r.range, callback.onmatch(str, start, r.range.end, Optional.of(r)));
			});
		};
	}

	public <R> IMatcher<R> map(IOnListMatch<T,R> callback) {
		return (String str, int start, boolean temporary) -> {
			return MatcherOfSequence.this.matchl(str, start, temporary).map(r -> {
				return MatchResult.of(r.range, callback.onmatch(str, start, r.range.end, r));
			});
		};
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

		for(IMatcher<T> matcher: matcherList)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				resultList.add(m);

				current = m.range.end;
			}
			else if(!result.isPresent())
			{
				return Optional.empty();
			}
		}

		return Optional.of(MatchResultList.of(new Range(start, current), resultList));
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

		for(IMatcher<T> matcher: matcherList)
		{
			Optional<MatchResult<T>> result = matcher.match(str, current, temporary);

			if(result.isPresent())
			{
				MatchResult<T> m = result.get();

				current = m.range.end;
			}
			else if(!result.isPresent())
			{
				return Optional.empty();
			}
		}

		if(temporary)
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, current), Optional.empty()));
		}
		else
		{
			return Optional.of(
					MatchResult.of(
							new Range(start, current), Optional.empty()));
		}
	}

}
