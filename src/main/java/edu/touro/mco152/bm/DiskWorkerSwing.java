package edu.touro.mco152.bm;

import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.util.List;

import static edu.touro.mco152.bm.App.dataDir;
import static edu.touro.mco152.bm.App.msg;

public class DiskWorkerSwing extends SwingWorker<Boolean, DiskMark> {

    @Override
    protected Boolean doInBackground() throws Exception {
        DiskWorker dWorker = new DiskWorker();
        dWorker.onCreate();
        Gui.updateLegend();

        if (App.autoReset == true) {
            App.resetTestData();
            Gui.resetTestData();
        }


        if (App.writeTest) {
            DiskWriter dw = new DiskWriter();
            for (int m = dw.getStartFileNum(); m < dw.getStartFileNum() + App.numOfMarks && !isCancelled(); m++) {
                dw.onCreate();
                dw.setUp(m);
                dw.write();
                setProgress(dw.getProgress());
                publish(dw.getPublishInfo(m));
                dw.setRunVars();
            }
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
            for (int m = dr.getStartFileNum(); m < dr.getStartFileNum() + App.numOfMarks && !isCancelled(); m++) {
                dr.onCreate();
                dr.setUp(m);
                dr.read();
                setProgress(dr.getProgress());
                publish(dr.getPublishInfo(m));
                dr.setRunVars();
            }
            dr.initializeEMManger();
            dr.updateGui();
        }
        App.nextMarkNumber += App.numOfMarks;
        return true;
    }

    @Override
    protected void process(List<DiskMark> markList) {
        markList.stream().forEach((m) -> {
            if (m.type == DiskMark.MarkType.WRITE) {
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
}
