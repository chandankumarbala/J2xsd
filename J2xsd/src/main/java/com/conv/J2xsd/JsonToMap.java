package com.conv.J2xsd;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonToMap {
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json != JSONObject.NULL) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	
	public static String readCompleteFileAsString(File file){
		String fileContects ="";
		try{
		FileInputStream fis = new FileInputStream(file);
		byte[] data  = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		
		 fileContects = new String(data, "UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			return fileContects;
		}
	}
	
	public JSONObject search(JSONObject data,String searchTerm){
		JSONObject ret=null;
		System.out.println(JSONObject.getNames(data).length);
		for(String key:JSONObject.getNames(data)){
			System.out.println("key=>"+key);
			if(key.equals(searchTerm)){
				ret= data.getJSONObject(key);
			}
			else{
				ret= search(data.getJSONObject(key),searchTerm);
			}
		}
		return ret;
	}
	
	 public static void main( String[] args )
	    {
		 try{
			 JsonToMap x=new JsonToMap();
			 String contents=readCompleteFileAsString(new File("D:\\spring_boot_projects\\jsonToXsd\\J2xsd\\src\\main\\resources\\commonSchemaV1_0.json"));
			//System.out.println(new JSONObject(contents).toString());
			 //Map<String, Object> data= jsonToMap(new JSONObject(contents).getJSONObject("properties"));
			JSONObject prop=new JSONObject(contents).getJSONObject("properties");
			System.out.println(x.search(prop,"partyStatus").toString());
		 }catch(Exception w){
			 
		 }
	    }

}
