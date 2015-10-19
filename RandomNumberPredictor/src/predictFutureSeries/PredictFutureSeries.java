package predictFutureSeries;

import java.util.Random;
import java.util.Scanner;

public class PredictFutureSeries {
	
	public void inputTwoNumber(){
		Scanner s = new Scanner(System.in);
		System.out.println("Please Enter any Random number from the Valid Series");
		long rand1 = s.nextInt();
		System.out.println("Please Enter other random number occuring right after the above random number");
		long rand2 = s.nextInt();
		
		computeFutureSeries(rand1, rand2);
	}
	
	private void computeFutureSeries(long rand1,long rand2){
		long multiplier = 0x5DEECE66DL;
	    long addend = 0xBL;
	    long mask = (1L << 48) - 1;
		long seed=0;
		System.out.println("---------------------------------");
		System.out.println("Any 2 number From the Series");
		System.out.println("R1 :"+rand1+"\nR2 :"+rand2);
		System.out.println("---------------------------------");
			for (int i = 0; i < 65536; i++) {
				
				seed = (((long) rand1) << 16) + i;
			    if ((((seed * multiplier + addend) & mask) >>> 16) == rand2) {
			    	System.out.println("Series Found");
			    	System.out.println("Seed :"+seed);
			        break;
			    }
			}
		
		seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
		System.out.println("Seed 2:"+seed);
		Random r = new Random(seed);
		System.out.println("Next 10 numbers of the series");
		for(int i=0;i<10;i++){
			System.out.println(i+")    "+r.nextInt());
		}
	}

}
