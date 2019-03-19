package App.helpers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class Connector {

    private Connection connection = null;

    void kill(){
        try{
            connection.close();
        }catch (SQLException e){e.printStackTrace();}
    }

    static Connector connectToDatabase(String host, String timezone, String database, String username, String password){
        Connector connector = new Connector();
        try{
            connector.connection = DriverManager.
                    getConnection("jdbc:mysql://"+host+"/"+database+"?user="+username+"&password="+password+"&serverTimezone="+timezone);
        } catch (Exception e){
            e.printStackTrace();
        }
        return connector;
    }

    Connection getConnection(){
        return connection;
    }

    boolean isConnected(){
        return connection != null;
    }

}
