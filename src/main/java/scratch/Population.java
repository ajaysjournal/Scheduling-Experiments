package scratch;

import java.io.Serializable;
import java.util.*;

public class Population implements Serializable{
	private Individual population2[];
	private List<Individual> population;


	private double populationFitness = -1;


	public List<Individual> getPopulation() {
		return population;
	}

	public void setPopulation(List<Individual> population) {
		this.population = population;
	}


	/**
	 * Initializes blank population of individuals
	 * 
	 * @param populationSize
	 *            The size of the population
	 */
	public Population(int populationSize) {
		// Initial population
		this.population = new ArrayList<Individual>(populationSize);
		for(int i =0;i<populationSize;i++){
			this.population.add(new Individual());
		}
		//this.population = new Individual[populationSize];
	}
	
	/**
     * Initializes population of individuals
     * 
     * @param populationSize The size of the population
     * @param timetable The timetable information
     */
	public Population(int populationSize, Timetable timetable) {
		// Initial population
		//this.population = new Individual[populationSize];
		this.population = new ArrayList<Individual>(populationSize);
		for(int i =0;i<populationSize;i++){
			this.population.add(new Individual());
		}
/*
		// todo - paralize the population at the end
		JavaRDD<Individual> dataSet = SparkUtil.getSparkContext().parallelize(this.population);

		List<Individual> tempPop =  dataSet.filter(p -> {
			return this.population.add(new Individual(timetable));
		 }).collect();*/

		// Loop over population size
		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			// Create individual
			Individual individual = new Individual(timetable);
			// Add individual to population
			this.population.set(individualCount,individual) ;
			//this.population[individualCount] = individual;
		}
	}


	/**
	 * Initializes population of individuals
	 * 
	 * @param populationSize
	 *            The size of the population
	 * @param chromosomeLength
	 *            The length of the individuals chromosome
	 */
	public Population(int populationSize, int chromosomeLength) {
		// Initial population
		//this.population = new Individual[populationSize];
		this.population = new ArrayList<Individual>(populationSize);
		for(int i =0;i<populationSize;i++){
			this.population.add(new Individual());
		}

		// Loop over population size
		for (int individualCount = 0; individualCount < populationSize; individualCount++) {
			// Create individual
			Individual individual = new Individual(chromosomeLength);
			// Add individual to population
			this.population.set(individualCount,individual) ;
			//this.population[individualCount] = individual;
		}
	}

	/**
	 * Get individuals from the population
	 * 
	 * @return individuals Individuals in population
	 */
	public List<Individual> getIndividuals() {
		return this.population;
	}

	/**
	 * Find fittest individual in the population
	 * 
	 * @param offset
	 * @return individual Fittest individual at offset
	 */
	public Individual getFittest(int offset) {
		System.out.print("offset"+offset);
		// todo : This must be Parallalized
		TimetableGA.countFittestCall++;
		/*JavaRDD<Individual > populationJavaRDD = SparkUtil.getSparkContext().parallelize(this.population);
		Individual fitestIndividual = populationJavaRDD.map( i -> {
			return i;
		}).reduce((i1, i2) -> {return i1.getFitness()>i2.getFitness()?i1:i2;} );

		//System.out.print("fitestIndividual"+fitestIndividual.toString());
		return fitestIndividual;*/

		//Collections.sort(this.population, (o1, o2) -> ( if(o1.getFitness > o2.getFitnes())) return
		// Order population by fitness
		Collections.sort(new ArrayList(this.population), new Comparator<Individual>() {

			public int compare(Individual o1, Individual o2) {

				if (o1.getFitness() > o2.getFitness()) {
					return -1;
				} else if (o1.getFitness() < o2.getFitness()) {
					return 1;
				}
				return 0;
			}
		});


		return this.population.get(offset);



		// Return the fittest individual

	}

	/**
	 * Set population's fitness
	 * 
	 * @param fitness
	 *            The population's total fitness
	 */
	public void setPopulationFitness(double fitness) {
		this.populationFitness = fitness;
	}

	/**
	 * Get population's fitness
	 * 
	 * @return populationFitness The population's total fitness
	 */
	public double getPopulationFitness() {
		return this.populationFitness;
	}

	/**
	 * Get population's size
	 * 
	 * @return size The population's size
	 */
	public int size() {
		return this.population.size();
	}

	/**
	 * Set individual at offset
	 * 
	 * @param individual
	 * @param offset
	 * @return individual
	 */
	public Individual setIndividual(int offset, Individual individual) {
		// return population[offset] = individual;
		return population.set(offset,individual);
	}

	/**
	 * Get individual at offset
	 * 
	 * @param offset
	 * @return individual
	 */
	public Individual getIndividual(int offset) {
		return population.get(offset);
	}

	/**
	 * Shuffles the population in-place
	 *
	 * @return void
	 */
	public void shuffle() {

		//Collections.shuffle(population,new Random(System.nanoTime()));


		/*Random rnd = new Random();
		for (int i = population.size() - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Individual a = population.get(index);
			population.set(index,population.get(i)) ;
			population.set(i,a);
		}*/
	}

}