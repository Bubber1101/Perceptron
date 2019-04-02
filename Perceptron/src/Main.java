import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the learning rate");
        Double learningRate = Double.parseDouble(scanner.nextLine());
        System.out.println("enter threshold");
        Double threshold = Double.parseDouble(scanner.nextLine());
        //System.out.println("Choose data");
       // String data = scanner.nextLine();
        //if (data.equals("iris")) {

            Perceptron perceptron = new Perceptron(
                    "C:/Users/patry/Desktop/Uni stuff/4th semester/NAI/project2/perceptrondata/iris_perceptron/training.txt",
                    "C:/Users/patry/Desktop/Uni stuff/4th semester/NAI/project2/perceptrondata/iris_perceptron/test.txt",
                    learningRate,
                    threshold);

     //   } else {
       /*     Perceptron perceptron = new Perceptron(
                    "C:/Users/patry/Desktop/Uni stuff/4th semester/NAI/project2/perceptrondata/example1/train.txt",
                    "C:/Users/patry/Desktop/Uni stuff/4th semester/NAI/project2/perceptrondata/example1/test.txt",
                    learningRate,
                    threshold);
        */
        String input = scanner.nextLine();
        while (!input.toLowerCase().equals("quit")) {
            String temp[] = input.split(" ");

            if (temp[0].toLowerCase().equals("train")) {
                int x =1;
                if (temp.length >1 )x = Integer.parseInt(temp[1]);
                for (int i = 0; i < x; i++) {
                    perceptron.train();
                }
                System.out.println("Finished training");
            } else if (temp[0].toLowerCase().equals("test")) {
                perceptron.test();
            } else if (temp[0].toLowerCase().equals("input")) {
                if ((temp.length - 1) == perceptron.getWeightCount()) {
                    String caseString = temp[1];
                    for (int i = 2; i < temp.length; i++) {
                        caseString += "," + temp[i];
                    }
                    Case userCase = new Case(caseString, false);
                    boolean result = perceptron.activatedBy(userCase);
                    String resultDecision;
                    if (result) resultDecision = perceptron.getDecisionPair().getValue();
                    else resultDecision = perceptron.getDecisionPair().getKey();
                    System.out.println("According to perceptron the user entered case belongs to: " + resultDecision
                    );
                }

            } else if (temp[0].toLowerCase().equals("status")){
                System.out.println("Weights: " +perceptron.getWeightstoString() + "  Threshold: " + perceptron.getThreshold());
            }
            else System.out.println("invalid input");

            System.out.println("Enter a request");
            input = scanner.nextLine();
        }
    }
}
