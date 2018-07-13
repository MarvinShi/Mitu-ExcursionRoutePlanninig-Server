import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import GA.Controller;
import GA.UserController;


@WebServlet("/AnswerRequest")

public class AnswerRequest extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private int hitCount;
    private int uid=1;
    public void init()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String responseSt=request.getParameter("point");
        String method=request.getParameter("method");
        PrintWriter out = response.getWriter();
        switch(method){
            case "getCollation":
                out.write(new UserController().getCollation(uid)+"");
                break;
            case "getHistory":
                out.write(new UserController().getHistory(uid)+"");
                break;
            case "getMyEvaluation":
                out.write(new UserController().getMyEvaluation(uid)+"");
                break;
            case "getHisEvaluation":
                out.write(new UserController().getHisEvaluation(request.getParameter("routeInfo"))+"");
                break;
            default:
                out.write("sssssssssssssssssss");
                out.write(responseSt);
        }
        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ;
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

      //  String responseSt=request.getParameter("point");
//        String xxxx=request.getInputStream().toString();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8")); // 实例化输入流
//        String s; // 依次循环，至到读的值为空
//        StringBuilder sb = new StringBuilder();
//        while ((s = reader.readLine()) != null) {
//            sb.append(s);
//        }
//        reader.close();
//        String str = sb.toString();
      //  String xx=request.getQueryString();
      // String sss=request.getAttribute("point").toString();
        String method=request.getParameter("method");
        switch (method) {
            case "navigation":
            int pointSize = Integer.parseInt(request.getParameter("pointSize"));
            int exTime = Integer.parseInt(request.getParameter("time"));
            int exMoney = Integer.parseInt(request.getParameter("money"));
            int stMoney = Integer.parseInt(request.getParameter("stMoney"));
            int stTime = Integer.parseInt(request.getParameter("stTime"));
            String totalPOI = request.getParameter("poi");
            //  将totalPOI用空格进行切割
            Controller GAController = new Controller(pointSize, exTime, exMoney, stTime, stMoney, totalPOI);
            ArrayList<ArrayList> routeList = GAController.response();
            String resString = "";
            //       System.out.println("2222211111111222222222");
            for (int i = 0; i < routeList.size(); i++) {
                ArrayList<Integer> idListOfRoute = routeList.get(i);
                for (int j = 0; j < idListOfRoute.size(); j++) {
                    resString = resString + idListOfRoute.get(j) + " ";
                }
                resString = resString + "+";
            }
            //      System.out.println("22222222222222");
            //       System.out.println(responseSt);
            //      System.out.println(str);
            PrintWriter out = response.getWriter();
            //      out.write(resString);
            out.write(resString);
            out.flush();
            out.close();
            break;
            case "login":
                System.out.println("1111111111111111");
                String lUserid = request.getParameter("userid");
                String lPassword = request.getParameter("password");
                String useInfo=null;
                useInfo=new UserController().loginInfo(lUserid,lPassword);
                this.uid=new UserController().login(lUserid,lPassword);
                PrintWriter lout=response.getWriter();
                lout.write(useInfo);
                lout.flush();
                lout.close();
                break;
            case "register":
                String rUserid = request.getParameter("userid");
                String rPassword = request.getParameter("password");
                String rUsername = request.getParameter("username");
                int reid=0;
                reid =new UserController().register(rUserid,rUsername,rPassword);
                PrintWriter rout=response.getWriter();
                System.out.println(reid);
                rout.write(reid+"");
                rout.flush();
                rout.close();
                break;
            case "addCollation":
                String acStart =request.getParameter("startPoi");
                String acEnd = request.getParameter("endPoi");
                String acRoute = request.getParameter("routeInfo");
                String addCollation = acStart+" "+acEnd+" "+acRoute;
                PrintWriter acout=response.getWriter();
                acout.write(new UserController().addCollation(uid,acStart,acEnd,acRoute)+"");
                acout.flush();
                acout.close();
                break;
            case "addHistory":
                String ahStart =request.getParameter("startPoi");
                String ahEnd = request.getParameter("endPoi");
                String ahRoute = request.getParameter("routeInfo");
                String addHistory = ahStart+" "+ahEnd+" "+ahRoute;
                PrintWriter ahout=response.getWriter();
                ahout.write(new UserController().addCollation(uid,ahStart,ahEnd,ahRoute)+"");
                ahout.flush();
                ahout.close();
            case "addEvaluation":
                String aeStart =request.getParameter("startPoi");
                String aeEnd = request.getParameter("endPoi");
                String aeRoute = request.getParameter("routeInfo");
                String aeContent = request.getParameter("content");
                String addEvaluation = aeStart+" "+aeEnd+" "+aeRoute+" "+aeContent;
                PrintWriter aeout=response.getWriter();
                aeout.write(new UserController().addEvaluation(uid,aeStart,aeEnd,aeRoute,aeContent)+"");
                aeout.flush();
                aeout.close();
                break;
            default: System.out.print("error");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
