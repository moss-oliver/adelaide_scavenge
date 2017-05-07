package database;

import java.util.List;

public class PhotoContract extends tableContract {

	public String getName() {
		System.out.println("ASPHOTO TABLE NAME GOT");
		return "ASPhoto";
	}
	
	private static String[][] COLUMNS = {
		{"photoId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"photoName", "VARCHAR(50) NOT NULL"},
		{"photoLocation", "VARCHAR(255) NOT NULL"},
		{"photoDate", "DATETIME NOT NULL"},
		{"photoLat", "DECIMAL(3,7) NOT NULL"},
		{"photoLong", "DECIMAL(3,7) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	//public static final String VALUE_UNORDERED = "unordered";
	//public static final String VALUE_ORDERED = "ordered";
	@Override
	public void getInitialSql(List<String> list) {

		list.add("INSERT INTO " + getName() + "(photoName, photoLocation, photoDate, photoLat, photoLong) VALUES ('JPEG_20140102_221657.jpg','/storage/emulated/0/Pictures/myScavengeAdelaidePics/JPEG_20140102_221657.jpg','2014/01/02 22:16:57','-34.92695','138.597225')");
		/*list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (1, '5k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (2,'10k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (3,'15k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (4,'Hints')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (5,'Secrets')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (6,'POI')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (7,'Toilets')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (8,'Sites')");*/
	}
}