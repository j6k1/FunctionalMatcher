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

	@SuppressWarnings("serial")
	@Test
	public void testMatch() {
		assertThat(MatcherExecutor.exec(csv, s1 -> MatcherOfFold.of(
			(str, start, end, lst) -> {
				ArrayList<ArrayList<String>> csv = new ArrayList<>();
				lst.stream().forEach(row -> {
					row.value.ifPresent(r -> csv.add(r));
				});
				return Optional.of(csv);
			},
			MatcherOfGreedyZeroOrMore.of(s2 ->
				MatcherOfFold.of(
					(str, start, end, lst) -> {
						ArrayList<String> row = new ArrayList<>();
						lst.stream().forEach(f -> {
							f.value.ifPresent(v -> row.add(v));
						});
						return Optional.of(row);
					}, MatcherOfGreedyZeroOrMore.of(s3 ->
						MatcherOfSelect.of(s4 ->
							MatcherOfJust.of("\"")
								.match(s4).flatMap(r0 ->
									r0.next(s4, MatcherOfGreedyZeroOrMore.of(
										(str, start, end, m) -> {
											return Optional.of(
													str.substring(start, end)
														.replace("\"\"", "\""));
										},
										MatcherOfSelect.of(
											MatcherOfJust.of("\"\"")
										).or(MatcherOfNegativeCharacterClass.of(
											MatcherOfAsciiCharacterClass.of("\"")
										)).toContinuation()
								)).flatMap(r1 -> r1.skip(s4, MatcherOfJust.of("\"")))
							)
						).or(MatcherOfGreedyZeroOrMore.of(
							(str, start, end, m) -> {
								return Optional.of(str.substring(start, end));
							}, MatcherOfNegativeCharacterClass.of(
								MatcherOfAsciiCharacterClass.of(",\r\n")
							).toContinuation()
						)).match(s3)
						.flatMap(r0 -> r0.next(s3, MatcherOfSelect.of(
							MatcherOfJust.of((str, start, end, m) -> {
									return Optional.of(false);
								}, ",")
							).or(MatcherOfAsciiCharacterClass.of(
								(str, start, end, m) -> {
									return Optional.of(true);
								}, "\r\n")
							).or(MatcherOfEndOfContent.of(
								(str, start, end, m) -> {
									return Optional.of(true);
								})
							).toContinuation(r1 -> {
								if(r1.value.orElse(false))
								{
									return Termination.of(
											r0.compositeOf(r1.range.end));
								}
								else
								{
									return Continuation.of(
											r0.compositeOf(r1.range.end));
								}
							}))
						)
					)
				).match(s2)
				.flatMap(r0 -> r0.next(s2,
					MatcherOfSelect.of(
						MatcherOfAsciiCharacterClass.of(
							(str, start, end, m) -> {
								return Optional.of(false);
							}, ",\r\n")
					)
					.or(MatcherOfEndOfContent.of(
						(str, start, end, m) -> {
							return Optional.of(true);
						})
					).toContinuation(r1 -> {
						if(r1.value.orElse(false).equals(Boolean.TRUE))
						{
							return Termination.of(
									r0.compositeOf(r1.range.end));
						}
						else
						{
							return Continuation.of(
									r0.compositeOf(r1.range.end));
						}
					})
				)
			))
		).match(s1)
		.flatMap(r0 -> r0.skip(s1, MatcherOfEndOfContent.of()))
	), org.hamcrest.core.Is.<Optional<MatchResult<ArrayList<ArrayList<String>>>>>is(
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
