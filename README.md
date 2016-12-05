# A Parallel Distributed Genetic Algorithm for Class Scheduling Problem and its performance analysis on MPICH, CUDA & Apache Spark

Genetic algorithm(GA) is an idea introduced in 1967, its core roots are from the Darwin’s theory of evolution. It is widely used in many fields relating to combinatorial optimization, machine learning, network routing, etc. We will design a parallel distributed genetic algorithm for Class scheduling problem , this problem is categorized as multi-constrained, NP-hard, combinatorial optimization problem . Our goal is to implement the designed algorithm for class scheduling in CUDA  ,MPICH [and Apache Spark  platforms to study on performance.


---
How to build the TTG - Apache Spark 

- Build
  - Download the repository,open TTG-Spark using Intellij or Eclipse using Existing project.
  - 
  - The `Reports.java` will run various test cases
  - `ParallelTimetableGA.java` is main class for parallel code.
  - SerialTimetableGA.java is main class for serial code.



### Parallel Genetic Algorithm 

![alt tag](https://github.com/ajayramesh23/ubiquitous-eureka/blob/master/algorithm.png?raw=true)


# Sample output 

# Time Table from Parallel Genetic Algorithm 
| Class | Module| Group  |  Room  | Professor | Time |
| ------------- | :-------------: | :-------------:  |  :-------------:  | :-------------: | -------------: |
|Class-1|Chemistry|1|SB-100|Dr P Smith Rez|Tue 9:00 -  11:00|
|Class-2|Maths-012|1|SB-100|Dr P Smith Rez|Wed 13:00 - 15:00|
|Class-3|Physics-1|1|SB-100|Dr R Williams|Thu 13:00 - 15:00|
|Class-4|English-1|2|RE-200|Dr P Smith Rez|Mon 11:00 - 13:00|
|Class-5|Maths-012|2|RE-200|Mrs E Mitchell|Wed 13:00 - 15:00|
|Class-6|History-1|2|RE-200|Mr A Thompson|Wed 11:00 - 13:00|
|Class-7|Drama-002|2|RE-200|Dr P Smith Rez|Tue 11:00 - 13:00|
|Class-8|Maths-012|3|SB-102|Mrs E Mitchell|Thu 11:00 - 13:00|
|Class-9|Physics-1|3|SB-101|Mr A Thompson|Wed 13:00 - 15:00|
|Class-10|History-1|3|SB-102|Mr A Thompson|Tue 9:00 -  11:00|
|Class-11|Chemistry|4|SB-102|Mrs E Mitchell|Thu 13:00 - 15:00|
|Class-12|Physics-1|4|SB-102|Dr R Williams|Fri 11:00 - 13:00|
|Class-13|English-1|5|SB-102|Dr R Williams|Fri 9:00 -  11:00|
|Class-14|Maths-012|5|SB-101|Mrs E Mitchell|Fri 13:00 - 15:00|
|Class-15|History-1|5|SB-101|Mr A Thompson|Tue 11:00 - 13:00|
|Class-16|Chemistry|6|SB-102|Dr P Smith Rez|Wed 9:00 -  11:00|
|Class-17|Physics-1|6|RE-200|Dr R Williams|Mon 13:00 - 15:00|
|Class-18|History-1|6|RE-200|Mr A Thompson|Thu 9:00 -  11:00|
|Class-19|Chemistry|7|SB-101|Mrs E Mitchell|Tue 13:00 - 15:00|
|Class-20|Maths-012|7|SB-101|Mrs E Mitchell|Wed 11:00 - 13:00|
|Class-21|English-1|8|SB-101|Dr P Smith Rez|Mon 13:00 - 15:00|
|Class-22|Drama-002|8|SB-101|Dr P Smith Rez|Fri 11:00 - 13:00|
|Class-23|Chemistry|9|RE-200|Mrs E Mitchell|Tue 9:00 -  11:00|
|Class-24|Drama-002|9|SB-102|Mr A Thompson|Mon 9:00 -  11:00|
|Class-25|Maths-012|10|SB-102|Dr P Smith Rez|Thu 9:00 -  11:00|
|Class-26|Physics-1|10|SB-102|Dr R Williams|Wed 13:00 - 15:00|

