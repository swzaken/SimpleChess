import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.net.*;


/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class OpeningBook {
 Hashtable pages=new Hashtable();

 public OpeningBook(){
 }

 void load(File f){
  try{
   BufferedReader reader=new BufferedReader(new FileReader(f));
   load(reader);
   reader.close();
  }catch(IOException e){
   System.out.println(""+e);
  }
 }

 void loadZIP(java.applet.Applet applet){
  try{
   loadZIP(new java.net.URL(applet.getDocumentBase(),"ecobook.gz"));
  }catch(IOException e){
    System.out.println(""+e);
  }

 }

 void loadZIP(URL url){
  try{
   BufferedReader reader=new BufferedReader(
       new InputStreamReader(
        new GZIPInputStream(url.openStream())));
   load(reader);
   reader.close();
  }catch(IOException e){
   System.out.println(""+e);
  }
 }



 void load(BufferedReader reader)
 throws IOException
 {
  String separator,s;
  while(true){
   s=reader.readLine().trim();
   getPage(s).load(reader);
   separator=reader.readLine().trim();
   if(separator.equals(";"))
    continue;
   else if(separator.equals("."))
    return;
   else
    break;
  }
  System.out.println("IOerror in Opening book:"+separator);
 }

 void save(File f){
  try{
   PrintWriter out= new PrintWriter(
      new BufferedWriter(new FileWriter(f)));
   save(out);
   out.close();
  }catch(IOException e){
   System.out.println(e);
  }
 }

 void save(PrintWriter out){
  String sit;
  for(Enumeration e=pages.keys();e.hasMoreElements();){
   sit=(String)e.nextElement();
   out.println(sit);
   getPage(sit).save(out);
   if(e.hasMoreElements())
    out.println(";");
  }
  out.println(".");
 }

 public int pages(){
  return pages.size();
 }

 public void add(String s,Move m){
  getPage(s).add(m);
 }

 public OpeningBookPage getPage(String s){
  OpeningBookPage r=(OpeningBookPage)pages.get(s);
  if(r==null)
  {
   r=new OpeningBookPage(s);
   pages.put(s,r);
  }
  return r;
 }

 public Move getRandomMove(String s){
  return getPage(s).getRandomMove();
 }

}

class OpeningBookPage{
 String situation;
 Vector entries=new Vector();
 private static Random ran=new Random();


 public OpeningBookPage(String sit){
  situation=sit;
 }

 public void load(BufferedReader reader)
 throws IOException
 {
  int n=Integer.parseInt(reader.readLine().trim());
  for(int i=0;i<n;i++){
   OpeningBookEntry en=new OpeningBookEntry(reader.readLine());
   getEntry(en.m).raise(en.n);
  }
 }

 public void add(Move m){
  getEntry(m).raise(1);
 }

 public void save(PrintWriter out){
  out.println(entries());
  for(int i=0;i<entries();i++)
   out.println(" "+getEntry(i));
 }

 int entries(){return entries.size();}

 public OpeningBookEntry getEntry(int i){
  return (OpeningBookEntry)entries.elementAt(i);
 }

 public OpeningBookEntry getEntry(Move m){
  for(int i=0;i<entries();i++)
   if(getEntry(i).m.equals(m))
    return getEntry(i);
  OpeningBookEntry newentry=new OpeningBookEntry(m,0);
  entries.addElement(newentry);
  return newentry;
 }

 public Move getRandomMove(){
  int sum=0;
  if(entries()==0)
   return null;
  for(int i=0;i<entries();i++)
   sum+=getEntry(i).n;
  int random=ran.nextInt(sum);
  for(int i=0;i<entries();i++)
   if(random<getEntry(i).n)
    return getEntry(i).m;
   else
    random-=getEntry(i).n;
  return getEntry(0).m;
 }
}

/**********************************************/
class OpeningBookEntry{
 Move m;
 int n;
 private char separator='=';

 public OpeningBookEntry(Move m,int n){
  this.m=m;
  this.n=n;
 }
 public OpeningBookEntry(String s){
  s=s.trim();
  int split=s.indexOf(separator);
  m=new Move(s.substring(0,split));
  n=Integer.parseInt(s.substring(split+1));
 }

 public String toString(){
  return (m+""+separator)+n;
 }

 public void raise(int increment){
  n+=increment;
 }
}

