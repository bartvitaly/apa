package providers;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import providers.bgcgfi.BgcGfi;

public class Data {

	final static Logger logger = Logger.getLogger(Data.class);

	public static void main(String[] args) throws Exception {

		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2");
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.toLevel("INFO"));

//		Get Bloomberg APA data
//		Bloomberg.save();

//		Save data to MongoDB
//		Bloomberg.saveDB();

//		BGC test
		BgcGfi bgcgfi_provider = new BgcGfi();
		bgcgfi_provider.login();
		bgcgfi_provider.savePreTrade();
		bgcgfi_provider.savePostTrade();
	}

}
