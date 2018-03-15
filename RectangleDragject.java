import java.awt.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RectangleDragject extends Dragject {
/**************************************************/
// fields
/**************************************************/
 Color incolor=Color.gray;
 Color outcolor=Color.black;
 Color highincolor=Color.blue;
 Color highoutcolor=Color.black;
 /**************************************************/
 // constructors
 /**************************************************/
 /**
  * create a RectangleDragject of a certain size
  * @param sx horizontal size
  * @param sy vertical size
  */
 public RectangleDragject(int sx,int sy){
   sizex=sx;
   sizey=sy;
  }
  /**************************************************/
  // public methods
  /**************************************************/

  /**
   * set the colors to Draw this object with.
   * @param inside
   * @param outline
   * @param highinside
   * @param highoutline
   */
  public void setColor(Color inside,Color outline,Color highinside,Color highoutline){
   incolor=inside;
   outcolor=outline;
   highincolor=highinside;
   highoutcolor=highoutline;
  }
  public void draw(Graphics g,DragGround dg,long time){
   if(isHigh){
    if(highincolor!=null){
     g.setColor(highincolor);
     g.fillRect(getTopX(),getTopY(),sizex,sizey);
    }
    if(highoutcolor!=null){
     g.setColor(highoutcolor);
     g.drawRect(getTopX(),getTopY(),sizex,sizey);
    }
   }else{
     if(incolor!=null){
      g.setColor(incolor);
      g.fillRect(getTopX(),getTopY(),sizex,sizey);
     }
     if(outcolor!=null){
      g.setColor(outcolor);
      g.drawRect(getTopX(),getTopY(),sizex,sizey);
     }
    }
   }


}