

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class AttackLine {
 int startfield;
 boolean isRook;
 int[] direction;
 int length;
 int blockfield=-1;

 public AttackLine(int field,int[] dir,boolean typeIsRook){
  startfield=field;
  direction=dir;
  isRook=typeIsRook;
 }

 void setLength(int l){
  length=l;
 }
 public int getLength(){
  return length;
 }
 public void setBlocked(int field){
  blockfield=field;
 }
 public int getBlocked(){
  return blockfield;
 }
 public boolean isBlocked(){
  return blockfield!=-1;
 }

 public void reverse()
 {
  startfield=getField(length);
  direction[0]=-direction[0];
  direction[1]=-direction[1];
 }
 public int getField(int i){
  return f(fx(startfield)+i*direction[0],fy(startfield)+i*direction[1]);
 }

 boolean fitsPiece(int piece){
  return (piece==Situation.QUEEN||(isRook&&piece==Situation.ROOK)
     ||(!isRook&&piece==Situation.BISHOP));
 }
 /***************************************************************/

 private static int f(int column,int row){
  if(column<0||column>7||row<0||row>7)
   return -1;
  return column+8*row;
 }
 private static int fx(int field){
  return field%8;
 }
 private static int fy(int field){
   return field/8;
  }

}