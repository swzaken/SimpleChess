import java.awt.event.*;

/**
 * <p>Title: DragGroundListener</p>
 * <p>Description: Interface for receiving clicks and drags from a
 * DragGround.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public interface DragGroundListener {
  /**
   * Called if a clickable object is clicked
   * @param d the object clicked
   * @param e contains the amount of clicks and the button.
   */
  void clicked(Dragject d, MouseEvent e);


  /**
   * Is called when a Dragject has been dragged to a new location
   * @param d the Dragject involved
   * @param px the original x location
   * @param py the original y location
   * @param e An event containing information on mouse buttons and number of
   * clicks.
   */
  void dragged(Dragject d, int px, int py, MouseEvent e);
  /**
   * IS called when the mouse is moved over the Dragject
   * @param d The dragject involved
   * @param onoff true if the mouse entered the Dragject, false if it
   * leaves the area of the Dragject
   */
  void mouseOver(Dragject d,boolean onoff);

}