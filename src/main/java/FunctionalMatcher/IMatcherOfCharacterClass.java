package FunctionalMatcher;

public interface IMatcherOfCharacterClass<T> extends IFixedLengthMatcher<T> {
	@Override
	default int length()
	{
		return 1;
	}
}
