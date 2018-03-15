import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class EditSelectFrame extends ImageSelectFrame
implements ImageSelectListener{
 int selectedPiece,selectedColor;
 MatchControl control;
 Image[][] image;

 public EditSelectFrame(int fieldsizex,
                        int fieldsizey,Image[][] image,MatchControl control){
   super(6,2,fieldsizex,fieldsizey,"Select piece");
    this.image=image;
    for(int i=0;i<2;i++){
     addImage(image[Situation.PAWN][i]);
     addImage(image[Situation.ROOK][i]);
     addImage(image[Situation.KNIGHT][i]);
     addImage(image[Situation.BISHOP][i]);
     addImage(image[Situation.QUEEN][i]);
     addImage(image[Situation.KING][i]);
    }
   addListener(this);
   this.control=control;
 }
 public synchronized void select(ImageSelectFrame isf,Image im){
  for(selectedPiece=0;selectedPiece<image.length;selectedPiece++)
   for(selectedColor=0;selectedColor<2;selectedColor++)
    if(image[selectedPiece][selectedColor]==im)
    {
     showSelected(im);
     return;
    }
  System.out.println("duh");
  selectedColor=0;
  selectedPiece=0;
 }

 public synchronized int getSelectedPiece(){
  return selectedPiece;
 }
 public synchronized int getSelectedColor(){
  return selectedColor;
 }
}