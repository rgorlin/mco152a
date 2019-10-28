package edu.touro.mco152.bm;

/**
 * A interface that allows for more generic testing anything that implements this interface will be able to be passed into
 * methods that require a ReadableMemory,, and should be the class that will be reading the desired test storage speeds.
 */
public interface ReadableMemory {
    void read();

    void onCreate();

    void setUp(int loopVar);

    void setProgress(int progress);

    int getProgress();

    void setRunVars();

    BenchMarker getPublishInfo(int loopVar);

}
