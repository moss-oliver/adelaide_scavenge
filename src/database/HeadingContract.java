package database;

import java.util.List;

public class HeadingContract extends tableContract {

	public String getName() {
		return "Heading";
	}
	
	private static String[][] COLUMNS = {
		{"headingId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"headingName", "VARCHAR(20) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(headingId,headingName) VALUES (1,'Explore')");
		list.add("INSERT INTO " + getName() + "(headingId,headingName) VALUES (2,'Course')");
		list.add("INSERT INTO " + getName() + "(headingId,headingName) VALUES (3,'Trophies')");
	}
}
