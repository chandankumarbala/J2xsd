package com.conv.J2xsd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import com.ethlo.schematools.jsons2xsd.Jsons2Xsd;
import com.ethlo.schematools.jsons2xsd.Jsons2Xsd.OuterWrapping;
import com.ethlo.schematools.jsons2xsd.Jsons2XsdSingleFile;
import com.ethlo.schematools.jsons2xsd.XmlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {

	
/*	public void testConversionCMTS() throws IOException, TransformerException
	{

			final Reader r = new InputStreamReader(getClass().getResourceAsStream("/depositAccountSchemaV1_1.json"));
			final Reader def = new InputStreamReader(getClass().getResourceAsStream("/commonSchemaV1_0.json"));


			final Document doc = Jsons2XsdSingleFile.convert(r, def, "http://cableapi.cablelabs.com/schemas/v1/CMTS", OuterWrapping.ELEMENT, "CMTS");
			System.out.println(XmlUtil.asXmlString(doc.getDocumentElement()));

	}
	*/
	
	public void testConversionMedium( String flePath) throws IOException, TransformerException
	{
		
			FileInputStream fis = new FileInputStream(flePath);
			Reader r = new InputStreamReader(fis);
			final Document doc = Jsons2Xsd.convert(r, "http://dbs.bank.com", OuterWrapping.TYPE, "AccountUsers");
			//System.out.println(XmlUtil.asXmlString(doc.getDocumentElement()));
			String outputFilePath=flePath.replace(".json", ".xsd");
			FileWriter fw=new FileWriter(outputFilePath);
			fw.write(XmlUtil.asXmlString(doc.getDocumentElement()));
			fw.close();
			System.out.println("XSD file generated at location => "+outputFilePath);
		
	}
	public void testConversionMedium( String flePath,String otherFile) throws IOException, TransformerException
	{
		try 
		{
			FileInputStream fis = new FileInputStream(flePath);
			Reader r = new InputStreamReader(fis);
			Reader def = new InputStreamReader( new FileInputStream(otherFile));
			Document doc = Jsons2XsdSingleFile.convert(r, def, "http://cableapi.cablelabs.com/schemas/v1/CMTS", com.ethlo.schematools.jsons2xsd.Jsons2XsdSingleFile.OuterWrapping.ELEMENT, "CMTS");
			//final Document doc = Jsons2Xsd.convert(r, "http://dbs.bank.com", OuterWrapping.TYPE, "AccountUsers");
			System.out.println(XmlUtil.asXmlString(doc.getDocumentElement()));
			String outputFilePath=flePath.replace(".json", ".xsd");
			FileWriter fw=new FileWriter(outputFilePath);
			fw.write(XmlUtil.asXmlString(doc.getDocumentElement()));
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*public String readCompleteFileAsString(File file){
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
	
	
	
	
	public void repackJson(String filename){
		try{
		ObjectMapper mapper = new ObjectMapper();
		 Reader r = new InputStreamReader(getClass().getResourceAsStream("/depositAccountSchemaV1_1.json"));
		 JsonNode rootNode = mapper.readTree(r);
		 JsonNode properties =rootNode.get("properties");
		 final Iterator<Entry<String, JsonNode>> fieldIter = properties.fields();
			while(fieldIter.hasNext())
			{
				final Entry<String, JsonNode> entry = fieldIter.next();
				final String key = entry.getKey();
				final JsonNode val = entry.getValue();
				//System.out.println(val.path("type"));
				//doIterateSingle(key, val, elem);
			}
			
			Matcher matcher=this.getReferences("D:\\spring_boot_projects\\jsonToXsd\\J2xsd\\src\\main\\resources\\depositAccountSchemaV1_1.json");
			while (matcher.find()) {
			    for (int i = 1; i <= matcher.groupCount(); i++) {
			    	
			    	//JsonNode resolvedJson=
			    			this.getResolvedJson(matcher.group(i).toString());
			        //System.out.println("Group " + i + ": " + matcher.group(i));
			    }
		} 
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}
	
	
	
	public Matcher getReferences(String filePath){
		String contents=this.readCompleteFileAsString(new File(filePath));
		//Pattern p = Pattern.compile("(.*?(\"\\$ref\").*?)(?:[^\"]*\"){2}");
		String regex = "(\\\"\\$ref\\\":(?:[^\\\"]*\\\"){2})";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(contents);
		return matcher;
		while (matcher.find()) {
		    for (int i = 1; i <= matcher.groupCount(); i++) {
		        System.out.println("Group " + i + ": " + matcher.group(i));
		    }
		}
		
		
	}
	
	public JsonNode getResolvedJson(String match){
		//System.out.println("Ref ->"+match);
		JsonNode capuuredJson=null;
		String reference=match.trim();
		String[] components=reference.split(":")[1].split("#");
		String path=components[1].replaceAll("/definitions/","");
		path=path.substring(0, path.length()-1);
		String file=components[0].trim();
		file=file.substring(1, file.length());
	
		if(file.length()!=0){//from file
			capuuredJson=resolveFromFile(file,path);
		}else{//from local
			 capuuredJson=resolveFromInternal(file,path);
			//System.out.println(path+"--");
			//System.out.println(path);
		}
		return capuuredJson;
	}
	
	public JsonNode resolveFromFile(String fileName,String path){
		
		JsonNode output=null;
		try{
		ObjectMapper mapper = new ObjectMapper();
		String contents=this.readCompleteFileAsString(new File("D:\\spring_boot_projects\\jsonToXsd\\J2xsd\\src\\main\\resources\\commonSchemaV1_0.json"));
		String  regex = "(:?\\\"matchedRec\\\":\\s*\\{(?>[^\\}{]+)*\\})";
		String mainRegex=regex.replace("matchedRec", path);
		Pattern pattern = Pattern.compile(mainRegex, Pattern.COMMENTS);
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()){
		  //System.out.println("Full match: " + matcher.group(0).replace("\""+path+"\":", ""));
		  output=mapper.readTree(matcher.group(0).replace("\""+path+"\":", ""));
		}else{
			 Reader r = new InputStreamReader(getClass().getResourceAsStream("/commonSchemaV1_0.json"));
			 JsonNode rootNode = mapper.readTree(r);
			 JsonNode properties =rootNode.get("properties");
			 output=properties.get("path");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("From def=>"+path+"=>"+output.toString());
		return output;
	}
	
	public JsonNode resolveFromInternal(String resourcefileName,String path){
		System.out.println("From source=>"+path);
		JsonNode output=null;
		try{
		ObjectMapper mapper = new ObjectMapper();
		String contents=this.readCompleteFileAsString(new File("D:\\spring_boot_projects\\jsonToXsd\\J2xsd\\src\\main\\resources\\depositAccountSchemaV1_1.json"));
		String  regex = "(:?\\\"matchedRec\\\":\\s*\\{(?>[^\\}{]+)*\\})";
		String mainRegex=regex.replace("matchedRec", path);
		Pattern pattern = Pattern.compile(mainRegex, Pattern.COMMENTS);
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()){
		  //System.out.println("Full match: " + matcher.group(0).replace("\""+path+"\":", ""));
		  output=mapper.readTree(matcher.group(0).replace("\""+path+"\":", ""));
		}else{
			 Reader r = new InputStreamReader(getClass().getResourceAsStream("/depositAccountSchemaV1_1.json"));
			 JsonNode rootNode = mapper.readTree(r);
			 JsonNode properties =rootNode.get("properties");
			 output=properties.get(path);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("From source=>"+path+"=>"+output.toString());
		return output;
	}*/
	
}
