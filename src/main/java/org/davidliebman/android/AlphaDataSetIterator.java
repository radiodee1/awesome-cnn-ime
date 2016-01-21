package org.davidliebman.android;

import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.datasets.vectorizer.ImageVectorizer;
import org.deeplearning4j.util.ImageLoader;
import org.nd4j.linalg.dataset.DataSet;

import java.io.File;
import java.util.Iterator;

/**
 * Created by dave on 1/21/16.
 */
public class AlphaDataSetIterator extends BaseDatasetIterator implements Iterator<DataSet>,DataSetIterator {

    public AlphaDataSetIterator(int batch, int numExamples, BaseDataFetcher fetcher) {
        super(batch, numExamples, fetcher);

        OneHotOutput oneHot = new OneHotOutput(OneHotOutput.TYPE_ALPHA_UPPER);
        File alphaIO = new File("/home/dave/workspace/sd_19_temp.bmp");
        String letter = "S";
        ImageLoader loader = new ImageLoader(28,28);
        ImageVectorizer loaderV = new ImageVectorizer(alphaIO,oneHot.length(),oneHot.getMemberNumber(letter) );

        Operation.showSquare(loaderV.vectorize().getFeatureMatrix());
    }
}
