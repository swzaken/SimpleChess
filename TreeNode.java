import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class TreeNode
implements Comparable
{
 //a move
 Move move;
 //the resulting situation
 Situation sit;
 //the evaluation of that situation
 //higher is better for the player to move
 int score;
 //the successors of the situation
 Vector successors;
 TreeNode conclusion;

 private static int[] levelsReached=new int[10];

 static void printLevels(){
  String s="levels ";
  for(int i=0;i<levelsReached.length;i++)
   s+=levelsReached[i]+" ";
  System.out.println(s);
 }

 /**
  * the thoroughness with which search is done.
  * Higher is better.
  * The meaning (subject to change,14-12-2003) is
  * 0 only move estimate, 1 quickScore,
  * 2 quickScore+capture back
  * 3..5 the number of moves ahead.
  */
 int exploredLevel;

 private static final int UNDEFINED=Integer.MIN_VALUE;

 public TreeNode(Move m,Situation s)
 {
  move=m;
  sit=s;
  score=UNDEFINED;
 }

 public int getScore(){
  return score;
 }
 public int getChildScore(int i){
  return -getNext(i).getScore();
 }
 public void setEmpty(){
  successors=null;
 }
 public void setAllEmpty(){
  for(int i=0;i<size();i++)
   getNext(i).setEmpty();
 }


 public void setScore(int score,int level){
  this.score=score;
  exploredLevel=level;
 }
 public TreeNode getNext(int i){
  return (TreeNode)successors.elementAt(i);
 }
 public Move getMove(){
  return move;
 }

 public int size(){
  if(successors==null)
   return 0;
  return successors.size();
 }

 public void add(TreeNode t){
  if(successors==null)
   successors=new Vector();
  successors.addElement(t);
 }
 /**
  * sort this list, move with lowest score first
  */
 public void sort(int n){
  if(n==size()){
   Collections.sort(successors);
   Collections.reverse(successors);
   return;
  }
  Vector temp=new Vector(n);
  for(int i=0;i<n;i++)
    temp.addElement(successors.elementAt(i));
  Collections.sort(temp);
  Collections.reverse(temp);
  for(int i=0;i<n;i++)
    successors.setElementAt(temp.elementAt(i),i);
 }
 /**
  * return 1 if this>b, -1 if this<b, 0 if equal
  * @param o
  * @return
  */
 public int compareTo(Object o){
  TreeNode node=(TreeNode)o;
  if(node.getScore()==getScore())
   return 0;
  return node.getScore()>getScore()?1:-1;
 }
 /**
  *
  * @param player the search functions to use.
  * @param level the level determines how much further we can seek.
  * @param alpha lower bound on this node's value. Alpha increases
  * each move that we try.
  * @param beta Higher bound on this nodes value.
  * A sufficient value to return, so if a move with value higher
  * than beta is found, we return.
  * @return
  */
 public int search(ComputerPlayer player,int level,int alpha,int beta,boolean keep){
  conclusion=null;
  if(sit.isFinished()){
   if(sit.isDraw())
    score=0;
   else
    score=ComputerPlayer.LOW-level;
   return score;
  }
  if(level==0){
   //score=reducedSearch(player,sit,move);
   score=player.quickScore(sit);
  }
  else{
   search2(player,level,alpha,beta,keep);
   score=-getNext(0).getScore();
   conclusion=getNext(0);
  }
  return score;
 }

 private int search2(ComputerPlayer player,int level,int alpha,int beta,boolean keep){
  if(successors==null||successors.size()==0){
    MoveList list=sit.getValidMoves();
    //create nodes for all moves
    for(int i=0;i<list.size();i++)
     add(new TreeNode(list.get(i),sit.getNextSituation(list.get(i))));
    //determine quickscores for moves
    int best=-1;
    /*optional: presort using quickScore*/
    for(int i=0;i<size();i++)
     getNext(i).setScore(-player.quickScore(list.get(i),sit),0);
    sort(size());
   }
   int i;
   for(i=0;i<size();i++){
     getNext(i).search(player,level-1,-beta,-alpha,false);
     if(getChildScore(i)>=alpha)
      alpha=getChildScore(i);
      if(alpha>beta){
       i++;
       break;
      }
    }
    sort(i);
    setScore(getChildScore(0),level);
    if(!keep){
     setAllEmpty();
    }
    return score;
   }





 public void print(){
  System.out.print("   "+move+" :"+score+"   ");
 }

 public void printAll(){
  System.out.println("ALL of> "+move+" ("+score+")");
  for(int i=0;i<size();i++)
   getNext(i).print();
  System.out.println("\nPath="+getPath());
 }
 public String getPath(){
  if(conclusion==null)
   return "["+move+":"+score+"]";
  return " "+move+":"+score+" "+conclusion.getPath();
 }

 int reducedSearch(ComputerPlayer player,Situation afterm,Move m){
  int alpha=player.quickScore(afterm),alpha2;
  MoveList list=afterm.getValidMoves();
  int to=m.getTo();
  for(int i=0;i<list.size();i++)
   if(list.get(i).getCapture()==to){
    Situation next=afterm.getNextSituation(list.get(i));
    alpha2=-reducedSearch(player,next,list.get(i));
    if(alpha2>alpha){
     alpha=alpha2;
     //System.out.println("red:"+m+"("+alpha2+")");
    }
   }
  return alpha;
 }

}