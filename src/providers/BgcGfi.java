package providers;

import java.io.File;

public class BgcGfi {

	public static final String URL = "https://regdata.fenicsmd.com/login";

	public static String username = "[id='username']";
	public static String password = "[id='password']";
	public static String submit = "[id='login-submit']";

	public static final String FOLDER = "files" + File.separator + "bgcgfi" + File.separator;
	public static final String DB = "bgcgfi_ind";
	public static final String COLLECTION = "apa";

	public static void login() {

	}

}
