
package edu.touro.mco152.bm.persist;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table(name = "DiskRun")
@NamedQueries({
        @NamedQuery(name = "DiskRun.findAll",
                query = "SELECT d FROM DiskRun d")
})
public class DiskRun implements Serializable, BenchRun {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    // configuration
    @Column
    String diskInfo = null;
    @Column
    private
    IOMode ioMode;
    @Column
    private
    BlockSequence blockOrder;
    @Column
    private
    int numMarks = 0;
    @Column
    private
    int numBlocks = 0;
    @Column
    private
    int blockSize = 0;
    @Column
    private
    long txSize = 0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private
    Date endTime = null;
    @Column
    int totalMarks = 0;
    @Column
    private
    double runMin = 0;
    @Column
    private
    double runMax = 0;
    @Column
    private
    double runAvg = 0;

    @Override
    public String toString() {
        return "Run(" + getIoMode() + "," + getBlockOrder() + "): " + totalMarks + " run avg: " + getRunAvg();
    }

    public DiskRun() {
        this.startTime = new Date();
    }

    public DiskRun(IOMode type, BlockSequence order) {
        this.startTime = new Date();
        setIoMode(type);
        setBlockOrder(order);
    }

    // display friendly methods
    @Override
    public String getStartTimeString() {
        return DATE_FORMAT.format(startTime);
    }

    @Override
    public String getMin() {
        return getRunMin() == -1 ? "- -" : DF.format(getRunMin());
    }

    @Override
    public void setMin(double min) {
        setRunMin(min);
    }

    @Override
    public String getMax() {
        return getRunMax() == -1 ? "- -" : DF.format(getRunMax());
    }

    @Override
    public void setMax(double max) {
        setRunMax(max);
    }

    @Override
    public String getAvg() {
        return getRunAvg() == -1 ? "- -" : DF.format(getRunAvg());
    }

    @Override
    public void setAvg(double avg) {
        setRunAvg(avg);
    }

    @Override
    public String getDuration() {
        if (getEndTime() == null) {
            return "unknown";
        }
        long duration = getEndTime().getTime() - startTime.getTime();
        long diffSeconds = duration / 1000 % 60;
        return String.valueOf(diffSeconds) + "s";
    }

    @Override
    public String getMemoryInfo() {
        return getDiskInfo();
    }

    @Override
    public void setMemoryInfo(String info) {
        setDiskInfo(info);
    }

    // basic getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiskInfo() {
        return diskInfo;
    }

    public void setDiskInfo(String info) {
        diskInfo = info;
    }


    @Override
    public IOMode getIoMode() {
        return ioMode;
    }

    @Override
    public void setIoMode(IOMode ioMode) {
        this.ioMode = ioMode;
    }

    @Override
    public BlockSequence getBlockOrder() {
        return blockOrder;
    }


    @Override
    public void setBlockOrder(BlockSequence blockOrder) {
        this.blockOrder = blockOrder;
    }

    @Override
    public int getNumMarks() {
        return numMarks;
    }

    @Override
    public void setNumMarks(int numMarks) {
        this.numMarks = numMarks;
    }

    @Override
    public int getNumBlocks() {
        return numBlocks;
    }

    @Override
    public void setNumBlocks(int numBlocks) {
        this.numBlocks = numBlocks;
    }

    @Override
    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public long getTxSize() {
        return txSize;
    }

    @Override
    public void setTxSize(long txSize) {
        this.txSize = txSize;
    }

    @Override
    public double getRunMax() {
        return runMax;
    }

    @Override
    public void setRunMax(double runMax) {
        this.runMax = runMax;
    }

    @Override
    public double getRunMin() {
        return runMin;
    }

    @Override
    public void setRunMin(double runMin) {
        this.runMin = runMin;
    }

    @Override
    public double getRunAvg() {
        return runAvg;
    }

    @Override
    public void setRunAvg(double runAvg) {
        this.runAvg = runAvg;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
