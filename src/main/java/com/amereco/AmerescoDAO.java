package com.amereco;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AmerescoDAO {
    private Connection connection;
    public AmerescoDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/terragoedge", "postgres", "password");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<String> getNoteWrongNoteTitles(int deletedNotebook, int currentNotebook){
        List<String> titles = new ArrayList<String>();
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select title from edgenote where notebookid="+currentNotebook+" and title in(select title from edgenote where notebookid="+deletedNotebook+" and iscurrent = true and isdeleted=false and createdby!='admin') and iscurrent=true and isdeleted=false");
            while (resultSet.next()){
                titles.add(resultSet.getString("title"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(statement != null){
                try {
                    statement.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return titles;
    }
    public String getNoteGuidByTitle(String title,int notebookid){
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select noteguid from edgenote where title="+title+"and notebookid="+notebookid);
            while (resultSet.next()){
                return resultSet.getString("noteguid");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(statement != null){
                try {
                    statement.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
