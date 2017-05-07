package database;

import java.util.List;

public class TitleContract extends tableContract {

	public String getName() {
		return "Title";
	}
	
	private static String[][] COLUMNS = {
		{"titleId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"title", "VARCHAR(50) NOT NULL"},
		{"description", "VARCHAR(255) NOT NULL"},
		//pic id
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}
	
	@Override
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (1,'Secret', 'It is a secret')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (2,'Rundle mall', 'Major shopping district')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (3,'Adelaide oval', 'oval')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (4,'hindley street', 'It is a street')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (5,'chinatown', 'Major shopping')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (6,'central markets', 'markets centraj')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (10,'C10', 'C10 Found')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (11,'D11', 'D11 found')");
		list.add("INSERT INTO " + getName() + "(titleId,title,description) VALUES (12,'B12', 'B12 Found')");

	}
}
