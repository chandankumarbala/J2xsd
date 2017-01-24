package com.conv.J2xsd;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
        Converter c=new Converter();
        try{
       // c.testConversionCMTS();
       // c.testConversionMedium(args[0]);
        c.testConversionMedium("D:\\spring_boot_projects\\drools\\J2npm\\static\\accountHolderDetl_Schema_V1_0_ouput.json");
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}
//D:\Users\chandanbala\AppData\Roaming\npm\node_modules\json-schema-ref-parser\custom\abhay\transactionHistory_Schema_V1_0_ouput.json