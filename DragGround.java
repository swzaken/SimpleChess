import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

class DragGround
    extends Canvas
    implements ImageObserver,Runnable {
 Vector[] layer;
 Color bgcolor;
 Image offImage;
 Graphics offGraphics;
 Dimension offDimension;
 DragControl dg;
 boolean isInterrupted=false;


 public DragGround(int layers, Color bgcolor) {
  layer = new Vector[layers];
  this.bgcolor = bgcolor;
  for (int i = 0; i < layer.length; i++) {
   layer[i] = new Vector();
  }
  dg = new DragControl(this);
  addMouseListener(dg);
  addMouseMotionListener(dg);
 }

 Dragject getDragject(Vector mylayer, int i) {
  return (Dragject) mylayer.elementAt(i);
 }

 void remove(Dragject d){
  for(int i=0;i<layer.length;i++)
   if(layer[i].remove(d))
    break;
 }

 void add(int mylayer, Dragject d) {
  layer[mylayer].addElement(d);
  d.setGround(this);
 }

 public void update(Graphics g) {
  Dimension d = getSize();
  if (offGraphics == null ||
      d.width != offDimension.width ||
      d.height != offDimension.height) {
   offDimension = d;
   offImage = createImage(d.width, d.height);
   offGraphics = offImage.getGraphics();
  }
  offGraphics.setColor(bgcolor);
  offGraphics.fillRect(0, 0, d.width, d.height);
  paint(offGraphics);
  g.drawImage(offImage, 0, 0, null);
 }

 public void paint(Graphics g) {
  long time=System.currentTimeMillis();

  for (int i = 0; i < layer.length; i++) {
   for (int j = 0; j < layer[i].size(); j++) {
    getDragject(layer[i], j).draw2(g, this,time);
   }
  }
 }

 /**
  * find an object at the specified location.
  * @param px
  * @param py
  * @param clickable set to true for finding a clickable object
  * @param draggable set to true for finding a draggable object
  * @param overable set to true for finding a MouseOver enabled object
  * @return
  */
 Dragject select(int px, int py, boolean clickable, boolean draggable,
                 boolean overable) {
  for (int i = layer.length - 1; i >= 0; i--) {
   for (int j = 0; j < layer[i].size(); j++) {
    if (getDragject(layer[i], j).contains(px, py)) {
     if ( (!clickable || getDragject(layer[i], j).canClick()) &&
         (!overable || getDragject(layer[i], j).canMouseOver()) &&
         (!draggable || getDragject(layer[i], j).canDrag())
         ) {
      return getDragject(layer[i], j);
     }
    }
   }
  }
  return null;
 }

 public static void main(String[] ps) {
  DragGround dg = new DragGround(2, Color.blue);
  Dragject d1 = new RectangleDragject(50, 20);
  Image im1 = Toolkit.getDefaultToolkit().getImage("images/01.gif");
  Dragject d3 = new ImageDragject(im1, im1);
  DefaultListener listener = new DefaultListener();
  d1.addListener(listener);
  d3.addListener(listener);
  d1.setDragClick(true, true, true);
  d3.setDragClick(false, true, true);
  dg.add(0, d1);
  dg.add(1, d3);
  Frame f = new Frame("DragGround");
  f.add("Center", dg);
  dg.setSize(200, 200);
  f.pack();
  f.show();
 }

 public boolean imageUpdate(Image img,
                            int infoflags,
                            int x,
                            int y,
                            int width,
                            int height) {
  if ( (infoflags & ImageObserver.ALLBITS) != 0) {
   return false;
  }
  return true;
 }

 public void stop(){
  isInterrupted=true;
 }
 public void start(){
  new Thread(this).start();
 }

 public void run() {
  while(!isInterrupted){
   try {
    Thread.sleep(150);
   }
   catch (InterruptedException ex) {
    stop();
   }
   repaint();
  }
 }

 public Frame showInFrame(String title) {
  Frame f = new Frame(title);
  f.setLayout(new BorderLayout());
  f.add("Center", this);
  f.pack();
  centerFrame(f);
  f.setVisible(true);
  return f;
 }

 private static void centerFrame(Component f) {
  int x = (f.getToolkit().getScreenSize().width - f.getWidth()) / 2;
  int y = (f.getToolkit().getScreenSize().height - f.getHeight()) / 2;
  f.setLocation(x, y);
 }

}
