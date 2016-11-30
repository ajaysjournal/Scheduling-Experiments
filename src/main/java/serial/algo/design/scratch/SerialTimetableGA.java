 package serial.algo.design.scratch;

 import all.util.Files;

 import java.util.Date;

 /**
 * Don't be daunted by the number of classes in this chapter -- most of them are
 * just simple containers for information, and only have a handful of properties
 * with setters and getters.
 * 
 * The real stuff happens in the GeneticAlgorithm class and the Timetable class.
 * 
 * The Timetable class is what the genetic algorithm is expected to create a
 * valid version of -- meaning, after all is said and done, a chromosome is read
 * into a Timetable class, and the Timetable class creates a nicer, neater
 * representation of the chromosome by turning it into a proper list of Classes
 * with rooms and professors and whatnot.
 * 
 * The Timetable class also understands the problem's Hard Constraints (ie, a
 * professor can't be in two places simultaneously, or a room can't be used by
 * two classes simultaneously), and so is used by the GeneticAlgorithm's
 * calcFitness class as well.
 * 
 * Finally, we overload the Timetable class by entrusting it with the
 * "database information" generated here in initializeTimetable. Normally, that
 * information about what professors are employed and which classrooms the
 * university has would come from a database, but this isn't a book about
 * databases so we hardcode it.
 * 
 * @author bkanber
 *
 */
public class SerialTimetableGA {

	 public static void main(String[] args) {
		 SerialTimetableGA.serialTimetableGA(100,1000,"test1");
		 SerialTimetableGA.serialTimetableGA(1000,1000,"test2");
		 SerialTimetableGA.serialTimetableGA(5000,1000,"test3");
		 // SerialTimetableGA.serialTimetableGA(10000,1000,"test4"); // 12 min it will take approx
	 }

    public static void serialTimetableGA(int size,int maxIteration,String filename) {

		StringBuilder output=new StringBuilder();
		output.append("# Time Table from Serial Genetic Algorithm \n");
		StringBuilder stats=new StringBuilder();

    	// Get a Timetable object with all the available information.
        Timetable timetable = initializeTimetable();  // Hard Coded Initialization
        
        // Initialize GA
		Date st_time = new Date();
		Date gSt = new  Date();
        GeneticAlgorithm ga = new GeneticAlgorithm(size, 0.01, 0.9, 2, 5);


		// Initialize population
        Population population = ga.initPopulation(timetable);


        // Evaluate population
        ga.evalPopulation(population, timetable);
        
        // Keep track of current generation
        int generation = 1;
        
        // Start evolution loop
		stats.append("--- \n   ### Program Statistics \n|Generations | Time Took(ms) |\n");
		stats.append("| ------------- | :-----------: |\n");
		Date gEn=new Date();
		while (ga.isTerminationConditionMet(generation, maxIteration) == false
            && ga.isTerminationConditionMet(population) == false) {
            // Print fitness
			gEn  = new Date();
			long dif = gEn.getTime()-gSt.getTime();
			stats.append("| Generation " + generation + " Best fitness: " + population.getFittest(0).getFitness()+"|"+dif+"|\n");
			gSt=new Date();
            // Apply crossover
            population = ga.crossoverPopulation(population);

            // Apply mutation
            population = ga.mutatePopulation(population, timetable);

            // Evaluate population
            ga.evalPopulation(population, timetable);

            // Increment the current generation
            generation++;
        }

        // Print fitness
        timetable.createClasses(population.getFittest(0));

		Date end_time = new Date();


		stats.append("\n- Time Spent "+ (end_time.getTime() - st_time.getTime())+" ms \n");
		stats.append("- Final solution fitness: " + population.getFittest(0).getFitness()+"\n");
		stats.append("- Clashes: " + timetable.calcClashes()+"\n");
		output.append("| Class | Module| Group  |  Room  | Professor | Time |\n");
		output.append("| ------------- | :-------------: | :-------------:  |  :-------------:  | :-------------: | -------------: |\n");
		  // Print classes
        Class classes[] = timetable.getClasses();
        int classIndex = 1;
        for (Class bestClass : classes) {
            output.append("|Class-" + classIndex + "");
            output.append("|" + timetable.getModule(bestClass.getModuleId()).getModuleName());
            output.append("|" + timetable.getGroup(bestClass.getGroupId()).getGroupId());
            output.append("|" + timetable.getRoom(bestClass.getRoomId()).getRoomNumber());
            output.append("|" +timetable.getProfessor(bestClass.getProfessorId()).getProfessorName());
            output.append("|" + timetable.getTimeslot(bestClass.getTimeslotId()).getTimeslot()+"|");
            output.append("\n");
            classIndex++;
        }
		output.append(stats);
		Files.createFile("results/report/"+filename+"output.md",output.toString());
		//Files.createFile("output"+Math.random()*100,output.toString());

    }

