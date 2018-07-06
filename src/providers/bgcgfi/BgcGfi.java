package providers.bgcgfi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;

import apa.common.CsvFileReader;
import apa.common.FileCommon;
import apa.common.MongoConnection;
import apa.common.WebConnection;

public class BgcGfi extends BgcGfiPage {

	public static final String DOWNLOAD_LINK_FIRST_PART = "https://regdata.fenicsmd.com";

	public static final String DB = "bgcgfi";
	public static final String PRE_COLLECTION = "pre";
	public static final String POST_COLLECTION = "post";

	public static final String PRE_INDEX_HEADER = "Quote Entry ID";
	public static final String POST_INDEX_HEADER = "Transaction Identification Code";

	static String folderFirstPart = "files" + File.separator + "bgcgfi" + File.separator;
	public static final String PRE_FOLDER = folderFirstPart + "pre" + File.separator;
	public static final String POST_FOLDER = folderFirstPart + "post" + File.separator;

	public void savePreTrade() {
		save(true);
	}

	public void savePostTrade() {
		save(false);
	}

	public void save(boolean tradeType) {

		List<String> urlList = new ArrayList<String>();

		int views = getViews(tradeType);
		for (int i = 0; i < views; i++) {
			getNextView(tradeType, i + 1);
			getFilesList(urlList, page.driver.getPageSource(), cssPathFile(tradeType));
		}

		for (String url : urlList) {
			if (!FileCommon.findFile(folder(tradeType) + getName(url))) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				WebConnection.download(DOWNLOAD_LINK_FIRST_PART + url, folder(tradeType) + getName(url));
			}
		}

	}

	public static String folder(boolean tradeType) {
		return tradeType ? PRE_FOLDER : POST_FOLDER;
	}

	public void saveDBPreTrade() {
		try {
			saveDB(true);
		} catch (Exception e) {
			logger.error("Can't save pre trade data");
			e.printStackTrace();
		}
	}

	public void saveDBPostTrade() {
		try {
			saveDB(false);
		} catch (Exception e) {
			logger.error("Can't save post trade data");
			e.printStackTrace();
		}
	}

	public void saveDB(boolean tradeType) throws Exception {

		CsvFileReader csv;
		MongoConnection mconnect = new MongoConnection();
		mconnect.connect(DB, collection(tradeType));

		Collection<File> files = FileUtils.listFiles(new File(folder(tradeType)), new String[] { "zip" }, false);

		System.out.println("Old transactions count: " + mconnect.collection.find().into(new ArrayList<>()).size());

		for (File file : files) {
			csv = new CsvFileReader(file);
			List<String> headers = csv.getHeaders();
			List<List<String>> rows = csv.getValues();
			int indexPosition = mconnect.indexPosition(headers, indexHeader(tradeType));

			for (List<String> row : rows) {
				if (!mconnect.tradeExists(indexHeader(tradeType), row.get(indexPosition))) {
					Document document = new Document();
					for (int i = 0; i < headers.size(); i++) {
						document.append(headers.get(i), row.get(i));
					}
					mconnect.collection.insertOne(document);
				}
			}
		}

		System.out.println("New transactions count: " + mconnect.collection.find().into(new ArrayList<>()).size());

	}

	public static String indexHeader(boolean tradeType) {
		return tradeType ? PRE_INDEX_HEADER : POST_INDEX_HEADER;
	}

	public static String collection(boolean tradeType) {
		return tradeType ? PRE_COLLECTION : POST_COLLECTION;
	}

}
