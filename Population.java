
public class Population {
    
    private Individual[] population;
    // Constructor that creates a Population obj and instantiates the
    // population array where size = populationSize
    public Population (int populationSize) {
        population = new Individual[populationSize];
    }
    
    // Constructor that also instantiates the Individual array with a given 
    // size but also populates it with random individuals with a given # of
    // variables (numVariables)
    public Population (int numVariables, int populationSize) {
        
        population = new Individual[populationSize];
        
        for (int i = 0; i < populationSize; ++i) {
            population[i] = new Individual(numVariables);
        }
    }
    // Last constructor that uses an Individual array as a parameter to 
    // construct a population obj
    public Population (Individual[] individuals) {
        population = individuals;
    }
    
    // Getter that returns the population/Individual array
    public Individual[] GetPopulation() {
        return population;
    }
    
    // Setter that sets the population to be the parameter field
    public void SetPopulation(Individual[] population) {
        this.population = population;
    }
    
    // Getter that returns individual at index
    public Individual GetIndividual(int index) {
        return population[index];
    }
    
    //Setter sets a new individual at the given index
    public void SetIndividual(Individual individual, int index) {
        population[index] = individual;
    }
    
    // Function that adds individual in the population at the next empty
    // cell of the array.  It takes an individual as an argument and returns
    // void. 
    public void AddIndividual(Individual individual) {
        for (int i = 0; i < population.length; ++i) {
            if (population[i] == null) {
                population[i] = individual;
                break;
            }
        }
    }
    
    //Getter that returns size of the population/Individual array
    public int GetSize() {
        return population.length;
    }

}
