package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfCharacterClassMultiple<T> implements IMatcher<T> {
	protected IOnMatch<T> callback;
	protected final ArrayList<IMatcherOfCharacterClass<T>> matcherList = new ArrayList<>();

	public MatcherOfCharacterClassMultiple(IOnMatch<T> callback, ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, matcherList);
	}

	public MatcherOfCharacterClassMultiple(ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		init(null, matcherList);
	}

	public MatcherOfCharacterClassMultiple(IOnMatch<T> callback, IMatcherOfCharacterClass<T> matcher)
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

		init(callback, lst);
	}

	public MatcherOfCharacterClassMultiple(IOnMatch<T> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		init(callback, new ArrayList<IMatcherOfCharacterClass<T>>());
	}

	public MatcherOfCharacterClassMultiple(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcherOfCharacterClass<T>> lst = new ArrayList<IMatcherOfCharacterClass<T>>();
		lst.add(matcher);
		init(null, lst);
	}

	protected void init(IOnMatch<T> callback, ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		this.matcherList.addAll(matcherList);
		this.callback = callback;
	}

	public static <T> MatcherOfCharacterClassMultiple<T> of(IOnMatch<T> callback, ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		return new MatcherOfCharacterClassMultiple<T>(callback, matcherList);
	}

	public static <T> MatcherOfCharacterClassMultiple<T> of(ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		return new MatcherOfCharacterClassMultiple<T>(matcherList);
	}

	public static <T> MatcherOfCharacterClassMultiple<T> of(IOnMatch<T> callback, IMatcherOfCharacterClass<T> matcher)
	{
		return new MatcherOfCharacterClassMultiple<T>(callback, matcher);
	}

	public static <T> MatcherOfCharacterClassMultiple<T> of(IOnMatch<T> callback)
	{
		return new MatcherOfCharacterClassMultiple<T>(callback);
	}

	public static <T> MatcherOfCharacterClassMultiple<T> of(IMatcherOfCharacterClass<T> matcher)
	{
		return new MatcherOfCharacterClassMultiple<T>(matcher);
	}

	public MatcherOfCharacterClassMultiple<T> add(IMatcherOfCharacterClass<T> matcher)
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
		else if(start == l)
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
												callback.onmatch(str, start, m.range.end,
																				Optional.of(m)))));
					}
				}
			}

			return Optional.empty();
		}
	}
}
