

import java.awt.*;
import java.util.Vector;
/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * A CompositeDragject consists of several dragjects. Using a composite
 * allows one to drag or select a group of objects. The individual
 * objects can still be selected as parts.
 */
public class CompositeDragject extends Dragject {
 /*************************************************************/
 // fields
 /*************************************************************/
 private Vector parts=new Vector();
 /*************************************************************/
 // constructors
 /*************************************************************/
 /**
  * create a new composite dragject of a certain size
  * @param sizeX
  * @param sizeY
  */
 public CompositeDragject(int sizeX, int sizeY){
  sizex=sizeX;
  sizey=sizeY;
 }
 /*************************************************************/
 // public methods
 /*************************************************************/
 /**
  * add a dragject to this composite. We recommend you first create
  * a composite, create and add all parts, and then call addAllto(...)
  * to add them al to a ground. add(d,0,0) puts d exactly in the middle
  * of this object.
  * @param d a dragject
  * @param relativeX x coordinate of relative position of d
  * @param relativeY y coordinate of relative position of d
  */
 public void add(Dragject d,int relativeX,int relativeY){
  parts.addElement(d);
  d.setPosition(x+relativeX,y+relativeY);
 }
 /**
  * get one of the added dragjects
  * @param i an index number
  * @return the i'th dragject added to this object
  */
 public Dragject getPart(int i){
  return (Dragject)parts.elementAt(i);
 }
 /**
  * get the number of parts of this composite
  * @return the number of parts added
  */
 public int parts(){
  return parts.size();
 }
 /**
  * this adds this object and all parts to a certain dragGround. Note that
  * since this object is not visible, the complayer only maters for events.
  * @param ground the ground to add to
  * @param complayer the layer in which to place this object
  * @param partlayer the layer to add all parts to
  */
 public void addAllTo(DragGround ground,int complayer,int partlayer){
  ground.add(complayer,this);
  for(int i=0;i<parts();i++)
   ground.add(partlayer,getPart(i));
 }

 /*************************************************************/
 // necessary methods
 /*************************************************************/
 /**
  * a Composite Dragject is itself not visible, so this draw method
  * does nothing. For debugging, a rectangle can be drawn.
  * @param g
  * @param dg
  * @param time
  */
 public void draw(Graphics g, DragGround dg, long time) {
  //g.setColor(Color.black);
  //g.drawRect(getTopX(),getTopY(),sizex,sizey);
 }
 /**
  * changing the position of a composite moves all parts with
  * the same amount.
  * @param nx a new x coordinate
  * @param ny a new y coordinate
  */
 public void setPosition(int nx,int ny){
  int shiftx=nx-x;
  int shifty=ny-y;
  x=nx;
  y=ny;
  for(int i=0;i<parts();i++)
   getPart(i).setPosition(getPart(i).getX()+shiftx,getPart(i).getY()+shifty);
 }
}

