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

        import java.util.*;

/**
 *
 */
public class Example {

    private static final Logger log = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) throws Exception {
        int numRows = 28;
        int numColumns = 28;
        int nChannels = 1;
        int outputNum = 10;
        int numSamples = 2000;
        int batchSize = 64;
        int iterations = 1; //10
        int splitTrainNum = (int) (batchSize*.8);
        int seed = 123;
        int listenerFreq = iterations/5;
        int nEpochs = 1;
        DataSet mnist;
        SplitTestAndTrain trainTest;
        DataSet trainInput;
        List<INDArray> testInput = new ArrayList<INDArray>();
        List<INDArray> testLabels = new ArrayList<INDArray>();



        //log.info("Load data....");
        //DataSetIterator mnistIter = new MnistDataSetIterator(batchSize,numSamples, true);
        log.info("Load data....");
        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize,true,12345);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize,false,12345);

        log.info("Build model....");
        MultiLayerConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iterations)
                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list(6)
                .layer(0, new ConvolutionLayer.Builder(5, 5)
                        .stride(1,1)
                        .nIn(nChannels)
                        .nOut(20)
                        .dropOut(0.5)
                        .weightInit(WeightInit.XAVIER)
                        .activation("relu")
                        .build())
                .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX, new int[] {2,2})
                        .stride(2,2)
                        .build())
                .layer(2, new ConvolutionLayer.Builder(5,5)
                        .stride(1,1)
                        .nIn(20) // nChannels
                        .nOut(6) // 6
                        .weightInit(WeightInit.XAVIER)
                        .activation("relu")
                        .build())
                .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX, new int[] {2,2})
                        .stride(2,2)
                        .build())
                .layer(4, new DenseLayer.Builder().activation("relu")
                        .nOut(500).build())
                .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(outputNum)
                        .weightInit(WeightInit.XAVIER)
                        .activation("softmax")
                        .build())
                .backprop(true).pretrain(false);

        new ConvolutionLayerSetup(builder,numRows,numColumns,nChannels);

        MultiLayerConfiguration conf = builder.build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        /*
        log.info("Train model....");
        model.setListeners(Arrays.asList((IterationListener) new ScoreIterationListener(listenerFreq)));
        while(mnistIter.hasNext()) {
            mnist = mnistIter.next();
            trainTest = mnist.splitTestAndTrain(splitTrainNum, new Random(seed)); // train set that is the result
            trainInput = trainTest.getTrain(); // get feature matrix and labels for training
            testInput.add(trainTest.getTest().getFeatureMatrix());
            testLabels.add(trainTest.getTest().getLabels());
            model.fit(trainInput);
        }

        log.info("Evaluate weights....");

        log.info("Evaluate model....");
        Evaluation eval = new Evaluation(outputNum);
        for(int i = 0; i < testInput.size(); i++) {
            INDArray output = model.output(testInput.get(i));
            eval.eval(testLabels.get(i), output);
        }
        INDArray output = model.output(testInput.get(0));
        eval.eval(testLabels.get(0), output);
        log.info(eval.stats());
        log.info("****************Example finished********************");
        */

        log.info("Train model....");
        model.setListeners(new ScoreIterationListener(1));
        for( int i=0; i<nEpochs; i++ ) {
            model.fit(mnistTrain);
            log.info("*** Completed epoch {} ***", i);

            log.info("Evaluate model....");
            Evaluation eval = new Evaluation(outputNum);
            while(mnistTest.hasNext()){
                DataSet ds = mnistTest.next();
                INDArray output = model.output(ds.getFeatureMatrix());
                eval.eval(ds.getLabels(), output);
            }
            log.info(eval.stats());
            mnistTest.reset();
        }
        log.info("****************Example finished********************");
        // 41 mins, 0.7613 accuracy
    }
}

