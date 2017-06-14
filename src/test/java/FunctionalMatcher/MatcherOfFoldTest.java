package FunctionalMatcher;

import static org.junit.Assert.*;

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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

		@Override
		public String toString()
		{
			List<String> lst = rows.stream().map(r -> r.toString()).collect(Collectors.toList());

			return String.join("\r\n", lst);
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

		@Override
		public String toString()
		{
			List<String> lst = fields.stream().map(f -> f.toString()).collect(Collectors.toList());

			return String.join(", ", lst);
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

		@Override
		public String toString()
		{
			return v.toString();
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
				(str, start, temporary) -> {
					return (new MatcherOfFold<ICsv>((str1, start1, end1, lst) -> {
						CsvDocument csv = new CsvDocument();
						lst.stream().forEach(row -> {
							csv.add((CsvRow)row.value.get());
						});
						return (ICsv)csv;
					},
					new MatcherOfGreedyZeroOrMore<ICsv>(
						(str1, start1, temporary1) -> {
							return (new MatcherOfFold<ICsv>(
								(str2, start2, end2, lst) -> {
									CsvRow row = new CsvRow();
									lst.stream().forEach(f -> row.add(((CsvField)f.value.get())));
									return (ICsv)row;
								}, new MatcherOfGreedyZeroOrMore<ICsv>(
								(str2, start2, temporary2) -> {
									return (new MatcherOfSelect<ICsv>(
										(String str3, int start3, boolean temporary3) -> {
											return (new MatcherOfJust<ICsv>("\""))
													.match(str, start3, temporary3).flatMap(r0 -> {
														return (new MatcherOfGreedyZeroOrMore<ICsv>(
															(str4, start4, end4, m) -> {
																return (ICsv)new CsvField(
																					str.substring(start4, end4)
																						.replace("\"\"", "\""));
															},
															IContinuationMatcher.of((new MatcherOfSelect<ICsv>(
																new MatcherOfJust<ICsv>("\"\"")
															)).add(new MatcherOfNegativeCharacterClass<ICsv>(
																new MatcherOfAsciiCharacterClass<ICsv>("\"")
															)))
														)).match(str, r0.range.end, temporary3)
														.flatMap(r1 -> {
															return r1.next(str, new MatcherOfJust<ICsv>("\"")).map(r2 -> {
																return MatchResult.of(r0.range.compositeOf(r2.range), r1.value);
															});
														});
													});

										}
									)).add(new MatcherOfGreedyZeroOrMore<ICsv>(
										(str3, start3, end3, m) -> {
											return (ICsv)new CsvField(str.substring(start3, end3));
										}, IContinuationMatcher.of(
											new MatcherOfNegativeCharacterClass<ICsv>(
												new MatcherOfAsciiCharacterClass<ICsv>(",\r\n")
											)
										)
									)).match(str, start2, temporary2)
									.flatMap(r0 -> {
										return r0.next(str, (new MatcherOfSelect<Boolean>(
												new MatcherOfJust<Boolean>(",")
											)).add(new MatcherOfAsciiCharacterClass<Boolean>(
													(str3, start3, end3, m) -> true, "\r\n")
											).add(new MatcherOfEndOfContent<Boolean>((str3, start3, end3, m) -> true))
										).map(r1 -> {
											if(r1.value.orElse(false))
											{
												return Termination.of(r0.compositeOf(r1.range.end));
											}
											else
											{
												return Continuation.of(r0.compositeOf(r1.range.end));
											}
										});
									});
								}
							)
						)).match(str, start1, temporary1)
						.flatMap(r0 -> {
							return r0.next(str,
								(new MatcherOfSelect<Boolean>(
									new MatcherOfAsciiCharacterClass<Boolean>((str2, start2, end2, m) -> false, ",\r\n")
								))
								.add(new MatcherOfEndOfContent<Boolean>((str2, start2, end2, m) -> true))
							)
							.map(r1 -> {
								if(r1.value.orElse(false))
								{
									return Termination.of(r0.compositeOf(r1.range.end));
								}
								else
								{
									return Continuation.of(r0.compositeOf(r1.range.end));
								}
							});
						});
					})
				)).match(str, start, temporary)
				.flatMap(r0 -> {
					return r0.next(str, new MatcherOfEndOfContent<ICsv>()).map(r1 -> r0);
				});
		}), org.hamcrest.core.Is.<Optional<MatchResult<ICsv>>>is(
					Optional.of(MatchResult.of(new Range(0, csv.length()), Optional.of(new CsvDocument() {{
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
						add(new CsvField("\"aaaあ\r\n\r\nあああ"));
					}});
					add(new CsvRow() {{
						add(new CsvField("いいいううう"));
						add(new CsvField("bbbb"));
						add(new CsvField("\"aaaa\""));
						add(new CsvField("aaabbbb"));
						add(new CsvField("ccccccc"));
					}});
				}})
			))));
	}
}
