package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.AppUtils.msg;
import static edu.touro.mco152.bm.BenchMarker.MarkType.READ;

/**
 * DiskReader is a child of DiskWorker. It handles all the necessary reading of a disk and is completely flexible
 * to be called from any platform. Works together with DiskWriter to be able to write and read a disk using any platform.
 * Extends DiskWorker to allow easy variable sharing with DiskWriter.
 */
public class DiskReader extends DiskWorker implements ReadableMemory {
    private DiskMark rMark;
    private DiskRun run = new DiskRun(DiskRun.IOMode.READ, App.blockSequence);
    private DiskUtils dhReader = new DiskUtils();
    private long totalBytesReadInMark = 0;

    public void onCreate() {
        dhReader.onCreate(run);
    }

    protected void initializeEMManger() {
        dhReader.initializeEMManager(run);
    }

    /**
     * only used if connected to a GUI
     */
    public void updateGui() {
        Gui.runPanel.addRun(run);
    }

    @Override
    public int getProgress() {
        return (int) percentComplete;
    }

    @Override
    public void setProgress(int progress) {
        percentComplete = progress;
    }

    @Override
    public void setUp(int m) {
        if (App.multiFile == true) {
            testFile = new File(dataDir.getAbsolutePath()
                    + File.separator + "testdata" + m + ".jdm");
        }
        rMark = new DiskMark();
        rMark.setMarkType(READ);
        rMark.setMarkNum(m);
        startTime = System.nanoTime();
    }

    @Override
    public void read() {
        try {
            try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                for (int b = 0; b < numOfBlocks; b++) {
                    if (App.blockSequence == DiskRun.BlockSequence.RANDOM) {
                        int rLoc = Util.randInt(0, numOfBlocks - 1);
                        rAccFile.seek(rLoc * blockSize);
                    } else {
                        rAccFile.seek(b * blockSize);
                    }
                    rAccFile.readFully(blockArr, 0, blockSize);
                    totalBytesReadInMark += blockSize;
                    rUnitsComplete++;
                    unitsComplete = rUnitsComplete + wUnitsComplete;
                    percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;
                    setProgress((int) percentComplete);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BenchMarker getPublishInfo(int m) {
        endTime = System.nanoTime();
        long elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
        rMark.setBwMbSec(mbRead / sec);
        msg("m:" + m + " READ IO is " + rMark.getBwMbSec() + " MB/s    "
                + "(MBread " + mbRead + " in " + sec + " sec)");
        AppUtils.updateMetrics(rMark);
        return rMark;

    }

    @Override
    public void setRunVars() {
        dhReader.setRunVars(run, rMark);
    }

    protected int getStartFileNum() {
        return startFileNum;
    }

}
