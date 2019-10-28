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
import static edu.touro.mco152.bm.BenchMarker.MarkType.WRITE;

/**
 * DiskWriter is a child of DiskWorker. It handles all the necessary writing of a disk and is completely flexible
 * to be called from any platform. Works together with DiskReader to be able to write and read a disk using any platform.
 * Extends DiskWorker to allow easy variable sharing with DiskReader.
 */
public class DiskWriter extends DiskWorker implements WriteableMemory {

    private DiskMark wMark;
    private long totalBytesWrittenInMark = 0;
    private String mode = "rw";
    DiskUtils dhWriter = new DiskUtils();
    DiskRun run = new DiskRun(DiskRun.IOMode.WRITE, App.blockSequence);

    @Override
    public void onCreate() {
        dhWriter.onCreate(run);
        if (App.multiFile == false) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }
    }

    @Override
    public int getProgress() {
        return (int) percentComplete;
    }

    @Override
    public void setProgress(int progress) {
        percentComplete = progress;
    }


    protected int getStartFileNum() {
        return startFileNum;
    }

    @Override
    public void setUp(int m) {
        if (App.multiFile == true) {
            testFile = new File(dataDir.getAbsolutePath()
                    + File.separator + "testdata" + m + ".jdm");
        }
        wMark = new DiskMark();
        wMark.setMarkType(WRITE);
        wMark.setMarkNum(m);
        startTime = System.nanoTime();
        if (App.writeSyncEnable) {
            mode = "rwd";
        }
    }

    @Override
    public void write() {
        try {
            try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                for (int b = 0; b < numOfBlocks; b++) {
                    if (App.blockSequence == DiskRun.BlockSequence.RANDOM) {
                        int rLoc = Util.randInt(0, numOfBlocks - 1);
                        rAccFile.seek(rLoc * blockSize);
                    } else {
                        rAccFile.seek(b * blockSize);
                    }
                    rAccFile.write(blockArr, 0, blockSize);
                    totalBytesWrittenInMark += blockSize;
                    wUnitsComplete++;
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

    @Override
    public BenchMarker getPublishInfo(int m) {
        endTime = System.nanoTime();
        elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
        wMark.setBwMbSec(mbWritten / sec);
        AppUtils.msg("m:" + m + " write IO is " + wMark.getBwMbSecAsString() + " MB/s     "
                + "(" + Util.displayString(mbWritten) + "MB written in "
                + Util.displayString(sec) + " sec)");
        AppUtils.updateMetrics(wMark);
        return wMark;
    }

    @Override
    public void setRunVars() {
        dhWriter.setRunVars(run, wMark);
    }

    protected void initializeEMManager() {
        dhWriter.initializeEMManager(run);
    }

    protected void updateGui() {
        Gui.runPanel.addRun(run);
    }

}
