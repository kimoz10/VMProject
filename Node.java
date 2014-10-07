//This class is simple a wrapper to a LinkedList

import java.util.*;
public class Node{
	private LinkedList<Integer> blockList;
        public Node(int blocknumber){
          blockList=new LinkedList<Integer>();
	  blockList.add(blocknumber);          
        }
        public void add(int i){
          blockList.add(i);
        }
        public LinkedList<Integer> getList(){
            return blockList;
        }
}
