import java.io.*;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * A Chess game consists of a current situation and everything needed to
 * go back and forth by undoing or redoing moves.
 */

public class ChessGame {
 /**
  * current situation
  */
 Situation sit=new Situation();
 /**
  * all previous situations
  */
 Stack oldSituations=new Stack();
 /**
  * all moves done
  */
 Stack oldMoves=new Stack();
 /**
  * all moves yet to do
  */
 Stack futureMoves=new Stack();

 public ChessGame(){}

 public ChessGame(Situation sit){
  this.sit=sit;
 }

 public Situation getSituation(){
  return sit;
 }

 public void doMove(Move m){
  oldSituations.push(sit);
  oldMoves.push(m);
  sit=sit.getNextSituation(m);
  futureMoves.empty();
 }
 public Move redoMove(){
  Move m=(Move)futureMoves.pop();
  oldSituations.push(sit);
  oldMoves.push(m);
  sit=sit.getNextSituation(m);
  return m;
 }
 public void undoMove(){
  futureMoves.push(oldMoves.pop());
  sit=(Situation)oldSituations.pop();
 }
 public boolean canUndo(){
  return oldSituations.size()>0;
 }
 public Move lastMove(){
  if(canUndo())
   return (Move)oldMoves.peek();
  return null;
 }

 public boolean canRedo(){
  return futureMoves.size()>0;
 }

 public void rewind(){
  while(canUndo())
   undoMove();
 }

}

class PgnReader{
 BufferedReader reader;
 StringTokenizer tok;

 void open(File f) {
  try {
   reader = new BufferedReader(new FileReader(f));
  }
  catch (Exception e) {
   e.printStackTrace();
  }
 }
 Exception error;

 private String eventComment, whiteComment, blackComment, siteComment,
     dateComment,roundComment,resultComment;

 String readLine(){
  try{
    return reader.readLine().trim();
  }catch(Exception e){
     error=e;
     e.printStackTrace();
     return null;
  }
 }

 public ChessGame readGame(){
  ChessGame game=new ChessGame();
  readComments();
  if(error!=null)
   return null;
  System.out.println(whiteComment+" vs "+blackComment);
  String s;
  while(error==null){
   s=getMoveToken();
   if(s==null)
    break;
   MoveList list=game.getSituation().getValidMoves();
   Move m=list.findPgn(s,game.getSituation());
   //System.out.println(s+" -> "+m);
   if(m!=null)
    game.doMove(m);
   else{
    System.out.println("token "+s+" is invalid");
    System.out.println("Situation "+game.getSituation());
    error=new Exception("Invalid move token:"+s);
    break;
   }
  }
  if(error!=null)
   return null;
  return game;
 }

 void readComments(){
  int comments=0;
  System.out.print(" comments ");
  while(error==null){
   String s=readLine();
   if(s==null)
    error=new Exception("end of file");
   if(error!=null)
    break;
   if(s.trim().length()==0){
    if (comments > 0)
     return;
    else
     continue;
   }
   if (! (s.startsWith("[") && s.endsWith("\"]")))
    return;
   comments++;
   if (s.startsWith("[Event")){
    eventComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[Site")){
    siteComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[Date")){
    dateComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[Round")){
    roundComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[White")){
    whiteComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[Black")){
    blackComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
   if (s.startsWith("[Result")){
    resultComment=s.substring(s.indexOf('"')+1,s.lastIndexOf('\"'));
   }
  }
 }
 public String getMoveToken(){
  String r;
  while(error==null){
   if(tok!=null&&tok.hasMoreTokens()){
    r = tok.nextToken();
    if(r.indexOf('.')==-1)
     return r;
    continue;
   }
   String line=readLine();
   if(line.length()!=0)
    tok=new StringTokenizer(line);
   else
    break;
  }
  return null;
 }

 public static void main(String[] ps){
  File f=new File("C:/download/chess openings/eco/eco.pgn");
  OpeningBook book=new OpeningBook();
  PgnReader reader=new PgnReader();
  reader.open(f);
  while(true){
   ChessGame game=reader.readGame();
   if(game==null)
    break;
   game.rewind();
   for(int i=0;i<12;i++){
    if(game.canRedo())
    {
     book.add(game.getSituation().toString(),game.redoMove());
    }
   }
  }
  System.out.println("saving");
  book.save(new File("C:/download/chess openings/ecobook.txt"));
 }
}