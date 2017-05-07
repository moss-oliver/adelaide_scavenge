package database;

public abstract class tableContract {
	
	public abstract String getName();
	
	public abstract String[][] getColumns();
	
	public void getInitialSql(java.util.List<String> list)
	{
		
	}
}
