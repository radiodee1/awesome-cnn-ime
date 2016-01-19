package org.davidliebman.android;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.*;

/**
 * Created by dave on 1/19/16.
 */
public class FileManager {
    MultiLayerNetwork model;

    String fileName = "/home/dave/workspace/lenet_example_digits.bin";


    FileManager (MultiLayerNetwork m ) {
        model = m;
    }

    public MultiLayerNetwork getModel() {return model;}
    public void setModel(MultiLayerNetwork m) {model = m;}

    public void setFileName(String name) {
        fileName = "/home/dave/workspace/"+ name +".bin";
    }

    public void setLongFileName(String name) {fileName = name;}

    public void loadModel() throws Exception{
        File filePath = new File(fileName);
        DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
        INDArray newParams = Nd4j.read(dis);
        dis.close();
        model.setParameters(newParams);
    }

    public void saveModel() throws Exception {
        //Write the network parameters:
        File filePointer = new File(fileName);
        //OutputStream fos = Files.newOutputStream(Paths.get(fileName));
        FileOutputStream fos = new FileOutputStream(filePointer);
        DataOutputStream dos = new DataOutputStream(fos);
        Nd4j.write(model.params(), dos);
        dos.flush();
        dos.close();
    }
}
