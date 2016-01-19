package org.davidliebman.android;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dave on 1/19/16.
 */
public class DataSetSplit {
    private static final Logger log = LoggerFactory.getLogger(DataSetSplit.class);

    private int batchSize = 64;
    DataSetIterator setTrain, setTest;

    public DataSetSplit() {
        splitData();
    }

    public void splitData () {
        log.info("Load data....");
        try {

            DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, 12345);
            DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, 12345);

            setTest = mnistTest;
            setTrain = mnistTrain;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public DataSetIterator getSetTrain() {return setTrain;}
    public DataSetIterator getSetTest() {return setTest;}
    public void setBatchSize(int b) {batchSize = b;}
    public int getBatchSize() {return batchSize;}
}
