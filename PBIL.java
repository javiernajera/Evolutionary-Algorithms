import java.util.Random;

public class PBIL {
    
    private String testFile;
    private int populationSize;
    private float mutationProb;
    private int numGenerations;
    
    private float posLearningRate;
    private float negLearningRate;
    private float mutationAmount;

    private int numVariables;
    private int numClauses;
    
    public PBIL() {}
    
    public void RunPBIL(String[] args, int[][] clauses, int numVariables, int numClauses) {
        
        this.numVariables = numVariables;
        this.numClauses = numClauses;
        
        ReadArguments(args);
        System.out.println("running");
        
        double[] popVector = new double[numVariables];
        FitNode[] fitnessArray;
        Population population = new Population(numVariables);
        Individual best, worst;
        FitNode bestNode;
        
        for (int i = 0; i < numVariables; ++i) {
            popVector[i] = 0.5;
        }
        
        for (int j = 0; j < numGenerations; ++j) {
            
            population = GeneratePop(popVector);
            fitnessArray = GenerateFitnessArray(population, clauses);
            if (numClauses - fitnessArray[populationSize-1].GetFitness() == 0) {
                algorithmDone(population.GetIndividual(fitnessArray[populationSize-1].GetPopIndex()), 
                        fitnessArray[populationSize-1].GetFitness(), j + 1);
            }
            
            best = population.GetIndividual(fitnessArray[populationSize-1].GetPopIndex());
            worst = population.GetIndividual(fitnessArray[0].GetPopIndex());
            
            if (j % 50 == 0) {
                bestNode = fitnessArray[populationSize-1];
                System.out.println("Generation " + j + ": " + "Best fitness so far is " + bestNode.GetFitness() + 
                        ", Assignment is " + population.GetIndividual(bestNode.GetPopIndex()).GetAssignments());
            }
            
            popVector = PositiveUpdate(popVector, best);
            popVector = NegativeUpdate(popVector, best, worst);
            popVector = Mutate(popVector);
            
        }
        
        // find and report best individual after all generations
        population = GeneratePop(popVector);
        fitnessArray = GenerateFitnessArray(population, clauses);
        algorithmDone(population.GetIndividual(fitnessArray[populationSize-1].GetPopIndex()), 
                fitnessArray[populationSize-1].GetFitness(), numGenerations-1);
        
        
    }
    
