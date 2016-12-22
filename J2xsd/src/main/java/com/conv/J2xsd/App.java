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
        c.repackJson("");
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}