    /**
     * Creates a Timetable with all the necessary course information.
     * 
     * Normally you'd get this info from a database.
     * 
     * @return
     */
	private static Timetable initializeTimetable() {
		// Create timetable
		Timetable timetable = new Timetable();

		// Set up rooms
		timetable.addRoom(1, "SB-100", 15);
		timetable.addRoom(2, "RE-200", 30);
		timetable.addRoom(4, "SB-101", 20);
		timetable.addRoom(5, "SB-102", 25);

		// Set up timeslots
		timetable.addTimeslot(1, 	"Mon 9:00 -  11:00");
		timetable.addTimeslot(2, 	"Mon 11:00 - 13:00");
		timetable.addTimeslot(3, 	"Mon 13:00 - 15:00");
		timetable.addTimeslot(4, 	"Tue 9:00 -  11:00");
		timetable.addTimeslot(5, 	"Tue 11:00 - 13:00");
		timetable.addTimeslot(6, 	"Tue 13:00 - 15:00");
		timetable.addTimeslot(7, 	"Wed 9:00 -  11:00");
		timetable.addTimeslot(8, 	"Wed 11:00 - 13:00");
		timetable.addTimeslot(9, 	"Wed 13:00 - 15:00");
		timetable.addTimeslot(10,	"Thu 9:00 -  11:00");
		timetable.addTimeslot(11, "Thu 11:00 - 13:00");
		timetable.addTimeslot(12, "Thu 13:00 - 15:00");
		timetable.addTimeslot(13, "Fri 9:00 -  11:00");
		timetable.addTimeslot(14, "Fri 11:00 - 13:00");
		timetable.addTimeslot(15, "Fri 13:00 - 15:00");

		// Set up professors
		timetable.addProfessor(1, "Dr P Smith Rez");
		timetable.addProfessor(2, "Mrs E Mitchell");
		timetable.addProfessor(3, "Dr R Williams");
		timetable.addProfessor(4, "Mr A Thompson");
        
		// Set up modules and define the professors that teach them
		timetable.addModule(1, "CH100", "Chemistry", new int[] { 1, 2 });
		timetable.addModule(2, "EN100", "English-1", new int[] { 1, 3 });
		timetable.addModule(3, "MA101", "Maths-012", new int[] { 1, 2 });
		timetable.addModule(4, "PY101", "Physics-1", new int[] { 3, 4 });
		timetable.addModule(5, "HT101", "History-1", new int[] { 4 });
		timetable.addModule(6, "DR100", "Drama-002", new int[] { 1, 4 });

		// Set up student groups and the modules they take.
		timetable.addGroup(1, 10, new int[] { 1, 3, 4 });
		timetable.addGroup(2, 30, new int[] { 2, 3, 5, 6 });
		timetable.addGroup(3, 18, new int[] { 3, 4, 5 });
		timetable.addGroup(4, 25, new int[] { 1, 4 });
		timetable.addGroup(5, 20, new int[] { 2, 3, 5 });
		timetable.addGroup(6, 22, new int[] { 1, 4, 5 });
		timetable.addGroup(7, 16, new int[] { 1, 3 });
		timetable.addGroup(8, 18, new int[] { 2, 6 });
		timetable.addGroup(9, 24, new int[] { 1, 6 });
		timetable.addGroup(10, 25,new int[] { 3, 4 });
		return timetable;
	}
}
