package algo.design.scratch;

import mpi.MPI;

public class GeneticAlgorithm {

	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
			int tournamentSize) {

		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;
	}

	/**
	 * Initialize population
	 * 
	 * @param chromosomeLength
	 *            The length of the individuals chromosome
	 * @return population The initial population generated
	 */
	public Population initPopulation(Timetable timetable) {
		// Initialize population
		Population population = new Population(this.populationSize, timetable);
		return population;
	}

	/**
	 * Check if population has met termination condition
	 * 
	 * @param generationsCount
	 *            Number of generations passed
	 * @param maxGenerations
	 *            Number of generations to terminate after
	 * @return boolean True if termination condition met, otherwise, false
	 */
	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	/**
	 * Check if population has met termination condition
	 *
	 * @param population
	 * @return boolean True if termination condition met, otherwise, false
	 */
	public boolean isTerminationConditionMet(Population population) {
		return population.getFittest(0).getFitness() == 1.0;
	}

	/**
	 * Calculate individual's fitness value
	 * 
	 * @param individual
	 * @param timetable
	 * @return fitness
	 */
	public double calcFitness(Individual individual, Timetable timetable) {

		// Create new timetable object to use -- cloned from an existing timetable
		Timetable threadTimetable = new Timetable(timetable);
		threadTimetable.createClasses(individual);

		// Calculate fitness
		int clashes = threadTimetable.calcClashes();
		double fitness = 1 / (double) (clashes + 1);

		individual.setFitness(fitness);

		return fitness;
	}

	/**
	 * Evaluate population
	 * 
	 * @param population
	 * @param timetable
	 */
	public void evalPopulation(Population population, Timetable timetable,String[] args) {
		double populationFitness = 0;

		// Loop over population evaluating individuals and summing population
		// fitness
		// MPI Barrier()
		// Scatter Individual 
		// Gather Individual
		// If Rank == 0  calcluate the sum
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int num_processes = MPI.COMM_WORLD.Size();
		int popSize = population.size();
		int chunkSize = popSize/(num_processes);				
		double popFitnessCopy[] = new double[popSize]; 
		int stIndex = rank*popSize/num_processes ;
		int endIndex = stIndex + popSize/num_processes;
		double [] partialArray = new double [chunkSize];
		double [] global_sum = new double [num_processes];		
		int i=0;
		System.out.println("..."+popSize);
		for (Individual individual : population.getIndividuals()) {
			popFitnessCopy[i] = this.calcFitness(individual, timetable);
			i=i+1;
		}
		MPI.COMM_WORLD.Barrier();	
		for (int k=0 ; k<popSize;k++) {
			System.out.println(popFitnessCopy[k]);
		}
		MPI.COMM_WORLD.Scatter(popFitnessCopy, stIndex, chunkSize, MPI.DOUBLE, partialArray ,endIndex , chunkSize, MPI.DOUBLE, 0);
		double local_sum[] = new double[1];
		local_sum[0]=calcFunction2(partialArray);
		
		MPI.COMM_WORLD.Gather(local_sum, 0, 1, MPI.DOUBLE, global_sum, rank, 1,	MPI.DOUBLE,0);
	
		if(rank==0){
			System.out.println("The total sum is: "+calcFunction2(global_sum));
			
		}
		MPI.COMM_WORLD.Barrier();
		MPI.Finalize();	
	/*	populationFitness =0.0;
		for (Individual individual : population.getIndividuals()) {
			//System.out.print("COPY");
			populationFitness += this.calcFitness(individual, timetable);
			
			//System.out.println(populationFitness += this.calcFitness(individual, timetable));
		
		}
		System.out.println("populationFitness"+populationFitness);
		System.exit(0);
	*/	
		population.setPopulationFitness(populationFitness);
	}
	
	/**
	 * Selects parent for crossover using tournament selection
	 * 
	 * Tournament selection works by choosing N random individuals, and then
	 * choosing the best of those.
	 * 
	 * @param population
	 * @return The individual selected as a parent
	 */
	public Individual selectParent(Population population) {
		// Create tournament
		Population tournament = new Population(this.tournamentSize);

		// Add random individuals to the tournament
		population.shuffle();
		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}

		// Return the best
		return tournament.getFittest(0);
	}


	/**
     * Apply mutation to population
     * 
     * @param population
     * @param timetable
     * @return The mutated population
     */
	public Population mutatePopulation(Population population, Timetable timetable) {
		// Initialize new population
		Population newPopulation = new Population(this.populationSize);

		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);

			// Create random individual to swap genes with
			Individual randomIndividual = new Individual(timetable);

			// Loop over individual's genes
			for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
				// Skip mutation if this is an elite individual
				if (populationIndex > this.elitismCount) {
					// Does this gene need mutation?
					if (this.mutationRate > Math.random()) {
						// Swap for new gene
						individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
					}
				}
			}

			// Add individual to population
			newPopulation.setIndividual(populationIndex, individual);
		}

		// Return mutated population
		return newPopulation;
	}

    /**
     * Apply crossover to population
     * 
     * @param population The population to apply crossover to
     * @return The new population
     */
	public Population crossoverPopulation(Population population) {
		// Create new population
		Population newPopulation = new Population(population.size());

		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual parent1 = population.getFittest(populationIndex);

			// Apply crossover to this individual?
			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
				// Initialize offspring
				Individual offspring = new Individual(parent1.getChromosomeLength());
				
				// Find second parent
				Individual parent2 = selectParent(population);

				// Loop over genome
				for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
					// Use half of parent1's genes and half of parent2's genes
					if (0.5 > Math.random()) {
						offspring.setGene(geneIndex, parent1.getGene(geneIndex));
					} else {
						offspring.setGene(geneIndex, parent2.getGene(geneIndex));
					}
				}

				// Add offspring to new population
				newPopulation.setIndividual(populationIndex, offspring);
			} else {
				// Add individual to new population without applying crossover
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}

		return newPopulation;
	}

	public static double calcFunction2(double[] pSum){
		double sum=0;
		for(int i=0;i<pSum.length;i++){			
			sum+=pSum[i];
		}
		return sum;

	}

}
