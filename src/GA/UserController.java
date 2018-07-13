package GA;
import Controller.UserDAO;

public class UserController {
    public int login(String userid,String password)
    {
        return new UserDAO().getUid(userid,password);
    }

    public String loginInfo(String userid,String password)
    {
        String loginInfo=null;
        loginInfo=""+login(userid,password)+" "+new UserDAO().getUserName(userid);
        return  loginInfo;
    }

    public int register(String userid,String username,String password)
    {
        if(login(userid,password)==-1)
        {
            if (new UserDAO().setNewUser(userid,username,password)!=0)
                return 1;
            else
                return 0;
        }
        else return -1;
    }

    public int addCollation(int uid,String startpoi,String endpoi,String routeInfo)
    {
        int routeID=new UserDAO().getRouteID(startpoi,endpoi,routeInfo);
        int rs=new UserDAO().setNewCollection(uid,routeID);
        return rs;
    }

    public int addEvaluation(int uid,String startpoi,String endpoi,String routeInfo,String content)
    {
        int routeID=0;
        routeID=new UserDAO().getRouteID(startpoi,endpoi,routeInfo);
        return new UserDAO().setNewEvaluation(uid,routeID,content);
    }

    public int addHistory(int uid,String startpoi,String endpoi,String routeInfo)
    {
        int routeID=new UserDAO().getRouteID(startpoi,endpoi,routeInfo);
        int rs=new UserDAO().setNewCollection(uid,routeID);
        if(rs==1)
            return 1;
        else return 0;
    }

    public String getCollation(int uid)
    {
        String collation=null;
        collation=new UserDAO().getCollection(uid);
        return collation;
    }
    public String getHistory(int uid)
    {
        String history=null;
        history=new UserDAO().getHistory(uid);
        return history;
    }

    public String getHisEvaluation(String routeInfo)
    {
        return new UserDAO().getHisEvaluation(routeInfo);
    }

    public String getMyEvaluation(int uid)
    {
        return new UserDAO().getMyEvaluation(uid);
    }
}
