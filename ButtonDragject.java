import java.awt.*;
import java.awt.event.*;
/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class ButtonDragject extends TextDragject
implements DragGroundListener
{
 ButtonCommand command;
 Color offColor=Color.gray,onColor=Color.black,overColor=Color.blue;

 public ButtonDragject(ButtonCommand command)
 {
  super(command.getText());
  this.command=command;
  setDragClick(false,true,true);
  this.addListener(this);
 }

 public void setColor(Color offColor,Color onColor,Color overColor){
  this.offColor=offColor;
  this.onColor=onColor;
  this.overColor=overColor;
 }

 public void draw(Graphics g, DragGround dg, long time) {
    if(command.isEnabled())
     setColor(onColor,overColor);
    else
     setColor(offColor,offColor);
    super.draw(g,dg,time);
  }
 public String getText(long time){
  return command.getText();
 }

 public void clicked(Dragject d, MouseEvent e) {
  if(command.isEnabled())
   command.getListener().doCommand(command.getText());
 }

 public void dragged(Dragject d, int px, int py, MouseEvent e) {
 }

 public void mouseOver(Dragject d, boolean onoff) {
  setHigh(onoff);
 }


}