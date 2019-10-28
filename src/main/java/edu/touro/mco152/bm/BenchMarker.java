package edu.touro.mco152.bm;

import java.text.DecimalFormat;

/**
 * An interface to be implemented with an Object that wants to have the background logic to bench mark storage
 * and will allow the object to be passed into the various GUI methods to allow for communication between the back end
 * logic and front end GUI.
 */
public interface BenchMarker {
    enum MarkType {READ, WRITE;}

    DecimalFormat df = new DecimalFormat("###.###");

    MarkType getMarkType();

    void setMarkType(MarkType type);

    int getMarkNum();

    void setMarkNum(int markNum);

    String getBwMbSecAsString();

    String getMinAsString();

    String getMaxAsString();

    String getAvgAsString();

    double getBwMbSec();

    void setBwMbSec(double bwMbSec);

    double getCumAvg();

    void setCumAvg(double cumAvg);

    double getCumMin();

    void setCumMin(double cumMin);

    double getCumMax();

    void setCumMax(double cumMax);


}
