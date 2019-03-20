package App.helpers.database;

import App.helpers.database.enums.SQLTypes;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

class QueryMaster {
    Connector connector;
    HashMap<String, StatementHelper> preparedStatements;

    QueryMaster(Connector connector){
        this.connector = connector;
        this.preparedStatements = new HashMap<>();
    }

    boolean addQuery(String key, String query, SQLTypes[] dataTypes, Class<?> someClass){
        //todo this has no way to confirm anything. Needs to have possible failure.
        boolean confirmation = false;
        try {
            preparedStatements.put(key, new StatementHelper(query, connector, dataTypes, someClass));
            confirmation = true;
        }catch (Exception e){}
        return confirmation;
    }

    boolean updateQueryWriteData(String queryKey, Object[] data){
        boolean isSuccessful = false;
        try{
            StatementHelper statementHelper = preparedStatements.get(queryKey);
            for(int i = 0; i < statementHelper.dataTypes.length; ++i){
                statementHelper.setSomething(i, data[i]);
            }
            isSuccessful = true;
        }catch (Exception e){}
        return isSuccessful;
    }

    void removeQuery(String key){
        try{
            this.preparedStatements.remove(key);
        } catch (Exception e){}
    }

    void kill(){
        try{
            for (StatementHelper statement : preparedStatements.values())
                statement.close();
        } catch (Exception e){}
    }

    ResultSet runQuery(String queryKey){
        return this.preparedStatements.get(queryKey).runQuery();
    }

}
