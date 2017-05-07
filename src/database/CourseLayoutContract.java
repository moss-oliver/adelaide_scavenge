package database;

import java.util.List;

public class CourseLayoutContract extends tableContract {

	public String getName() {
		return "CourseLayout";
	}
	
	private static String[][] COLUMNS = {
		{"courseLayoutId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"courseLayoutName", "VARCHAR(40) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	public static final int VALUE_VERTICAL_ID = 0;
	public static final String VALUE_VERTICAL_STR = "VERTICAL";
	public static final int VALUE_HORIZONTAL_ID = 1;
	public static final String VALUE_HORIZONTAL_STR = "HORIZONTAL";
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(courseLayoutId,courseLayoutName) VALUES ("+Integer.toString(VALUE_VERTICAL_ID)+",'"+VALUE_VERTICAL_STR+"')");
		list.add("INSERT INTO " + getName() + "(courseLayoutId,courseLayoutName) VALUES ("+Integer.toString(VALUE_HORIZONTAL_ID)+",'"+VALUE_HORIZONTAL_STR+"')");
	}
	
}

















