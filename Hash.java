import java.util.*;
import java.io.*;
import java.lang.*;

public class Hash{
int hash;

public int hash(byte[] bytes){//creates a hasvalue of int and returns
int hash1 = 0;
for(int x=0;x<bytes.length;x++){
   hash1= ((hash1*Math.abs(bytes[x])) + (bytes[x]^2) + (bytes[x])) *2;
   hash1= hash1/bytes.length;
   
   }
   return hash1;
}

public boolean hashCheck(byte[] bytes, int hash2){ // returns a boolean if two hash values are the same assuming the packet has a int value included
   int hashVal =0;
   boolean isSame = false;
   for(int y=0;y<bytes.length;y++){
   hashVal= ((hashVal*Math.abs(bytes[y])) + (bytes[y]^2) + (bytes[y])) *2;
   hashVal= hashVal/bytes.length;
   
   }
   if(hash2==hashVal){
      isSame = true;
      return isSame;
      }
         
   return isSame;
}




public static void main(String[] args) {// code to check if it works
   Hash hasher = new Hash();
   String s ="Hello World";
   int s1;
   boolean s2;
   byte buf1[] = s.getBytes();
   String p ="Hello World";
   int p1;
   boolean p2;
   byte buf2[]=p.getBytes();

   s1 = hasher.hash(buf1);
   p2 = hasher.hashCheck(buf2, s1);

   if(p2){
      System.out.println("They are the same");
   }
   else{
      System.out.println("they are not");
   }
   
}

}
