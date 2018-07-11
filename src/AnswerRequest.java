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


@WebServlet("/AnswerRequest")

public class AnswerRequest extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private int hitCount;
    public void init()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String responseSt=request.getParameter("point");
        PrintWriter out = response.getWriter();
        out.write("sssssssssssssssssss");
        out.write(responseSt);
        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ;
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        System.out.println("ssssssssssssssssssss11111111111s");
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
        int pointSize = Integer.parseInt(request.getParameter("pointSize"));
        int exTime = Integer.parseInt(request.getParameter("time"));
        int exMoney = Integer.parseInt(request.getParameter("money"));
        int stMoney =Integer.parseInt(request.getParameter("stMoney"));
        int stTime =Integer.parseInt(request.getParameter("stTime"));
        String totalPOI= request.getParameter("poi");
      //  将totalPOI用空格进行切割
        Controller GAController= new Controller(pointSize, exTime, exMoney,stTime,stMoney,totalPOI);
        ArrayList<ArrayList> routeList= GAController.response();
        String resString="";
 //       System.out.println("2222211111111222222222");
        for(int i=0;i<routeList.size();i++)
        {
            ArrayList<Integer> idListOfRoute= routeList.get(i);
            for(int j=0;j<idListOfRoute.size();j++)
           {
                resString=resString+idListOfRoute.get(j)+" ";
            }
            resString=resString+"+";
        }
  //      System.out.println("22222222222222");
 //       System.out.println(responseSt);
        //      System.out.println(str);
        PrintWriter out = response.getWriter();
  //      out.write(resString);
        out.write(resString);
        out.flush();
        out.close();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
