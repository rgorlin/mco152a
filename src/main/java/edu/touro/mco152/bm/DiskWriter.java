package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

public class DiskWriter extends DiskWorker {

    private DiskMark wMark;
    private long totalBytesWrittenInMark = 0;
    private String mode = "rw";
    DiskHelper dhWriter = new DiskHelper();
    DiskRun run = new DiskRun(DiskRun.IOMode.WRITE, App.blockSequence);

    protected void onCreate() {
        dhWriter.onCreate(run);
        if (App.multiFile == false) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }
    }

    protected int getProgress() {
        return (int) percentComplete;
    }

    protected void setProgress(int progress) {
        percentComplete = progress;
    }

    protected int getStartFileNum() {
        return startFileNum;
    }

    protected void setUp(int m) {
        if (App.multiFile == true) {
            testFile = new File(dataDir.getAbsolutePath()
                    + File.separator + "testdata" + m + ".jdm");
        }
        wMark = new DiskMark(WRITE);
        wMark.setMarkNum(m);
        startTime = System.nanoTime();
        if (App.writeSyncEnable) {
            mode = "rwd";
        }
    }

    protected void write() {
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

    protected DiskMark getPublishInfo(int m) {
        endTime = System.nanoTime();
        elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
        wMark.setBwMbSec(mbWritten / sec);
        msg("m:" + m + " write IO is " + wMark.getBwMbSecAsString() + " MB/s     "
                + "(" + Util.displayString(mbWritten) + "MB written in "
                + Util.displayString(sec) + " sec)");
        App.updateMetrics(wMark);
        return wMark;
    }

    protected void setRunVars() {
        dhWriter.setRunVars(run, wMark);
    }

    protected void initializeEMManager() {
        dhWriter.initializeEMManager(run);
    }

    protected void updateGui() {
        Gui.runPanel.addRun(run);
    }
}
