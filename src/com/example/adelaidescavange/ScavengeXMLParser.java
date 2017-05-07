package com.example.adelaidescavange;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import android.app.Activity;

import java.io.File;
import java.io.InputStream;
import java.util.StringTokenizer;
 
public class ScavengeXMLParser {
 
  public ScavengeXMLParser(MainActivity a, String fileName, int dbVersionNumber) {
	  System.out.println("ScavengeXMLParser");
    try { 
    	InputStream is =  a.getAssets().open(fileName) ;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is); 
		doc.getDocumentElement().normalize();
	 
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("databaseVersion");

		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
			NamedNodeMap nodeMap = nNode.getAttributes();
			int nodeDBVersionNumber = dbVersionNumber;
			for (int i = 0; i < nodeMap.getLength(); i++) {
 
				Node node = nodeMap.item(i);
				//System.out.println("attr name : " + node.getNodeName());
				//System.out.println("attr value : " + node.getNodeValue());
				nodeDBVersionNumber = Integer.parseInt(node.getNodeValue());
			}
			
			//System.out.println("dbVersionNumber [" + dbVersionNumber + "] < nodeDBVersionNumber [" + nodeDBVersionNumber + "]"); 
			if(dbVersionNumber < nodeDBVersionNumber){
				//System.out.println("UPDATING"); 

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						//System.out.println("nname" + nNode.getNodeName() );
						Element eElement = (Element) nNode;
							//create
							//System.out.println("" + dbVersionNumber  + "< " +nodeDBVersionNumber );
							String sqlstatement = eElement.getElementsByTagName("create").item(0).getTextContent().trim();
							StringTokenizer st = new StringTokenizer(sqlstatement,";");
							//System.out.println("create Statement : [" + sqlstatement.length() + "] \n" + sqlstatement);
							while(st.hasMoreTokens()){
								String tempToken = st.nextToken();
								//System.out.println(tempToken);
								a.getDb().execSQL(tempToken);
							
							}
							//insert
							sqlstatement = eElement.getElementsByTagName("insert").item(0).getTextContent().trim();
							//System.out.println("insert Statement : [" + sqlstatement.length() + "] \n" + sqlstatement);
							st = new StringTokenizer(sqlstatement,";");
							while(st.hasMoreTokens()){
								//System.out.println("WILL CALL insert");
								a.getDb().execSQL(st.nextToken());
							}
							//update
							sqlstatement = eElement.getElementsByTagName("update").item(0).getTextContent().trim();
							//System.out.println("update Statement : [" + sqlstatement.length() + "] \n" + sqlstatement);
							st = new StringTokenizer(sqlstatement,";");
							while(st.hasMoreTokens()){
								//System.out.println("WILL CALL update");
								a.getDb().execSQL(st.nextToken());
							}
							//delete
							sqlstatement = eElement.getElementsByTagName("delete").item(0).getTextContent().trim();
							//System.out.println("delete Statement : [" + sqlstatement.length() + "] \n" + sqlstatement);
							st = new StringTokenizer(sqlstatement,";");
							while(st.hasMoreTokens()){
								//System.out.println("WILL CALL delete");
								a.getDb().execSQL(st.nextToken());
							}
					}

			}
		}
    } catch (Exception e) {
    	System.out.println("ERROR LOADING DATA IN");
    	e.printStackTrace();
    }
  }
 
}