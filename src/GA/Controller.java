package GA;

import java.awt.*;
import java.util.ArrayList;
import Controller.DAO;


public class Controller {

    public int pointSize;
    public int exTime;
    public int exMoney;
    public int []poiId;
    public String []poiName;

    public double []tL;
    public double []mL;

    public double [][] poi_way_Tcost ;
    public double [][] poi_way_Mcost ;
    public float[] results_time;
    public float[] results_money;
    DAO dao;

    public  Controller(int pSize, int eTime, int eMoney,double stTime,double stMoney, String totalPOI ) //考虑Controller的生存周期
    {
        pointSize=pSize;
        exMoney=eMoney;
        exTime=eTime;
        poiId = new int [pointSize-2];
        poiName =new String [pointSize-2];
        poi_way_Mcost=new double [pointSize][pointSize];
        poi_way_Tcost=new double [pointSize][pointSize];
        dao =new DAO();
        tL=new double [pointSize];
        mL=new double [pointSize];
        //对矩阵初始化
        tL[0]=tL[1]=mL[0]=mL[1]=0;
        poi_way_Tcost[0][0]=poi_way_Tcost[1][1]=0;
        poi_way_Tcost[0][1]=poi_way_Tcost[1][0]=stTime;
        poi_way_Mcost[0][0]=poi_way_Mcost[1][1]=0;
        poi_way_Mcost[0][1]=poi_way_Mcost[1][0]=stMoney;
        String []spilitArrray =totalPOI.split(" "); //按这种可能出BUG，就是会出现有没跑出来的，这里要要求每个都有起点时间和终点时间
        for(int i=0;i<pointSize-2;i++)
        {
            poiId[i]=Integer.parseInt(spilitArrray[i*6]);
            System.out.println("poid"+poiId[i]);
            poiName[i]=spilitArrray[i*6+1];
            poi_way_Tcost[i+2][0]=poi_way_Tcost[0][i+2]=Integer.parseInt(spilitArrray[i*6+2]);
            poi_way_Tcost[i+2][1]=poi_way_Tcost[1][i+2]=Integer.parseInt(spilitArrray[i*6+4]);  //起点到各个POI的时间矩阵填充
            poi_way_Mcost[i+2][0]=poi_way_Mcost[0][i+2]=Integer.parseInt(spilitArrray[i*6+2]);
            poi_way_Mcost[i+2][1]=poi_way_Mcost[1][i+2]=Integer.parseInt(spilitArrray[i*6+4]);  //起点到各个POI的时间矩阵填充
            //数据库取,进行填充,需要填充tL mL tL 和 ML0 1位分别是起终点
            tL[i+2]=dao.queryPOITime(poiId[i]);
            System.out.println("first query");
            mL[i+2]=dao.queryPOIMoney(poiId[i]);
        }
        //对矩阵进行填充
        for(int k=2;k<pointSize;k++)
        {
            System.out.println("query"+k);
            for(int j=2;j<pointSize;j++)
            {
                if(k==j)
                {
                    poi_way_Tcost[k][j]=0;
                    poi_way_Mcost[k][j]=0;
                    System.out.print(poi_way_Tcost[k][j]+" ");
                }
                else {
                    poi_way_Tcost[k][j] = dao.queryRouteTime(poiId[k-2], poiId[j-2]);
                    System.out.print(poi_way_Tcost[k][j]+" ");
                    poi_way_Mcost[k][j] = dao.queryRouteMoney(poiId[k-2], poiId[j-2]);
                }
                System.out.println("");
            }
        }

        System.out.println("init complete");
    }

