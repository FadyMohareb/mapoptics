package Datasets.Default;

import Algorithms.DetectSV;
import DataTypes.*;
import FileHandling.CmapReader;
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

    public static List<Indel> indels = new ArrayList<>();
    public static List<Inversion> inversions = new ArrayList<>();
    public static List<Duplication> duplications = new ArrayList<>();
    public static List<Translocation> translocations = new ArrayList<>();

    public static void resetData() {
        SVViewData.indels.clear();
        SVViewData.inversions.clear();
        SVViewData.duplications.clear();
        SVViewData.translocations.clear();


    }

    public static void setSVViewData(MapOpticsModel model, DetectSV detectSV) {
        Reference ref = model.getSelectedRef();
        DetectSV.setChosenRef(ref);
        detectSV.setSVList();

    }

}
