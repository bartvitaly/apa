package providers;

public class Data {

	public static void main(String[] args) throws Exception {

//		Get Bloomberg APA data
		Bloomberg.save();

//		Save data to MongoDB
		Bloomberg.saveDB();

//		Search test
//		Bloomberg.search();

	}

}
