package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.File;

/**
 * A class that handles the Actual benchmarking of DiskWorker, will be modified in the future to allow Benchmarking
 * on any "worker" implementations and will be renamed to MasterBenchMark
 */
public class DiskBenchMark {

    public static void startBenchmark() {

        //1. check that there isn't already a worker in progress
        if (App.state == App.State.DISK_TEST_STATE) {
            //if (!worker.isCancelled() && !worker.isDone()) {
            AppUtils.msg("Test in progress, aborting...");
            return;
            //}
        }

        //2. check can write to location
        if (App.locationDir.canWrite() == false) {
            AppUtils.msg("Selected directory can not be written to... aborting");
            return;
        }

        //3. update state
        App.state = App.State.DISK_TEST_STATE;
        Gui.mainFrame.adjustSensitivity();

        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath()+File.separator+App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.autoRemoveData && App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                AppUtils.msg("removed existing data dir");
            } else {
                AppUtils.msg("unable to remove existing data dir");
            }
        }

        //6. create data dir if not already present
        if (App.dataDir.exists() == false) { App.dataDir.mkdirs(); }

        //7. start disk worker thread
        App. worker = new DiskWorkerSwing();
        App.worker.addPropertyChangeListener((final PropertyChangeEvent event) -> {
            switch (event.getPropertyName()) {
                case "progress":
                    int value = (Integer)event.getNewValue();
                    Gui.progressBar.setValue(value);
                    long kbProcessed = (value) *  AppUtils.targetTxSizeKb() / 100;
                    Gui.progressBar.setString(String.valueOf(kbProcessed)+" / "+String.valueOf( AppUtils.targetTxSizeKb()));
                    break;
                case "state":
                    switch ((SwingWorker.StateValue) event.getNewValue()) {
                        case STARTED:
                            Gui.progressBar.setString("0 / "+String.valueOf( AppUtils.targetTxSizeKb()));
                            break;
                        case DONE:
                            break;
                    } // end inner switch
                    break;
            }
        });
        App.worker.execute();
    }

    public static void cancelBenchmark() {
        if (App.worker == null) {
            AppUtils.msg("worker is null abort...");
            return;
        }
        App.worker.cancel(true);
    }
    public void createWorker(Worker worker){
        // to be used with future programs, as DiskWorker implements Worker, if another "Worker"Class is created
        //it would be able to be called here as well and lines 47-64 would be changed
    }
}
