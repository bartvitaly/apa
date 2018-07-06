package apa.common;

import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoConnection {

	public MongoCollection<Document> collection;

	static Block<Document> printBlock = new Block<Document>() {
		@Override
		public void apply(final Document document) {
			System.out.println(document.toJson());
		}
	};

	public void connect(String db, String collectionName) {
		MongoClient mongoClient = MongoClients.create();
		MongoDatabase database = mongoClient.getDatabase(db);
		collection = database.getCollection(collectionName);
	}

	public boolean tradeExists(String indexKey, Object transactionId) {

		if (collection.find(Filters.eq(indexKey, transactionId)).first() == null) {
			System.out.println("New transaction: " + transactionId);
			return false;
		} else {
			return true;
		}

	}

	public int indexPosition(List<String> headers, String indexHeader) throws Exception {

		int i = 0;
		for (String header : headers) {
			if (header.equals(indexHeader)) {
				return i;
			}
			i++;
		}

		if (i == 0) {
			throw new Exception(indexHeader + " was not found");
		}

		return 0;
	}

}
