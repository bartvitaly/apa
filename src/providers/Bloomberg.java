package providers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import apa.common.CsvFileReader;
import apa.common.FileCommon;
import apa.common.MongoConnection;
import apa.common.WebConnection;

public class Bloomberg {

	public static final String HISTORY = "https://www.bloombergapa.com/historyfiles";
	public static final String DOWNLOAD_LINK_FIRST_PART = "https://www.bloombergapa.com/download?key=";

	public static final String CSS_PATH = "[href*='.csv']";

	public static final String FOLDER = "files" + File.separator + "bloomberg" + File.separator;
	public static final String DB = "bloomberg_ind";
	public static final String COLLECTION = "bapa";

	public static List<String> getFilesList() {

		List<String> historyList = new ArrayList<String>();
		try {
			Elements files = Jsoup.parse(WebConnection.getSource(HISTORY)).select(CSS_PATH);
			System.out.println("Found files on page: " + files.size());
			for (Element file : files) {
				historyList.add(file.text());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return historyList;
	}

	public static void save() {

		List<String> historyList = getFilesList();
		for (String file : historyList) {
			if (!FileCommon.findFile(FOLDER + file.replace(':', '-'))) {
				WebConnection.download(DOWNLOAD_LINK_FIRST_PART + file, FOLDER + file.replace(':', '-'));
			}
		}

	}

	public static void saveDB() throws Exception {

		CsvFileReader csv;
		MongoConnection mconnect = new MongoConnection();
		mconnect.connect(DB, COLLECTION);

		Collection<File> files = FileUtils.listFiles(new File(FOLDER), new String[] { "csv" }, false);

		System.out.println("Old transactions count: " + mconnect.collection.find().into(new ArrayList<>()).size());

		for (File file : files) {
			csv = new CsvFileReader(file);
			List<String> headers = csv.getHeaders();
			List<List<String>> rows = csv.getValues();
			int indexPosition = mconnect.indexPosition(headers, "TRANSACTION_ID");

			for (List<String> row : rows) {
				if (!mconnect.tradeExists("TRANSACTION_ID", row.get(indexPosition))) {
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

}
