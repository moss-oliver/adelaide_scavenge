package database;

import java.util.List;

public class CourseContract extends tableContract {

	public String getName() {
		return "Course";
	}
	
	private static String[][] COLUMNS = {
		{"courseId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"categoryId", "INTEGER REFERENCES Category(categoryID) NOT NULL"},
		{"courseName", "VARCHAR(40) NOT NULL"},
		{"description", "VARCHAR(255) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (5,6,'POIs','Points Of Interest')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (6,7,'All Toilets','All Toilets')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (7,7,'Female Toilets','Female Toilets')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (8,7,'Male Toilets','Male Toilets')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (9,7,'Accessable Toilets','Accessable Toilets')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (10,8,'Sites 1','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (11,8,'Sites 2','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (12,8,'Sites 3','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (13,8,'Sites 4','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (14,8,'Sites 5','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (15,8,'Sites 6','test course')");
		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (16,8,'Sites 7','test course')");

		list.add("INSERT INTO " + getName() + "(courseId, categoryId, courseName, description) VALUES (17,1,'Flagstaff frollick','Explore flagstaff hill')");

	}
}
















