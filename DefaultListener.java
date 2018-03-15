import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DefaultListener
    implements DragGroundListener {
 private static DefaultListener singleton;

 public static DefaultListener getDefaultListener(){
  if(singleton==null)
   singleton=new DefaultListener();
  return singleton;
 }
 public void mouseOver(Dragject d, boolean onoff) {
    System.out.println("mouseOver "+onoff);
    d.setHigh(onoff);
  }

  public void clicked(Dragject d, MouseEvent e) {
    System.out.println("clicked");
  }

  public void dragged(Dragject d, int px, int py, MouseEvent e) {
    System.out.println("dragged");
  }

}