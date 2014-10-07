import java.util.*;
public class testBloomFilter{
	public static void main(String[] args){
		/* Setting the bloom filter */
		int bloom_filter_size = Integer.parseInt(args[1]);
                BloomFilter bf = new BloomFilter(bloom_filter_size);
		int HashBitSize = (int)Math.ceil((Math.log(bloom_filter_size)/Math.log(2)));
		/* Setting experimental pages */
		int distinct_page_count = Integer.parseInt(args[0]);
		
		Random rg = new Random();
		int[] pagecount = new int[distinct_page_count];
		int realmaxcount=0;
		int realmaxpage=0;
		for(int i=0; i<pagecount.length;i++){
			int c = rg.nextInt(32)+1;
			if(c>realmaxcount){
				realmaxcount = c;
				realmaxpage = i+1;
			}
			int h1 = ((i+1) * 43) % bloom_filter_size;
			int h2 = ((i+1) * 101) % (bloom_filter_size);
			if(h2 == h1) h2 = h1 + 1;
			int h3 = ((i+1) * 79) % bloom_filter_size;
			if (h3 == h2 || h3 == h1) h3 = (h2 + h1+1)%bloom_filter_size;
			if(h1 == h2 || h2 == h3 || h1 == h3)
				System.out.println("PANIC MORE THAN ONE HASH FUNCTION TO THE SAME SPOT"+h1+h2+h3);
			pagecount[i] = c;
			bf.increment(h1, c);
			bf.increment(h2, c);
			//bf.increment(h3, c);
			//System.out.println("Page: "+(i+1)+" count: "+c+" h1: "+h1+" h2: "+h2+" h3: "+h3);
			
			
		}

		/* Getting the max count from bf */
		int max_count = 0;
		int candidate_page = 0;
		int count;
		for(int i = 0; i<pagecount.length;i++){
			int c = rg.nextInt(32)+1;
                        int h1 = ((i+1) * 43) % bloom_filter_size;
                        int h2 = ((i+1) * 101) % (bloom_filter_size);
                        if(h2 == h1) h2 = h1 + 1;
                        int h3 = ((i+1) * 79) % bloom_filter_size;
                        if (h3 == h2 || h3 == h1) h3 = (h2 + h1+1)%bloom_filter_size;
			System.out.println("page: "+(i+1)+"real count: "+pagecount[i]+" "+bf.get(h1)+" "+bf.get(h2)+" "+bf.get(h3));
			count = Math.min(bf.get(h1), bf.get(h2));//, bf.get(h3));
			//System.out.println(count);
			if(count > max_count) {
				max_count = count;
				candidate_page = i+1;
			}
		}
		System.out.println("From BF: Candidate Page "+candidate_page+" Count "+max_count);
		System.out.println("Reality Page: "+realmaxpage+" Count "+realmaxcount);
		
	}
}