    // Function takes the args parameter and sets all the variables needed
    // for the PBIL algorithm (i.e. args[x]= paramter that corresponds to 
    // some variable for our algorithm)
    public void ReadArguments(String[] args) {
        try {
            testFile = args[0];
            populationSize = Integer.parseInt(args[1]);
            posLearningRate = Float.parseFloat(args[2]);
            negLearningRate = Float.parseFloat(args[3]);
            mutationProb = Float.parseFloat(args[4]);
            mutationAmount = Float.parseFloat(args[5]);
            numGenerations = Integer.parseInt(args[6]);
            
        }   catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Too few arguments. Reevaluate input and give it another shot.");
            Sytstem.exit(0);
        }   catch(NumberFormatException e) {
            System.out.println("Error: Arguments in incorrect format. Reevaluate input and give it another shot.");
            Sytstem.exit(0);
        }   catch(Exception e) {
            System.out.println("Error: Unknown error encountered. Reevaluate input and give it another shot.");
            Sytstem.exit(0);
        }
    }
    
    // Generate and sort an array of fitnesses for the individuals by taking 
    // each individual, calculating its fitness, and using the fitness and its
    // index to create a FitNode.  After creating all the FitNodes we sort the
    // array so that it returns the list already ranked. The function takes in
    // the population and clauses (2d array) as arguments and it returns a 
    // FitNode array. 
    public FitNode[] GenerateFitnessArray(Population population, int[][] clauses) {
        
        FitNode[] fitnessArray = new FitNode[populationSize];
        FitNode indieFit, temp;
        Individual indie;
        int fitness;
        
        // create fitness nodes
        for (int i = 0; i < populationSize; ++i) {
            indie = population.GetIndividual(i);
            fitness = fitnessOf(indie, clauses);
            indieFit = new FitNode(i, fitness);
            fitnessArray[i] = indieFit;
            
        }
        
        // sort fitness array
        for (int j = 0; j < populationSize; ++j) {
            for (int k = j+1; k < populationSize; ++k) {
                if (fitnessArray[j].GetFitness() > fitnessArray[k].GetFitness()) {
                    temp = fitnessArray[k];
                    fitnessArray[k] = fitnessArray[j];
                    fitnessArray[j] = temp;
                }
            }
        }
        
        return fitnessArray;
    }
    
    // Calculate the fitness of an individual by counting how many clauses
    // it's assignment string satisfies.  It takes an individual and the 2d 
    // array of clauses as arugments, and returns an int that is the fitness.
    public int fitnessOf(Individual indie, int[][] clauses){

         int fitness = 0;

         for (int i = 0; i < numClauses; ++i) {
             // break out of clause once a single satisfying assignment found
             for (int j = 0; j < clauses[i].length; ++j) {
                 int variableIndex = Math.abs(clauses[i][j])-1;
                 // if not, need false to satisfy
                 if (clauses[i][j] < 0) {
                     if (indie.GetVariable(variableIndex) == '0') {
                         ++fitness;
                         break;
                     }
                 }
                 // need true to satisfy
                 else {
                     if (indie.GetVariable(variableIndex) == '1') {
                         ++fitness;
                         break;
                     }
                 }
             }
         }

         return fitness;
     }

    // GeneratePop generates a population from a given population vector.
    // It takes a double array (the popVector) and returns a Population obj. 
    public Population GeneratePop(double[] popVector) {
        
        Random rand = new Random();
        
        String assignments = "";
        Individual indie = new Individual();
        Population population = new Population(populationSize);
        
        for (int i = 0; i < populationSize; ++i) {
            for (int j = 0; j < numVariables; ++j) {
                if (rand.nextDouble() <= popVector[j]) {
                    assignments += "0";
                }
                else {
                    assignments += "1";
                }
            }
            indie.SetAssignments(assignments);
            population.SetIndividual(indie, i);
            assignments = "";
        }
        return population;
    }

    // PositiveUpdate updates the p-vector towards the best individual
    // Takes double array (the popVector) and an Individual as arguments
    // and it returns the updated double array (popVector).
    public double[] PositiveUpdate(double[] popVector, Individual best) {
        
        int bestProb;
        
        for (int i = 0; i < numVariables; ++i) {
            bestProb = Integer.parseInt(best.GetAssignments().split("")[i]);    
            popVector[i] = (popVector[i] * (1.0 - posLearningRate)) + (bestProb * posLearningRate); 
        }
        
        return popVector;
    }

    // NegativeUpdate updates the vector away from the worst individual
    // Takes double array (the popVector) and an Individual as arguments
    // and it returns the updated double array (popVector).
    public double[] NegativeUpdate(double[] popVector, Individual best, Individual worst) {
        
        int bestProb, worstProb;
        
        for (int i = 0; i < numVariables; ++i) {
            bestProb = Integer.parseInt(best.GetAssignments().split("")[i]);
            worstProb = Integer.parseInt(worst.GetAssignments().split("")[i]);  
            if (bestProb != worstProb) {
                popVector[i] = (popVector[i] * (1.0 - negLearningRate)) + (bestProb * negLearningRate); 
            }
        }
        return popVector;
    }
    
    // Mutate function takes the popVector and given the mutation probability
    // the function decides whether to mutate and in which direction.
    public double[] Mutate(double[] popVector) {
        
        Random rand = new Random();
        int mutationDirection;
        
        for (int i = 0; i < numVariables; ++i) {
            
            if (rand.nextDouble() < mutationProb) {
                if (rand.nextDouble() > 0.5) {
                    mutationDirection = 1;
                }
                else {
                    mutationDirection = 0;
                }
                popVector[i] = (popVector[i] * (1.0 - mutationAmount)) + (mutationDirection * mutationAmount);
            }
        }
        return popVector;
    }
    
    // prints out the relevant info when algorithm is finished.
    public void algorithmDone(Individual best, int fitness, int iteration) {

         double percentClauses = ((double)fitness/(double)numClauses)*100;

         System.out.println("Problem file: " + testFile);
         System.out.println("Number of variables: " + numVariables);
         System.out.println("Number of clauses: " + numClauses);
         System.out.println("Number of clauses satisfied: " + fitness);
         System.out.println("Percentage of clauses satisfied: " + percentClauses + "%");
         System.out.println("Best assignment: " + best.GetAssignments());
         System.out.println("Found in iteration " + (iteration+1));
         System.exit(0);
     }
}
