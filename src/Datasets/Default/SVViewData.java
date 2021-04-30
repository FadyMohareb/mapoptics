package Datasets.Default;

import Algorithms.DetectSV;
import DataTypes.Indel;
import DataTypes.Reference;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.ArrayList;
import java.util.List;

/*
* @author Anisha
*
* Sets up SV View data
*
* */


public class SVViewData {

    public static List<Indel> indels = new ArrayList<>();

    public static void resetData() {
        SVViewData.indels.clear();
    }

    public static void setSVViewData(MapOpticsModel model, DetectSV detectSV) {
        Reference ref = model.getSelectedRef();
        detectSV.setChosenRef(ref);
        detectSV.setSVList();

    }

}
