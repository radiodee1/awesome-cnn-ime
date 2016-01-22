package org.davidliebman.android;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.factory.Nd4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dave on 1/21/16.
 */
public class AlphaDataSet extends DataSet {



    ArrayList<String> list = new ArrayList<String>();

    int searchType = 0;
    long seed = 0;

    public AlphaDataSet(int type, long mSeed)  throws Exception{
        super();

        searchType = type;
        seed = mSeed;

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

        makeFileList();

        randomizeList() ;
    }

    public void makeFileList() throws Exception{
        String homeDir = System.getProperty("user.home") +
                File.separator +"workspace" + File.separator + "sd_nineteen" + File.separator;

        String pattern = "HSF*/F*/HSF*.bmp";
        pattern = homeDir + pattern;
        final PathMatcher matcher =
                FileSystems.getDefault().getPathMatcher("glob:" + pattern);

        System.out.println(pattern +"----");

        //final PathMatcher matcher2 = FileSystems.getDefault().getPathMatcher("glob:d:/**/*.zip");
        Files.walkFileTree(Paths.get(homeDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(file)) {
                    //System.out.println(file);
                    list.add(file.toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println(list.size());
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

    public void randomizeList() {
        Random r = new Random(seed);
        ArrayList<String> newList = new ArrayList<String>();

        OneHotOutput output = new OneHotOutput(searchType);
        int startConst = 6, stopConst = 4;

        for (int i = 0; i < list.size(); i ++) {
            String num = list.get(i).substring(list.get(i).length() - startConst, list.get(i).length()-stopConst);
            //System.out.println(num);
            if (output.getMemberNumber(num) != -1) {
                newList.add(list.get(i));
            }
        }

        list = newList;
        newList = new ArrayList<String>();
        int size = list.size();
        for (int i = 0; i < size; i ++) {

            //System.out.println(list.get(i));
            int choose = r.nextInt(list.size());
            newList.add(list.get(choose));
            list.remove(choose);
        }

        list = newList;

        for(int i = 0; i < list.size(); i ++) {
            System.out.println(list.get(i));

        }

        //System.out.println(String.valueOf((char)output.getMemberNumber("63")));
        //System.out.println(list.get(list.size() - 1));
    }



    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public INDArray getFeatureMatrix() {
        return super.getFeatureMatrix();
    }

    @Override
    public INDArray getLabels() {
        return super.getLabels();
    }

    @Override
    public SplitTestAndTrain splitTestAndTrain(int numHoldout, Random rng) {
        return super.splitTestAndTrain(numHoldout, rng);
    }

    @Override
    public SplitTestAndTrain splitTestAndTrain(int numHoldout) {
        return super.splitTestAndTrain(numHoldout);
    }


}
