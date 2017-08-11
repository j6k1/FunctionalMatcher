package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Iterator;

public class MatcherOfFixedLengthSelect<T> implements Iterable<IFixedLengthMatcher<T>> {
	protected final ArrayList<IFixedLengthMatcher<T>> matcherList;

	protected MatcherOfFixedLengthSelect(ArrayList<IFixedLengthMatcher<T>> matcherList)
	{
		this.matcherList = matcherList;
	}

	public static <T> MatcherOfFixedLengthSelect<T> of(ArrayList<IFixedLengthMatcher<T>> matcherList)
	{
		if(matcherList == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcherList is null.");
		}

		return new MatcherOfFixedLengthSelect<T>(matcherList);
	}

	public static <T> MatcherOfFixedLengthSelect<T> of(IFixedLengthMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		ArrayList<IFixedLengthMatcher<T>> lst = new ArrayList<IFixedLengthMatcher<T>>();
		lst.add(matcher);

		return new MatcherOfFixedLengthSelect<T>(lst);
	}

	public MatcherOfFixedLengthSelect<T> add(IFixedLengthMatcher<T> matcher)
	{
		if(matcher == null)
		{
			throw new NullReferenceNotAllowedException("The reference to the argument matcher is null.");
		}

		matcherList.add(matcher);

		return this;
	}

	@Override
	public Iterator<IFixedLengthMatcher<T>> iterator() {
		return matcherList.iterator();
	}
}
