package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;

import javax.persistence.EntityManager;
import java.util.Date;

import static edu.touro.mco152.bm.App.dataDir;
import static edu.touro.mco152.bm.App.msg;

public class DiskHelper extends DiskWorker {

    protected void onCreate(DiskRun run) {
        initializeBlock();
        initializeVars(run);
        initializeGUI(run);
    }

    protected void setRunVars(DiskRun run, DiskMark mark) {
        run.setRunMax(mark.getCumMax());
        run.setRunMin(mark.getCumMin());
        run.setRunAvg(mark.getCumAvg());
        run.setEndTime(new Date());
    }

    protected void initializeEMManager(DiskRun run) {
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

    private void initializeVars(DiskRun run) {
        run.setNumMarks(App.numOfMarks);
        run.setNumBlocks(App.numOfBlocks);
        run.setBlockSize(App.blockSizeKb);
        run.setTxSize(App.targetTxSizeKb());
        run.setDiskInfo(Util.getDiskInfo(dataDir));
    }

    private void initializeGUI(DiskRun run) {
        msg("disk info: (" + run.getDiskInfo() + ")");
        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());
    }


}
