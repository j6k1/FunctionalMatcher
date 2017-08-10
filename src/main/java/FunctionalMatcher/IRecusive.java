package FunctionalMatcher;
@FunctionalInterface
public interface IRecusive<T> {
	T apply(IRecusive<T> r);
}
