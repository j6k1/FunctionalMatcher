package FunctionalMatcher;

@FunctionalInterface
public interface IOnMatch<T> {
	public T onmatch(String str, MatchResult<T> m);
}
