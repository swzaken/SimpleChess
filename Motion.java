

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class Motion {
 long starttime,endtime;
 int startx,starty,endx,endy;

 public Motion(int x1,int y1,int x2,int y2){
  setTime(System.currentTimeMillis(),1000);
  startx=x1;
  starty=y1;
  endx=x2;
  endy=y2;
 }

 public void setTime(long start,long duration){
  starttime=start;
  endtime=start+duration;
 }

 double getFraction(long time){
  if(time<=starttime)
   return 0;
  if(time>=endtime)
   return 1;
  double length=endtime-starttime;
  return (time-starttime)/length;
 }

 int getX(long time){
  double frac=getFraction(time);
  return (int)(frac*endx+(1-frac)*startx);
 }
 int getY(long time){
  double frac=getFraction(time);
  return (int)(frac*endy+(1-frac)*starty);
 }
 boolean isFinished(long time){
  return time>=endtime;
 }
}