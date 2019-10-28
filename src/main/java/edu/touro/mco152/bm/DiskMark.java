
package edu.touro.mco152.bm;

/**
 *
 */
public class DiskMark implements BenchMarker {

    MarkType type;
    private int markNum = 0;       // x-axis
    private double bwMbSec = 0;    // y-axis
    private double cumMin = 0;
    private double cumMax = 0;
    private double cumAvg = 0;

    @Override
    public String toString() {
        return "Mark(" + type + "): " + getMarkNum() + " bwMbSec: " + getBwMbSecAsString() + " avg: " + getAvgAsString();
    }

    @Override
    public String getBwMbSecAsString() {
        return df.format(getBwMbSec());
    }

    @Override
    public String getMinAsString() {
        return df.format(getCumMin());
    }


    @Override
    public String getMaxAsString() {
        return df.format(getCumMax());
    }

    @Override
    public String getAvgAsString() {
        return df.format(getCumAvg());
    }

	@Override
	public MarkType getMarkType() {
		return type;
	}

	@Override
	public void setMarkType(MarkType type) {
		this.type=type;
	}

	@Override
    public int getMarkNum() {
        return markNum;
    }

    @Override
    public void setMarkNum(int markNum) {
        this.markNum = markNum;
    }


    @Override
    public double getBwMbSec() {
        return bwMbSec;
    }

    @Override
    public void setBwMbSec(double bwMbSec) {
        this.bwMbSec = bwMbSec;
    }

    @Override
    public double getCumAvg() {
        return cumAvg;
    }

    @Override
    public void setCumAvg(double cumAvg) {
        this.cumAvg = cumAvg;
    }

    @Override
    public double getCumMin() {
        return cumMin;
    }

    @Override
    public void setCumMin(double cumMin) {
        this.cumMin = cumMin;
    }

    @Override
    public double getCumMax() {
        return cumMax;
    }

    @Override
    public void setCumMax(double cumMax) {
        this.cumMax = cumMax;
    }
}
