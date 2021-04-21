package DataTypes;

import Algorithms.DetectSV;

/*
* @author Anisha
*
* Class for creating duplication objects and associated detection methods.
*
* */
public class Duplication extends SV {

    Duplication() {
        super();
    }

    @Override
    public String setType() {
        return "Duplication";
    }

    @Override
    void setSVRegion(SV sv) {

    }
}
