
package edu.touro.mco152.bm;

import java.io.File;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import edu.touro.mco152.bm.ui.SelectFrame;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Primary class for global variables.
 */
public class App {
    public static final String APP_CACHE_DIR = System.getProperty("user.home") + File.separator + ".jDiskMark";
    public static final String PROPERTIESFILE = "jdm.properties";
    public static final String DATADIRNAME = "jDiskMarkData";
    public static final int MEGABYTE = 1024 * 1024;
    public static final int KILOBYTE = 1024;
    public static final int IDLE_STATE = 0;
    public static final int DISK_TEST_STATE = 1;

    public static enum State {IDLE_STATE, DISK_TEST_STATE};
    public static State state = State.IDLE_STATE;

    public static Properties p;
    public static File locationDir = null;
    public static File dataDir = null;
    public static File testFile = null;

    // options
    public static boolean multiFile = true;
    public static boolean autoRemoveData = false;
    public static boolean autoReset = true;
    public static boolean showMaxMin = true;
    public static boolean writeSyncEnable = true;

    // run configuration
    public static boolean readTest = false;
    public static boolean writeTest = true;
    public static DiskRun.BlockSequence blockSequence = DiskRun.BlockSequence.SEQUENTIAL;
    public static int numOfMarks = 25;      // desired number of marks
    public static int numOfBlocks = 32;     // desired number of blocks
    public static int blockSizeKb = 512;    // size of a block in KBs

    public static DiskWorkerSwing worker = null;
    public static int nextMarkNumber = 1;   // number of the next mark
    public static double wMax = -1, wMin = -1, wAvg = -1;
    public static double rMax = -1, rMin = -1, rAvg = -1;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
             * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
        }
        /* Create and display the form */
        //runs whole program
        java.awt.EventQueue.invokeLater(App::init);
    }

    
    /**
     * Initialize the GUI Application.
     */
    public static void init() {
        Gui.mainFrame = new MainFrame();
        Gui.selFrame = new SelectFrame();
        p = new Properties();
        ConfigUtils.loadConfig();
        System.out.println(ConfigUtils.getConfigString());
        Gui.mainFrame.refreshConfig();
        Gui.mainFrame.setLocationRelativeTo(null);
        Gui.progressBar = Gui.mainFrame.getProgressBar();
        
        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", APP_CACHE_DIR);
        AppUtils.loadSavedRuns();
        
        Gui.mainFrame.setVisible(true);
        
        // save configuration on exit...
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() { ConfigUtils.saveConfig(); }
        });
    }





}
