import java.io.*;
import java.util.*;
import java.security.*;
public class ram{
  private long size; //size of the physical RAM of the VM in bytes
  private int PageSize; //pagesize in bytes
  private File MemImg; //the file holding the raw memory image of this ram
  private HashMap<String, Node> table;
  public ram(long size, int PageSize, File MemImg){
    this.size = size;
    this.PageSize = PageSize;
    this.MemImg = MemImg;
    this.table = new HashMap<String, Node>();
  }

  //Reading the value of the byte at address byte address
  public int readMemoryLocation(int byteAddress) throws IOException{
    FileInputStream in = new FileInputStream(MemImg);
    in.skip(byteAddress);
    int val = in.read();
    in.close();
    return val;
  }
  
  public byte[] readMemoryBlock(int ppn, int bn) throws IOException{
    FileInputStream in = new FileInputStream(this.MemImg);
    //System.out.println(ppn*4096+bn*64);
    in.skip(ppn*4096L+bn*64L);
    byte[] output = new byte[64];
    in.read(output);
    in.close();
    return output;
  }

  public boolean isZeroBlock(int ppn, int blockno, int blocksize) throws IOException{
    boolean zero = true;
    FileInputStream in = new FileInputStream(this.MemImg);
    in.skip(ppn*PageSize+blockno*blocksize);
    for(int i=0; i<blocksize; i++){
      int Readbyte = in.read();
      if(Readbyte!=0){
        zero=false;
        break;
      }
    }
    in.close();
    return zero;

  }
  public boolean isZeroPage(int ppn) throws IOException{
    boolean zero = true;
    FileInputStream in = new FileInputStream(this.MemImg);
    in.skip(ppn*PageSize);
    for(int i=0; i<PageSize; i++){
      int Readbyte = in.read();
      if(Readbyte!=0){
        zero=false;
        break;
      }
    }
    in.close();
    return zero;
  }

  public HashMap<String, Node> getTable(){
    return table;
  }

  public void createHashMap(int blockSize) throws IOException{
     FileInputStream in = new FileInputStream(this.MemImg);
     byte[] block = new byte[blockSize];//blocksize is the page size in this case

     for(int i = 0; i < size/blockSize; i++){
         in.read(block);
         String key="";
         try{
	     MessageDigest md = MessageDigest.getInstance("SHA1");
	     md.update(block);
             byte[] output = md.digest();
             key = bytesToHex(output);
         }
	 catch (Exception e){}
         if(!table.containsKey(key))table.put(key,new Node(i));
         else table.get(key).add(i);
     }
     in.close();
  }
  
  /* takes in a byte array and returns a hex string representing sha1 */
  public String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }

  public void writeBlock(int ppn, int bn, int blocksize, File f) throws IOException{
    BufferedWriter out = new BufferedWriter(new FileWriter(f));
    FileInputStream in = new FileInputStream(this.MemImg);
    in.skip(ppn*PageSize+bn*blocksize);
    for(int i=0; i<blocksize; i++){
      int Readbyte = in.read();
      out.write(Readbyte+" ");
    }
    in.close();
    out.close();

  }

  //Having two RAM objects, compare PPN1 against PPN2 of the second ram (ram2). report how many blocks of size blocksize were different.
  public int comparePages(ram ram2, int ppn1, int ppn2, int blocksize) throws IOException{
    int difference = 0;
    FileInputStream in1 = new FileInputStream(this.MemImg);
    FileInputStream in2 = new FileInputStream(ram2.MemImg);
    in1.skip(ppn1*PageSize);
    in2.skip(ppn2*ram2.PageSize);
    int blocknumber = PageSize/blocksize;
    byte[] block1 = new byte[blocksize];
    byte[] block2 = new byte[blocksize];
    for(int i=0; i<blocknumber;i++){
      in1.read(block1);
      in2.read(block2);
      if(!Arrays.equals(block1, block2))difference++;
    }
    in1.close();
    in2.close();
    return difference;    
  }
  
  public int byteCompareBlocks(ram ram2, int block1, int block2, int blocksize) throws IOException{
    int difference = 0;
    FileInputStream in1 = new FileInputStream(this.MemImg);
    FileInputStream in2 = new FileInputStream(ram2.MemImg);
    in1.skip(block1*blocksize);
    in2.skip(block2*blocksize);
    
    byte[] b1 = new byte[1];
    byte[] b2 = new byte[1];
    for(int i=0; i<blocksize;i++){
      in1.read(b1);
      in2.read(b2);
      if(!Arrays.equals(b1, b2))difference++;
    }
    in1.close();
    in2.close();
    return difference;
  }
  public boolean isIdenticalBlocks(ram ram2, int ppn1, int ppn2, int bn1, int bn2, int blocksize) throws IOException{
    boolean difference = true;
    FileInputStream in1 = new FileInputStream(this.MemImg);
    FileInputStream in2 = new FileInputStream(ram2.MemImg);
    in1.skip(ppn1*PageSize+bn1*blocksize);
    in2.skip(ppn2*ram2.PageSize+bn2*blocksize);
    byte[] block1 = new byte[blocksize];
    byte[] block2 = new byte[blocksize];
    in1.read(block1);
    in2.read(block2);
    if(!Arrays.equals(block1, block2))difference=false;
    in1.close();
    in2.close();
    return difference;
  }

  public byte[] readPage(int ppn) throws IOException{
    FileInputStream in = new FileInputStream(this.MemImg);
    byte[] p = new byte[4096];
    in.skip(ppn*4096L);
    in.read(p);
    in.close();
    return p;
  }


}
