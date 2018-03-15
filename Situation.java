import java.util.Vector;

/**
* represents a board situation in Chess
*/
class Situation
{
 /*piece types*/
 public final static int BLACK=1,WHITE=0;
 public final static int PAWN=1,ROOK=2,KNIGHT=3,BISHOP=4,QUEEN=5,KING=6;
 /*all game status. STALEMATE means draw, checkmate means this player lost,
  normal and check mean the game is still on.
  */
 public final static int NORMAL=1,CHECK=2,CHECKMATE=3,STALEMATE=4;
 private final static int UNDEFINED=5;

 /*the player to move*/
 private int player;
 /*stores the piece on each field*/
 private int[] fieldvalue=new int[64];
 /*indicates*/
 private int gameStatus;
 /*are true if corresponding castling move is still possible*/
 private boolean castleSW,castleLW,castleSB,castleLB;

 /** The field where, in the previous move, moved the
  * pawn to in a double initial move. -1 if this did
  * not happen.
  */
 private int pawndoublemove;

 /**
  * the list of all valid moves of this situation. Is generated when needed
  * and cached for future use. Must be set to zero if a set function is called
  */
  private MoveList list;
 /*******************************************************/
 //initialisation
 /*******************************************************/
 public Situation(){
  reset();
 }

 /**
  * initializes board to opening situation
  */
  void reset(){
   int[] myline={ROOK,KNIGHT,BISHOP,QUEEN,KING,BISHOP,KNIGHT,ROOK};
   int[] mycolumn={1,1,0,0,0,0,-1,-1};
   for(int i=0;i<8;i++)
    for(int j=0;j<8;j++)
     if(j==1||j==6)
      fieldvalue[f(i,j)]=PAWN*mycolumn[j];
     else
          fieldvalue[f(i,j)]=myline[i]*mycolumn[j];
   pawndoublemove=-1;
   castleSW=castleLW=castleSB=castleLB=true;
   player=WHITE;
   gameStatus=NORMAL;
  }

  /**
   * make a copy of a Situation
   * @param original the situation to copy
   */
  public Situation(Situation original){
   for(int i=0;i<64;i++){
    if(!original.isOccupied(i))
     fieldvalue[i]=0;
    else if(original.getColor(i)==WHITE)
     fieldvalue[i]=original.getPiece(i);
    else
     fieldvalue[i]=-original.getPiece(i);
   }
   castleSW=original.canCastle(true,true);
   castleLW=original.canCastle(true,false);
   castleSB=original.canCastle(false,true);
   castleLB=original.canCastle(false,false);
   player=original.getPlayer();
   pawndoublemove=original.getLastPawnDouble();
  }

/*******************************************************/
// get functions
/*******************************************************/
 /**
 * @return player currently to move
 */
 public int getPlayer(){return player;}
 /**
 * returns whether field is occupied
 */

 public static int oppositeColor(int color){
  return (color==WHITE)?BLACK:WHITE;
 }

 public boolean isOccupied(int field){
  return fieldvalue[field]!=0;
 }
 /**
 * returns type of piece on field
 */
 public int getPiece(int field){
  if(fieldvalue[field]==0){
   new Throwable("Do not ask empty fields!!!").printStackTrace();
   System.exit(1);
  }
  return Math.abs(fieldvalue[field]);
 }
 /**
 * returns color of current field (black, white, -1 for empty)
 */
 public int getColor(int field){
  if(fieldvalue[field]>0)
   return WHITE;
  if(fieldvalue[field]<0)
   return BLACK;
  return -1;
 }
 /**
  * returns true if game has ended
  */
  public boolean isFinished(){
   return getGameStatus()==STALEMATE||getGameStatus()==CHECKMATE;
  }
  /**
  * returns true if game has ended and neither side won
  */
  public boolean isDraw(){
   return getGameStatus()==STALEMATE;
  }
  /**
  * if the game has finished, this function returns either WHITE, BLACK or -1
  */
  public int getGameStatus(){
   if(gameStatus==UNDEFINED)
    setGameStatus();
   return gameStatus;
  }

