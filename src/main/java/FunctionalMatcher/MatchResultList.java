package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Iterator;

public class MatchResultList<T> implements Iterable<MatchResult<T>> {
	public final Range range;
	public final ArrayList<MatchResult<T>> results;

	protected MatchResultList(Range range, ArrayList<MatchResult<T>> results)
	{
		this.range = range;
		this.results = results;
	}

	public Iterator<MatchResult<T>> iterator()
	{
		return results.iterator();
	}

	public static <T> MatchResultList<T> of(Range range, ArrayList<MatchResult<T>> results)
	{
		return new MatchResultList<T>(range, results);
	}

	public MatchResult<T> get(int index)
	{
		return results.get(index);
	}

	public int size()
	{
		return results.size();
	}

	@Override
	public boolean equals(Object o)
	{
		if(o instanceof MatchResultList)
		{
			@SuppressWarnings("rawtypes")
			MatchResultList r = (MatchResultList)o;

			return (this.range.equals(r.range) && this.results.equals(r.results));
		}
		else
		{
			return false;
		}
	}
}
