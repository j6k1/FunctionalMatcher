package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfSelect<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final ArrayList<IMatcher<T>> matcherList;

	protected MatcherOfSelect(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		this.matcherList = matcherList;
		this.callback = callback;
	}

	public static <T> MatcherOfSelect<T> of(ArrayList<IMatcher<T>> matcherList, IOnMatch<T> callback)
	{
		return new MatcherOfSelect<T>(matcherList, callback);
	}

	public static MatcherOfSelect<Nothing> of(ArrayList<IMatcher<Nothing>> matcherList)
	{
		return new MatcherOfSelect<Nothing>(matcherList, null);
	}

	public static <T> MatcherOfSelect<T> of(IOnMatch<T> callback)
	{
		return new MatcherOfSelect<T>(new ArrayList<IMatcher<T>>(), callback);
	}

	public MatcherOfSelect<T> add(IMatcher<T> matcher)
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
			for(IMatcher<T> matcher: matcherList)
			{
				Optional<MatchResult<T>> result = matcher.match(str, start, temporary);

				if(result.isPresent())
				{
					MatchResult<T> m = result.get();

					if(callback == null || temporary)
					{
						return Optional.of(MatchResult.of(new Range(start, m.range.end), Optional.empty()));
					}
					else
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											Optional.of(callback.onmatch(str, MatchResult.of(
												new Range(start, m.range.end), Optional.empty())))));
					}
				}
			}

			return Optional.empty();
		}
	}
}
