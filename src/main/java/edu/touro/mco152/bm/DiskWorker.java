
package edu.touro.mco152.bm;

import static edu.touro.mco152.bm.App.KILOBYTE;
import static edu.touro.mco152.bm.App.blockSizeKb;
import static edu.touro.mco152.bm.AppUtils.msg;
import static edu.touro.mco152.bm.App.numOfBlocks;
import static edu.touro.mco152.bm.App.numOfMarks;

/**
 * Right now just a global variables  class that's used by all that extend DiskWorker
 */
public class DiskWorker {

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


}
