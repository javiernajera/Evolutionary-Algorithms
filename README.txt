***************************README FOR RUNTESTS**************************************

To run our program you must have:

 
Population.java
 
Individual.Java
 
PBIL.Java


GeneticAlgorithm.java
 
FitNode.java

RunTests.java





Our RunTests.java file is the main file in our evolutionary algorithms project.


What RunTests does when you run the file is take a series of commands in this
 
order:





java RunTests.java filename pop_size selection crossover pC pM generations algorithm


=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


filename - This is where you specify which file holds the
 MAXSAT problem you want to use.





pop_size - This argument specifies how large you want the breeding pool to be


	through every iteration.





selection - This is the kind of selection you want to use. We have implemented 3:

            ts = tournament selection
   
         
	    rs = rank selection
 

            bs = Boltzmann 



selection

crossover - This argument specifies which type of crossover to perform. 

		     We have 2:


            1c = 1-point crossover

            uc = uniform crossover





pC- This is the probability that crossover is performed (double)





pM- This is the probability that the algorithm performs mutation (double)





generations - The number of generations it should iterate through (int)





algorithm - this argument specifies whether the program should use GA or PBIL



            g = Genetic algorithm
            

	    p = Population Based Incremental Learning







After giving the arguments that are needed for the program to run, it will use


conditional statements to decide how it should implement the GA or PBIL algorithm



Both of the algorithm create a population object, which is an array of individuals


whose assignments are randomized.  Every individual is an object that holds a


string that is the assignments.  Genetic algorithm, with the arguments from 
the 

command line, performs the given selection and crossover methods.  It then 
performs

a mutation on the new population at the end of every iteration.  It goes


through every symbol and given the pM it will change the assignment of the symbol.


It will repeat this process for the given number of generations. 

After breeding the 

last generation it will have coverged on some fit individual,
the file used, the 

number of variables, number of clauses, number of clauses satisfied,
 percentage of 

clauses satisfied, best assignment string, and in which iteration 
it was found.





The PBIL algorithm is similar, except it doesn't perform selection or crossover.


Instead it initatiates a probability vector (all the values are set to 0.5).  

And
 depending on the most fit and least fit individual, it will update the p vector


towards the best individual and away from the worst individual by updating their


corresponding probabilities in the vector.
