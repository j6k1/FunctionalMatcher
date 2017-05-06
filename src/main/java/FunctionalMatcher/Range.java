package FunctionalMatcher;

public class Range {
	public final int start;
	public final int end;

	public Range(int start, int end)
	{
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Range)) return false;
		else return this.start == ((Range)o).start && this.end == ((Range)o).end;
	}
}
