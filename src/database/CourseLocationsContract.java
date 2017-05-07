package database;

import java.util.List;

public class CourseLocationsContract extends tableContract {

	public String getName() {
		return "CourseLocation";
	}
	
	private static String[][] COLUMNS = {
		{"courseId", "INTEGER REFERENCES Course(courseID) NOT NULL"},
		{"DingId", "INTEGER REFERENCES Ding(dingID) NOT NULL"},
		{"orderNumber", "INTEGER NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}
	
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,1,1)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,2,2)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,3,3)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,4,4)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (6,5,1)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (6,6,2)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,7,5)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (5,8,5)");
		

		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (17,10,1)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (17,11,2)");
		list.add("INSERT INTO " + getName() + "(courseId, DingId, orderNumber) VALUES (17,12,3)");
	}
	
}
