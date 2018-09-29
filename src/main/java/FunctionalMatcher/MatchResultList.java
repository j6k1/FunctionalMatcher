package FunctionalMatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchResultList<T> implements Iterable<MatchResult<T>>  {
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

	public Stream<MatchResult<T>> stream()
	{
		return results.stream();
	}

	public <R> Optional<MatchResult<R>> next(String str, IMatcher<R> matcher)
	{
		return matcher.match(str, range.end, false);
	}

	public <R> Optional<MatchResultList<T>> skip(String str, IMatcher<R> matcher)
	{
		return matcher.match(str, range.end, true).map(r -> this.compositeOfEnd(r.range.end));
	}

	public <R> Optional<MatchResult<R>> back(String str, IFixedLengthMatcher<R> matcher)
	{
		if(range.start - matcher.length() < 0) return Optional.empty();
		else return matcher.match(str, range.start - matcher.length(), true).map(r -> {
			return MatchResult.of(new Range(range.start, range.start), Optional.empty());
		});
	}

	public <R> Optional<MatchResult<R>> back(String str, MatcherOfFixedLengthSelect<R> matcher)
	{
		for(IFixedLengthMatcher<R> m: matcher)
		{
			if(range.start - m.length() >= 0)
			{
				Optional<MatchResult<R>> optR = m.match(str, range.start - m.length(), true);

				if(optR.isPresent()) return optR.map(r -> {
					return MatchResult.of(new Range(range.start, range.start), Optional.empty());
				});
			}
		}

		return Optional.empty();
	}

	public <R> Optional<MatchResultList<R>> nextl(String str, IListMatcher<R> matcher)
	{
		return matcher.matchl(str, range.end, false);
	}

	public MatchResultList<T> compositeOf(int end)
	{
		return MatchResultList.of(new Range(range.start, end), results);
	}

	public MatchResultList<T> compositeOf(int start, int end)
	{
		return MatchResultList.of(new Range(start, end), results);
	}

	public MatchResultList<T> compositeOf(Range range)
	{
		return MatchResultList.of(range, results);
	}

	public <I> MatchResultList<T> compositeOf(MatchResult<I> m)
	{
		return MatchResultList.of(m.range, results);
	}

	public MatchResultList<T> compositeOfStart(int start)
	{
		return MatchResultList.of(new Range(start, range.end), results);
	}

	public MatchResultList<T> compositeOfEnd(int end)
	{
		return MatchResultList.of(new Range(range.start, end), results);
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

	@Override
	public String toString()
	{
		List<String> lst = results.stream().map(r -> r.toString()).collect(Collectors.toList());
		return String.join(", ", lst);
	}
}
