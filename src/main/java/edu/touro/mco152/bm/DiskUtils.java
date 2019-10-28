package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.BenchRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;

import javax.persistence.EntityManager;
import java.util.Date;

import static edu.touro.mco152.bm.App.dataDir;
import static edu.touro.mco152.bm.AppUtils.msg;

/**
 * DiskUtils is another child of DiskWorker that helps its siblings DiskWriter and DiskReader with performing the necessary
 * tasks for benchmarking. Its called in DiskWriter and DiskReader to clean up their code and initialize all there
 * variables and if needed there Gui.
 * in the future this class will be implemented to allow for more generic Util Benchmarking methods
 */
public class DiskUtils extends DiskWorker {

    protected void onCreate(BenchRun run) {
        initializeBlock();
        initializeVars(run);
        initializeGUI(run);
    }

    protected void setRunVars(BenchRun run, BenchMarker mark) {
        run.setRunMax(mark.getCumMax());
        run.setRunMin(mark.getCumMin());
        run.setRunAvg(mark.getCumAvg());
        run.setEndTime(new Date());
    }

    protected void initializeEMManager(BenchRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();
    }

    private void initializeBlock() {
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }
    }

    private void initializeVars(BenchRun run) {
        run.setNumMarks(App.numOfMarks);
        run.setNumBlocks(App.numOfBlocks);
        run.setBlockSize(App.blockSizeKb);
        run.setTxSize(AppUtils.targetTxSizeKb());
        run.setMemoryInfo(Util.getDiskInfo(dataDir));
    }

    /**
     * initializeGUI only should be called when using program with a GUI
     * @param run
     * DiskRun run is used for saving,getting, and setting info about the disk see DiskRun.java.
     */
    private void initializeGUI(BenchRun run) {
        msg("disk info: (" + run.getMemoryInfo() + ")");
        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getMemoryInfo());
    }


}
