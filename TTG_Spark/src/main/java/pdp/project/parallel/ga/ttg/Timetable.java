package pdp.project.parallel.ga.ttg;

import all.util.SparkUtil;
import org.apache.spark.api.java.JavaRDD;
import pdp.project.parallel.ga.ttg.ttg.spark.rdd_datasets.ClassRDD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Timetable is the main evaluation class for the class scheduler GA.
 * 
 * A timetable represents a potential solution in human-readable form, unlike an
 * Individual or a chromosome. This timetable class, then, can read a chromosome
 * and develop a timetable from it, and ultimately can evaluate the timetable
 * for its fitness and number of scheduling clashes.
 * 
 * The most important methods in this class are createClasses and calcClashes.
 * 
 * The createClasses method accepts an Individual (really, a chromosome),
 * unpacks its chromosome, and creates ClassRoom objects from the genetic
 * information. ClassRoom objects are lightweight; they're just containers for
 * information with getters and setters, but it's more convenient to work with
 * them than with the chromosome directly.
 * 
 * The calcClashes method is used by GeneticAlgorithm.calcFitness, and requires
 * that createClasses has been run first. calcClashes looks at the ClassRoom objects
 * created by createClasses, and figures out how many hard constraints have been
 * violated.
 * 
 */
public class Timetable implements Serializable{
	private final HashMap<Integer, Room> rooms;
	private final HashMap<Integer, Professor> professors;
	private final HashMap<Integer, Module> modules;
	private final HashMap<Integer, Group> groups;
	private final HashMap<Integer, Timeslot> timeslots;
	private ClassRoom classRooms[];

	public  JavaRDD<ClassRoom> classesRDD ;
	public static boolean setParallel = false;

	public JavaRDD<ClassRoom> setParallel(){
        Timetable.setParallel = true;

        return SparkUtil.getSparkContext().parallelize(new ArrayList<ClassRoom>(Arrays.asList(classRooms)));


    }

	private int numClasses = 0;

	/**
	 * Initialize new Timetable
	 */
	public Timetable() {
		this.rooms = new HashMap<Integer, Room>();
		this.professors = new HashMap<Integer, Professor>();
		this.modules = new HashMap<Integer, Module>();
		this.groups = new HashMap<Integer, Group>();
		this.timeslots = new HashMap<Integer, Timeslot>();
	}

	/**
	 * "Clone" a timetable. We use this before evaluating a timetable so we have
	 * a unique container for each set of classRooms created by "createClasses".
	 * Truthfully, that's not entirely necessary (no big deal if we wipe out and
	 * reuse the .classRooms property here), but Chapter 6 discusses
	 * multi-threading for fitness calculations, and in order to do that we need
	 * separate objects so that one thread doesn't step on another thread's
	 * toes. So this constructor isn't _entirely_ necessary for Chapter 5, but
	 * you'll see it in action in Chapter 6.
	 * 
	 * @param cloneable
	 */
	public Timetable(Timetable cloneable) {
		this.rooms = cloneable.getRooms();
		this.professors = cloneable.getProfessors();
		this.modules = cloneable.getModules();
		this.groups = cloneable.getGroups();
		this.timeslots = cloneable.getTimeslots();
	}

	private HashMap<Integer, Group> getGroups() {
		return this.groups;
	}

	private HashMap<Integer, Timeslot> getTimeslots() {
		return this.timeslots;
	}

	private HashMap<Integer, Module> getModules() {
		return this.modules;
	}

	private HashMap<Integer, Professor> getProfessors() {
		return this.professors;
	}

	/**
	 * Add new room
	 * 
	 * @param roomId
	 * @param roomName
	 * @param capacity
	 */
	public void addRoom(int roomId, String roomName, int capacity) {
		this.rooms.put(roomId, new Room(roomId, roomName, capacity));
	}

	/**
	 * Add new professor
	 * 
	 * @param professorId
	 * @param professorName
	 */
	public void addProfessor(int professorId, String professorName) {
		this.professors.put(professorId, new Professor(professorId, professorName));
	}

	/**
	 * Add new module
	 * 
	 * @param moduleId
	 * @param moduleCode
	 * @param module
	 * @param professorIds
	 */
	public void addModule(int moduleId, String moduleCode, String module, int professorIds[]) {
		this.modules.put(moduleId, new Module(moduleId, moduleCode, module, professorIds));
	}

	/**
	 * Add new group
	 * 
	 * @param groupId
	 * @param groupSize
	 * @param moduleIds
	 */
	public void addGroup(int groupId, int groupSize, int moduleIds[]) {
		this.groups.put(groupId, new Group(groupId, groupSize, moduleIds));
		this.numClasses = 0;
	}

	/**
	 * Add new timeslot
	 * 
	 * @param timeslotId
	 * @param timeslot
	 */
	public void addTimeslot(int timeslotId, String timeslot) {
		this.timeslots.put(timeslotId, new Timeslot(timeslotId, timeslot));
	}

