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
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;

public class DiskReader extends DiskWorker {
    private DiskMark rMark;
    private DiskRun run = new DiskRun(DiskRun.IOMode.READ, App.blockSequence);
    private DiskHelper dhReader = new DiskHelper();
    long totalBytesReadInMark = 0;
    protected void onCreate() {
        dhReader.onCreate(run);
    }

    protected void initializeEMManger() {
        dhReader.initializeEMManager(run);
    }

    protected void updateGui() {
        Gui.runPanel.addRun(run);
    }
    protected int getProgress() {
        return (int) percentComplete;
    }

    protected void setProgress(int progress) {
        percentComplete = progress;
    }
    protected void setUp(int m){
        if (App.multiFile == true) {
            testFile = new File(dataDir.getAbsolutePath()
                    + File.separator + "testdata" + m + ".jdm");
        }
        rMark = new DiskMark(READ);
        rMark.setMarkNum(m);
        startTime = System.nanoTime();
    }
    protected void read(){
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
    protected DiskMark getPublishInfo (int m){
        endTime = System.nanoTime();
        long elapsedTimeNs = endTime - startTime;
        double sec = (double) elapsedTimeNs / (double) 1000000000;
        double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
        rMark.setBwMbSec(mbRead / sec);
        msg("m:" + m + " READ IO is " + rMark.getBwMbSec() + " MB/s    "
                + "(MBread " + mbRead + " in " + sec + " sec)");
        App.updateMetrics(rMark);
       return rMark;

    }
    protected void setRunVars(){
        dhReader.setRunVars(run, rMark);
    }
    protected int getStartFileNum() {
        return startFileNum;
    }
}
