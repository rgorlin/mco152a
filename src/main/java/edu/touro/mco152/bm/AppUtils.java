package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.BenchRun;
import edu.touro.mco152.bm.persist.BenchRunUtils;
import edu.touro.mco152.bm.ui.Gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppUtils {

    /**
     * A utilities class that goes along with App. Has methods that helps App communicate with the GUI, and for the Gui
     * to send info to App.
     */
    public static String getVersion() {
        Properties bp = new Properties();
        String version = "0.0";
        try {
            bp.load(new FileInputStream("build.properties"));
            version = bp.getProperty("version");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }

    public static void loadSavedRuns() {
        Gui.runPanel.clearTable();

        // populate run table with saved runs from db
        System.out.println("loading stored run data");
        BenchRunUtils.findAll().stream().forEach((BenchRun run) -> {
            Gui.runPanel.addRun(run);
        });
    }

    public static void clearSavedRuns() {
        BenchRunUtils.deleteAll();

        loadSavedRuns();
    }

    public static void msg(String message) {
        Gui.mainFrame.msg(message);
    }

    public static long targetMarkSizeKb() {
        return App.blockSizeKb * App.numOfBlocks;
    }

    public static long targetTxSizeKb() {
        return App.blockSizeKb * App.numOfBlocks * App.numOfMarks;
    }

    public static void updateMetrics(BenchMarker mark) {
        if (mark.getMarkType() == BenchMarker.MarkType.WRITE) {
            if (App.wMax == -1 || App.wMax < mark.getBwMbSec()) {
                App.wMax = mark.getBwMbSec();
            }
            if (App.wMin == -1 || App.wMin > mark.getBwMbSec()) {
                App.wMin = mark.getBwMbSec();
            }
            if (App.wAvg == -1) {
                App.wAvg = mark.getBwMbSec();
            } else {
                int n = mark.getMarkNum();
                App.wAvg = (((double) (n - 1) * App.wAvg) + mark.getBwMbSec()) / (double) n;
            }
            mark.setCumAvg(App.wAvg);
            mark.setCumMax(App.wMax);
            mark.setCumMin(App.wMin);
        } else {
            if (App.rMax == -1 || App.rMax < mark.getBwMbSec()) {
                App.rMax = mark.getBwMbSec();
            }
            if (App.rMin == -1 || App.rMin > mark.getBwMbSec()) {
                App.rMin = mark.getBwMbSec();
            }
            if (App.rAvg == -1) {
                App.rAvg = mark.getBwMbSec();
            } else {
                int n = mark.getMarkNum();
                App.rAvg = (((double) (n - 1) * App.rAvg) + mark.getBwMbSec()) / (double) n;
            }
            mark.setCumAvg(App.rAvg);
            mark.setCumMax(App.rMax);
            mark.setCumMin(App.rMin);
        }
    }

    static public void resetSequence() {
        App.nextMarkNumber = 1;
    }

    static public void resetTestData() {
        App.nextMarkNumber = 1;
        App.wAvg = -1;
        App.wMax = -1;
        App.wMin = -1;
        App.rAvg = -1;
        App.rMax = -1;
        App.rMin = -1;
    }
}
