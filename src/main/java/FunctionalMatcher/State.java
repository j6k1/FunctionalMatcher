package FunctionalMatcher;

public class State {
	public final String str;
	public final int start;
	public final boolean temporary;

	protected State(String str, int start, boolean temporary) {
		this.str = str;
		this.start = start;
		this.temporary = temporary;
	}

	public static State of(String str, int start, boolean temporary) {
		if(str == null)
		{
			throw new NullReferenceNotAllowedException("A null value was passed as a reference to the content string.");
		}

		return new State(str, start, temporary);
	}
}
