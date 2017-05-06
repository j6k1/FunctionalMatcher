package FunctionalMatcher;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.isThrowable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class MatcherOfFoldTest {
	private static String csv;

	@BeforeClass
	public static void loadCsv() throws UnsupportedCharsetException, UnsupportedEncodingException, FileNotFoundException, IOException
	{
		String currentDir = System.getProperty("user.dir");

		String path = String.join(File.separator, new String[] { currentDir, "testdata", "sample.csv" });

		LinkedList<String> lines = new LinkedList<String>();

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"SJIS"))) {

			String line;

			while((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		}

		csv = String.join("\r\n", lines);
	}

	static interface ICsv {

	}

	static class CsvDocument implements ICsv {
		protected ArrayList<CsvRow> rows;

		public CsvDocument()
		{
			this.rows = new ArrayList<>();
		}

		public void add(CsvRow row)
		{
			rows.add(row);
		}

		@Override
		public boolean equals(Object o)
		{
			if(!(o instanceof CsvDocument)) return false;
			else return this.rows.equals(((CsvDocument)o).rows);
		}
	}

	static class CsvRow implements ICsv {
		protected ArrayList<CsvField> fields;

		public CsvRow()
		{
			this.fields = new ArrayList<>();
		}

		public void add(CsvField f)
		{
			this.fields.add(f);
		}
		@Override
		public boolean equals(Object o)
		{
			if(!(o instanceof CsvRow)) return false;
			else return this.fields.equals(((CsvRow)o).fields);
		}
	}

	static class CsvField implements ICsv {
		protected String v;

		public CsvField(String f)
		{
			v = f;
		}

		@Override
		public boolean equals(Object o)
		{
			if(!(o instanceof CsvField)) return false;
			else return this.v.equals(((CsvField)o).v);
		}
	}
	/*
	@Test
	public void testOf() {
		fail("まだ実装されていません");
	}
	*/

	@Test
	public void testMatch() {
		assertThat(MatcherExecutor.exec(csv,
				MatcherOfSequence.of(
					MatcherOfFold.of(
						MatcherOfGreedyZeroOrMore.of(
							MatcherOfSequence.of(
								MatcherOfSelect.of(
									MatcherOfGreedyZeroOrMore.of(
										MatcherOfNegativeCharacterClass.of(
											new MatcherOfAsciiCharacterClass<ICsv>(",\r\n")
										),
										(str, r, m) -> {
											return (ICsv)new CsvField(str.substring(r.start, r.end));
										}
									)
								).add(
									MatcherOfSequence.of(
										new MatcherOfJust<ICsv>("\"")
									).add(
										MatcherOfGreedyZeroOrMore.of(
											MatcherOfSelect.of(
												new MatcherOfJust<ICsv>("\"\"")
											)
											.add(MatcherOfNegativeCharacterClass.of(
												new MatcherOfAsciiCharacterClass<ICsv>("\"")
											)),
											(str, r, m) -> {
												return (ICsv) new CsvField(
																	str.substring(r.start, r.end)
																		.replace("\"\"", "\""));
											}
										)
									).add(new MatcherOfJust<ICsv>("\""))
								)
							)
							.add(MatcherOfSelect.of(new MatcherOfJust<ICsv>(","))
								.add(new MatcherOfJust<ICsv>("\r\n"))
								.add(new MatcherOfAsciiCharacterClass<ICsv>("\r\n"))
								.add(new MatcherOfTerminateWithNewLine<ICsv>())
								.add(new MatcherOfEndOfContent<ICsv>()))
						),
						(str, r, lst) -> {
							return (ICsv)new CsvDocument();
						}
					)
				)
				.add(new MatcherOfEndOfContent<ICsv>())
			), is(Optional.of(MatchResult.of(
				new Range(0, csv.length()), Optional.of(new CsvDocument() {{
					add(new CsvRow() {{
						add(new CsvField("aaa"));
						add(new CsvField("11aabb"));
						add(new CsvField("ああああ"));
						add(new CsvField("aa!\"jasfaljああああ"));
						add(new CsvField("あああああ"));
					}});
					add(new CsvRow() {{
						add(new CsvField("11aaabb{]:"));
						add(new CsvField("bbbb"));
						add(new CsvField("いいいい"));
						add(new CsvField("aaaa\"111!!!"));
						add(new CsvField("いいいい"));
					}});
					add(new CsvRow() {{
						add(new CsvField("あああ11bb"));
						add(new CsvField("aaaa"));
						add(new CsvField("うううう"));
						add(new CsvField("aabbbb\"11"));
						add(new CsvField("aaaあ\"\r\nあああ"));
					}});
					add(new CsvRow() {{
						add(new CsvField("いいいううう"));
						add(new CsvField("bbbb"));
						add(new CsvField("\"aaaa\""));
						add(new CsvField("aaabbbb"));
						add(new CsvField("ccccccc"));
					}});
				}}))))
			);
	}
}
