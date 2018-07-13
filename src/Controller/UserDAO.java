package Controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/mituDB";
    static final String USER="root";
    static final String PASSWORD="123456";
    public DataSource dataSource;
    public Connection conn;
    public Statement stmt;


    public UserDAO()
    {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");  //path
            dataSource = (DataSource)envCtx.lookup("jdbc/datasource");   //context.xml中resource的name
        } catch (NamingException e) {
            e.printStackTrace();
        }
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUid(String userID,String password) {
        int uid = -1;
        String queryUidSql = "select uid from User where UserID='"+userID+"' and Password ='"+ password +"'";
        try {
            ResultSet rs = stmt.executeQuery(queryUidSql);
            while(rs.next()){
                uid = rs.getInt("uid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uid;
    }

    public String getUserID(String userName) {
        String  userID = null;
        String queryUserIDSql = "select UserID from User where UserName='"+userName+"'";
        try {
            ResultSet rs = stmt.executeQuery(queryUserIDSql);
            while(rs.next()){
                userID = rs.getString("UseID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    public String getUserName(String userID) {
        String  userName = null;
        String queryUserNameSql = "select UserName from User where UserID='"+userID+"'";
        try {
            ResultSet rs = stmt.executeQuery(queryUserNameSql);
            while(rs.next()){
                userName = rs.getString("UserName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    public int getPoiID(String routeInfo)
    {
        int poiID=0;
        String queryPoiIDSql = "select PoiID from PoiRoute where RouteInfo='"+routeInfo+"'";
        try {
            ResultSet rs = stmt.executeQuery(queryPoiIDSql);
            while(rs.next()){
                poiID = rs.getInt("PoiID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return poiID;
    }

    public int getRouteID(String startPoi,String endPoi,String routeInfo)
    {
        int routeId=0;
        int poiID=getPoiID(routeInfo);
        if(poiID!=0)
        {
        String queryRouteIDSql = "select RouteID from Route where StartPoi='"+startPoi+"' and EndPoi='"+endPoi+"' and PoiID="+poiID;
        try {
            ResultSet rs = stmt.executeQuery(queryRouteIDSql);
            while(rs.next()){
                routeId = rs.getInt("RouteID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routeId;
        }
        else return 0;
    }

    public String getCollection(int uid)
    {
        String  cRoute = null;
        String queryUserNameSql = "select RouteID,StartPoi,EndPoi,RouteInfo from Collection natural join Route natural join PoiRoute where uid="+uid;
        try {
            ResultSet rs = stmt.executeQuery(queryUserNameSql);
            while(rs.next()){
                if(cRoute!=null)
                cRoute= cRoute+rs.getString("RouteID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+"#";
                else cRoute=rs.getString("RouteID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+"#";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cRoute;
    }

    public String getHistory(int uid)
    {
        String  hRoute = null;
        String queryUserNameSql = "select RouteID,StartPoi,EndPoi,RouteInfo from Histroy natural join Route natural join PoiRoute where uid="+uid;
        try {
            ResultSet rs = stmt.executeQuery(queryUserNameSql);
            while(rs.next()){
                if(hRoute!=null)
                hRoute= hRoute+rs.getString("RouteID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+"#";
                else hRoute= rs.getString("RouteID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+"#";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hRoute;
    }

    public String getMyEvaluation(int uid)
    {
        String myEvalu = null;
        String queryEvaluSql="select EvaluationID,StartPoi,EndPoi,RouteInfo,Content from Evaluation natural join Route natural join PoiRoute where uid ="+uid;
        try {
            ResultSet rs = stmt.executeQuery(queryEvaluSql);
            while(rs.next()){
                if(myEvalu!=null)
                myEvalu= myEvalu+rs.getString("EvaluationID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+" "+rs.getString("Content")+"#";
                else myEvalu= rs.getString("EvaluationID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+" "+rs.getString("Content")+"#";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myEvalu;
    }

    public String getHisEvaluation(String RouteInfo)
    {
        String hisEvalu = null;
        String queryEvaluSql="select EvaluationID,StartPoi,EndPoi,RouteInfo,Content from Evaluation natural join Route natural join PoiRoute where RouteInfo ="+RouteInfo;
        try {
            ResultSet rs = stmt.executeQuery(queryEvaluSql);
            while(rs.next()){
                if(hisEvalu!=null)
                hisEvalu= hisEvalu+rs.getString("EvaluationID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+" "+rs.getString("Content")+"#";
                else hisEvalu= rs.getString("EvaluationID")+" "+rs.getString("StartPoi")+" "+rs.getString("EndPoi")+" "+rs.getString("RouteInfo")+" "+rs.getString("Content")+"#";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hisEvalu;
    }

    public int setNewUser(String userID,String userName,String password)
    {
        int res=0;
        String setNewUserSql = "insert into User (UserId,UserName,Password) values ('"+userID+"','"+userName+"','"+password+"')";
        try {
             res = stmt.executeUpdate(setNewUserSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public int setNewPoiRoute(String routeInfo)
    {
        int res=0;
        int check=0;
        String getPoiRouteSql = "select PoiID from PoiRoute where RouteInfo='"+routeInfo+"'";
        try {
            ResultSet rs = stmt.executeQuery(getPoiRouteSql);
            while(rs.next()){
                check = rs.getInt("PoiID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0) {
            String setNewPoiRouteSql = "insert into PoiRoute (RouteInfo) values ('" + routeInfo + "')";
            try {
                res = stmt.executeUpdate(setNewPoiRouteSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        }
        return -1;
    }

    public int setNewRoute(int poiID,String startPoi,String endPoi)
    {
        int res =0;
        int check=0;
        String getRouteSql = "select RouteID from Route where PoiId="+poiID+" and StartPoi='"+startPoi+"' and EndPoi ='"+endPoi+"'";
        try {
            ResultSet rs = stmt.executeQuery(getRouteSql);
            while(rs.next()){
                check = rs.getInt("RouteID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0) {
            String setNewRouteSql = "insert into Route (PoiID,StartPoi,EndPoi) values (" + poiID + ",'" + startPoi + "','" + endPoi + "')";
            try {
                res = stmt.executeUpdate(setNewRouteSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        }
        else return -1;
    }

    public int setNewCollection(int uid,int routeID)
    {
        int res=0;
        int check=0;
        String getCollectionSql = "select uid from Collection where uid="+uid+"and routeID="+routeID;
        try {
            ResultSet rs = stmt.executeQuery(getCollectionSql);
            while(rs.next()){
                 check = rs.getInt("uid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0)
        {
            String setNewCollectionSql = "insert into Collection (uid,RouteID) values ("+uid+","+routeID+")";
        try {
             res = stmt.executeUpdate(setNewCollectionSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
        }
        else return -1;
    }

    public int setNewHistory(int uid,int routeID)
    {
        int res=0;
        int check=0;
        String getHistorySql = "select uid from History where uid="+uid+"and routeID="+routeID;
        try {
            ResultSet rs = stmt.executeQuery(getHistorySql);
            while(rs.next()){
                check = rs.getInt("uid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(check==0) {
            String setNewHistorySql = "insert into History (uid,RouteID) values (" + uid + "," + routeID + ")";
            try {
                res = stmt.executeUpdate(setNewHistorySql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return res;
        }
        else return -1;
    }

    public int setNewEvaluation(int uid,int routeID,String content)
    {
        int res=0;
        String setNewEvaluationSql = "insert into Route (PoiID,StartPoi,EndPoi) values ("+uid+","+routeID+",'"+content+"')";
        try {
             res = stmt.executeUpdate(setNewEvaluationSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
