package edu.touro.mco152.bm.persist;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An interface that when implemented allows the object to be passed into all methods that accept BenchRun, to allow
 * testing for reading and writing of any kind
 *
 */
public interface BenchRun {
    long serialVersionUID = 1L;
    DecimalFormat DF = new DecimalFormat("###.##");
    DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMM d HH:mm:ss");

    enum IOMode {READ, WRITE, READ_WRITE;}

    enum BlockSequence {SEQUENTIAL, RANDOM;}

    String getDuration();

    String getMemoryInfo();

    void setMemoryInfo(String info);

    IOMode getIoMode();

    void setIoMode(IOMode ioMode);

    BlockSequence getBlockOrder();

    void setBlockOrder(BlockSequence blockOrder);

    int getNumMarks();

    void setNumMarks(int numMarks);

    int getNumBlocks();

    void setNumBlocks(int numBlocks);

    int getBlockSize();

    void setBlockSize(int blockSize);

    long getTxSize();

    void setTxSize(long txSize);

    double getRunMax();

    void setRunMax(double runMax);

    double getRunMin();

    void setRunMin(double runMin);

    double getRunAvg();

    void setRunAvg(double runAvg);

    Date getEndTime();

    void setEndTime(Date endTime);

    String getStartTimeString();

    String getMin();

    void setMin(double min);

    String getMax();

    void setMax(double max);

    String getAvg();

    void setAvg(double avg);

}
