package apa.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class WebConnection {

	final static Logger logger = Logger.getLogger(WebConnection.class);

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
			logger.info("Failed to get input stream: " + address);
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
			logger.error("Failed to get source: " + address);
			e.printStackTrace();
		}
		return source;
	}

	public static void download(String address, String path) {

		try {
			InputStream in = getInputStream(address);
			File targetFile = new File(path);
			FileUtils.copyInputStreamToFile(in, targetFile);
			logger.info("File " + address + ", saved to " + path);
			in.close();
		} catch (Exception e) {
			logger.error("Download of file " + path + " failed.");
			e.printStackTrace();
		}

	}

}
