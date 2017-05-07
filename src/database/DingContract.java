package database;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DingContract extends tableContract {

	public String getName() {
		return "Ding";
	}
	
	private static String[][] COLUMNS = {
		{"dingId", "INTEGER PRIMARY KEY ASC AUTOINCREMENT NOT NULL"},
		{"loc_lat", "REAL NOT NULL"},
		{"loc_long", "REAL NOT NULL"},
		{"found", "BOOLEAN NOT NULL"},
		{"unfoundTitle", "INTEGER REFERENCES Title(titleId) NOT NULL"},
		{"foundTitle", "INTEGER REFERENCES Title(titleId) NOT NULL"},
		{"url", "VARCHAR(225) NOT NULL"},
		{"photographer", "VARCHAR(255) NOT NULL"},
		{"circa", "INTEGER(4) NOT NULL"},
	};
	@Override
	public String[][] getColumns() {
		return COLUMNS;
	}
	
	public void getInitialSql(List<String> list) {
		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
					" VALUES (1, '-34.92618','138.60076','FALSE'," +
				"1,1, 'http://farm6.staticflickr.com/5228/5636605417_bafe514d21_o.jpg','BOB','1999')");
		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
				" VALUES (2, '-34.9217','138.59296','FALSE', " +
				"2,2, 'http://farm6.staticflickr.com/5230/5637180426_13772f88e9_o.jpg','BOB j','1998')");
		
		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
				" VALUES (3, '-34.9268082','138.6066482','FALSE'," +
			"3,3, 'http://images.slsa.sa.gov.au/mpcimg/05250/B5149.jpg','BOB','1999')");
		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
			" VALUES (4, '-34.92315','138.596122','FALSE', " +
			"4,4, 'http://farm6.staticflickr.com/5230/5637180426_13772f88e9_o.jpg','BOB j','1998')");

		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
			" VALUES (5, '-34.42618','138.60076','FALSE'," +
				"5,5, 'http://farm6.staticflickr.com/5230/5637180426_13772f88e9_o.jpg','BOB','1999')");
		list.add("INSERT INTO " + getName() + "(dingId, loc_lat, loc_long, found, unfoundTitle, foundTitle, url, photographer, circa) " +
				" VALUES (6, '-34.6217','138.49296','FALSE', " +
				"6,6, 'http://farm6.staticflickr.com/5230/5637180426_13772f88e9_o.jpg','BOB j','1998')");
		
		//-34.92618	138.60076	"				Pilgrim Lane – Art Hire"			http://www.cityofadelaide.com.au/sights/pilgrim-lane-art-hire

		
	}
	
	

	
	
	
}
