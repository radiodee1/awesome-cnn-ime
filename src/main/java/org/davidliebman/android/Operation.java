package org.davidliebman.android;

import org.deeplearning4j.datasets.vectorizer.ImageVectorizer;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by dave on 1/19/16.
 */
public class Operation {
    Network network;
    MultiLayerNetwork model;
    DataSetSplit data;
    DataSetIterator train, test;
    int batchSize;
    int epochs;
    int iterations;

    int evalType = 0;

    public static final int EVAL_SINGLE_NUMERIC = 1;
    public static final int EVAL_SINGLE_ALPHA_UPPER = 2;
    public static final int EVAL_SINGLE_ALPHA_LOWER = 3;
    public static final int EVAL_TRAIN_NUMERIC = 4;
    public static final int EVAL_TRAIN_ALPHA_UPPER = 5;
    public static final int EVAL_TRAIN_ALPHA_LOWER = 6;
    public static final int EVAL_TRAIN_NUMERIC_SHOW = 7;


    private static final Logger log = LoggerFactory.getLogger(Operation.class);


    public Operation(Network model, DataSetSplit data, int batchSize, int epochs, int iterations) throws Exception{
        this.network = model;
        this.model = model.getModel();
        this.data = data;
        this.batchSize = batchSize;
        this.epochs = epochs;
        this.iterations = iterations;

    }

    public Operation(Network model) throws Exception {
        this.network = model;
        this.model = model.getModel();


    }

    public void setEvalType(int type) {evalType = type;}

    public void startOperation() throws Exception {

        switch (evalType) {
            case EVAL_SINGLE_ALPHA_UPPER:
                startOperationAlphaShow();
                break;
            case EVAL_SINGLE_ALPHA_LOWER:
                break;
            case EVAL_SINGLE_NUMERIC:
                break;
            case EVAL_TRAIN_ALPHA_UPPER:
                break;
            case EVAL_TRAIN_ALPHA_LOWER:
                break;
            case EVAL_TRAIN_NUMERIC:
                startOperationMnistTrain();
                break;
            case EVAL_TRAIN_NUMERIC_SHOW:
                startOperationMnistShow();
                break;
        }

    }

    public void startOperationMnistShow() throws Exception {
        boolean saveValues = false;
        boolean loadValues = true;
        boolean trainValues = false;
        boolean evalValues = true;

        float evalsTotal = 0, evalsCorrect = 0;

        int nextNum = 0;

        log.info("Load data....");
        //DataSetSplit mnist = new DataSetSplit();

        train = data.getSetTrain();
        test = data.getSetTest();

        FileManager files = new FileManager("lenet_example_digits");

        OneHotOutput oneHot = new OneHotOutput(OneHotOutput.TYPE_NUMERALS);

        System.out.println(oneHot.toString());

        if (loadValues) {
            files.loadModel(model);
        }

        log.info("Train model....");
        model.setListeners(new ScoreIterationListener(1));
        for( int i=0; i<epochs; i++ ) {

            if (trainValues) model.fit(train);

            log.info("*** Completed epoch {} ***", i);
            if (evalValues) {
                log.info("Evaluate model....");
                Evaluation eval = new Evaluation(network.getOutputNum());
                while (test.hasNext()) {
                    DataSet ds = test.next();

                    for(int jj = 0; jj < ds.getFeatureMatrix().length() / (28*28); jj ++) {
                        System.out.println(" jj " + jj + " next " + nextNum + " percent " + (evalsCorrect/evalsTotal));
                        showSquare(ds.get(jj).getFeatureMatrix());
                        //int label = showNumForSquare(ds.get(jj).getLabels());

                        String hotLabel = oneHot.getMatchingOut(ds.get(jj).getLabels());
                        System.out.println("label "+ hotLabel);

                        INDArray output = model.output(ds.get(jj).getFeatureMatrix());
                        eval.eval(ds.get(jj).getLabels(), output);

                        //System.out.println(output.length() + " -- " + output.toString());
                        //int prediction = showNumForSquare(output);

                        String hotOut = oneHot.getMatchingOut(output);
                        System.out.println("output " + hotOut);

                        evalsTotal ++;
                        if (hotLabel.equals(hotOut) ) evalsCorrect ++;
                    }


                    nextNum ++;
                }
                log.info(eval.stats());
                test.reset();
                System.out.println("Percent : " + (evalsCorrect/evalsTotal));
            }
        }
        log.info("****************Example finished********************");
        // 38 mins, 0.9446 Accuracy

        if(saveValues && trainValues) {
            files.saveModel(model);
            log.info("values saved....");
        }
    }

