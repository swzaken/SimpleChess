

import java.awt.*;
/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * this dragject can display a timer.
 */

public class ClockDragject extends TextDragject {
 /*******************************************/
 //fields
 /*******************************************/
 Timer timer;
 /*******************************************/
 // constructors
 /*******************************************/
 /**
  * create a dragject showing this timer.
  * @param timer
  */
 public ClockDragject(Timer timer){
  super(""+timer);
  this.timer=timer;
 }
 /*******************************************/
 // public methods
 /*******************************************/
 /**
  * returns the current value of the timer
  * @param time the time for which to show the value.
  * @return the current time in String format
  */
 public String getText(long time){
  return timer.toString(time);
 }

}