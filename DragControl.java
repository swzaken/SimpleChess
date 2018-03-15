import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * known problem: draggable object cannot be clicked.
 */

public class DragControl implements MouseListener,MouseMotionListener{
 int dragstartx,dragstarty;
 Dragject dragtarget;
 Dragject mouseOver;
 int targetx,targety;
 DragGround ground;
 long dragstarttime;
 public long MINDRAGTIME=200;

 public DragControl(DragGround dg){
  ground=dg;
 }

 public void mouseClicked(MouseEvent e){
  //System.out.println("clicked "+e.getButton());
  Dragject clicktarget=ground.select(e.getX(),e.getY(),true,false,false);
  if(clicktarget!=null){
   if(clicktarget.getListener()!=null)
    clicktarget.getListener().clicked(clicktarget,e);
  }
 }

 public void mousePressed(MouseEvent e){
  if(dragtarget!=null)
   return;
  //System.out.println("pressed "+e.getButton());
  dragtarget=ground.select(e.getX(),e.getY(),false,true,false);
  if(dragtarget!=null)
  {
    dragstartx=e.getX();
    dragstarty=e.getY();
    targetx=dragtarget.getX();
    targety=dragtarget.getY();
    dragstarttime=System.currentTimeMillis();
  }
 }

 private void moveTarget(MouseEvent e){
  if(dragtarget!=null)
   dragtarget.setPosition(targetx+e.getX()-dragstartx,targety+e.getY()-dragstarty);
 }


 public void mouseReleased(MouseEvent e){
  //System.out.println("released "+e.getButton());
  if(dragtarget!=null){
   long dragtotaltime=System.currentTimeMillis()-dragstarttime;
   if(dragtotaltime<MINDRAGTIME)
   {
    dragtarget.setPosition(targetx,targety);
    dragtarget=null;
    return;
   }
   moveTarget(e);
   DragGroundListener dg=dragtarget.getListener();
   if(dg!=null)
    dg.dragged(dragtarget,targetx,targety,e);
   dragtarget=null;
  }
 }
 public void mouseDragged(MouseEvent e){
   moveTarget(e);
   ground.repaint();
 }

 private void mouseOverOff(){
   if(mouseOver!=null&&mouseOver.getListener()!=null)
    mouseOver.getListener().mouseOver(mouseOver,false);
 }

 public void mouseMoved(MouseEvent e){
  Dragject mouseOver2=ground.select(e.getX(),e.getY(),false,false,true);
  if(mouseOver2==mouseOver)
    return;
  mouseOverOff();
  mouseOver=mouseOver2;
  if(mouseOver!=null&&mouseOver.getListener()!=null)
    mouseOver.getListener().mouseOver(mouseOver,true);
 }
 public void mouseEntered(MouseEvent e){}
 public void mouseExited(MouseEvent e){
  if(dragtarget!=null){
   dragtarget.setPosition(targetx,targety);
   dragtarget=null;
  }
  mouseOverOff();
 }


}

