import java.io.File;
import java.util.Scanner;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class RunTests {

    public static char algorithm;
    public static String testFile;
    public static int populationSize;
    public static float mutationProb;
    public static int numGenerations;

    public static String selectionType;
    public static String crossoverMethod;
    public static float crossoverProb;

    public static float posLearningRate;
    public static float negLearningRate;
    public static float mutationAmount;

    public static int numVariables;
    public static int numClauses;
    
    //we use command line arguments from the terminal as our parameters
    public static void main(String[] args) {
    	
    	algorithm = args[7].charAt(0);
    	testFile = args[0];

        int[][] clauses = ReadCNFFile();

        // genetic algorithm
        if (algorithm == 'g') {
        	GeneticAlgorithm ga = new GeneticAlgorithm();
        	ga.RunGA(args, clauses, numVariables, numClauses);
        }

        // pbil
        if (algorithm == 'p') {
        	PBIL pbil = new PBIL();
        	pbil.RunPBIL(args, clauses, numVariables, numClauses);
        }

    }
    
    
    // Read CNFFile is a function that takes the String testFile that holds
    // the name of the file that has the MAXSAT problem we want to use. It places
    // the MAXSAT clauses into a 2-d array, that it then returns at the end.  
    public static int[][] ReadCNFFile() {
        try {
            File file = new File(testFile);
            Scanner scan = new Scanner(file);
            int[][] cnfArray = new int[0][0];
            int active = 0;
            int i = 0;

            while (scan.hasNextLine()) {
                String currentLine = scan.nextLine();
                if (currentLine.charAt(0) == 'p') {
                    active = 1;

                    String[] split = currentLine.split("\\s+");
                    numVariables = Integer.parseInt(split[2]);
                    numClauses = Integer.parseInt(split[3]);

                    // do 1st clause line to figure out num variables in clauses
                    currentLine = scan.nextLine();
                    int[] temp = Arrays.stream(currentLine.split("\\s+")).mapToInt(Integer::parseInt).toArray();

                    cnfArray = new int[numClauses][temp.length];

                    cnfArray[i] = Arrays.copyOf(temp, temp.length-1);
                    ++i;
                }
                else if (active == 1) {
                    int[] temp = Arrays.stream(currentLine.split("\\s+")).mapToInt(Integer::parseInt).toArray();
                    cnfArray[i] = Arrays.copyOf(temp, temp.length-1);
                    ++i;
                }
            }
            return cnfArray;
        } catch (FileNotFoundException e) {
            System.out.println("GEE, this file didn't work! Enter a valid test file name...");
            return null;
        }
    }
}
    
    
    
