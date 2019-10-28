package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.SelectFrame;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * a Class that handles everything to do with configurations. interacts with App, and MainFrame.
 */
public class ConfigUtils {

    public static void loadConfig() {
        File pFile = new File(App.PROPERTIESFILE);
        if (!pFile.exists()) {
            return;
        }
        try {
            InputStream is = new FileInputStream(pFile);
            App.p.load(is);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        String value;
        value = App.p.getProperty("locationDir", System.getProperty("user.home"));
        App.locationDir = new File(value);
        value = App.p.getProperty("multiFile", String.valueOf(App.multiFile));
        App.multiFile = Boolean.valueOf(value);
        value = App.p.getProperty("autoRemoveData", String.valueOf(App.autoRemoveData));
        App.autoRemoveData = Boolean.valueOf(value);
        value = App.p.getProperty("autoReset", String.valueOf(App.autoReset));
        App.autoReset = Boolean.valueOf(value);
        value = App.p.getProperty("blockSequence", String.valueOf(App.blockSequence));
        App.blockSequence = DiskRun.BlockSequence.valueOf(value);
        value = App.p.getProperty("showMaxMin", String.valueOf(App.showMaxMin));
        App.showMaxMin = Boolean.valueOf(value);
        value = App.p.getProperty("numOfFiles", String.valueOf(App.numOfMarks));
        App.numOfMarks = Integer.valueOf(value);
        value = App.p.getProperty("numOfBlocks", String.valueOf(App.numOfBlocks));
        App.numOfBlocks = Integer.valueOf(value);
        value = App.p.getProperty("blockSizeKb", String.valueOf(App.blockSizeKb));
        App.blockSizeKb = Integer.valueOf(value);
        value = App.p.getProperty("writeTest", String.valueOf(App.writeTest));
        App.writeTest = Boolean.valueOf(value);
        value = App.p.getProperty("readTest", String.valueOf(App.readTest));
        App.readTest = Boolean.valueOf(value);
        value = App.p.getProperty("writeSyncEnable", String.valueOf(App.writeSyncEnable));
        App.writeSyncEnable = Boolean.valueOf(value);
    }

    public static void saveConfig() {
        App.p.setProperty("locationDir", App.locationDir.getAbsolutePath());
        App.p.setProperty("multiFile", String.valueOf(App.multiFile));
        App.p.setProperty("autoRemoveData", String.valueOf(App.autoRemoveData));
        App.p.setProperty("autoReset", String.valueOf(App.autoReset));
        App.p.setProperty("blockSequence", String.valueOf(App.blockSequence));
        App.p.setProperty("showMaxMin", String.valueOf(App.showMaxMin));
        App.p.setProperty("numOfFiles", String.valueOf(App.numOfMarks));
        App.p.setProperty("numOfBlocks", String.valueOf(App.numOfBlocks));
        App.p.setProperty("blockSizeKb", String.valueOf(App.blockSizeKb));
        App.p.setProperty("writeTest", String.valueOf(App.writeTest));
        App.p.setProperty("readTest", String.valueOf(App.readTest));
        App.p.setProperty("writeSyncEnable", String.valueOf(App.writeSyncEnable));

        try {
            OutputStream out = new FileOutputStream(new File(App.PROPERTIESFILE));
            App.p.store(out, "jDiskMark Properties File");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SelectFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SelectFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getConfigString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Config for Java Disk Mark ").append(AppUtils.getVersion()).append('\n');
        sb.append("readTest: ").append(App.readTest).append('\n');
        sb.append("writeTest: ").append(App.writeTest).append('\n');
        sb.append("locationDir: ").append(App.locationDir).append('\n');
        sb.append("multiFile: ").append(App.multiFile).append('\n');
        sb.append("autoRemoveData: ").append(App.autoRemoveData).append('\n');
        sb.append("autoReset: ").append(App.autoReset).append('\n');
        sb.append("blockSequence: ").append(App.blockSequence).append('\n');
        sb.append("showMaxMin: ").append(App.showMaxMin).append('\n');
        sb.append("numOfFiles: ").append(App.numOfMarks).append('\n');
        sb.append("numOfBlocks: ").append(App.numOfBlocks).append('\n');
        sb.append("blockSizeKb: ").append(App.blockSizeKb).append('\n');
        return sb.toString();
    }
}
