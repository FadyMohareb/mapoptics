package Datasets.Default;

//import Algorithms.SortOrientation;
import DataTypes.*;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.awt.geom.Rectangle2D;
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
}