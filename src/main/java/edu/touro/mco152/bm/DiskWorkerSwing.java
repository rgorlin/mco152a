package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.util.List;

import static edu.touro.mco152.bm.App.dataDir;
import static edu.touro.mco152.bm.AppUtils.msg;

public class DiskWorkerSwing extends SwingWorker<Boolean, BenchMarker> implements Worker {
    /**
     * This class brings together DiskWriter and DiskReader, and combines it with a SwingWorker class to allow for a GUI
     * version of a Disk Tester.
     * @return true, Ignore used for swing background purposes
     * @throws Exception
     */
    @Override
    protected Boolean doInBackground() throws Exception {
        onCreate();
        Gui.updateLegend();

        if (App.autoReset == true) {
            AppUtils.resetTestData();
            Gui.resetTestData();
        }


        if (App.writeTest) {
            DiskWriter dw = new DiskWriter();
            write(dw);
            dw.initializeEMManager();
            dw.updateGui();
        }


        // try renaming all files to clear catch
        if (App.readTest && App.writeTest && !isCancelled()) {
            JOptionPane.showMessageDialog(Gui.mainFrame,
                    "For valid READ measurements please clear the disk cache by\n" +
                            "using the included RAMMap.exe or flushmem.exe utilities.\n" +
                            "Removable drives can be disconnected and reconnected.\n" +
                            "For system drives use the WRITE and READ operations \n" +
                            "independently by doing a cold reboot after the WRITE",
                    "Clear Disk Cache Now", JOptionPane.PLAIN_MESSAGE);
        }

        if (App.readTest) {
            DiskReader dr = new DiskReader();
            read(dr);
            dr.initializeEMManger();
            dr.updateGui();
        }
        App.nextMarkNumber += App.numOfMarks;
        return true;
    }


    protected void onCreate() {
        System.out.println("*** starting new worker thread");

        msg("Running readTest " + App.readTest + "   writeTest " + App.writeTest);
        msg("num files: " + App.numOfMarks + ", num blks: " + App.numOfBlocks
                + ", blk size (kb): " + App.blockSizeKb + ", blockSequence: " + App.blockSequence);
    }

    @Override
    protected void process(List<BenchMarker> markList) {
        markList.stream().forEach((m) -> {
            if (m.getMarkType() == BenchMarker.MarkType.WRITE) {
                Gui.addWriteMark(m);
            } else {
                Gui.addReadMark(m);
            }
        });
    }

    @Override
    protected void done() {
        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }

    @Override
    public void read(ReadableMemory dr) {
        for (int m = App.nextMarkNumber; m < App.nextMarkNumber + App.numOfMarks && !isCancelled(); m++) {
            dr.onCreate();
            dr.setUp(m);
            dr.read();
            setProgress(dr.getProgress());
            publish(dr.getPublishInfo(m));
            dr.setRunVars();
        }
    }

    @Override
    public void write(WriteableMemory dw) {
        for (int m = App.nextMarkNumber; m < App.nextMarkNumber + App.numOfMarks && !isCancelled(); m++) {
            dw.onCreate();
            dw.setUp(m);
            dw.write();
            setProgress(dw.getProgress());
            publish(dw.getPublishInfo(m));
            dw.setRunVars();
        }
    }
}
