import java.awt.Graphics;
import java.util.*;
import java.applet.*;

public abstract class Dragject
{
 protected int x,y;
 protected int sizex,sizey;
 protected boolean canDrag,canClick,isHigh,canMouseOver;
 DragGroundListener dgl;
 //DragGround ground;

 Motion motion;

 public Dragject(){
   x=y=0;
   sizex=sizey=10;
 }

 DragGroundListener getListener(){
  return dgl;
 }
 void addListener(DragGroundListener list){
  dgl=list;
 }
 void setGround(DragGround dg){
  //ground=dg;
 }
 void setMotion(Motion m){
  motion=m;
 }

 public void setDragClick(boolean icanDrag,boolean icanClick, boolean icanMouseOver){
  canDrag=icanDrag;
  canClick=icanClick;
  canMouseOver=icanMouseOver;
 }
  public boolean canClick(){return canClick;}
  public boolean canDrag(){return canDrag;}
  public boolean canMouseOver(){return canMouseOver;}

  public void setHigh(boolean highvalue){
  isHigh=highvalue;
 }
  public boolean isHigh(){return isHigh;}

 protected int getTopX(){return x-sizex/2;}
 protected int getTopY(){return y-sizey/2;}
 public int getX(){return x;}
 public int getY(){return y;}
 public int getWidth(){return sizex;}
 public int getHeight(){return sizey;}

 public void setPosition(int ix,int iy){
  x=ix;
  y=iy;
 }

 public void moveToPosition(int endx,int endy,long duration){
  Motion m=new Motion(getX(),getY(),endx,endy);
  m.setTime(System.currentTimeMillis(),duration);
  setMotion(m);
 }
 public boolean contains(int px,int py){
  return Math.abs(x-px)<sizex/2&&Math.abs(y-py)<sizey/2;
 }
 public void draw2(Graphics g,DragGround dg,long time){
  if(motion!=null){
   setPosition(motion.getX(time),motion.getY(time));
   if(motion.isFinished(time))
    motion=null;
  }
  draw(g,dg,time);
 }

 public abstract void draw(Graphics g,DragGround dg,long time);

}