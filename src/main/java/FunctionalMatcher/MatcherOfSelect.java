package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfSelect<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected ArrayList<IMatcher<T>> matcherList;

	public MatcherOfSelect(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(matcherList, callback);
	}

	public MatcherOfSelect(ArrayList<IMatcher<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		init(matcherList, null);
	}

	public MatcherOfSelect(IMatcher<T> matcher, IOnMatch<T> callback)
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
		init(lst, callback);
	}

	public MatcherOfSelect(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(new ArrayList<IMatcher<T>>(), callback);
	}

	public MatcherOfSelect(IMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcher<T>> lst = new ArrayList<IMatcher<T>>();
		lst.add(matcher);
		init(lst, null);
	}

	protected void init(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		this.matcherList = matcherList;
		this.callback = callback;
	}

	public static <T> MatcherOfSelect<T> of(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		return new MatcherOfSelect<T>(matcherList, callback);
	}

	public static <T> MatcherOfSelect<T> of(ArrayList<IMatcher<T>> matcherList)
	{
		return new MatcherOfSelect<T>(matcherList);
	}

	public static <T> MatcherOfSelect<T> of(IMatcher<T> matcher, IOnMatch<T> callback)
	{
		return new MatcherOfSelect<T>(matcher, callback);
	}

	public static <T> MatcherOfSelect<T> of(IOnMatch<T> callback)
	{
		return new MatcherOfSelect<T>(callback);
	}

	public static <T> MatcherOfSelect<T> of(IMatcher<T> matcher)
	{
		return new MatcherOfSelect<T>(matcher);
	}

	public MatcherOfSelect<T> add(IMatcher<T> matcher)
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
			for(IMatcher<T> matcher: matcherList)
			{
				Optional<MatchResult<T>> result = matcher.match(str, start, temporary);

				if(result.isPresent())
				{
					MatchResult<T> m = result.get();

					if(callback == null || temporary)
					{
						if(m.value.isPresent())
						{
							return Optional.of(MatchResult.of(new Range(start, m.range.end), m.value));
						}
						else
						{
							return Optional.of(MatchResult.of(new Range(start, m.range.end), Optional.empty()));
						}
					}
					else
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											Optional.of(
												callback.onmatch(
													str, new Range(start, m.range.end), Optional.of(m)))));

					}
				}
			}

			return Optional.empty();
		}
	}
}
