package com.autismscreening.ml;

import java.util.*;

/**
 * Lightweight Random Forest classifier for the academic prototype.
 *
 * IMPORTANT: The calibration data generated here are synthetic/demo data.
 * Replace with ethically collected, de-identified and clinically labelled data
 * before any research/clinical use.
 */
public class RandomForestScreeningModel {
    private static final int FEATURES = 6;
    private static final int TREES = 61;
    private final List<Node> forest = new ArrayList<>();
    private final Random random = new Random(42);

    public RandomForestScreeningModel() {
        trainSyntheticPrototype();
    }

    public Prediction predict(double[] x) {
        if (x == null || x.length < FEATURES) {
            double[] padded = new double[FEATURES];
            Arrays.fill(padded, 50.0);
            if (x != null) {
                System.arraycopy(x, 0, padded, 0, Math.min(x.length, FEATURES));
            }
            x = padded;
        }
        int positive = 0;
        for (Node tree : forest) positive += tree.predict(x);
        double probability = forest.isEmpty() ? 0.5 : (double) positive / forest.size();
        return new Prediction(probability, probability >= 0.50 ? 1 : 0, forest.size());
    }

    private void trainSyntheticPrototype() {
        double[][] x = new double[500][FEATURES];
        int[] y = new int[500];

        for (int i = 0; i < 500; i++) {
            int label = random.nextDouble() < 0.40 ? 1 : 0;
            y[i] = label;
            if (label == 1) {
                x[i] = new double[]{
                    clamp(random.nextGaussian()*10+48), clamp(random.nextGaussian()*10+48),
                    clamp(random.nextGaussian()*10+50), clamp(random.nextGaussian()*10+54),
                    clamp(random.nextGaussian()*10+50), clamp(random.nextGaussian()*12+55)
                };
            } else {
                x[i] = new double[]{
                    clamp(random.nextGaussian()*8+82), clamp(random.nextGaussian()*8+80),
                    clamp(random.nextGaussian()*8+84), clamp(random.nextGaussian()*7+84),
                    clamp(random.nextGaussian()*8+83), clamp(random.nextGaussian()*8+18)
                };
            }
        }

        for (int t = 0; t < TREES; t++) {
            int[] sample = new int[x.length];
            for (int i = 0; i < sample.length; i++) sample[i] = random.nextInt(x.length);
            forest.add(buildTree(x, y, sample, 0));
        }
    }

    private Node buildTree(double[][] x, int[] y, int[] rows, int depth) {
        if (depth >= 5 || rows.length < 10 || pure(y, rows)) return leaf(y, rows);

        int[] features = shuffledFeatures();
        double bestGini = Double.POSITIVE_INFINITY;
        int bestFeature = -1;
        double bestThreshold = 0;

        for (int f : features) {
            double[] values = new double[rows.length];
            for (int i = 0; i < rows.length; i++) values[i] = x[rows[i]][f];
            Arrays.sort(values);
            for (int k = 2; k < values.length; k += Math.max(1, values.length / 12)) {
                double threshold = (values[k-1] + values[k]) / 2.0;
                double g = splitGini(x, y, rows, f, threshold);
                if (g < bestGini) { bestGini = g; bestFeature = f; bestThreshold = threshold; }
            }
        }
        if (bestFeature < 0) return leaf(y, rows);

        int leftN=0;
        for (int r:rows) if(x[r][bestFeature] <= bestThreshold) leftN++;
        if(leftN==0 || leftN==rows.length) return leaf(y, rows);

        int[] left=new int[leftN], right=new int[rows.length-leftN];
        int li=0,ri=0;
        for(int r:rows) {
            if(x[r][bestFeature] <= bestThreshold) left[li++]=r;
            else right[ri++]=r;
        }
        Node n=new Node(); n.feature=bestFeature; n.threshold=bestThreshold;
        n.left=buildTree(x,y,left,depth+1); n.right=buildTree(x,y,right,depth+1);
        return n;
    }

    private double splitGini(double[][] x,int[] y,int[] rows,int f,double th){
        int ln=0,lp=0,rn=0,rp=0;
        for(int r:rows){
            if(x[r][f]<=th){ln++; if(y[r]==1)lp++;} else {rn++; if(y[r]==1)rp++;}
        }
        if(ln==0||rn==0)return Double.POSITIVE_INFINITY;
        return (ln*gini(lp,ln)+rn*gini(rp,rn))/rows.length;
    }
    private double gini(int pos,int n){double p=(double)pos/n;return 1-p*p-(1-p)*(1-p);}
    private boolean pure(int[] y,int[] rows){int first=y[rows[0]];for(int r:rows)if(y[r]!=first)return false;return true;}
    private Node leaf(int[] y,int[] rows){int pos=0;for(int r:rows)if(y[r]==1)pos++;Node n=new Node();n.leaf=true;n.value=pos*2>=rows.length?1:0;return n;}
    private int[] shuffledFeatures(){List<Integer> fs=new ArrayList<>();for(int i=0;i<FEATURES;i++)fs.add(i);Collections.shuffle(fs,random);int m=2;int[] out=new int[m];for(int i=0;i<m;i++)out[i]=fs.get(i);return out;}
    private double clamp(double v){return Math.max(0,Math.min(100,v));}

    public static class Prediction {
        public final double elevatedConcernProbability;
        public final int predictedClass;
        public final int treeCount;
        public Prediction(double p,int c,int t){elevatedConcernProbability=p;predictedClass=c;treeCount=t;}
    }
    private static class Node {
        boolean leaf; int value,feature; double threshold; Node left,right;
        int predict(double[] x){if(leaf)return value;return x[feature]<=threshold?left.predict(x):right.predict(x);}
    }
}
