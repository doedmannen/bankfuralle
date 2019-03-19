package App.helpers.database;

import App.helpers.database.enums.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class StatementHelper {
    Class<?> mappingClass;
    PreparedStatement preparedStatement;
    QueryCommand CRUD;
    SQLTypes[] dataTypes;

    StatementHelper(String query, Connector connector, SQLTypes[] dataTypes, Class<?> mappingClass){
        try{
            this.preparedStatement = connector.getConnection().prepareStatement(query);
            this.mappingClass = mappingClass;
            this.CRUD = query.toLowerCase().startsWith("select") ? QueryCommand.READ : QueryCommand.UPDATE;
            this.dataTypes = dataTypes;
        } catch (Exception e){e.printStackTrace();}
    }

    void close(){
        try{
            this.preparedStatement.close();
        }catch (Exception e){}
    }

    <T> void setSomething(int index, T value) throws Exception{
        switch (dataTypes[index]){
            case INT:
                preparedStatement.setInt((index+1), (Integer) value);
                break;
            case LONG:
                preparedStatement.setLong((index+1), (Long) value);
                break;
            case DOUBLE:
                preparedStatement.setDouble((index+1), (Double) value);
                break;
            case STRING:
                preparedStatement.setString((index+1), (String) value);
                break;
        }
    }

    ResultSet runQuery(){
        ResultSet rs = null;
        try{
            if(this.CRUD == QueryCommand.READ)
                rs = preparedStatement.executeQuery();
            else
                preparedStatement.executeUpdate();
        }catch (Exception e){e.printStackTrace();}
        return rs;
    }
}
