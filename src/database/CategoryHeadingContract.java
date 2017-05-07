package database;

import java.util.List;
public class CategoryHeadingContract extends tableContract {

	public String getName() {
		return "CategoryHeading";
	}

	public static String CATEGORYID = "categoryId";
	public static String HEADINGID = "headingId";
	public static String COURSE_LAYOUT_ID = "courseLayoutId";
	
	private static String[][] COLUMNS = {
		{CATEGORYID, "INTEGER REFERENCES Category(categoryId) NOT NULL"},
		{HEADINGID, "INTEGER REFERENCES Heading(headingId) NOT NULL"},
		{COURSE_LAYOUT_ID, "INTEGER REFERENCES CourseLayout(courseLayoutId) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}

	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (1, 6, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (1, 7, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (1, 8, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (2, 1, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (1, 2, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (2, 3, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (2, 4, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (3, 1, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (3, 2, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (3, 3, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (3, 4, 0)");
		list.add("INSERT INTO " + getName() + "(headingId,categoryId,courseLayoutId) VALUES (3, 5, 0)");
	}
}

