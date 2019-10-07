
package edu.touro.mco152.bm;

import static edu.touro.mco152.bm.App.KILOBYTE;
import static edu.touro.mco152.bm.App.MEGABYTE;
import static edu.touro.mco152.bm.App.blockSizeKb;
import static edu.touro.mco152.bm.App.dataDir;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.App.numOfBlocks;
import static edu.touro.mco152.bm.App.numOfMarks;
import static edu.touro.mco152.bm.App.testFile;
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;

/**
 * Thread running the disk benchmarking. only one of these threads can run at
 * once.
 */
public class DiskWorker <Boolean, DiskMark> {

    protected int wUnitsComplete = 0,
            rUnitsComplete = 0,
            unitsComplete;
    protected int wUnitsTotal = App.writeTest ? numOfBlocks * numOfMarks : 0;
    protected int rUnitsTotal = App.readTest ? numOfBlocks * numOfMarks : 0;
    protected int unitsTotal = wUnitsTotal + rUnitsTotal;
    protected float percentComplete;
    protected int startFileNum = App.nextMarkNumber;
    protected int blockSize = blockSizeKb * KILOBYTE;
    protected byte[] blockArr = new byte[blockSize];
    protected long startTime, endTime, elapsedTimeNs;


    protected void onCreate() {
        System.out.println("*** starting new worker thread");

        msg("Running readTest " + App.readTest + "   writeTest " + App.writeTest);
        msg("num files: " + App.numOfMarks + ", num blks: " + App.numOfBlocks
                + ", blk size (kb): " + App.blockSizeKb + ", blockSequence: " + App.blockSequence);
    }

       protected void runTests(){

        if (App.writeTest) {
            DiskWriter dw = new DiskWriter();
            dw.onCreate();
        }

        if (App.readTest) {
            DiskReader dr = new DiskReader();
            dr.onCreate();
        }
        App.nextMarkNumber += App.numOfMarks;
    }
}
