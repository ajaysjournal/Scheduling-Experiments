package pdp.project.reports;

import pdp.project.parallel.ga.ttg.ParallelTimetableGA;
import serial.algo.design.scratch.SerialTimetableGA;

import static java.lang.System.exit;

/**
 * Created by Ajay on 11/30/16.
 */
public class Reports {
    public static void generateReports(){
        // test case 1 - algorithm version 1
        /*ParallelTimetableGA.parallelTimetableGA(1000, 1000,"testC-1-popSize-1000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(1500, 1000,"testC-1-popSize-1500-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(2000, 1000,"testC-1-popSize-2000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(2500, 1000,"testC-1-popSize-2500-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(3000, 1000,"testC-1-popSize-3000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(4000, 1000,"testC-1-popSize-4000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(5000, 1000,"testC-1-popSize-5000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(6000, 1000,"testC-1-popSize-6000-iteration-1000-np-50");
        ParallelTimetableGA.parallelTimetableGA(7000, 1000,"testC-1-popSize-7000-iteration-1000-np-50");

        SerialTimetableGA.serialTimetableGA(1000, 1000,"serial-1000iteration-1000");
        SerialTimetableGA.serialTimetableGA(1500, 1000,"serial-1500iteration-1000");
        SerialTimetableGA.serialTimetableGA(2000, 1000,"serial-2000iteration-1000");
        SerialTimetableGA.serialTimetableGA(2500, 1000,"serial-2500iteration-1000");
        SerialTimetableGA.serialTimetableGA(3000, 1000,"serial-3000iteration-1000");
        SerialTimetableGA.serialTimetableGA(4000, 1000,"serial-4000iteration-1000");
        SerialTimetableGA.serialTimetableGA(5000, 1000,"serial-5000iteration-1000");
        SerialTimetableGA.serialTimetableGA(6000, 1000,"serial-6000iteration-1000");
        SerialTimetableGA.serialTimetableGA(7000, 1000,"serial-7000iteration-1000");
*/


        // test case 2- algorithm version 1
        ParallelTimetableGA.parallelTimetableGA(1000, 1000,"testC-2-popSize-1000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(1500, 1000,"testC-2-popSize-1500-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(2000, 1000,"testC-2-popSize-2000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(2500, 1000,"testC-2-popSize-2500-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(3000, 1000,"testC-2-popSize-3000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(4000, 1000,"testC-2-popSize-4000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(5000, 1000,"testC-2-popSize-5000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(6000, 1000,"testC-2-popSize-6000-iteration-1000-np-*");
        ParallelTimetableGA.parallelTimetableGA(7000, 1000,"testC-2-popSize-7000-iteration-1000-np-*");

        SerialTimetableGA.serialTimetableGA(1000, 1000,"serial-1000iteration-1000");
        SerialTimetableGA.serialTimetableGA(1500, 1000,"serial-1500iteration-1000");
        SerialTimetableGA.serialTimetableGA(2000, 1000,"serial-2000iteration-1000");
        SerialTimetableGA.serialTimetableGA(2500, 1000,"serial-2500iteration-1000");
        SerialTimetableGA.serialTimetableGA(3000, 1000,"serial-3000iteration-1000");
        SerialTimetableGA.serialTimetableGA(4000, 1000,"serial-4000iteration-1000");
        SerialTimetableGA.serialTimetableGA(5000, 1000,"serial-5000iteration-1000");
        SerialTimetableGA.serialTimetableGA(6000, 1000,"serial-6000iteration-1000");
        SerialTimetableGA.serialTimetableGA(7000, 1000,"serial-7000iteration-1000");


        exit(0);


    }

    public static void main(String[] args) {
        Reports.generateReports();
    }
}
