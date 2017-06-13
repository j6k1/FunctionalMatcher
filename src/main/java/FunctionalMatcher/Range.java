package FunctionalMatcher;

public class Range {
	public final int start;
	public final int end;

	public Range(int start, int end)
	{
		this.start = start;
		this.end = end;
	}

	public Range compositeOf(int end)
	{
		return new Range(this.start, end);
	}

	public Range compositeOf(Range r)
	{
		return new Range(this.start, r.end);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Range)) return false;
		else return this.start == ((Range)o).start && this.end == ((Range)o).end;
	}

	@Override
	public String toString()
	{
		return "(" + start + ".." + end + ")";
	}
}
