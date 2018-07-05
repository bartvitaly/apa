package apa.common;

import java.io.File;

public class FileCommon {

	public static boolean findFile(String path) {
		File file = new File(path);
		boolean exist = file.exists() && !file.isDirectory();
		System.out.println("File '" + path + "' status: " + exist);
		return exist;
	}

}
