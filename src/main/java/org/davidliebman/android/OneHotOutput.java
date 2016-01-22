package org.davidliebman.android;

import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.ArrayList;

/**
 * Created by dave on 1/21/16.
 */
public class OneHotOutput {

    String stringList = "";

    public static final int TYPE_NUMERALS = 0;
    public static final int TYPE_ALPHA_UPPER = 1;
    public static final int TYPE_ALPHA_LOWER = 2;
    public static final int TYPE_SYMBOL = 4;


    public OneHotOutput(int type) {
        switch (type) {
            case TYPE_NUMERALS:
                makeList( "0","9");
                break;
            case TYPE_ALPHA_LOWER:
                makeList("a","z");
                break;
            case TYPE_ALPHA_UPPER:
                makeList("A","Z");
                break;
        }
    }

    public String makeList(int start, int stop) {
        String stringList = "";
        for (int i = start; i <= stop; i ++) {
            stringList = stringList + String.valueOf( (char)i);
        }
        return stringList;
    }

    public void makeList(String start, String stop) {
        int startNum = start.charAt(0);
        int stopNum = stop.charAt(0);
        stringList = makeList(startNum,stopNum);
    }

    public void addToList(String append) {
        stringList = stringList + append;
    }

    public String toString() {
        return stringList;
    }

    public String getMatchingOut (double [] in) {
        String out = "";
        double largest = 0;
        if (stringList.length() > 0 && in.length == stringList.length()) {
            out = stringList.substring(0,1);
            for (int i = 0; i < stringList.length(); i ++) {
                if (in[i] > largest) {
                    largest = in[i];
                    out = stringList.substring(i,i+1);
                }
            }
        }
        return out;

    }

    public String getMatchingOut (INDArray in) {
        String out = "";
        double largest = 0;
        in = in.linearView();
        if (stringList.length() > 0 && in.length() == stringList.length()) {
            out = stringList.substring(0,1);
            for (int i = 0; i < stringList.length(); i ++) {
                if (in.getDouble(i) > largest) {
                    largest = in.getDouble(i);
                    out = stringList.substring(i,i+1);
                }
            }
        }
        return out;

    }

    public boolean getIsMember (String in) {
        boolean out = false;

        if (stringList.length() > 0 ) {
            if (stringList.contains(in)) out = true;
        }

        return out;
    }

    public int length() {
        return stringList.length();
    }

    public int getMemberNumber(String in) {
        int out = -1;
        double largest = 0;
        if (in.length() > 1) {
            int num = Integer.parseInt(in,16);
            in = String.valueOf((char) num);
        }
        // input is a visual representation of the character
        if (stringList.length() > 0) {

            for (int i = 0; i < stringList.length(); i++) {
                if (in.equals(stringList.substring(i, i + 1))) {

                    out = i;
                }
            }
        }

        return out;
    }
}
