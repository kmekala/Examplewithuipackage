package com.pega.uiframwork.uitest.util;

import java.util.Hashtable;

/**
 * Created by mekak2 on 4/17/17.
 */
public class Constants {

    public static final boolean GRID_RUN=false;

    //paths
    //public static final String CHROME_DRIVER_EXE=System.getProperty("user.dir")+"/ChromeDriver/chromedriver";
    //paths
    public static final String REPORTS_PATH = System.getProperty("user.dir")+ "/reports";
    public static final String DATA_XLS_PATH = System.getProperty("user.dir") + "/config/Data.xlsx";
    public static final String TESTDATA_SHEET = "TestData";
    public static final Object RUNMODE_COL = "Runmode";
    public static final String TESTCASES_SHEET = "TestCases";


    public static Hashtable<String,String> table;


    public static Hashtable<String,String> getEnvDetails(){
        if(table==null){
            table = new Hashtable<String,String>();
        }
        return table;
    }
}