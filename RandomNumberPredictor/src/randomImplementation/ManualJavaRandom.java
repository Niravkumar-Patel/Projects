package randomImplementation;

public class ManualJavaRandom {

	private static long multiplier = 0x5DEECE66DL;
	private static long mask = (1L << 48) - 1;
	private static long addend = 0xBL;
	private long seed;
	
	public ManualJavaRandom() {
		this(defaultSeed());
	}
	
	static long defaultSeed(){
		return  (8682522807148012L * 181783497276652981L) ^ System.nanoTime();
	}
	
	public ManualJavaRandom(long seed){
		this.seed = (seed ^ multiplier) & mask;		
	}
	
	public int generateNextInt(){
		return (int) (generateNextSeed(this.seed) >>> 16);
	}
	
	public long generateNextSeed(long seed){
		this.seed = (seed * multiplier + addend) & mask;
		return  this.seed;
	}
	
	
}
