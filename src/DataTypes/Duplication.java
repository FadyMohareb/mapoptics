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

    // query contig has matches to the same region on the reference
    @Override
    public void setType() {
        this.type = "Duplication";
    }

}
