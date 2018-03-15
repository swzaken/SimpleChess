

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class Timer {
 long lastStarted=-1;
 long timeGathered=0;

 public Timer(){};

 void reset(){
  lastStarted=-1;
  timeGathered=0;
 }

 void start(){
  if(lastStarted==-1)
   lastStarted=System.currentTimeMillis();
 }
 void stop(){
  if(lastStarted!=-1)
   timeGathered=getTime();
  lastStarted=-1;
 }
 long getTime(){
  return getTime(System.currentTimeMillis());
 }

 long getTime(long now){
   if(lastStarted==-1)
    return timeGathered;
   return timeGathered+(now-lastStarted);
 }


 String twoDigit(int i){
  String s=""+i;
  while (s.length()<2)
   s="0"+s;
  return s;
 }
 public String toString(){
  return toString(System.currentTimeMillis());
 }

 public String toString(long now){
  long total=getTime();
  total/=1000;
  int seconds=(int)(total%60);
  total/=60;
  int minutes=(int)(total%60);
  total/=60;
  int hours=(int)(total%100);
  return twoDigit(hours)+":"+twoDigit(minutes)+":"+twoDigit(seconds);
 }


}