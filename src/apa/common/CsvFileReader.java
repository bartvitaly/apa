package apa.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvFileReader {

	static FileReader in = null;
	CSVParser parser = null;

	public CsvFileReader(Object path) {
		try {
			if (path.getClass().equals(File.class)) {
				in = new FileReader((File) path);
			} else {
				in = new FileReader((String) path);
			}

			parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader());
		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			in.close();
		} catch (IOException e) {
			System.out.println("Error while closing fileReader/csvFileParser !!!");
			e.printStackTrace();
		}
	}

	public List<List<String>> getValues() {

		List<List<String>> records = new ArrayList<>();

		for (final CSVRecord record : parser) {

			final List<String> items = CsvFileReader.csvRecordToList(record);
			final int size = items.size();

			// Don't add row if the line was empty.
			if (size > 1 || (size == 1 && items.get(0).length() > 0)) {
				records.add(items);
			}
		}

		close();
		return records;
	}

	public List<String> getHeaders() {

		final Map<String, Integer> headerMap = parser.getHeaderMap();
		final List<String> labels = new ArrayList<>(headerMap.size());
		for (final String label : headerMap.keySet()) {
			final int pos = headerMap.get(label);
			labels.add(pos, label);
		}

		return labels;
	}

	private static List<String> csvRecordToList(CSVRecord record) {

		final List<String> list = new ArrayList<>();
		for (final String value : record) {
			list.add(value);
		}
		return list;
	}

}
