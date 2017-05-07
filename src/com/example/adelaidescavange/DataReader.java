package com.example.adelaidescavange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DataReader{
	
	public DataReader(Context con, MainActivity a){
		File file ;
		FileReader in = null;
		String lSQLStatement = "";
		String ltoken = "";
		//Double dbVersion = MainActivity.DATABASE_VERSION;
		int dbVersion = 0;
		
		boolean ldoesTblExist = doesTableExist(a.getDb(),"DatabaseVersion");
		if (ldoesTblExist ==false){
			createDatabaseVersionTable(a.getDb());
			dbVersion = 0;
		}
		else{
			dbVersion = getDataBaseVersion(a.getDb());
		}
 		
		try {
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(con.getAssets().open( "dataLoad.txt")));

		    String mLine = reader.readLine();
		    while (mLine != null) {
		       //process line
		    	/*StringTokenizer st = new StringTokenizer(mLine,";");
		    	while(st.hasMoreTokens()){
            		ltoken = st.nextToken();
            		
		    	}*/
		    	 
		      	if(mLine.contains("<databaseVersion>")){
		      		String sectionDBverion = ltoken.substring(0, ltoken.length()- "</databaseVersion>".length());
            		double secDBVersion =0;
            		secDBVersion = Double.parseDouble(sectionDBverion);
            		
            		if (dbVersion < secDBVersion){
            			 //Update Version Table
            			
            			//insert data
            		}
		      	}
		      	
		      	StringTokenizer st = new StringTokenizer(mLine,"<databaseVersion>");
		      	
		    	while(st.hasMoreTokens()){
            		ltoken = st.nextToken();
            		String sectionDBverion = ltoken.substring(0, ltoken.length()- "</databaseVersion>".length());
            		double secDBVersion =0;
            		secDBVersion = Double.parseDouble(sectionDBverion);
            		
            		if (dbVersion < secDBVersion){
            			 //Update Version Table
            			
            			//insert data
            		}
		    	}
		      	
		      	if(mLine.contains(";")){
		      		
		      		
		      	}
		      	mLine = reader.readLine(); 
		    }

		    reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public int getDataBaseVersion(SQLiteDatabase db){
 
    	Cursor cursor = db.rawQuery("SELECT MAX(dbVersionId) as dbVersion FROM DatabaseVersion WHERE dbACTIVE = true", null);
    	int versionNo =0;
        while (cursor.moveToNext()) {
        	//photoLocText = photoLocText +" " + cursor.getString(cursor.getColumnIndex("photoLocation"));
        	//System.out.println("getDataBaseVersion");
        	versionNo = cursor.getInt(cursor.getColumnIndex("dbVersion"));
        	//System.out.println("V:" + versionNo);
        	
        }
        return versionNo;
	}
        
   public boolean doesTableExist(SQLiteDatabase db,String tableName){     
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                       cursor.close();
                return true;
            }
                        cursor.close();
        }
        return false;
   }
   
   public void createDatabaseVersionTable(SQLiteDatabase db){
	   
	   String createTableStatement = "CREATE TABLE IF NOT EXISTS DatabaseVersion (" +
        	"dbVersionId 		INTEGER PRIMARY KEY ASC AUTOINCREMENT 	NOT NULL,"+
        	"dbActive 		BOOLEAN 				NOT NULL,"+
        	"dbDateReleased		DateTime				NOT NULL,"+
        	"dbDateUpdated		DateTime				NOT NULL"+
        ");";

	   String insertStatement = "INSERT INTO Category (dbVersionId, dbActive, dbDateReleased, dbDateUpdated ) VALUES (0, True, '2013-12-04', '2013-12-04');";
        
       db.execSQL(createTableStatement) ;
       db.execSQL(insertStatement);
   }
	/*public DataReader(Context con, String filename, String sqlStart){
		File file ;
		FileReader in = null;
		String inputLineString = "";
		String ltoken = "";
		try {
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(con.getAssets().open( filename + ".csv")));

		    String mLine = reader.readLine();
		    while (mLine != null) {
		       //process line
		//    	StringTokenizer st = new StringTokenizer(mLine,";");
		//    	while(st.hasMoreTokens()){
        //    		ltoken = st.nextToken();
            		
		//    	} 
		    	
		      	mLine = reader.readLine(); 
		      	inputLineString = inputLineString + sqlStart + ";";
		      	
		    }

		    reader.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	*/
	
}