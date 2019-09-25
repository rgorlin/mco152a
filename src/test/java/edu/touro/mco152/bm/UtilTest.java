package edu.touro.mco152.bm;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Testing using Boundary conditions and error  conditions
 * Set Low=0--passed
 * set high= Max--passed
 * set low=high--passed
 * set low= negative--passed
 * set high<low--fail
 */

 public class UtilTest {


    /**
     * Checking Boundary condition.
     *when low=high
     * when low is negative
     * when high is max
     * normal case
     * @param high
     */
    @ParameterizedTest(name = "Random result should be between {0} and {1}")
    @CsvSource({
            "0, 0",
            "-19 ,15",
            "0, 2_147_483_647 ",
            "1, 50"
    })
    void randInt(int low, int high) {
        double result = Util.randInt(low, high);

        assertTrue(high >= result);
        assertTrue(low <= result);
    }


    /**
     * I created a  file path C:\Users\reube\delete, will then check if it gets deleted
     * Uses performance characteristics as this is real life file that i am deleting.
     * Update- method works file deleted
     * forced error condition by using bad file path
     */
    @Test
    void deleteDirectoryTest(){
        File path = new File("C:\\Users\\reube\\delete");
        Util.deleteDirectory(path);
        //making this fail on purpose by switching it to assertTrue
       assertTrue(path.exists());
    }

}
