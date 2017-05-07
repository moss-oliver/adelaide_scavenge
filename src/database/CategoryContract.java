package database;

import java.util.List;

public class CategoryContract extends tableContract {

	public String getName() {
		return "Category";
	}
	
	private static String[][] COLUMNS = {
		{"categoryId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"categoryName", "VARCHAR(20) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	//public static final String VALUE_UNORDERED = "unordered";
	//public static final String VALUE_ORDERED = "ordered";
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (1, '5k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (2,'10k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (3,'15k')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (4,'Hints')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (5,'Secrets')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (6,'POI')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (7,'Toilets')");
		list.add("INSERT INTO " + getName() + "(categoryId,categoryName) VALUES (8,'Sites')");
	}
}


















