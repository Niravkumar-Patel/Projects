package main;

import java.util.Random;
import predictFutureSeries.PredictFutureSeries;
import predictPastSeries.PredictPastSeries;
import randomImplementation.ManualJavaRandom;

public class Main {
		
	public static void main(String[] args) {
		
		System.out.println("Java Generated Series\t\t\t\tManual Computed Series");
		System.out.println("-----------------------------------------------------------------------------");
		Random r = new Random(46L);
		ManualJavaRandom mjr = new ManualJavaRandom(46L);		
		
		for(int i=0;i<10;i++){
			System.out.println("\t"+r.nextInt()+"\t\t\t||\t\t"+mjr.generateNextInt());
		}

		System.out.println("-----------------------------------------------------------------------------");
		
		PredictFutureSeries pfs = new PredictFutureSeries();
		pfs.inputTwoNumber();
		
		PredictPastSeries pps = new PredictPastSeries();
		pps.inputTwoNumber();
				
	}
}
