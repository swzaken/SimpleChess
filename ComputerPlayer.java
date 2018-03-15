

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class ComputerPlayer implements Runnable{
 /**
  * the current thread.
  */
 Thread thread;
 /**
  * time started thinking on a move. To make this player more human,
  * it is not allowed to think less that two seconds.
  */
 private long startthinktime;

 /**
  * if interrupted is true, any long calculation should be terminated as soon
  * as possible, in a reasonable way. Will be used for: "move now".
  */
 private volatile boolean interrupted;
 /**
  *
  */
 private volatile boolean mustAnswer;
 /**
  * indicated whether the tread should end and teh object destroyed
  */
 private boolean finished;
 /**
  * the situation this object thinks about. null if not thinking
  */
 private volatile Situation situation;
 /**
  * the object interested in the answer
  */
 private MatchControl matchcontrol;
 /**
  * opening book to use
  */
 private OpeningBook book;

 public final static int LOW=-1000000,HIGH=1000000;
 /****************************************************************/
 // constructors
 /****************************************************************/
 /**
  * create and start a player
  */
 public ComputerPlayer(OpeningBook book){
  this.book=book;
  interrupted=finished=false;
  thread=new Thread(this);
  thread.start();
 }
 /**
  * start thinking on a new move. This invalidates the old
  * @param control
  * @param sit
  */
 void request(MatchControl control,Situation sit){
  mustAnswer=false;
  if(isWorking()){
   interrupt();
  }
  synchronized(this){
   mustAnswer = true;
   situation = sit;
   matchcontrol = control;
   interrupted = false;
   notify();
  }
 }
 /**
  * while finished is falsed, it continually waits until
  * Situation is not null, then it starts calculating on a move.
  */
 public void run(){
  System.out.println("running");
  while(!finished)
  {
    try{
     synchronized(this){
      wait(1024);
     }
    }
    catch(Exception e){
     e.printStackTrace();
    }
    if(situation!=null){
     Move m=calcMoveDelay(situation,1300);
     if(mustAnswer)
      matchcontrol.answer(m,situation);
     situation=null;
    }
   }
 }
 /**
  * stops the current calculation
  */
 public void interrupt(){
  interrupted=true;
  synchronized(this){
   notify();
  }
  while(situation!=null){
   try{Thread.sleep(100);}
   catch(InterruptedException e){}
  }
 }

 public void destroy(){
  mustAnswer=false;
  finished=true;
  interrupt();
 }

 public boolean isWorking(){
  return (situation!=null);
 }

 public boolean isWorkingOn(Situation sit){
  return situation==sit;
 }

 public Move calcMoveDelay(Situation sit,long delay){
  startthinktime=System.currentTimeMillis();
  Move m=calcMove(sit);
  startthinktime=System.currentTimeMillis()-startthinktime;
  if(interrupted||!mustAnswer)
   return null;
  if(startthinktime<delay)
  {
   try {
    Thread.sleep(delay - startthinktime);
   }
   catch (InterruptedException ex) {
   }
  }
  return m;
 }


 public Move calcMove(Situation sit)
 {
  System.out.println("quickscore="+quickScore(sit));
  Move opening=book.getRandomMove(""+sit);
  MoveList list=sit.getValidMoves();
  if(opening!=null){
   if(!list.contains(opening))
    System.out.println("opening book error "+sit+":"+opening);
   System.out.println("book move");
   return opening;
  }
  TreeNode t=new TreeNode(null,sit);
  System.out.println("first search quickscore="+t.search(this,2,LOW,HIGH,true));
  System.out.println("first path:"+t.getPath());
  System.out.println("second search quickscore="+t.search(this,4,LOW,HIGH,false));
  System.out.println("second path:"+t.getPath());
  return t.getNext(0).getMove();
 }

 static int getKingBonus(int fx,int fy){
  int[] kingline={20,30,20,15,25,20,30,20};
  if(fx==0)
   return kingline[fx];
  else return 0;
 }
 static int getKnightBonus(int fx,int fy){
  if(fx==0||fy==0||fx==7||fy==7)
   return 0;
  if(fx==1||fy==1||fx==6||fy==6)
   return 9;
  return 15;
 }
 static int getPawnBonus(int fx,int fy){
  int[] midline={0,0,14,40,40,50,150,200};
  int[] outline={0,0,0,0,0,50,150,200};
  if(fx==3||fx==4){
   return midline[fy];
  }
  return outline[fy];
 }


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


 static int quickValue(int type,int field,int color){
  int fx=fx(field);
  int fy=fy(field);
  if(color==Situation.BLACK){
   fx=7-fx;
   fy=7-fy;
  }
  if(type==Situation.PAWN)
   return 100+getPawnBonus(fx,fy);
  else if(type==Situation.ROOK)
   return 500;
  else if(type==Situation.KNIGHT)
   return 300+getKnightBonus(fx,fy);
  else if(type==Situation.BISHOP)
   return 350;
  else if(type==Situation.QUEEN)
   return 900;
  else if(type==Situation.KING)
   return 3000+getKingBonus(fx,fy);
  return 0;
 }
 /**
  * return a quick estimate of the benefit of this move, based
  * upon the value f to, from and capture. does not work well for
  * castling, does work for enpassant.
  * @param m a move
  * @param s the situation to which the move applies
  * @return a score between LOW and HIGH
  */
 public int quickScore(Move m,Situation s){
   int from=m.getFrom(),to=m.getTo(),cap=m.getCapture();
   int r=-quickValue(s.getPiece(from),from,s.getColor(from))
         +quickValue(s.getPiece(from),to,s.getColor(from));
   if(s.isOccupied(cap))
    r+=quickValue(s.getPiece(cap),cap,s.getColor(cap));
   return r;
  }

 static int quickScore(Situation s){
  int gstatus=s.getGameStatus();
  if(gstatus==Situation.CHECKMATE)
   return LOW;
  int score[]=new int[2];
  for(int i=0;i<64;i++){
   if(s.isOccupied(i))
    score[s.getColor(i)]+=quickValue(s.getPiece(i),i,s.getColor(i));
  }
  int randomizer=(int)(Math.random()*5);
  return score[s.getPlayer()]-score[s.oppositeColor(s.getPlayer())]+randomizer;
 }
}