    public ArrayList<ArrayList> response() {

        String responseString;

        ArrayList<Integer> poiID = new ArrayList<Integer>();
        ArrayList<ArrayList> routeList = new ArrayList<ArrayList>();
        ArrayList<Integer> idResultList = new ArrayList<Integer>();
        ArrayList<Double> TimeList = new ArrayList<Double>();
        ArrayList<Double> MoneyList = new ArrayList<Double>();
        ArrayList<ArrayList> totalList=new ArrayList<>();
        double totaltime = 0;

        int totalcost = 0;

        Preparer preparer = new Preparer(poi_way_Tcost, poi_way_Mcost, tL, mL, exTime, exMoney, pointSize);
        World world = new World(preparer);
        world.initSetting();
        world.initPopulation();
        while (world.goNext()) world.Evolution();  //遗传算法进行筛选

        for (int i = 0; i < world.king.staff.size(); i++) //
        {
            System.out.println("路线" + i);
            for (int j = 0; j < world.king.staff.get(i).path.size(); j++) {
                poiID.add(world.king.staff.get(i).path.get(j));  //此时获得的ID是小矩阵的id（包括0，1）
                System.out.println(poiID + "  ");
            }
//                    for (int t=0;t< poiID.size()-1;t++)
//                    {
//                        totaltime = poiItemResult.get(poiID.get(t)).getTime_cost()+poi_way_Tcost[poiID.get(t)][poiID.get(t+1)];
//                        totalcost = (int) (poiItemResult.get(poiID.get(t)).getMoney_cost()+poi_way_Mcost[poiID.get(t)][poiID.get(t+1)]);
//                    }
//                    System.out.println("总时间花费:"+totaltime);
//                    System.out.println("总金钱花费:"+totalcost);
//        System.out.println("总时间花费:" + totaltime_sort);
//        System.out.println("总金钱花费:" + totalcost_sort);

            poiID.remove(0);
            poiID.remove(poiID.size()-1);

            ArrayList<Integer> tmpResultList=new ArrayList<Integer>();
            ArrayList<Integer> resultList=new ArrayList<Integer>();
            double[] resultTime=new double[1];
            resultTime[0]=999999;
            arrangementSelect(poiID,tmpResultList,0,resultList,resultTime);
            System.out.println("ssssssssssssssss");
//        resultList.add(1);
//        resultList.add(0,0);
            //找到resultList里面的所有的本来数据库的id
            double routeTime=poi_way_Tcost[0][resultList.get(0)];
            double routeMoney=poi_way_Mcost[0][resultList.get(0)];
            for(int temp=0;temp<resultList.size();temp++)
                {
                routeTime+=poi_way_Tcost[resultList.get(temp)][resultList.get(temp+1)];
                routeMoney+=poi_way_Mcost[resultList.get(temp)][resultList.get(temp+1)];
            }
            routeTime+=poi_way_Tcost[1][resultList.get(resultList.size()-1)];
            routeMoney+=poi_way_Mcost[1][resultList.get(resultList.size()-1)];
            for(int temp=0;temp<resultList.size();temp++)
            {
                idResultList.add(poiId[resultList.get(temp)-2]);
            }
            MoneyList.add(routeMoney);
            TimeList.add(routeTime);
            routeList.add(idResultList);
            poiID.clear();
            idResultList.clear();
        }
//结果写到了routeList 里面包含五条route,不包含起点和终点
        totalList.add(MoneyList);
        totalList.add(TimeList);
        totalList.add(routeList);
        return totalList;
    }

    private  void arrangementSelect(ArrayList<Integer> dataList, ArrayList<Integer>tmpResultList, int resultIndex ,ArrayList<Integer> resultList,double resultTime[]) {
        // 递归选择下一个,List里面存的是小矩阵的id的最初排列
        // System.out.println("最初的结构："+resultTime[0]);
        if(tmpResultList.size()==dataList.size())
        {
            double totaltime=0;
            for (int t=0;t< tmpResultList.size()-1;t++)
            {
                totaltime += tL[tmpResultList.get(t)]+poi_way_Tcost[tmpResultList.get(t)][tmpResultList.get(t+1)];
            }
            totaltime+=tL[tmpResultList.get(tmpResultList.size()-1)];
            totaltime+=poi_way_Tcost[0][tmpResultList.get(0)];
            totaltime+=poi_way_Tcost[tmpResultList.get(tmpResultList.size()-1)][1];
            if(resultTime[0]>totaltime)
            {
                resultTime[0]=totaltime;
                resultList.clear();
                for(int i=0;i<tmpResultList.size();i++)
                {
                    resultList.add(tmpResultList.get(i));
                }
                System.out.println(resultTime[0]);
                System.out.println(resultList+" ");
            }

        }
        else{
            for (int i = 0; i < dataList.size(); i++) {
                // 判断待选项是否存在于排列结果中
                boolean exists = false;
                for (int j = 0; j < resultIndex; j++) {
                    if (dataList.get(i).equals(tmpResultList.get(j))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) { // 排列结果不存在该项，才可选择
                    tmpResultList.add(dataList.get(i));
                    arrangementSelect(dataList, tmpResultList, resultIndex + 1,resultList,resultTime);
                    tmpResultList.remove(tmpResultList.size()-1);
                }
            }
        }
    }



}
