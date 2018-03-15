import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class PromotionFrame extends ImageSelectFrame
implements ImageSelectListener
{
 Image rook,knight,bishop,queen;
 MatchControl control;

 public PromotionFrame(int color,int sizex,int sizey,
                       Image[][] image,MatchControl control){
  super(4,1,sizex,sizey,"select piece");
  this.control=control;
  rook=image[Move.ROOK][color];
  knight=image[Move.KNIGHT][color];
  bishop=image[Move.BISHOP][color];
  queen=image[Move.QUEEN][color];
  addImage(rook);
  addImage(knight);
  addImage(bishop);
  addImage(queen);
  addListener(this);
 }

 public void select(ImageSelectFrame isf,Image im){
  if(im==rook)
   control.promote(this,Situation.ROOK);
  else if(im==knight)
   control.promote(this,Situation.KNIGHT);
  else if(im==bishop)
   control.promote(this,Situation.BISHOP);
  else if(im==queen)
   control.promote(this,Situation.QUEEN);
 }


}