package apa.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class WebConnection {

	public static InputStream getInputStream(String address) {
		InputStream in = null;

		URL url;

		try {
			url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			in = con.getInputStream();
		} catch (Exception e) {
			System.out.println("Failed to get input stream: " + address);
			e.printStackTrace();
		}
		return in;
	}

	public static void closeInputStream(InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSource(String address) {
		InputStream in = getInputStream(address);
		String source = null;
		try {
			source = IOUtils.toString(in, "UTF-8");
			in.close();
		} catch (IOException e) {
			System.out.println("Failed to get source: " + address);
			e.printStackTrace();
		}
		return source;
	}

	public static void download(String address, String path) {

		try {
			InputStream in = getInputStream(address);
			File targetFile = new File(path);
			FileUtils.copyInputStreamToFile(in, targetFile);
			System.out.println("File " + address + ", saved to " + path);
			in.close();
		} catch (Exception e) {
			System.out.println("Download of file " + path + " failed.");
			e.printStackTrace();
		}

	}

}
