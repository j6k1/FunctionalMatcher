package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherOfCharacterClassMultiple<T,R> implements IFixedLengthMatcher<R> {
	protected IOnMatch<T,R> callback;
	protected IOnMatch<T,R> emptyCallback;
	protected final ArrayList<IMatcherOfCharacterClass<T>> matcherList = new ArrayList<>();

	protected MatcherOfCharacterClassMultiple(IOnMatch<T,R> callback, ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		this.matcherList.addAll(matcherList);
		this.callback = callback;
		this.emptyCallback = (str, start, end, m) -> Optional.empty();
	}

	protected MatcherOfCharacterClassMultiple(IOnMatch<T,R> callback)
	{
		this(callback, new ArrayList<IMatcherOfCharacterClass<T>>());
	}

	public static <T,R> MatcherOfCharacterClassMultiple<T,R> of(IOnMatch<T,R> callback, ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}
		else if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfCharacterClassMultiple<T,R>(callback, matcherList);
	}

	public static <T> MatcherOfCharacterClassMultiple<T,T> of(ArrayList<IMatcherOfCharacterClass<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcherOfCharacterClassMultiple<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, matcherList);
	}

	public static <T,R> MatcherOfCharacterClassMultiple<T,R> of(IOnMatch<T,R> callback, IMatcherOfCharacterClass<T> matcher)
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

		return new MatcherOfCharacterClassMultiple<T,R>(callback, lst);
	}

	public static <T,R> MatcherOfCharacterClassMultiple<T,R> of(IOnMatch<T,R> callback)
	{
		if(callback == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument callback is null.");
		}

		return new MatcherOfCharacterClassMultiple<T,R>(callback);
	}

	public static <T> MatcherOfCharacterClassMultiple<T,T> of(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IMatcherOfCharacterClass<T>> lst = new ArrayList<IMatcherOfCharacterClass<T>>();
		lst.add(matcher);

		return new MatcherOfCharacterClassMultiple<T,T>((str, start, end, m) -> {
			return m.flatMap(r -> r.value);
		}, lst);
	}

	public MatcherOfCharacterClassMultiple<T,R> add(IMatcherOfCharacterClass<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		matcherList.add(matcher);
		return this;
	}

	@Override
	public int length()
	{
		return 1;
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

					if(temporary)
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											emptyCallback.onmatch(str, start, m.range.end,
																				Optional.of(m))));
					}
					else
					{
						return Optional.of(
								MatchResult.of(
										new Range(start, m.range.end),
											callback.onmatch(str, start, m.range.end,
																				Optional.of(m))));
					}
				}
			}

			return Optional.empty();
		}
	}
}
