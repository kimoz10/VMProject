/* This is the driver main code */
import java.io.*;
import java.util.*;
import java.security.*;
/* Find duplicate and Similar pages */
public class Main{
  /* takes in a byte array and returns a hex string representing sha1 */
  public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
  }


  public static void updateSimilarArray(int[] x, int d){
      if(d>0 && d<=4) x[0]++;
      else if(d>4 && d<=8) x[1]++;
      else if(d>8 && d<=16) x[2]++;
      else x[3]++;
  }

  public static int comparePages(byte[] refPage, byte[] p){
      int d = 0;
      for(int block = 0; block < 64; block++){
          for(int i = 0;i < 64; i++){
            if(refPage[block*64+i]!=p[block*64+i]){
                d++;
                break;
            }
          }
      }
      return d;
  }
  public static void main(String[] args)throws IOException{
    int ram_instances = Integer.parseInt(args[0]);
    File[] f = new File[ram_instances];
    for(int i =0; i<ram_instances; i++)f[i] = new File(args[i+1]);
    Long memsize = Long.parseLong(args[ram_instances+1]);
    int PageSize = Integer.parseInt(args[ram_instances + 2]);
    int blocksize = Integer.parseInt(args[ram_instances + 3]);
    System.out.println("Ram instances: "+ram_instances);
    for(int i = 0; i < ram_instances ; i++)System.out.println("Image "+(i+1)+": "+ f[i].toString());
    System.out.println("PageSize: "+PageSize);
    System.out.println("blocksize: "+blocksize);
    ram[] mem = new ram[ram_instances];
    for(int i=0;i<ram_instances;i++){
        mem[i] = new ram(memsize, PageSize, f[i]);
        mem[i].createHashMap(blocksize);
    }
    String zeroBlockHash = "";
    //zeroBlockHash is the hash value of a zero block
    try{
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(new byte[blocksize]);
        byte[] output = md.digest();
        zeroBlockHash = bytesToHex(output);
    }
    catch(Exception e){}
    //int zeroBlockHash = Arrays.hashCode(new byte[blocksize]);
    int similarZeroBlocks = 0;
    int Zeroblocks1 = 0;
    int Zeroblocks2 = 0;
    int similarPagesinMem1 = 0;
    int similarPagesinMem2 = 0;
    
    HashMap<String, Node> t1 = mem[0].getTable();//This gets the hashtable representing the memory
    //HashMap<Integer, Node> t2 = mem[1].getTable();
    HashMap<String, LinkedList<Pair>> identical_pages_table = new HashMap<String, LinkedList<Pair>>();//table is the identical page table
    /* table to hold similar pages */
    HashMap<Integer, LinkedList<Integer>> similar_pages_table = new HashMap<Integer, LinkedList<Integer>>();
    int sharedBlocks = 0;
    int AlignedsharedBlocks = 0;
    int count = 0;   
    for(String key: t1.keySet()){
        if(!identical_pages_table.containsKey(key)){
          LinkedList<Pair> list = new LinkedList<Pair>();
          LinkedList<Integer> l1 = t1.get(key).getList();
          for(Integer ppn: l1)list.add(new Pair(ppn, "m1"));
          identical_pages_table.put(key, list);
        }
        else
        {
          LinkedList<Integer> l1 = t1.get(key).getList();
          for(Integer ppn: l1)(identical_pages_table.get(key)).add(new Pair(ppn, "m1"));
        }
    }
    /*
    for(Integer key: t2.keySet()){
        if(!table.containsKey(key)){
          LinkedList<Pair> list = new LinkedList<Pair>();
          LinkedList<Integer> l2 = t2.get(key).getList();
          for(Integer ppn: l2)list.add(new Pair(ppn, "m2"));
          table.put(key, list);
        }
        else
        {
          LinkedList<Integer> l2 = t2.get(key).getList();
          for(Integer ppn: l2)(table.get(key)).add(new Pair(ppn, "m2"));
        }
    }
    */
    //Now we have table as the unified hash table

    /* Starting to create the similar_pages_table */
    int[] x = new int[4];
    for (int i=0;i<4;i++)x[i]=0;
    Random rand = new Random();
    int firstBlock = rand.nextInt(64);
    int secondBlock = rand.nextInt(64);
    System.err.println("First Block "+firstBlock);
    System.err.println("Second Block "+secondBlock);
    for(String key: t1.keySet()){
        LinkedList<Integer> PL = t1.get(key).getList();
	if(PL.size()==1){ //This is a unique page
            int ppn = PL.get(0);
            int diff1 = -1;
            int diff2 = -1;
            byte[] blockContentOne = mem[0].readMemoryBlock(ppn, firstBlock);
	    byte[] blockContentTwo = mem[0].readMemoryBlock(ppn, secondBlock);
            int hashOne = Arrays.hashCode(blockContentOne);
            int hashTwo = Arrays.hashCode(blockContentTwo);
            if(!similar_pages_table.containsKey(hashOne)){
                LinkedList<Integer> list = new LinkedList<Integer>();
                list.add(ppn);
                similar_pages_table.put(hashOne, list); 
            }
            else{
                int ref = similar_pages_table.get(hashOne).get(0);
		if(ref!=ppn){
                	byte[] refpage = mem[0].readPage(ref);
                	byte[] p = mem[0].readPage(ppn);
                	diff1 = comparePages(refpage, p);
		}
	    }
            if(!similar_pages_table.containsKey(hashTwo)){
                LinkedList<Integer> list = new LinkedList<Integer>();
                list.add(ppn);
                similar_pages_table.put(hashTwo, list);
            }
            else{
                int ref = similar_pages_table.get(hashTwo).get(0);
                if(ref!=ppn){
			byte[] refpage = mem[0].readPage(ref);
                	byte[] p = mem[0].readPage(ppn);
                	diff2 = comparePages(refpage, p);
		}
            }
            
            if(diff1==-1 && diff2!=-1 && diff2<32){
                similar_pages_table.get(hashTwo).add(ppn);
                updateSimilarArray(x, diff2);
            }
            else if(diff2==-1 && diff1!=-1 && diff1<32){
                similar_pages_table.get(hashOne).add(ppn);
                updateSimilarArray(x, diff1);
            }
            else if(diff1!=-1 && diff2!=-1){
                if(diff1<=diff2){
                    if(diff1<32){
                      similar_pages_table.get(hashOne).add(ppn);
                      updateSimilarArray(x, diff1);
                    }
                }
                else{
                    if(diff2<32){
                      similar_pages_table.get(hashTwo).add(ppn);
                      updateSimilarArray(x, diff2);
                    }
                }
            }
        }
    }
    /* This will just print out the identical pages */
    for(String key: identical_pages_table.keySet()){
      LinkedList<Pair> list = identical_pages_table.get(key);
      if(list.size()<=1)continue;
      if(key.equals(zeroBlockHash))System.out.print("ZERO: ");
      for(Pair p: list){
        System.out.print("("+p.ppn+","+p.memory_instance+") ");
      }
      System.out.println();
    }
    
    for(Integer key: similar_pages_table.keySet()){
      LinkedList<Integer> list = similar_pages_table.get(key);
      if(list.size()==1)continue;
      for(Integer ppn: list){
         System.err.print("("+ppn+") ");
      }
      System.err.println();
    }
    for(int i = 0; i < 4; i++) System.err.println(x[i]);   
  }
}
