package Datasets.Default;

//import Algorithms.SortOrientation;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.*;

/*
    @author Josie
    Class containing hashmaps of references and queries where rectangle2D objects
    are scaled relative to the query view panel and query contig
 */
public class QueryViewData {

    // scale variables
    private static double panelLength;
    private static double panelHeight;
    private static double hScale;
    private static double vScale;
    private static double hShift;
    private static List<String> refIDs;
    private static String[] refInfo;
    private String qryID;
    private static Map<String,String[]> refData;
    private static double RefLen;
    private static double RefStart;
    private static double QryLen;
    private static double QryStart;
    //get query and ref connection

    public static void setQueryData(MapOpticsModel model,String qryID){
        List<String> refIDs =(List<String>) XmapReader.getQueryData(model, qryID).keySet();
        for (String s : refIDs) {
            refIDs.add(s);
            refInfo=XmapReader.getQueryData(model, qryID).get(s);
            refData.put(s,refInfo);
        };
    }
    public static Map<String,String[]> getConnection(){
        return refData;
    }
    public static void resetData(){
        List<String> refIDs=new ArrayList<>();
        Map<String,String[]> refData= new HashMap<>();
    }

    public static void setSelectedRefLen(double reflen){
        RefLen =reflen;
    }
    public static double getRefLen(){
        return RefLen;
    }
    public static void setRefStart(double start){
        RefStart=start;
    }
    public static double getRefStart(){
        return RefStart;
    }
    public static void setSelectedQryLen(double qrylen){
        QryLen =qrylen;
    }
    public static double getQryLen(){
        return QryLen;
    }
    public static void setQryStart(double start){
        QryStart=start;
    }
    public static double getQryStart(){
        return QryStart;
    }
}