package database;

import java.util.List;

public class CourseTypeContract extends tableContract {

	public String getName() {
		return "CourseType";
	}
	
	private static String[][] COLUMNS = {
		{"courseTypeId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"name", "VARCHAR(20) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	public static final int VALUE_UNORDERED_ID = 0;
	public static final String VALUE_UNORDERED = "unordered";
	public static final int VALUE_ORDERED_ID = 1;
	public static final String VALUE_ORDERED = "ordered";
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(courseTypeId, name) VALUES ("+Integer.toString(VALUE_UNORDERED_ID)+",'" + VALUE_UNORDERED + "')");
		list.add("INSERT INTO " + getName() + "(courseTypeId, name) VALUES ("+Integer.toString(VALUE_ORDERED_ID)+",'" + VALUE_ORDERED + "')");
	}
	
}





























