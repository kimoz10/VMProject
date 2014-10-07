/* The purpose of this code is to make a synthetic RAM image for debugging purposes by various tools 
   Usually the synthetic RAM image is small in size in order to be able to test it efficiently   */

import java.io.*;
import java.util.*;

public class SynthRamImg{
  public static void main(String[] args) throws IOException{
    File f = new File("/home/karim/VMProject/images/"+args[0]+".synth");
    FileOutputStream out = new FileOutputStream(f);
    System.out.println("Enter the contents of the RAM byte by byte. Enter -1 to end");
    BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
    int val;
    while((val = Integer.parseInt(in.readLine()))!=-1)out.write(val);
    in.close();
    out.close();
  }
}
