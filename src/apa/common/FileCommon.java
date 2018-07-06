package apa.common;

import java.io.File;

import org.apache.log4j.Logger;

public class FileCommon {

	final static Logger logger = Logger.getLogger(FileCommon.class);

	public static boolean findFile(String path) {
		File file = new File(path);
		boolean exist = file.exists() && !file.isDirectory();
		System.out.println("File '" + path + "' status: " + exist);
		return exist;
	}

}
