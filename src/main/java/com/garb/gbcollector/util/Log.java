package com.garb.gbcollector.util;

import java.io.*;
import java.util.Date;

import com.garb.gbcollector.web.vo.RequestInforVO;

import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Log{

    //public static String m_PathName = Util.LOG_FILE_PATH; 
   
    public static String m_FileName = "log";     
    private static FileWriter objfile = null;

    /***************************
     * Logging Method          * 
     ***************************/
    
    public void TraceLog(String log)
    {
        int i                 = 0;
        String stPath         = "";
        String stFileName     = "";
       
        String m_PathName = "c://test//log//";  

        stPath     = m_PathName;
        stFileName = m_FileName;
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyyMMdd");
        SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
       
        String stDate = formatter1.format(new Date());
        String stTime = formatter2.format(new Date());
        StringBuffer bufLogPath  = new StringBuffer();      
                     bufLogPath.append(stPath);
                     bufLogPath.append(stFileName);
                     bufLogPath.append("_");
                     bufLogPath.append(stDate);
                     bufLogPath.append(".log") ;
        StringBuffer bufLogMsg = new StringBuffer();
            bufLogMsg.append("[");
            bufLogMsg.append(stTime);
            bufLogMsg.append("]\r\n");            
            bufLogMsg.append(log);
                    
        try{

                objfile = new FileWriter(bufLogPath.toString(), true);
                objfile.write(bufLogMsg.toString());
                objfile.write("\r\n");
        }catch(IOException e){
           
        }
        finally
        {
            try{
             objfile.close();
            }catch(Exception e1){}
        }
    }

    public void TraceLog(Exception err)
    {
        int i                 = 0;
        String stPath         = "";
        String stFileName     = "";
       
        String m_PathName = "c://test//log//";  

        StringWriter sw = new StringWriter();
        err.printStackTrace(new PrintWriter(sw));
        String log = sw.toString();
        stPath     = m_PathName;
        stFileName = m_FileName;
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyyMMdd");
        SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
       
        String stDate = formatter1.format(new Date());
        String stTime = formatter2.format(new Date());
        StringBuffer bufLogPath  = new StringBuffer();      
                     bufLogPath.append(stPath);
                     bufLogPath.append(stFileName);
                     bufLogPath.append("_");
                     bufLogPath.append(stDate);
                     bufLogPath.append(".log") ;
        StringBuffer bufLogMsg = new StringBuffer();
            bufLogMsg.append("[");
            bufLogMsg.append(stTime);
            bufLogMsg.append("]\r\n");            
            bufLogMsg.append(log);
                    
        try{

                objfile = new FileWriter(bufLogPath.toString(), true);
                objfile.write(bufLogMsg.toString());
                objfile.write("\r\n");
        }catch(IOException e){
           
        }
        finally
        {
            try{
             objfile.close();
            }catch(Exception e1){}
        }
    }
    
    public void TraceLog(RequestInforVO infor, String desc)
    {
        int i                 = 0;
        String stPath         = "";
        String stFileName     = "";
       
        String m_PathName = "c://test//log//";  

        stPath     = m_PathName;
        stFileName = m_FileName;
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyyMMdd");
        SimpleDateFormat formatter2 = new SimpleDateFormat ("HH:mm:ss");
       
        String stDate = formatter1.format(new Date());
        String stTime = formatter2.format(new Date());
        StringBuffer bufLogPath  = new StringBuffer();      
                     bufLogPath.append(stPath);
                     bufLogPath.append(stFileName);
                     bufLogPath.append("_");
                     bufLogPath.append(stDate);
                     bufLogPath.append(".log") ;
        StringBuffer bufLogMsg = new StringBuffer();
            bufLogMsg.append("[");
            bufLogMsg.append(stTime);
            bufLogMsg.append("]\r\n");
            bufLogMsg.append(infor.getInfor());
            bufLogMsg.append(desc);
            
        System.out.println(bufLogMsg);        
        try{

                objfile = new FileWriter(bufLogPath.toString(), true);
                objfile.write(bufLogMsg.toString());
                objfile.write("\r\n");
        }catch(IOException e){
           
        }
        finally
        {
            try{
             objfile.close();
            }catch(Exception e1){}
        }
    }
}