package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfSelect<T,R> implements IMatcher<R> {
	protected final IOnMatch<T,R> callback;
	protected final IOnMatch<T,R> emptyCallback;
	protected final ArrayList<IMatcher<T>> matcherList;

	protected MatcherOfSelect(IOnMatch<T,R> callback, ArrayList<IMatcher<T>> matcherList)
	{
		this.matcherList = matcherList;
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	public static <T,R> MatcherOfSelect<T,R> of(IOnMatch<T,R> callback, ArrayList<IMatcher<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfSelect<T,R>(callback, matcherList);
	}

	public static <T> MatcherOfSelect<T,T> of(ArrayList<IMatcher<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcherOfSelect<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcherList);
	}

	public static <T,R> MatcherOfSelect<T,R> of(IOnMatch<T,R> callback, IMatcher<T> matcher)
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

		return new MatcherOfSelect<T,R>(callback, lst);
	}

	public static <T,R> MatcherOfSelect<T,R> of(IOnMatch<T,R> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfSelect<T,R>(callback, new ArrayList<IMatcher<T>>());
	}

	public static <T> MatcherOfSelect<T,T> of(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcher<T>> lst = new ArrayList<IMatcher<T>>();
		lst.add(matcher);

		return new MatcherOfSelect<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, lst);
	}

	public MatcherOfSelect<T,R> or(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		matcherList.add(matcher);

		return this;
	}

	@Override
	public Optional<MatchResult<R>> match(String str, int start, boolean temporary)
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
			for(IMatcher<T> matcher: matcherList)
			{
				Optional<MatchResult<T>> result = matcher.match(str, start, temporary);

				if(result.isPresent())
				{
					MatchResult<T> m = result.get();

					if(temporary)
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											emptyCallback.onmatch(
													str, start, m.range.end, Optional.of(m))));
					}
					else
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											callback.onmatch(
													str, start, m.range.end, Optional.of(m))));
					}
				}
			}

			return Optional.empty();
		}
	}
}
