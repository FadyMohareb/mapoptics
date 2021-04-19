package Datasets.Default;

import DataTypes.Duplication;
import DataTypes.Indel;
import DataTypes.Inversion;
import DataTypes.Translocation;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* @author Anisha
*
* Sets up SV View data
*
* */


public class SVViewData {
    private static List<String> refIDs;
    private static String[] refInfo;
    private String qryID;
    private static Map<String,String[]> refData;
    private static double RefLen;
    private static double RefStart;
    private static double QryLen;
    private static double QryStart;
    public static List<Indel> indels = new ArrayList<>();
    public static List<Inversion> inversions = new ArrayList<>();
    public static List<Duplication> duplications = new ArrayList<>();
    public static List<Translocation> translocations = new ArrayList<>();

    public static void resetData() {
        SVViewData.indels.clear();
        SVViewData.inversions.clear();
        SVViewData.duplications.clear();
        SVViewData.translocations.clear();
        List<String> refIDs=new ArrayList<>();
        Map<String,String[]> refData= new HashMap<>();

    }

    public static void setSVViewData(MapOpticsModel model, String qryID) {
        if(XmapReader.getQueryData(model, qryID) != null){
            List<String> refIDs= new ArrayList<>();
            refData= new HashMap<>();
            for (String s : XmapReader.getQueryData(model, qryID).keySet()) {
                refIDs.add(s);
                refInfo=XmapReader.getQueryData(model, qryID).get(s);
                refData.put(s,refInfo);
            }
        }else {refData=null;}

    }
    public static Map<String,String[]> getConnection(){
        return refData;
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
