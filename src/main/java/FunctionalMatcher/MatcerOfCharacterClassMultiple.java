package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcerOfCharacterClassMultiple<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final ArrayList<IMatcherOfCharacterClass<T>> matcherList;

	protected MatcerOfCharacterClassMultiple(ArrayList<IMatcherOfCharacterClass<T>> matcherList, IOnMatch<T> callback)
	{
		this.matcherList = matcherList;
		this.callback = callback;
	}

	public static <T> MatcerOfCharacterClassMultiple<T> of(ArrayList<IMatcherOfCharacterClass<T>> matcherList, IOnMatch<T> callback)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcerOfCharacterClassMultiple<T>(matcherList, callback);
	}

	public static MatcerOfCharacterClassMultiple<Nothing> of(ArrayList<IMatcherOfCharacterClass<Nothing>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcerOfCharacterClassMultiple<Nothing>(matcherList, null);
	}

	public static <T> MatcerOfCharacterClassMultiple<T> of(IMatcherOfCharacterClass<T> matcher, IOnMatch<T> callback)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		ArrayList<IMatcherOfCharacterClass<T>> lst = new ArrayList<IMatcherOfCharacterClass<T>>();
		lst.add(matcher);
		return new MatcerOfCharacterClassMultiple<T>(lst, callback);
	}

	public static <T> MatcerOfCharacterClassMultiple<T> of(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcerOfCharacterClassMultiple<T>(new ArrayList<IMatcherOfCharacterClass<T>>(), callback);
	}


	public static <T> MatcerOfCharacterClassMultiple<T> of(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcherOfCharacterClass<T>> lst = new ArrayList<IMatcherOfCharacterClass<T>>();
		lst.add(matcher);
		return new MatcerOfCharacterClassMultiple<T>(lst, null);
	}

	public MatcerOfCharacterClassMultiple<T> add(IMatcherOfCharacterClass<T> matcher)
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

		if(matcherList.size() == 0) throw new MatcherEmptyException("Matcher is not set.");
		else if(start < 0)
		{
			throw new InvalidMatchStateException("A negative value was specified for the current position.");
		}
		else if(start >= str.length() + 1)
		{
			throw new InvalidMatchStateException("The current position is outside the content range.");
		}
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
												callback.onmatch(str,
														new Range(start, m.range.end),
														Optional.of(m)))));
					}
				}
			}

			return Optional.empty();
		}
	}
}
