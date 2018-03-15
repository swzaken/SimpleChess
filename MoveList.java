import java.util.Vector;

public class MoveList
{
 /**********************************************************/
// fields
 /**********************************************************/
 private Vector v=new Vector();
/**********************************************************/
// constructors
/**********************************************************/
 public MoveList(){

 }
 /**********************************************************/
 // public methods
 /**********************************************************/
 /**
  * add a move to this list
  * @param m the move to add
  */
 public void add(Move m){
  v.addElement(m);
 }
 /**
  * make this list empty
  */
 public void clear(){
  v.clear();
 }
 /**
  * returns a Move from this list
  * @param i a move number
  * @return the move at position i
  */
 public Move get(int i){
  return (Move)v.elementAt(i);
 }
 /**
  * remove a Move from this list
  * @param i the number of a Move to remove
  */
 public void remove(int i){
  v.remove(i);
 }

 /**
  * @return the number of moves in this list
  */
 public int size(){
  return v.size();
 }
 /**
  * get the to fields of Moves that start at a certain field
  * a certain field
  * @param source a field number
  * @return the to fields of all moves in this list that start at source. If
  * none an array of length 0 is returned
  */
 public int[] getTargets(int source){
  int s=0;
  for(int i=0;i<size();i++)
   if(get(i).getFrom()==source)
    s++;
  int[] r=new int[s];
  s=0;
  for(int i=0;i<size();i++)
     if(get(i).getFrom()==source)
      r[s++]=get(i).getTo();
  return r;
 }
 /**
  * get a move starting at a certain point
  * @param source a field number
  * @return a move that whose from field equals source, or null
  */
 public Move getFrom(int source){
  for(int i=0;i<size();i++)
    if(get(i).getFrom()==source)
     return get(i);
  return null;
 }
 /**
  * get a move starting at a certain field, going to a certain field
  * @param from a field number
  * @param to a field number
  * @return all moves whose from field equals from and whose to field equals to
  */
 public Move getFromTo(int from,int to){
   for(int i=0;i<size();i++)
     if(get(i).getFrom()==from&&get(i).getTo()==to)
      return get(i);
   return null;
 }

/**
 * get a move starting at a certain field, going to a certain field with
 * a cetain option.
 * @param from a field number
 * @param to a field number
 * @return all moves whose from field equals from,whose to field equals to
 * and whose option field equals option.
*/
 public Move getFromToOption(int from,int to,int option){
   for(int i=0;i<size();i++)
     if(get(i).getFrom()==from&&get(i).getTo()==to&&get(i).getOption()==option)
      return get(i);
   return null;
 }
 /**
  * find out whether a certain move is contained in this list
  * @param m a move
  * @return true if this list contains a move equal to m
  */
 public boolean contains(Move m){
  for(int i=0;i<size();i++)
   if(get(i).equals(m))
    return true;
  return false;
 }
 /**
  * print the whole list to standard output.
  */
 public void print(){
  System.out.println("Movelist "+v.size());
  for(int i=0;i<size();i++)
   System.out.println(i+": "+get(i));
 }
 /**
  * returns the move described by the Pgn notation String.
  * examples:
  * e4 Kg4 Na2 fxe3 O-O
  * @param move
  * @return
  */
 private int getPgnPiece(char c){
  if(c=='N')
   return Situation.KNIGHT;
  if(c=='K')
   return Situation.KING;
  if(c=='B')
    return Situation.BISHOP;
  if(c=='R')
    return Situation.ROOK;
  if(c=='Q')
    return Situation.QUEEN;
  if(c=='P')
   return Situation.PAWN;
  return -1;
 }

 int getPgnPromotion(String s){
  int split = s.indexOf("=");
  if (split == -1)
   return Move.NORMAL;
  return getPgnPiece(s.charAt(split+1));
 }

 int getPgnToField(String s){
  int i = s.length()-1;
  while(i>0&&"12345678".indexOf(s.charAt(i))==-1)
   i--;
  if(i==0)
   return -1;
  return f(s.charAt(i-1)-'a',s.charAt(i)-'1');
 }

 int getPgnFromRank(String s){
  int i = s.length()-1;
  int j=0;//used to find second digit
  for(;i>=0;i--)
   if("12345678".indexOf(s.charAt(i))!=-1)
    if(++j==2)
     break;
  if(i<0)
    return -1;
  return s.charAt(i)-'1';
 }

 int getPgnFromFile(String s){
  int i = s.length()-1;
  int j=0;//used to find second digit
  for(;i>=0;i--)
   if("abcdefgh".indexOf(s.charAt(i))!=-1)
    if(++j==2)
     break;
  if(i<0)
    return -1;
  return s.charAt(i)-'a';
 }

 int getPgnPiece(String s){
  if ("PKNBQR".indexOf(s.charAt(0)) != -1)
   return getPgnPiece(s.charAt(0));
  return Situation.PAWN;
 }

 public Move findPgn(String move,Situation sit){
  if(move.equals("O-O")){
   for(int i=0;i<size();i++)
    if(get(i).getOption()==Move.OO)
     return get(i);
    return null;
  }
  if(move.equals("O-O-O")){
   for(int i=0;i<size();i++)
    if(get(i).getOption()==Move.OOO)
     return get(i);
    return null;
  }
  int to=getPgnToField(move);
  int piece=getPgnPiece(move);
  int rank=getPgnFromRank(move);
  int option=getPgnPromotion(move);
  int file=getPgnFromFile(move);
  for(int i=0;i<size();i++){
   Move m=get(i);
   if((m.getTo()==to)&&
      (piece==-1||sit.getPiece(m.getFrom())==piece)&&
      (m.getOption()==option||option==Move.NORMAL)&&
      (rank==-1||fy(m.getFrom())==rank)&&
      (file==-1||fx(m.getFrom())==file)
      )
    return m;
  }
  return null;
 }
 /*******************************************************/
// calculate numbers of fields
/*******************************************************/

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