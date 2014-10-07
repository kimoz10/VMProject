import java.io.*;
import java.util.*;
public class similarBlocks{
  public static void main(String[] args) throws IOException{
    //ArrayList<HashMap<String, Boolean>> hashList = new ArrayList<HashMap<String, Boolean>>();
    //BufferedWriter out = new BufferedWriter(new FileWriter(new File("output/results.txt")));
    int difference = Integer.parseInt(args[0]);
    int blockSize = Integer.parseInt(args[1]);
    double sum =0;
    for(int i = blockSize; i>=blockSize; i=i/2){
      for(int j=1; j<=40-difference; j++){//j from 1 to 40 here
        BufferedReader in1 = new BufferedReader(new FileReader(new File("output/"+Integer.toString(j)+"/_"+Integer.toString(i)+".txt")));
        BufferedReader in2 = new BufferedReader(new FileReader(new File("output/"+Integer.toString(j+difference)+"/_"+Integer.toString(i)+".txt")));
        String s1;
        String s2;
        HashMap<String, Boolean> table1 = new HashMap<String, Boolean>();
        HashMap<String, Boolean> table2 = new HashMap<String, Boolean>();
        while((s1 = in1.readLine())!=null){
           table1.put(s1, true);
           //System.out.println(s);
        }
        while((s2 = in2.readLine())!=null){
           table2.put(s2, true);
           //System.out.println(s);
        }
        int similarPairs=0;
        
        for(String key: table2.keySet()){
              //count++;
              if(table1.containsKey(key))similarPairs++;
        }
        sum+=((double)similarPairs/table2.size()*100);
        System.out.println(sum);
        //hashList.add(table);
        in1.close();
        in2.close();
      }
      System.out.println("avg is "+(sum/(40-difference)));
      //System.out.println(",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40");
      //for (int j = 0; j<40; j++){//j<40 here
          //System.out.print((j+1));
          //HashMap<String, Boolean> currentTable = hashList.get(j);
          //for(int k = 0; k<j+1; k++){
            //System.out.println("Comparing "+(j+1)+" to "+(k+1));
            //System.out.println("*********************");
            //HashMap<String, Boolean> prevTable = hashList.get(k);
            //int count = 0;
            //int similarPairs = 0;
            //for(String key: currentTable.keySet()){
              //count++;
              //if(prevTable.containsKey(key))similarPairs++;
            //}
            //System.out.print(","+Math.round(((double)similarPairs/currentTable.size())*100));
            //System.out.println(similarPairs+" "+count);
          //}
          //System.out.println();
      //}
    }
    
  }
}