  /**
  * returns true if indicate way of castling is still allowed
  */
  public boolean canCastle(boolean white, boolean isShort){
   if(white)
    return (isShort&&castleSW)||(!isShort&&castleLW);
   return (isShort&&castleSB)||(!isShort&&castleLB);
  }
  /**
  * returns -1 if last move was not a two field pawn move. Otherwise
  * it returns the number of the field the pawn skipped.
  */
  public int getLastPawnDouble(){
   return pawndoublemove;
  }
  /**
   * checks whether this situation represents a valid
   * chess situation. Checks whether there are two kings,
   * whether there are not too many pieces, no pawns at
   * unlikely places.
   * @return null if everything is ok, or an error message
   */
  public String getErrorMessage(){
   int wking=KING,bking=-KING;
   int kw=0,kb=0;
   int wtotal,btotal;
   for(int i=0;i<64;i++){
    if(fieldvalue[i]==wking)
     kw++;
    else if(fieldvalue[i]==bking)
     kb++;
   }
   if(kw!=1||kb!=1)
    return "There should be one king for each color";
   for(int i=0;i<8;i++){
    int f0=f(i,0),f7=f(i,7);
    if((isOccupied(f0)&&getPiece(f0)==PAWN)||
       (isOccupied(f7)&&getPiece(f7)==PAWN))
     return "Pawns Cannot occur on the first or last row";
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



/*******************************************************/
// String representation
/*******************************************************/

 private char toChar(int value){
  if(value>=0)
   return " PRNBQK".charAt(value);
  else
   return " prnbqk".charAt(-value);
 }

 /**
 * returns String representation of board. It uses the FEN notation,
 * without the halfmoves and full moves since we do not have these counters
 * example:
 * Here's the FEN for the starting position:
   rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
   And after the move 1. e4:
   rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1
   And then after 1. ... c5:
   rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2
   And then after 2. Nf3:
   rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2
 */
 public String toString(){
  String s="";
  int emptycount=0;
  for(int j=7;j>=0;j--){
   for(int i=0;i<8;i++){
    if(!isOccupied(f(i,j)))
    {emptycount++;continue;}
    if(emptycount>0)
	    s+=emptycount;
    emptycount=0;
    s+=toChar(fieldvalue[f(i,j)]);
   }
   if(emptycount>0)
    s+=emptycount;
   emptycount=0;
   if(j<8)
    s+="/";
  }
  if(getPlayer()==WHITE)
   s+=" w ";
  else
   s+=" b ";

  if(castleSW)
   s+="K";
  if(castleLW)
   s+="Q";
  if(castleSB)
   s+="k";
  if(castleLB)
   s+="q";
  if(pawndoublemove==-1)
   s+=" -";
  else
   s+=" "+field2String(pawndoublemove);
  return s;
 }

 /**
  * translates field numbers to conventional names. For example
  * field2String(12)="E2"
  * @param i a field number
  * @return a string consisting of a character and a digit
  */
 private String field2String(int i){
  return (char)('a'+i%8)+""+(char)('1'+i/8);
 }


/*******************************************************/
// Move generation
/*******************************************************/


 public MoveList getValidMoves(){
  if(list!=null)
   return list;
  list=new MoveList();
  addMoves(getPlayer(),list);
  MoveList filteredList=new MoveList();
  for(int i=0;i<list.size();i++){
   Situation next=this.getNextSituation(list.get(i));
   if(!next.isChecked(getPlayer()))
    filteredList.add(list.get(i));
  }
  list=filteredList;
  return list;
 }


 /**
  * add all the valid moves of teh given color to the list.
  * @param color the side for which to generate
  * @param list the list to add the moves to.
  */



 private void addMoves(int color,MoveList list){
  for(int i=0;i<64;i++)
   if(isOccupied(i)&&getColor(i)==color)
    addMoves(i,color,list);
  addCastling(list,color);
 }
 /**
  * Add all moves for the piece on this field to the list
  * @param field a field containing a piece
  * @param color the color of the piece on the field
  * @param list the list to add the moves to
  */
 void addMoves(int field,int color,MoveList list){
  if(getPiece(field)==KING)
   addKingKnightMoves(kingtable,field,color,list);
  else if(getPiece(field)==KNIGHT) {
    addKingKnightMoves(knighttable, field, color, list);
  } else if(getPiece(field)==ROOK){
   addRookBishopMoves(rookDir,field,color,list);
  } else if(getPiece(field)==BISHOP){
   addRookBishopMoves(bishopDir,field,color,list);
  } else if(getPiece(field)==QUEEN){
   addRookBishopMoves(bishopDir,field,color,list);
   addRookBishopMoves(rookDir,field,color,list);
  } else if(getPiece(field)==PAWN){
   addPawnMoves(field,color,list);
  }

 }

 /**
  * these arrays list the fields involved in castling.
  * First field is the king, next the rook, the king target and
  * the remainder contains fields the king is travelling over.
  */
 private int[] WLCastling={f(4,0),f(0,0),f(2,0),f(3,0)};
 private int[] WSCastling={f(4,0),f(7,0),f(6,0),f(5,0)};
 private int[] BLCastling={f(4,7),f(0,7),f(2,7),f(3,7)};
 private int[] BSCastling={f(4,7),f(7,7),f(6,7),f(5,7)};

 /**
  * add castling moves to the list
  * @param list
  * @param color
  */
 private void addCastling(MoveList list, int color){
  if(color==WHITE){
   if(castleSW)
    addCastling(list,WSCastling,Move.OO,color);
   if(castleLW)
    addCastling(list,WLCastling,Move.OOO,color);
  }else{
   if(castleSB)
    addCastling(list,BSCastling,Move.OO,color);
   if(castleLB)
    addCastling(list,BLCastling,Move.OOO,color);
  }
 }
 /**
  * possibly add a castling move to the list
  * @param list the list to add to
  * @param fields array containing fields involved
  * @param option indicates long or short castling
  */
 private void addCastling(MoveList list,int[] fields,int option, int color){
  if(canMoveTo(fields[0],oppositeColor(color)))
   return;
  for(int i=2;i<fields.length;i++)
    if(isOccupied(fields[i])||canMoveTo(fields[i],oppositeColor(color)))
     return;
   list.add(new Move(fields[0],fields[2],option));
  }

 int[][] kingtable={{1,0,},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
 int[][] knighttable={{2,1,},{2,-1},{1,2},{1,-2},{-1,2},{-1,-2},{-2,1},{-2,-1}};

 private void addKingKnightMoves(int[][] table, int field, int color,
                                MoveList list) {
  int[] dest=getFields(field,table);
  for(int i=0;i<table.length;i++){
   if(dest[i]!=-1&&getColor(dest[i])!=color)
    list.add(new Move(field,dest[i]));
  }
 }

  int[][] rookDir={{1,0},{0,1},{-1,0},{0,-1}};
  int[][] bishopDir={{1,1},{-1,1},{1,-1},{-1,-1}};

  private void addRookBishopMoves(int[][] table, int field, int color,
                                 MoveList list) {
   for(int i=0;i<table.length;i++){
    int length=getLineLength(field,table[i]);
    for(int j=1;j<length;j++){
     int dest=getLineField(field,table[i],j);
     if(getColor(dest)!=color)//empty or opposite color
      list.add(new Move(field,dest));
    }
  }
 }

  /**
   * @param list the list to add to
   * @param from from field of move
   * @param to to field of move
   * @param color the color of the moving piece
   */
  private void addPawnMove(MoveList list,int from, int to){
   Move m;
   //we do not need the color because pawns cannot move backwards
   if(fy(to)==7||fy(to)==0){
    list.add(new Move(from,to,Move.QUEEN));
    list.add(new Move(from,to,Move.ROOK));
    list.add(new Move(from,to,Move.KNIGHT));
    list.add(new Move(from,to,Move.BISHOP));
   }else{
    list.add(new Move(from,to,Move.NORMAL));
   }
  }

  private void addPawnMoves(int field, int color,MoveList list){
   int dir=(color==WHITE)?1:-1;
   int fx=fx(field),fy=fy(field);
   int newfield=f(fx,fy+dir);
   /*move straight ahead*/
   if(newfield!=-1&&!isOccupied(newfield))
    addPawnMove(list,field,newfield);
   /*attack*/
   newfield=f(fx+1,fy+dir);
   if(newfield!=-1&&isOccupied(newfield)&&getColor(newfield)!=color)
    addPawnMove(list,field,newfield);
   newfield=f(fx-1,fy+dir);
   if(newfield!=-1&&isOccupied(newfield)&&getColor(newfield)!=color)
    addPawnMove(list,field,newfield);
   /*double initial move*/
   if((dir==1&&fy==1)||(dir==-1&&fy==6))
   {
    newfield=f(fx,fy+2*dir);
    if(!isOccupied(newfield)&&!isOccupied(f(fx,fy+dir)))
     list.add(new Move(field,newfield,Move.PAWNDOUBLE));
   }
   /*en passant*/
   if(pawndoublemove!=-1){
    int pdx=fx(pawndoublemove),pdy=fy(pawndoublemove);
    if(pdy==fy&&(pdx==fx-1||pdx==fx+1))
     list.add(new Move(field,f(pdx,fy+dir),Move.ENPASSANT));
   }
  }

 /************************************************************/
 // state changing functions
 /************************************************************/

 /**
  * does not change this Situation, but computes a next situation
  * @param m a valid move
  * @return the result situation when doing the given move
  */
 Situation getNextSituation(Move m){
  Situation s=new Situation(this);
  s.update(m);
  return s;
 }
 /**
  * make the indicated field empty
  * @param f a field number 0..63
  */
 void clearField(int f){
  list=null;
  fieldvalue[f]=0;
 }
 /**
  * place a piece on the indicated field
  * @param i a field number
  * @param type a piece type
  * @param color either BLACK or WHITE
  */
 void setField(int i,int type,int color){
  list=null;
  if(color==WHITE){
   fieldvalue[i]=type;
  }else{
   fieldvalue[i]=-type;
  }
 }

 void setPlayer(int color){
  list=null;
  player=color;
 }

 /**
  * change this situation into the result situation of doing m
  * @param m a valid move
  */
 void update(Move m){
  /*remove from old position set on new position*/
  int v=m.getFrom();
  int mycolor=getColor(v);
  fieldvalue[m.getTo()]=v;
  setField(m.getTo(),getPiece(v),mycolor);
  clearField(m.getFrom());
  /*remove attacked pawn for enpassant*/
  if(m.isEnPassant())
   clearField(f(fx(m.getTo()),fy(m.getFrom())));
   /*remove attacked pawn for enpassant*/
  else if(m.isPromotion())
   setField(m.getTo(),m.getOption(),mycolor);
  /*move rook in case of castling*/
  else if(m.getOption()==m.OO){
   setField(f(5,fy(v)),ROOK,mycolor);
   clearField(f(7,fy(v)));
  }
  else if(m.getOption()==m.OOO){
   setField(f(3,fy(v)),ROOK,mycolor);
   clearField(f(0,fy(v)));
  }
  /*change player to move*/
  if(player==WHITE)
   player=BLACK;
  else
   player=WHITE;
  /*check whether rook or king moved. If so, forbid castling*/
  if(fieldvalue[0]!=ROOK||fieldvalue[4]!=KING)
   castleLW=false;
  if(fieldvalue[7]!=ROOK||fieldvalue[4]!=KING)
   castleSW=false;
  if(fieldvalue[56]!=-ROOK||fieldvalue[60]!=-KING)
   castleLB=false;
  if(fieldvalue[63]!=-ROOK||fieldvalue[60]!=-KING)
   castleSB=false;
  pawndoublemove=(m.getOption()==Move.PAWNDOUBLE)?m.getTo():-1;
  gameStatus=UNDEFINED;
 }
/************************************************************/
// analysis functions
/************************************************************/



 private void setGameStatus(){
  boolean check=isChecked(getPlayer());
  boolean canMove=getValidMoves().size()>0;
   if(check&&canMove)
    gameStatus=CHECK;
   else if(check&&!canMove)
    gameStatus=CHECKMATE;
   else if(!check&&!canMove)
    gameStatus=STALEMATE;
   else if(!check&&canMove)
    gameStatus=NORMAL;
 }
 /************************************************************/

 private boolean canPawnAttack(int from,int to,int color){
  if(existsOccupiedColor(from,color)&&getPiece(from)==PAWN)
   return true;
  else
   return false;
 }
 private boolean canEnPassant(int from,int to,int color){
  int dir=color==WHITE?-1:1;
  int enpassantfield=f(fx(from),fy(to));
  if(getLastPawnDouble()!=-1&&enpassantfield==getLastPawnDouble()&&
     !isOccupied(to))
   return true;
  return false;
 }

 private void getPawnMovesTo(MoveList list,int color,int tofield,boolean attack){
   int tfx=fx(tofield),tfy=fy(tofield);
   int from=-1;
   int dir=color==WHITE?-1:1;
   if(attack){
    /*attack from the left*/
    from=f(tfx-1,tfy+dir);
    if(canPawnAttack(from,tofield,color))
     addPawnMove(list,from,tofield);
    from=f(tfx+1,tfy+dir);
    /*and attack from the right*/
    if(canPawnAttack(from,tofield,color))
     addPawnMove(list,from,tofield);
    /*get en passant*/
    //XXX

    }else{
    /*come from front*/
    from=f(tfx,tfy+dir);
    if(existsOccupiedColor(from,color)&&getPiece(from)==PAWN)
     addPawnMove(list,from,tofield);
    /*do a pawndouble*/
    from=f(tfx,tfy+2*dir);
    int empty=f(tfx,tfy+dir);
    boolean candouble=(color==WHITE&&tfy==3)||(color==BLACK&&tfy==4);
    if(candouble&&existsOccupiedColor(from,color)&&
       getPiece(from)==PAWN&&!isOccupied(empty))
     list.add(new Move(from,tofield,Move.PAWNDOUBLE));
    //enpassant
   }
 }

 /**
  * notes: castling is not considered.
  * enpassant to a field does not attack that field, thus is
  * generated when attack is false.
  * @param field the to field of moves you are looking for
  * @param color the color that should move to the field
  * @param attack if true it must be a move attacking field.
  * This is only different for pawns.
  * @return true if the given side can move to the given field
  */
 public boolean canMoveTo(int field, int color){
  return canJumpAttack(field,color)||canLineAttack(field,color);
 }
 /**
   * @param field the field the attacker must move to
   * @param table a king, knight or pawntable.
   * @param color the color the attacker must be
   * @param type the type the attacker must be.
   * @return
   */
  private boolean canJumpAttack(int field,int[][] table,int color,int type){
   int r=0;
   int[] dest=getFields(field,table);
   for(int i=0;i<dest.length;i++){
    if(existsOccupiedColor(dest[i],color)&&getPiece(dest[i])==type)
     return true;
   }
   return false;
  }
  /**
   * note that we are reverse-seeking moves, starting with a given to
   * field. Therefore, the opposite pawntable must be used
   * @param field the field to attack to
   * @param color the color of attacking piece
   * @return whether an attack is possible
   */
  private boolean canJumpAttack(int field,int color){
   return canJumpAttack(field,kingtable,color,KING)||
   canJumpAttack(field,knighttable,color,KNIGHT)||
   canJumpAttack(field,getPawnAttackTable(oppositeColor(color)),color,PAWN);
  }
  private boolean canLineAttack(int field, int color){
   return canLineAttack(field,color,rookDir,true)||
       canLineAttack(field,color,bishopDir,false);
  }


  private boolean canLineAttack(int field, int color, int[][] dir,
                              boolean rook){
   for(int i=0;i<dir.length;i++)
    if(canLineAttack(field,color,dir[i],rook))
       return true;
   return false;
  }


  private boolean canLineAttack(int field, int color, int[] dir, boolean rook){
   AttackLine line=new AttackLine(field,dir,rook);
   for(int i=1;i<=8;i++){
    int f=line.getField(i);
    if(f==-1)
     break;
    if(!isOccupied(f))
     continue;
    return (getColor(f)==color&&line.fitsPiece(getPiece(f)));
   }
   return false;
  }
  int[][] wpawnattacktable={{1,1},{-1,1}};
  int[][] bpawnattacktable={{1,-1},{-1,-1}};
  int[][] wpawntable={{0,1}};
  int[][] bpawntable={{0,-1}};

  private int[][] getPawnAttackTable(int color){
    return (color==WHITE)?wpawnattacktable:bpawnattacktable;
  }

  private int findKing(int color){
    int kingvalue=(color==WHITE)?KING:-KING;
    int i;
    for(i=0;i<64;i++)
     if(fieldvalue[i]==kingvalue)
      return i;
    return -1;
   }

   /**
    * returns whether the king of the given site is in check
    * @param color
    * @return
    */
   private boolean isChecked(int color){
    return canMoveTo(findKing(color),oppositeColor(color));
   }




 /**
  * get destination fields of the translations described in table
  * @param field a start field
  * @param table a table of (tx,ty) fields
  * @return an array containg -1 or field numbers
  */
 private int[] getFields(int field,int[][] table){
  int fx=fx(field),fy=fy(field);
  int[] r=new int[table.length];
  for(int i=0;i<table.length;i++)
   r[i]=f(fx+table[i][0],fy+table[i][1]);
  return r;
 }

/********************************************************************/






/*
intermezzo: we would like to evaluate a situation:
  normal, check, checkmate or stalemate without doing
 explicitly all moves. This is not easy.

 Check can easily be established. If check, and we can move
 the king to a safer field, we are not in checkmate.

 A piece is pinned if removal would check us. This calls for the
 calculation of Xrays. Our own pieces can be pinned, or the opponents
 lastpawndouble (enpassant matters. argh!!)
 If there is only one attackline, we can move an unpinned piece
 in the attackline (does enpassant matter here? no.) In this case we
 are not checkmate.

 If not in check, in general we should have a move.


 Summary: we need a function for determining check

 We need a function calculating pinned pieces.

 We need a function for calculating moves when in check.

  detail: pinned pieces are allowed to move, as long as
 they stay on the line.


 conclusion:
 isAttacked1 determines attacks by kings, knights and pawns(no enpassant)
 getAttackLines returns all attack lines by rook, bishops and queens, including
 blocked lines by any piece

  */


 private Vector getAttackLines(int color,int field){
  Vector lines=new Vector();
  /*add lines to v*/
  //rook bishop and queen
  for(int i=0;i<4;i++){
   addAttackLine(lines,field,rookDir[i],color,true);//rook and queen
   addAttackLine(lines,field,bishopDir[i],color,false);//bishop and queen
  }
  return lines;
 }

 private AttackLine[] convert(Vector v){
  /*convert vector to array*/
  AttackLine[] r=new AttackLine[v.size()];
  for(int i=0;i<v.size();i++)
   r[i]=(AttackLine)v.elementAt(i);
  return r;
 }
 /**
  * returns the length of a line
  * @param field the start of the line
  * @param dir the direction of the line
  * @return number of fields including blocked field
  */
 private int getLineLength(int field,int[] dir){
  int fx=fx(field),fy=fy(field),newfield;
   for(int i=2;i<=8;i++){
    fx+=dir[0];
    fy+=dir[1];
    newfield=f(fx,fy);
    if(newfield==-1)
     return i-1;
    if(isOccupied(newfield))
     return i;
   }
  return 8;
 }
 /**
  * returns end field of a certain line
  * @param field start field of the line
  * @param dir direction of the line
  * @return getLineField(getLineLength(field,dir)-1)
  */
 private int getLineEnd(int field,int[] dir){
  int fx=fx(field),fy=fy(field),newfield,oldfield=field;
   for(int i=0;i<8;i++){
    fx+=dir[0];
    fy+=dir[1];
    newfield=f(fx,fy);
    if(newfield==-1)
     return oldfield;
    if(isOccupied(newfield))
     return newfield;
    oldfield=newfield;
   }
  return 8;
 }
 /**
  * return co-ordinates of the index'th field of a line
  * @param field start field of the line
  * @param dir the direction of the line
  * @param index the index: 0<=index<getLineLength(field,dir)
  * @return
  */
 private int getLineField(int field,int[] dir,int index){
  return f(fx(field)+index*dir[0],fy(field)+index*dir[1]);
 }
 /**
  * if rookorbishop, returns whether piece can move as a rook.
  * otherwise, returns whether piece can move as a bishop.
  * A queen can move as both a bishop and a rook
  * @param rookorbishop
  * @param piece
  * @return true if the piece can move as needed
  */
 private boolean canMoveAs(boolean rookorbishop,int piece){
  return piece==QUEEN||
      (rookorbishop&&piece==ROOK)||
      (!rookorbishop||piece==BISHOP);
 }
 /**
  * calculate all attacklines focused on a certain field. Note
  * that some of these lines can be blocked.
  * @param v the vector to add a possible line to
  * @param field the field to attack
  * @param dir the direction from which to attack
  * @param color the color of attacking pieces
  * @param rook if true this is a rook type line, otherwise a bishop line.
  */
 private void addAttackLine(Vector v, int field, int[] dir, int color,
                             boolean rook) {
  AttackLine line=new AttackLine(field,dir,rook);
  for(int i=1;i<=8;i++){
   int f=line.getField(i);
   if(f==-1)
    break;
   if(!isOccupied(f))
    continue;
   if(getColor(f)==color&&line.fitsPiece(getPiece(f))){
    line.setLength(i+1);
    v.add(line);
    return;
   }
   if(line.isBlocked())
    break;
   else
    line.setBlocked(f);
  }
 }
 /**
  * returns whether field is not -1, is occupied and contains a
  * piece of the necessary color.
  * @param field a field number, or -1
  * @param color either BLACK or WHITE
  * @return true if requirements are met
  */
 private boolean existsOccupiedColor(int field,int color){
  return field!=-1&&isOccupied(field)&&getColor(field)==color;
 }

 private void addAttackLines(Vector v, int field, int[][] table, int color,
                             int type) {
  int fx=fx(field),fy=fy(field);
  for(int i=0;i<table.length;i++){
   int newfield=f(fx+table[i][0],fy+table[i][1]);
   if(existsOccupiedColor(newfield,color)&&getPiece(newfield)==type){
    int[] a={field,newfield};
    v.add(a);
   }
  }
 }


}