	/**
	 * Create classRooms using individual's chromosome
	 * 
	 * One of the two important methods in this class; given a chromosome,
	 * unpack it and turn it into an array of ClassRoom (with a capital C) objects.
	 * These ClassRoom objects will later be evaluated by the calcClashes method,
	 * which will loop through the Classes and calculate the number of
	 * conflicting timeslots, rooms, professors, etc.
	 * 
	 * While this method is important, it's not really difficult or confusing.
	 * Just loop through the chromosome and create ClassRoom objects and store them.
	 * 
	 * @param individual
	 */
	public void createClasses(Individual individual) {
		// Init classRooms
		ClassRoom classRooms[] = new ClassRoom[this.getNumClasses()];

		// Get individual's chromosome
		int chromosome[] = individual.getChromosome();
		int chromosomePos = 0;
		int classIndex = 0;

		for (Group group : this.getGroupsAsArray()) {
			int moduleIds[] = group.getModuleIds();
			for (int moduleId : moduleIds) {
				classRooms[classIndex] = new ClassRoom(classIndex, group.getGroupId(), moduleId);

				// Add timeslot
				classRooms[classIndex].addTimeslot(chromosome[chromosomePos]);
				chromosomePos++;

				// Add room
				classRooms[classIndex].setRoomId(chromosome[chromosomePos]);
				chromosomePos++;

				// Add professor
				classRooms[classIndex].addProfessor(chromosome[chromosomePos]);
				chromosomePos++;

				classIndex++;
			}
		}

		this.classRooms = classRooms;
	}

	/**
	 * Get room from roomId
	 * 
	 * @param roomId
	 * @return room
	 */
	public Room getRoom(int roomId) {
		if (!this.rooms.containsKey(roomId)) {
			System.out.println("Rooms doesn't contain key " + roomId);
		}
		return (Room) this.rooms.get(roomId);
	}

	public HashMap<Integer, Room> getRooms() {
		return this.rooms;
	}

	/**
	 * Get random room
	 * 
	 * @return room
	 */
	public Room getRandomRoom() {
		Object[] roomsArray = this.rooms.values().toArray();
		Room room = (Room) roomsArray[(int) (roomsArray.length * Math.random())];
		return room;
	}

	/**
	 * Get professor from professorId
	 * 
	 * @param professorId
	 * @return professor
	 */
	public Professor getProfessor(int professorId) {
		return (Professor) this.professors.get(professorId);
	}

	/**
	 * Get module from moduleId
	 * 
	 * @param moduleId
	 * @return module
	 */
	public Module getModule(int moduleId) {
		return (Module) this.modules.get(moduleId);
	}

	/**
	 * Get moduleIds of student group
	 * 
	 * @param groupId
	 * @return moduleId array
	 */
	public int[] getGroupModules(int groupId) {
		Group group = (Group) this.groups.get(groupId);
		return group.getModuleIds();
	}

	/**
	 * Get group from groupId
	 * 
	 * @param groupId
	 * @return group
	 */
	public Group getGroup(int groupId) {
		return (Group) this.groups.get(groupId);
	}

