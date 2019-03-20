package App.helpers.database;

import App.helpers.database.enums.SQLTypes;
import App.helpers.database.revivers.ObjectRelationMapper;
import java.sql.ResultSet;
import java.util.List;

public class SQLHelper {

    Connector connector;
    QueryMaster queryMaster;

    public SQLHelper(String host, String timezone, String DB, String user, String password){
        connector = Connector.connectToDatabase(host, timezone, DB, user, password);
        queryMaster = new QueryMaster(connector);
    }

    public void kill(){
        connector.kill();
        queryMaster.kill();
    }

    public boolean createQuery(String queryKey, String query){
        return this.queryMaster.addQuery(queryKey, query, null, null);
    }

    public boolean createQuery(String queryKey, String query, SQLTypes[] dataTypes){
        return this.queryMaster.addQuery(queryKey, query, dataTypes, null);
    }

    public boolean createQuery(String queryKey, String query, Class<?> someClass){
        return this.queryMaster.addQuery(queryKey, query, null, someClass);
    }

    public boolean createQuery(String queryKey, String query, SQLTypes[] dataTypes, Class<?> someClass){
        return this.queryMaster.addQuery(queryKey, query, dataTypes, someClass);
    }


    public List<?> getListFromQuery(String queryKey){
        //todo this needs to be able to take a list of filters
        ResultSet rs = queryMaster.runQuery(queryKey);
        Class mapperClass = queryMaster.preparedStatements.get(queryKey).mappingClass;
        ObjectRelationMapper valueMapper = new ObjectRelationMapper(mapperClass);
        return (List<?>) valueMapper.map(rs);
    }

    public List<?> getListFromQuery(String queryKey, Object ...data){
        if(queryMaster.updateQueryWriteData(queryKey, data)){
            return getListFromQuery(queryKey);
        }
        return null;
    }

    public Object getObjectFromQuery(String queryKey, Object ...data){
        if(queryMaster.updateQueryWriteData(queryKey, data)){
            return getListFromQuery(queryKey).get(0);
        }
        return null;
    }

    public boolean runQueryWithData(String queryKey, Object ...data){
        boolean isSuccessful = false;
        if(queryMaster.updateQueryWriteData(queryKey, data)){
            queryMaster.runQuery(queryKey);
            isSuccessful = true;
        }
        System.out.println(isSuccessful);
        return isSuccessful;
    }


}
