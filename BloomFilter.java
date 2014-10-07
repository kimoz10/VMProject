public class BloomFilter{
	private int size;
	private int[] count;
	public BloomFilter(int s){
		size = s;
		count = new int[s];
	}
	public void increment(int idx, int inc){
		count[idx]+=inc;
	}
	public void decrement(int idx){
		count[idx]--;
	}
	public int get(int idx){
		return count[idx];
	}
}
