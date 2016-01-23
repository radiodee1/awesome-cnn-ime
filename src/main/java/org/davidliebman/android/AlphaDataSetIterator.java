package org.davidliebman.android;

import org.deeplearning4j.datasets.fetchers.BaseDataFetcher;
import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.datasets.vectorizer.ImageVectorizer;
import org.deeplearning4j.util.ImageLoader;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dave on 1/21/16.
 */
public class AlphaDataSetIterator extends BaseDatasetIterator implements Iterator<DataSet>,DataSetIterator {

    ArrayList<String> list = new ArrayList<String>();

    public AlphaDataSetIterator(int batch, int numExamples, BaseDataFetcher fetcher) throws Exception {
        super(batch, numExamples, fetcher);
        OneHotOutput oneHot = new OneHotOutput(Operation.EVAL_SINGLE_ALPHA_UPPER);
        File alphaIO = new File("/home/dave/workspace/sd_19_temp.bmp");
        String letter = "S";
        /*
        INDArray arr = loadImageBMP(alphaIO);


        INDArray out = convert28x28(arr);
        Operation.showSquare(out);

        makeFileList();
        */
    }

    /*




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
        //System.out.println(out.toString());
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
    */

}
