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

	@Test
	public void testMatch() {
		assertThat(MatcherExecutor.exec(csv,
				(str, start, temporary) -> {
					return MatcherOfFold.of((str1, start1, end1, lst) -> {
						ArrayList<ArrayList<String>> csv = new ArrayList<>();
						lst.stream().forEach(row -> {
							row.value.ifPresent(r -> csv.add(r));
						});
						return Optional.of(csv);
					},
					MatcherOfGreedyZeroOrMore.of(
						(str1, start1, temporary1) -> {
							return MatcherOfFold.of(
								(str2, start2, end2, lst) -> {
									ArrayList<String> row = new ArrayList<>();
									lst.stream().forEach(f -> {
										f.value.ifPresent(v -> row.add(v));
									});
									return Optional.of(row);
								}, MatcherOfGreedyZeroOrMore.of(
								(str2, start2, temporary2) -> {
									return MatcherOfSelect.of(
										(String str3, int start3, boolean temporary3) -> {
											return MatcherOfJust.of("\"")
													.match(str, start3, temporary3).flatMap(r0 -> {
														return (MatcherOfGreedyZeroOrMore.of(
															(str4, start4, end4, m) -> {
																return Optional.of(
																		str.substring(start4, end4)
																			.replace("\"\"", "\""));
															},
															(str4, start4, temporary4) -> {
																return (MatcherOfSelect.of(
																	MatcherOfJust.of("\"\"")
																)).add(MatcherOfNegativeCharacterClass.of(
																	MatcherOfAsciiCharacterClass.of("\"")
																)).match(str, start4, temporary4)
																.map(r -> Continuation.of(new MatchResultType<Nothing>(), r));
															}
														)).match(str, r0.range.end, temporary3)
														.flatMap(r1 -> {
															return r1.next(str, MatcherOfJust.of("\"")).map(r2 -> {
																return MatchResult.of(r0.range.compositeOf(r2.range), r1.value);
															});
														});
													});

										}
									).add(MatcherOfGreedyZeroOrMore.of(
										(str3, start3, end3, m) -> {
											return Optional.of(str.substring(start3, end3));
										}, (String str3, int start3, boolean temporary3) -> {
											return MatcherOfNegativeCharacterClass.of(
												MatcherOfAsciiCharacterClass.of(",\r\n")
											).match(str, start3, temporary3)
											.map(r -> {
												return Continuation.of(new MatchResultType<Nothing>(), r);
											});
										}
									)).match(str, start2, temporary2)
									.flatMap(r0 -> {
										return r0.next(str, (MatcherOfSelect.of(
												MatcherOfJust.of((str3, start3, end3, m) -> {
													return Optional.of(false);
												}, ",")
											)).add(MatcherOfAsciiCharacterClass.of(
												(str3, start3, end3, m) -> {
													return Optional.of(true);
												}, "\r\n")
											).add(MatcherOfEndOfContent.of(
												(str3, start3, end3, m) -> {
													return Optional.of(true);
												})
											)
										).map(r1 -> {
											if(r1.value.orElse(false))
											{
												return Termination.of(
														new MatchResultType<String>(),
														r0.compositeOf(r1.range.end));
											}
											else
											{
												return Continuation.of(
														new MatchResultType<String>(),
														r0.compositeOf(r1.range.end));
											}
										});
									});
								}
						)).match(str, start1, temporary1)
						.flatMap(r0 -> {
							return r0.next(str,
								(MatcherOfSelect.of(
									MatcherOfAsciiCharacterClass.of(
										(str2, start2, end2, m) -> {
											return Optional.of(false);
										}, ",\r\n")
								))
								.add(MatcherOfEndOfContent.of(
										(str2, start2, end2, m) -> {
											return Optional.of(true);
										}))
							)
							.map(r1 -> {
								if(r1.value.orElse(false).equals(Boolean.TRUE))
								{
									return Termination.of(
											new MatchResultType<ArrayList<String>>(),
											r0.compositeOf(r1.range.end));
								}
								else
								{
									return Continuation.of(
											new MatchResultType<ArrayList<String>>(),
											r0.compositeOf(r1.range.end));
								}
							});
						});
					})
				).match(str, start, temporary)
				.flatMap(r0 -> {
					return r0.next(str, MatcherOfEndOfContent.of()).map(r1 -> r0);
				});
		}), org.hamcrest.core.Is.<Optional<MatchResult<ArrayList<ArrayList<String>>>>>is(
					Optional.of(MatchResult.of(new Range(0, csv.length()),Optional.of(
						new ArrayList<ArrayList<String>>() {{
							add(new ArrayList<String>() {{
								add("aaa");
								add("11aabb");
								add("ああああ");
								add("aa!\"jasfaljああああ");
								add("あああああ");
							}});
							add(new ArrayList<String>() {{
								add("11aaabb{]:");
								add("bbbb");
								add("いいいい");
								add("aaaa\"111!!!");
								add("いいいい");
							}});
							add(new ArrayList<String>() {{
								add("あああ11bb");
								add("aaaa");
								add("うううう");
								add("aabbbb\"11");
								add("\"aaaあ\r\n\r\nあああ");
							}});
							add(new ArrayList<String>() {{
								add("いいいううう");
								add("bbbb");
								add("\"aaaa\"");
								add("aaabbbb");
								add("ccccccc");
							}});
						}})
			))));
	}
}
