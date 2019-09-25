package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import org.junit.jupiter.api.Test;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Test to see if the set and get num blocks test works
 * uses cross check results, because it makes sure set and get work together
 */
public class DiskRunTest {
    DiskRun d = new DiskRun();
    @Test
    void setGetNumBlocksTest(){
        d.setNumBlocks(3);
        assertEquals(d.getNumBlocks(),3);
    }
   @Test
    void setGetBlockSizeTest(){
        d.setBlockSize(4);
        assertEquals(d.getBlockSize(),4);

   }

}
