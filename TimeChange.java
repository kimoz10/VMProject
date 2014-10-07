import java.io.*;
import java.util.*;
public class TimeChange{
  public static void main(String[] args) throws IOException{
   //ArrayList<HashMap<String, Boolean>> hashList = new ArrayList<HashMap<String, Boolean>>();
   //BufferedWriter out = new BufferedWriter(new FileWriter(new File("output/results.txt")));
   //int difference = Integer.parseInt(args[0]);
   String image1 = args[0];//These are the images of the second snapshot (VERY IMPORTANT)
   String image2 = args[1];
   File f1 = new File(image1);
   File f2 =  new File(image2);
   ram ram1 = new ram(512*1024*1024, 4096, f1);
   ram ram2 = new ram(512*1024*1024, 4096, f2);   
   int SnapShot_no = Integer.parseInt(args[2]);
   int blockSize = Integer.parseInt(args[3]);
   //double sum =0;
   BufferedReader in1 = new BufferedReader(new FileReader(new File("output/"+Integer.toString(SnapShot_no)+"/_"+Integer.toString(blockSize)+".txt")));
   BufferedReader in2 = new BufferedReader(new FileReader(new File("output/"+Integer.toString(SnapShot_no+1)+"/_"+Integer.toString(blockSize)+".txt")));
   String s1;
   String s2;
   HashMap<String, Boolean> table1 = new HashMap<String, Boolean>();//A hashSet that holds all the identical pairs from snapshot 1
   HashMap<String, Boolean> table2 = new HashMap<String, Boolean>();//A hasSet that holds all the identical pairs from snapshot 2
   while((s1 = in1.readLine())!=null){
       table1.put(s1, true);
       //System.out.println(s);
   }
   while((s2 = in2.readLine())!=null){
           table2.put(s2, true);
           //System.out.println(s);
   }
   int similarPairs=0;
        
   for(String key: table1.keySet()){
          //count++;
          if(!table2.containsKey(key)){
              String[] pair = key.split(",");
              try{
              int b1 = Integer.parseInt(pair[0]);
              int b2 = Integer.parseInt(pair[1]);
              int difference = ram1.byteCompareBlocks(ram2, b1, b2, blockSize);
              System.out.println(difference);
              }
              catch (Exception e){
              }
          }
          else{
              //System.out.println(key);
          }
    }
    
    in1.close();
    in2.close();
       
    
  }
}

