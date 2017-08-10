package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Optional;

public class MatcherExecutor {
	public static <T> Optional<MatchResult<T>> exec(String str, IMatcher<T> matcher)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		return matcher.match(str, 0, false);
	}

	public static <T> IMatchFinder<T> find(String str, IMatcher<T> matcher)
	{
		return MatchFinder.of(matcher, str, 0);
	}

	public static <T> Optional<ArrayList<MatchResult<T>>> findAll(String str, IMatcher<T> matcher)
	{
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}
		else if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<MatchResult<T>> results = new ArrayList<>();

		for(int i=0, l=str.length(); i <= l; i++)
		{
			Optional<MatchResult<T>> result = Optional.empty();

			while(i <= l && !(result = matcher.match(str, i, false)).isPresent())
			{
				i++;
			}

			result.ifPresent(r -> results.add(r));
		}

		return results.size() > 0 ? Optional.of(results) : Optional.empty();
	}
}
