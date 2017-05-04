package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcerOfCharacterClassMultiple<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final ArrayList<IMatcerOfCharacterClass<T>> matcherList;

	protected MatcerOfCharacterClassMultiple(ArrayList<IMatcerOfCharacterClass<T>> matcherList, IOnMatch<T> callback)
	{
		this.matcherList = matcherList;
		this.callback = callback;
	}

	public static <T> MatcerOfCharacterClassMultiple<T> of(ArrayList<IMatcerOfCharacterClass<T>> matcherList, IOnMatch<T> callback)
	{
		return new MatcerOfCharacterClassMultiple<T>(matcherList, callback);
	}

	public static MatcerOfCharacterClassMultiple<Nothing> of(ArrayList<IMatcerOfCharacterClass<Nothing>> matcherList)
	{
		return new MatcerOfCharacterClassMultiple<Nothing>(matcherList, null);
	}

	public static <T> MatcerOfCharacterClassMultiple<T> of(IOnMatch<T> callback)
	{
		return new MatcerOfCharacterClassMultiple<T>(new ArrayList<IMatcerOfCharacterClass<T>>(), callback);
	}

	public MatcerOfCharacterClassMultiple<T> add(IMatcerOfCharacterClass<T> matcher)
	{
		matcherList.add(matcher);
		return this;
	}

	@Override
	public Optional<MatchResult<T>> match(String str, int start, boolean temporary)
	{
		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start >= str.length())
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
