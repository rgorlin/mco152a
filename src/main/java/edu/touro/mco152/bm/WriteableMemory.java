package edu.touro.mco152.bm;

/**
 * A interface that allows for more generic testing anything that implements this interface will be able to be passed into
 *  methods that require a WriteableMemory, and should be the class that will be writing to the desired test storage
 */
public interface WriteableMemory {
    void write();

    void onCreate();

    int getProgress();

    void setProgress(int progress);

    void setUp(int loopVar);

    BenchMarker getPublishInfo(int loopVar);

    void setRunVars();


}
