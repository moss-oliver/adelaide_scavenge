package com.example.adelaidescavange;

import database.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ScavengeDatabaseHelper extends SQLiteOpenHelper {

	public java.util.LinkedList<tableContract> getContracts()
	{
		java.util.LinkedList<tableContract> c = new java.util.LinkedList<tableContract>();

		c.add(new HeadingContract());
		//c.add(new CategoryContract());//
		c.add(new CategoryHeadingContract());
		c.add(new CourseLayoutContract());
		//c.add(new CourseLocationsContract());
		c.add(new CourseTypeContract());
		//c.add(new DingContract());//
		//c.add(new CourseContract());
		//c.add(new TitleContract());
		c.add(new PhotoContract());
		
		return c;
	}
	public ScavengeDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db ) {
		// TODO Auto-generated method stub
		execSQL(db, getCreateTableQueries() );
		//System.out.println(sql);
		//db.execSQL(sql);
	}
	
	public java.util.LinkedList<String> getCreateTableQueries()
	{
		java.util.LinkedList<tableContract> contracts = getContracts();
		java.util.LinkedList<String> sql_total = new java.util.LinkedList<String>();
		for (int i = 0; i<contracts.size(); i++)
		{
			tableContract contract = contracts.get(i);
			
			String sql = "CREATE TABLE " + contract.getName() + " (";
			String[][] columns= contract.getColumns();
			for (int j = 0; j< columns.length; j++)
			{
				if(j!= 0)
				{
					sql+=", ";
				}
				sql += columns[j][0] + " " + columns[j][1];
			}
			sql+=");";
			//sql_total += sql + ";";
			sql_total.add(sql);
		}
		for (int i = 0; i<contracts.size(); i++)
		{
			tableContract contract = contracts.get(i);
			contract.getInitialSql(sql_total);
		}
		return sql_total;
	}
	
	public void execSQL(SQLiteDatabase db, java.util.LinkedList<String> sql)
	{
		for (int i = 0; i<sql.size();i++)
		{
			db.execSQL(sql.get(i));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}