	/**
	 * Get all student groups
	 * 
	 * @return array of groups
	 */
	public Group[] getGroupsAsArray() {
		return (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
	}

	/**
	 * Get timeslot by timeslotId
	 * 
	 * @param timeslotId
	 * @return timeslot
	 */
	public Timeslot getTimeslot(int timeslotId) {
		return (Timeslot) this.timeslots.get(timeslotId);
	}

	/**
	 * Get random timeslotId
	 * 
	 * @return timeslot
	 */
	public Timeslot getRandomTimeslot() {
		Object[] timeslotArray = this.timeslots.values().toArray();
		Timeslot timeslot = (Timeslot) timeslotArray[(int) (timeslotArray.length * Math.random())];
		return timeslot;
	}

	/**
	 * Get classRooms
	 * 
	 * @return classRooms
	 */
	public ClassRoom[] getClassRooms() {
		return this.classRooms;
	}

	/**
	 * Get number of classRooms that need scheduling
	 * 
	 * @return numClasses
	 */
	public int getNumClasses() {
		if (this.numClasses > 0) {
			return this.numClasses;
		}

		int numClasses = 0;
		Group groups[] = (Group[]) this.groups.values().toArray(new Group[this.groups.size()]);
		for (Group group : groups) {
			numClasses += group.getModuleIds().length;
		}
		this.numClasses = numClasses;

		return this.numClasses;
	}

	/**
	 * Calculate the number of clashes between Classes generated by a
	 * chromosome.
	 * 
	 * The most important method in this class; look at a candidate timetable
	 * and figure out how many constraints are violated.
	 * 
	 * Running this method requires that createClasses has been run first (in
	 * order to populate this.classRooms). The return value of this method is
	 * simply the number of constraint violations (conflicting professors,
	 * timeslots, or rooms), and that return value is used by the
	 * GeneticAlgorithm.calcFitness method.
	 * 
	 * There's nothing too difficult here either -- loop through this.classRooms,
	 * and check constraints against the rest of the this.classRooms.
	 * 
	 * The two inner `for` loops can be combined here as an optimization, but
	 * kept separate for clarity. For small values of this.classRooms.length it
	 * doesn't make a difference, but for larger values it certainly does.
	 * 
	 * @return numClashes
	 */
	public int calcClashes() {
		int clashes = 0;

		int count = 0;
		if (ClassRDD.getParallelRDD() != null) {
			count = ClassRDD.getParallelRDD().map(c -> {
				int roomCapacity = this.getRoom(c.getRoomId()).getRoomCapacity();
				int groupSize = this.getGroup(c.getGroupId()).getGroupSize();
				return roomCapacity < groupSize ? 1 : 0;
			}).reduce((c1, c2) -> c1 + c2);


			count += ClassRDD.getParallelRDD().map(c -> {
				for (ClassRoom classB: ClassRDD.getBroadcastClassRDDVar()) {
					if (c.getRoomId() == classB.getRoomId() && c.getTimeslotId() == classB.getTimeslotId()
							&& c.getClassId() != classB.getClassId()) {
						return 1;
					}
				}
				return 0;
			}).reduce((c1, c2) -> c1 + c2);

			count += ClassRDD.getParallelRDD().map(c -> {
				for (ClassRoom classB: ClassRDD.getBroadcastClassRDDVar()) {
					if (c.getProfessorId() == classB.getProfessorId() && c.getTimeslotId() == classB.getTimeslotId()
							&& c.getClassId() != classB.getClassId()) {
						return 1;
					}
				}
				return 0;
			}).reduce((c1, c2) -> c1 + c2);
		}
        /*
        Too much of prallaziation . Not good

       		int count = 0;
            if (ClassRDD.getParallelRDD() != null) {
                 count = ClassRDD.getParallelRDD().map(c -> {
                    int roomCapacity = this.getRoom(c.getRoomId()).getRoomCapacity();
                    int groupSize = this.getGroup(c.getGroupId()).getGroupSize();
                    return roomCapacity < groupSize ? 1 : 0;
                }).reduce((c1, c2) -> c1 + c2);


                 count += ClassRDD.getParallelRDD().map(c -> {
                    for (ClassRoom classB: ClassRDD.getBroadcastClassRDDVar()) {
                        if (c.getRoomId() == classB.getRoomId() && c.getTimeslotId() == classB.getTimeslotId()
                                && c.getClassId() != classB.getClassId()) {
                            return 1;
                        }
                    }
                    return 0;
                }).reduce((c1, c2) -> c1 + c2);

                count += ClassRDD.getParallelRDD().map(c -> {
                    for (ClassRoom classB: ClassRDD.getBroadcastClassRDDVar()) {
                        if (c.getProfessorId() == classB.getProfessorId() && c.getTimeslotId() == classB.getTimeslotId()
                                && c.getClassId() != classB.getClassId()) {
                            return 1;
                        }
                    }
                    return 0;
                }).reduce((c1, c2) -> c1 + c2);
            }

        */




        for (ClassRoom classRoomA : this.classRooms) {
            // Check room capacity
            int roomCapacity = this.getRoom(classRoomA.getRoomId()).getRoomCapacity();
            int groupSize = this.getGroup(classRoomA.getGroupId()).getGroupSize();

            if (roomCapacity < groupSize) {
                clashes++;
            }

            // Check if room is taken
            for (ClassRoom classRoomB : this.classRooms) {
                if (classRoomA.getRoomId() == classRoomB.getRoomId() && classRoomA.getTimeslotId() == classRoomB.getTimeslotId()
                        && classRoomA.getClassId() != classRoomB.getClassId()) {
                    clashes++;
                    break;
                }
            }

            // Check if professor is available
            for (ClassRoom classRoomB : this.classRooms) {
                if (classRoomA.getProfessorId() == classRoomB.getProfessorId() && classRoomA.getTimeslotId() == classRoomB.getTimeslotId()
                        && classRoomA.getClassId() != classRoomB.getClassId()) {
                    clashes++;
                    break;
                }
            }
        }
	    return clashes;
	}


	public int calcClashes2() {

		JavaRDD<ClassRoom> classesRDD =  SparkUtil.getSparkContext().parallelize(new ArrayList<ClassRoom>(Arrays.asList(classRooms)));
		int clashes = 0;

        int count = classesRDD.map(c -> {
            int roomCapacity = this.getRoom(c.getRoomId()).getRoomCapacity();
            int groupSize = this.getGroup(c.getGroupId()).getGroupSize();
            return roomCapacity  < groupSize ? 1 : 0;
        }).reduce((c1,c2) -> c1 + c2);


        System.out.print("Count"+count);


        /*Persons = dataSet.filter(individual -> {
            if(individual.getName().contains("smart"))
                return true;
            else return false;
        }).collect();
        System.out.println("Smart count "+nPersons.size());
*/




		return clashes;
	}
}