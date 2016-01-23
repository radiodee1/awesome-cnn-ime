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

    public DataSetSplit( int type ) {
        switch (type) {
            case Operation.EVAL_TRAIN_NUMERIC:
            case Operation.EVAL_TRAIN_NUMERIC_SHOW:
                splitDataMnist();
                break;
            case Operation.EVAL_SINGLE_ALPHA_UPPER:
                splitDataAlphaUpper();
                break;
        }
        //splitDataMnist();
    }

    public void splitDataMnist () {
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

    public void splitDataAlphaUpper() {
        try {


            AlphaDataSet alphaTrain = new AlphaDataSet(Operation.EVAL_SINGLE_ALPHA_UPPER, true, 0.20f, 9999);
            alphaTrain.limitList(100);
            System.out.println(alphaTrain.length());
            setTrain = alphaTrain;
            AlphaDataSet alphaTest = new AlphaDataSet(Operation.EVAL_SINGLE_ALPHA_UPPER, false,0.20f, 9999);
            alphaTest.limitList(20);
            System.out.println(alphaTest.length());
            setTest = alphaTest;
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
