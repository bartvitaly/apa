package apa.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Common {

	final static Logger logger = Logger.getLogger(Common.class);

	/**
	 * This method is to get canonical path
	 * 
	 * @return String
	 * 
	 */
	public static String canonicalPath() {
		try {
			return (new File(".")).getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is to generate random string with words count
	 * 
	 * @param numberOfWords
	 * @return String
	 * 
	 */
	public static String randomStringWordsCount(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(8) + 3];
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
		}
		return arrayToString(randomStrings);
	}

	/**
	 * This method is to convert an array to string
	 * 
	 * @param arr
	 * @return String
	 * 
	 */
	public static String arrayToString(String[] arr) {
		StringBuilder builder = new StringBuilder();
		for (String s : arr) {
			builder.append(s + " ");
		}
		return builder.toString();
	}

	/**
	 * This method is to extract numbers from a List
	 * 
	 * @param text
	 * @return ArrayList
	 * 
	 */
	public static ArrayList<String> extractNumbers(String text) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(text);
		ArrayList<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group());
		}
		return result;
	}

	/**
	 * This method is to get a current timestamp
	 * 
	 * @return String
	 * 
	 */
	public static String timestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		long time = date.getTime();
		return String.valueOf(time);
	}

	/**
	 * This method is to read from file
	 * 
	 * @param path
	 * @param encoding
	 * @return String
	 * 
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * This method is to read from file to array
	 * 
	 * @param filename
	 * @return String
	 * 
	 */
	public static String[] readLines(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return lines.toArray(new String[lines.size()]);
	}

	/**
	 * This method is to write to file
	 * 
	 * @param path
	 * @param text
	 * @throws IOException
	 * 
	 */
	public static void writeToFile(String path, String text) throws IOException {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
			writer.write(text);
		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * This method is to return random Boolean
	 * 
	 * @return Boolean
	 * 
	 */
	public static boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

}
