import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class TextDragject extends Dragject {
 String text;
 Font font;
 Color color,highColor;
 FontMetrics metrics;

 static final Font defaultfont=new Font("Arial,Helvetica",Font.PLAIN,13);

 public TextDragject(String s)
 {
  text=s;
  setFont(defaultfont);
  color=Color.black;
  highColor=Color.red;
 }
 public TextDragject(String s,int x,int y)
 {
  this(s);
  setPosition(x,y);
 }

 void setFont(Font f){
  font=f;
  metrics=Toolkit.getDefaultToolkit().getFontMetrics(font);
  sizex=metrics.stringWidth(text);
  sizey=metrics.getHeight();
 }

 void setColor(Color color,Color highColor){
  this.color=color;
  this.highColor=highColor;
 }
 public String getText(long time){return text;}

 public void setText(String s){
  text=s;
  sizex=metrics.stringWidth(text);
 }

 public void draw(Graphics g,DragGround dg,long time){
  g.setFont(font);
  if(isHigh)
   g.setColor(highColor);
  else
   g.setColor(color);
  g.drawString(getText(time),getTopX(),getTopY()+metrics.getAscent());
 }

}