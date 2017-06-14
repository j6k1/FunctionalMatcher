package FunctionalMatcher;

@FunctionalInterface
public interface IOnListMatch<T> {
	public T onmatch(String str, int start, int end, MatchResultList<T> lst);
}
