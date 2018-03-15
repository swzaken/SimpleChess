import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class ImageSelectFrame implements DragGroundListener{
 /******************************************************/
 // fields
 /******************************************************/
 private Vector images;
 private Dragject[][] dragject;
 private Dragject selectorDragject;
 private boolean selectShown;
 private ImageSelectListener listener;
 protected DragGround ground;
 protected Frame frame;
 private String title;
 /**
  * maximum size of images in pixels
  */
 private int sizex,sizey;
 /**
  * nr of images in x and y direction
  */
 int nx,ny;
 /******************************************************/
 // constructors
 /******************************************************/
 /**
  * Creates a new ImageSelectFrame
  * @param nX the number of images to show in horizontal direction
  * @param nY the number of images to show in vertical direction
  * @param sizex the horizontal spaces each image needs, in pixels
  * @param sizey the vertical space each image needs, in pixels
  * @param title the title for the frame
  */
 public ImageSelectFrame(int nX,int nY,int sizex,int sizey,String title){
  images=new Vector();
  this.title=title;
  this.sizex=sizex;
  this.sizey=sizey;
  nx=nX;
  ny=nY;
  ground=new DragGround(2,Color.white);
  ground.setSize(nx*sizex,ny*sizey);
  RectangleDragject rd=new RectangleDragject(sizex,sizey);
  rd.setColor(null,Color.black,null,Color.black);
  selectorDragject=rd;
  selectShown=false;
 }
 /**
  * calculate screen co-ordinate of an image
  * @param imnumber the number of the image
  * @return the x co-ordinate in pixels
  */
 public int getX(int imnumber){
  return (imnumber%nx)*sizex+sizex/2;
 }
 /**
  * calculate screen co-ordinate of an image
  * @param imnumber the number of the image
  * @return the y co-ordinate in pixels
  */
 public int getY(int imnumber){
  return (imnumber/nx)*sizey+sizey/2;
 }

 /******************************************************/
 // private methods
 /******************************************************/

 /**
  * add an Image to show in this frame
  * @param im an image the user can select
  */
 public void addImage(Image im){
  images.addElement(im);
  Dragject d=new ImageDragject(im,im);
  d.setDragClick(false,true,false);
  d.addListener(this);
  d.setPosition(getX(images.size()-1),getY(images.size()-1));
  ground.add(1,d);
 }
 /**
  * sets the image-selectListener of this object. Despite the
  * suggestive 'add' name,only one ISF can be added to this object.
  * @param l a listener that is notified when an image is selected
  */
 public void addListener(ImageSelectListener l){
  listener=l;
 }
 /**
  * make this frame visible
  */
 public void show(){
  if(frame==null)
   frame=ground.showInFrame(title);
  else
   frame.show();
 }
 /**
  * make this screen invisible
  */
 public void hide(){
  if(frame!=null)
   frame.hide();
 }

 public void showSelected(Image im){
  if(selectShown){
   ground.remove(selectorDragject);
   ground.repaint();
   selectShown=false;
  }
  int i=images.indexOf(im);
  if(i!=-1){
   selectorDragject.setPosition(getX(i),getY(i));
   ground.add(0,selectorDragject);
   ground.repaint();
   selectShown=true;
  }
 }
 /******************************************************/
 // necessary public methods
 /******************************************************/
 /**
  * a click means the user made a selection. The listener is notified.
  * @param d
  * @param e
  */
 public void clicked(Dragject d, MouseEvent e){
  int x=d.getX()/sizex;
  int y=d.getY()/sizey;
  listener.select(this,(Image)(images.elementAt(x+nx*y)));
 }
 /**
  * unused
  * @param d
  * @param px
  * @param py
  * @param e
  */
 public void dragged(Dragject d, int px, int py, MouseEvent e){
 }
 /**
  * unused
  * @param d
  * @param onoff
  */
 public void mouseOver(Dragject d,boolean onoff){}

}

interface ImageSelectListener {
 /**
  *
  * @param isf The imageSelectFrame in which the event was generated
  * @param i the Image that was selected.
  */
 void select(ImageSelectFrame isf, Image i);
}