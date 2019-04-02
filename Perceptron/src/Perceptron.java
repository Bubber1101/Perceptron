import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Perceptron {
    private LinkedList<Case> training;
    private LinkedList<Case> test;
    private double learningRate;
    private double[] weights;
    private double threshold;
    private  HashMap<String,Boolean> decisions;
    private int accuracy;
    private int count;
    private int[][] precision;
    private Pair<String,String> decisionPair;
    private int len;


    public Perceptron(String trainingFilePath, String testFilePath, double learningRate, double threshold) throws FileNotFoundException {
        training = getCasesFromFile(trainingFilePath);
        test = getCasesFromFile(testFilePath);
        this.learningRate = learningRate;
        weights = new double[training.getFirst().getAttributes().length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }
        decisions = new HashMap<>();
//        System.out.println(training.toString());
        decisions.put(training.stream().filter((k)-> !k.getDecision().equals(training.getFirst().getDecision())).findFirst().get().getDecision(),
                true);
        decisions.put(training.getFirst().getDecision(),false);
        decisionPair = new Pair<>(training.getFirst().getDecision(),training.stream().filter((k)-> !k.getDecision().equals(training.getFirst().getDecision())).findFirst().get().getDecision());
        this.threshold = threshold;
        accuracy = 0;
        count = 0;
        precision = new int[2][2] ;//first false second true  [0][0] - how much was false and was supposed to be false
                                            //[0][1] - how much was false and was supposed to be true
                                            //[1][0] how much was true and was supposed to be false
                                            //[1][1] how much was true and was supposed to be true
        len = weights.length;

    }

    public void test() {
        count = 0;
        accuracy = 0;
        precision[0][0] = 0;
        precision[0][1] = 0;
        precision[1][1] = 0;
        precision[1][0] = 0;

        for (Case c: test){
            boolean outcome = activatedBy(c);
            int got = boolToInt(outcome);
            int wanted =boolToInt(decisions.get(c.getDecision()));
            precision[got][wanted]++;
            count++;
            if (got == wanted) accuracy++;
            //debug
            if (got == wanted) System.out.println("Correct " + c.toString());
            else System.out.println("incorect " + c.toString());


        }

        System.out.println("Accuracy: "+ (precision[0][0] + precision[1][1]) +"/"+ (precision[0][0]+precision[1][0]+precision[0][1]+precision[1][1]) + "" +
                " = " + (double)(precision[0][0] + precision[1][1]) / (double)(precision[0][0]+precision[1][0]+precision[0][1]+precision[1][1]));
        System.out.println("Precision: " );
        System.out.println("X 0 1");
        System.out.println("0 "+precision[0][0]+" "+precision[0][1]);
        System.out.println("1 "+precision[1][0]+" "+precision[1][1]);
        System.out.println("Precision 0: " + (double)(precision[0][0])/(double)(precision[0][0]+precision[1][0]));
        System.out.println("Precision 1: " + (double)(precision[1][1]/(double)(precision[1][1]+precision[0][1])));

    }

    public void train() {
        for (Case c : training) {
            boolean temp = activatedBy(c);
            boolean x = decisions.get(c.getDecision());
           // if(temp != x){
                deltaRule(c.getAttributes(),x,temp);
           // }
        }
    }


    public LinkedList<Case> getCasesFromFile(String path) throws FileNotFoundException {
        LinkedList<Case> result = new LinkedList<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNextLine()) {
            result.add(new Case(scanner.nextLine(), true));
        }
        return result;
    }

    public boolean activatedBy(Case c) {
        //System.out.print("");
        double x = 0;
        double arr[] = c.getAttributes();
        for (int i = 0; i < len; i++) {
            x += arr[i] * weights[i];
        }
        return x >= threshold;
    }

    public void deltaRule(double[] inputs, boolean desiredOutcomebool, boolean outcomebool) {
        double xa = threshold;
        double xb = -1;
        int desiredOutcome = boolToInt(desiredOutcomebool);
        int outcome = boolToInt(outcomebool);
        double dy = (desiredOutcome - outcome) * learningRate;

        double[] newWeight = new double[inputs.length + 1];
        double[] input = new double[inputs.length + 1];
        for (int i = 0; i < inputs.length; i++) {
            newWeight[i] = weights[i];
            input[i] = inputs[i];
        }
        newWeight[newWeight.length - 1] = xa; //puting threshold to the equation
        input[newWeight.length - 1] = xb;   //olso here
        for (int i = 0; i < input.length; i++) {
            input[i] = input[i] * dy;
            newWeight[i] = newWeight[i] + input[i];

        }
        for (int i = 0; i < len; i++) {
            weights[i] = newWeight[i];
        }
        threshold = newWeight[len];

    }

    public int boolToInt(boolean xd){
        if (xd) return 1;
        return 0;
    }
    public int getWeightCount(){
        return len;
    }

    public HashMap<String, Boolean> getDecisions() {
        return decisions;
    }

    public Pair<String, String> getDecisionPair() {
        return decisionPair;
    }
    public String getWeightstoString(){
        String a = "[" + weights[0];

        for (int i = 1; i <len; i++) {
            a+="," + weights[i];
        }
        return a;
    }

    public double getThreshold() {
        return threshold;
    }
}
