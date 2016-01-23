package org.davidliebman.android;

/**
 * Created by dave on 1/18/16.
 */

        import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
        import org.deeplearning4j.eval.Evaluation;
        import org.deeplearning4j.nn.api.OptimizationAlgorithm;
        import org.deeplearning4j.nn.conf.GradientNormalization;
        import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
        import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
        import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
        import org.deeplearning4j.nn.conf.layers.DenseLayer;
        import org.deeplearning4j.nn.conf.layers.OutputLayer;
        import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
        import org.deeplearning4j.nn.conf.layers.setup.ConvolutionLayerSetup;
        import org.deeplearning4j.nn.conf.preprocessor.CnnToFeedForwardPreProcessor;
        import org.deeplearning4j.nn.conf.preprocessor.FeedForwardToCnnPreProcessor;
        import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
        import org.deeplearning4j.nn.weights.WeightInit;
        import org.deeplearning4j.optimize.api.IterationListener;
        import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
        import org.nd4j.linalg.api.ndarray.INDArray;
        import org.nd4j.linalg.dataset.DataSet;
        import org.nd4j.linalg.dataset.SplitTestAndTrain;
        import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
        import org.nd4j.linalg.factory.Nd4j;
        import org.nd4j.linalg.lossfunctions.LossFunctions;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.io.*;
        import java.util.*;

/**
 *
 */
public class Example {

    private static final Logger log = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) throws Exception {



        int batchSize = 64;
        int iterations = 1; //10

        int nEpochs = 1;

        int operation = Operation.EVAL_SINGLE_ALPHA_UPPER;
        OneHotOutput oneHot = new OneHotOutput(operation);

        Network cnn = new Network(oneHot.length());
        //FileManager files = new FileManager();

        DataSetSplit data = new DataSetSplit(operation);

        final Operation opTest = new Operation(cnn,data,batchSize, nEpochs,iterations);
        opTest.setEvalType(operation);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {

                    opTest.saveModel();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        opTest.startOperation();


    }
}

