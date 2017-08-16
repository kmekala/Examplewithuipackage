package com.pega.uiframework.uitest;

/**
 * Created by mekak2 on 5/9/17.
 */

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
public class PropertiesFileTest {
    Properties props=new Properties();
    String propertyFileName=System.getProperty("user.dir")+"/resources/test.properties";

   @Test(priority = 0)
    public void testSetProperty() throws Exception{

       String propertyKey="ManagerLogin";
       String value="test@pega.com";
       props.setProperty(propertyKey, value);
       props.store(new FileOutputStream(propertyFileName),null);
      System.out.println(props.setProperty(propertyKey, value));
   }

    @Test(priority = 1)
    public void testGetProperty() throws Exception {
        String propertyName = "test@pega.com";
        props.load(new FileInputStream(propertyFileName));
        String expectedPropertyValue = props.getProperty("ManagerLogin");
        Assert.assertEquals(propertyName,expectedPropertyValue);
    }

    @AfterClass

    public void removeFile(){
        try{
            File file = new File(propertyFileName);
            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}