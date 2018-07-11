package Controller;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;

public class DAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/mituDB";
    static final String USER="root";
    static final String PASSWORD="123456";
    public DataSource dataSource;
    public Connection conn;
    public Statement stmt;
    public DAO() {

        try {
              Context initCtx = new InitialContext();
              Context envCtx = (Context) initCtx.lookup("java:comp/env");  //path
              dataSource = (DataSource)envCtx.lookup("jdbc/datasource");   //context.xml中resource的name
        } catch (NamingException e) {
            e.printStackTrace();
        }
//        try {
           //Class.forName("com.mysql.jdbc.Driver");
            try {
                conn = dataSource.getConnection();
              //  conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

                stmt = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

  public  double queryRouteTime(int POIA,int POIB)
    {
        double waytime=600;
        String queryRouteTimeSql="select waytime from routedbitem where poidbitem_id ="+  POIA+" AND poidbitem2_id ="+POIB;
        try {
          ResultSet rs = stmt.executeQuery(queryRouteTimeSql);
            while(rs.next()){
                waytime = rs.getDouble("waytime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return waytime;
    }

   public  double queryRouteMoney(int POIA,int POIB)
    {
        double waycost=0;
        String queryRouteTimeSql="select waycost from routedbitem where poidbitem_id="+  POIA+" AND poidbitem2_id ="+POIB;
        try {
            ResultSet rs = stmt.executeQuery(queryRouteTimeSql);
            while(rs.next()){
                waycost = rs.getDouble("waycost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return waycost;
    }

  public  double queryPOITime(int POI)
    {
        double poiTime=0;
        String queryRouteTimeSql="select time_cost from poidbitem where id="+  POI;
        try {
            ResultSet rs = stmt.executeQuery(queryRouteTimeSql);
            while(rs.next()) poiTime = rs.getDouble("time_cost");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poiTime*3600;
    }

   public  double queryPOIMoney(int POI)
    {
        double poiMoney=0;
        String queryRouteTimeSql="select money_cost from poidbitem where id="+  POI;
        try {
            ResultSet rs = stmt.executeQuery(queryRouteTimeSql);
            while(rs.next()) poiMoney = rs.getDouble("money_cost");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poiMoney;
    }



}
