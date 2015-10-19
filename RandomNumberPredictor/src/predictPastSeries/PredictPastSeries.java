package predictPastSeries;

import java.util.Random;
import java.util.Scanner;

public class PredictPastSeries {

	public void inputTwoNumber(){
		Scanner s = new Scanner(System.in);
		System.out.println("Please Enter any Random number from the Valid Series");
		long rand1 = s.nextInt();
		System.out.println("Please Enter other random number occuring right after the above random number");
		long rand2 = s.nextInt();
		
		long currentSeed = computeSeed(rand1, rand2);
		
		long tempPastSeed = ((currentSeed - 0xBL) * 0xdfe05bcb1365L) & ((1L << 48) - 1);
		int i = 10;
		while(i>0){
			tempPastSeed = ((tempPastSeed - 0xBL) * 0xdfe05bcb1365L) & ((1L << 48) - 1);
			i--;
		}	

		tempPastSeed = (tempPastSeed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
		Random rt = new Random(tempPastSeed);
		System.out.println("Computed Past Series");
		for(int j=0;j<10;j++){
			System.out.println(j+")    "+rt.nextInt());
		}
	}
	
	private long computeSeed(long rand1,long rand2){
		 long multiplier = 0x5DEECE66DL;
		    long addend = 0xBL;
		    long mask = (1L << 48) - 1;
			long seed=0;
			System.out.println("---------------------------------");
			System.out.println("Any 2 number From the Series");
			System.out.println("R1 :"+rand1+"\nR2 :"+rand2);
			System.out.println("---------------------------------");
				for (int i = 0; i < 65536; i++) {
				    seed = rand1 * 65536 + i; // SEED1
				    if ((((seed * multiplier + addend) & mask) >>> 16) == rand2) {
				    	System.out.println("Series Found");
				    	System.out.println("Seed :"+seed);
				        break;
				    }
				}			
			return seed;
	 }
}
