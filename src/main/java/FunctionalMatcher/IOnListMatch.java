package FunctionalMatcher;

@FunctionalInterface
public interface IOnListMatch<T> {
	public T onmatch(String str, Range range, MatchResultList<T> lst);
}