    public void startOperationMnistTrain() throws Exception {

        boolean saveValues = false;
        boolean loadValues = true;
        boolean trainValues = false;
        boolean evalValues = true;

        log.info("Load data....");
        //DataSetSplit mnist = new DataSetSplit();

        train = data.getSetTrain();
        test = data.getSetTest();

        FileManager files = new FileManager("lenet_example_digits");

        if (loadValues) {
            files.loadModel(model);
        }

        log.info("Train model....");
        model.setListeners(new ScoreIterationListener(1));
        for( int i=0; i<epochs; i++ ) {

            if (trainValues) model.fit(train);

            log.info("*** Completed epoch {} ***", i);
            if (evalValues) {
                log.info("Evaluate model....");
                Evaluation eval = new Evaluation(network.getOutputNum());
                while (test.hasNext()) {
                    DataSet ds = test.next();
                    INDArray output = model.output(ds.getFeatureMatrix());
                    eval.eval(ds.getLabels(), output);
                }
                log.info(eval.stats());
                test.reset();
            }
        }
        log.info("****************Example finished********************");
        // 38 mins, 0.9446 Accuracy

        if(saveValues && trainValues) {
            files.saveModel(model);
            log.info("values saved....");
        }
    }

    public void startOperationAlphaShow() throws Exception {


        OneHotOutput oneHot = new OneHotOutput(OneHotOutput.TYPE_ALPHA_UPPER);
        File alphaIO = new File("/home/dave/workspace/sd_19_temp.bmp");
        String letter = "S";
        INDArray arr = loadImageBMP(alphaIO);
        //ImageLoader loader = new ImageLoader(28,28);
        //ImageVectorizer loaderV = new ImageVectorizer(alphaIO,oneHot.length(),oneHot.getMemberNumber(letter) );
        //loaderV.normalize();
        //loaderV.binarize(20);
        //DataSet vector = loaderV.vectorize();


        INDArray out = convert28x28(arr);
        Operation.showSquare(out);
    }


    public static void showSquare(INDArray num) {
        System.out.println("---------");
        INDArray num2 = num.linearView();
        for (int k = 0; k < 28*28; k ++ ) {
            if ( num2.getDouble(k) > 0.5d ) {
                System.out.print("*");
            }
            else {
                System.out.print(" ");
            }
            if (k % 28 == 0) System.out.println();
        }
        System.out.println();
        System.out.println("----------");
    }

    public static INDArray convert28x28 (INDArray in) {
        double magx = 28/128.0d;
        double magy = 28/128.0d;
        int transx = 0, transy = 0;
        double outArray[][] = new double[28][28];

        for (int i  = 0; i < Math.sqrt(in.length()); i ++) {
            for (int j = 0; j < Math.sqrt(in.length()); j ++) {
                if (in.getRow(i).getDouble(j) > 0.5d) {
                    if (i*magx >=0 && i*magx + transx< 28 && j * magy >=0 && j * magy+ transy < 28) {
                        outArray[(int)(j*magy)+transy][(int)(i*magx) + transx] = 1.0d;
                    }
                }
            }
        }
        INDArray out = Nd4j.create(outArray);
        //out.transpose();
        System.out.println(out.toString());
        return out;
    }

    public  INDArray loadImageBMP ( File file) throws Exception {
        System.out.println(file.toString());
        BufferedImage image = ImageIO.read(file);

        double[][] array2D = new double[image.getWidth()][image.getHeight()];

        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++)
        {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++)
            {
                int color = image.getRGB(xPixel, yPixel);
                if (color== Color.BLACK.getRGB()) {
                    array2D[xPixel][yPixel] = 1;
                } else {
                    array2D[xPixel][yPixel] = 0; // ?
                }
            }
        }
        return Nd4j.create(array2D);
    }
}
