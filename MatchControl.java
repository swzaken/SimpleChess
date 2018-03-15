import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * contains main() function
 */

public class MatchControl
    implements DragGroundListener,CommandListener{
 /**********************************************************************/
 // fields
 /**********************************************************************/
 /**
  * the current game.
  */
 ChessGame game=new ChessGame();
 /**
  * the view that displays the program
  */
 MatchView view;
 /**
  * two promotion frames, one for each side
  */
 PromotionFrame[] promotionframe;

 EditSelectFrame editselectframe;
 /**
  * the window for starting a new game
  */
 NewGameView newGameView;
 /**
  * the status defines how input is handled.
  */
 private Object status;
 /** possible values for status.
  * USER means the ser must do a move
  * WAIT means nothing happens
  * PROMOTION means the promotion selectframe is showing
  * WAITFORNEWGAME means the new game frame is visible
  * COMPUTER means we are waiting for the computer to move
  * EDIT means the board situation can be edited
  */
 private final Object USER = "user", PLAYWAIT = "plywait",
     COMPUTER = "computer", EDIT =new Object(), EDITWAIT=new Object(),
     EMPTY=new Object(),IDLE="idle";
 /**
  * stores to and from value in case status is PROMOTION
  */
 int tempfrom, tempto;
 /**
  * keeps the time of both players
  */
 Timer[] playtimer = {
     new Timer(), new Timer()};
 /**
  * stores up to two computer players
  */
 ComputerPlayer[] compplayer = new ComputerPlayer[2];
 /**
  *
  */
 OpeningBook book = new OpeningBook();

 private java.applet.Applet applet;


 private ButtonCommand exitCommand=new ButtonCommand("exit",this),
 newGameCommand=new ButtonCommand("new game",this),
 undoCommand=new ButtonCommand("undo",this),
 redoCommand=new ButtonCommand("redo",this),
 editCommand=new ButtonCommand("edit",this),
 playCommand=new ButtonCommand("play",this);


 /**********************************************************************/
 // constructors
 /**********************************************************************/
 /**
  * use this constructor when running as an application
  */
 public MatchControl() {
  this(null);
  book.load(new File("ecobook.txt"));
 }

 /**
  * use this constructor when running as an applet.
  * @param applet
  */
 public MatchControl(java.applet.Applet applet) {
  this.applet=applet;
  view = new MatchView(getSit(), this, applet);
  promotionframe=new PromotionFrame[2];
  promotionframe[Situation.WHITE]=view.getPromotionFrame(Situation.WHITE,this);
  promotionframe[Situation.BLACK]=view.getPromotionFrame(Situation.BLACK,this);
  view.makeClock(playtimer[Situation.WHITE], playtimer[Situation.BLACK]);
  newGameView = new NewGameView(this);
  view.makeMenu(
   new ButtonCommand[]{
    exitCommand,
    newGameCommand,
    undoCommand,
    redoCommand,
    editCommand,
    playCommand
   }
  );
  setCommandsEnabled();
  editselectframe = view.getEditSelectFrame(this);
  reset(new Situation(),false, true);
  if(applet!=null){
   book.loadZIP(applet);
  }

 }

 /**********************************************************************/
 // public methods
 /**********************************************************************/
 /**
  * get the view of this object. Can be shown in frame.
  * @return a matchView to show.
  */
 public MatchView getView() {
  return view;
 }

  public Situation getSit(){
   return game.getSituation();
  }

 /**
  * creates a MatchControl and shows the match in a frame.
  * @param ps
  */
 public static void main(String[] ps) {
  new MatchControl().getView().showInFrame();
 }

 public Timer getTimer(int color) {
  return playtimer[color];
 }

 /**********************************************************************/
 // required methods
 /**********************************************************************/
 /**
  * called by PromotionFrame, to report a user choice.
  * @param frame
  * @param piece
  */
 public void promote(PromotionFrame frame,int piece){
  Move m = getList().getFromToOption(tempfrom, tempto, piece);
  if (m != null) {
   frame.hide();
   doMove(m, true);
  }
 }


 void setEditStatus() {
   status=EDIT;
   view.setClickField(true,this);
   editselectframe.show();
   setCommandsEnabled();
 }

 /**
  * required for DragGroundListener
  * @param d
  * @param e
  */
 public void clicked(Dragject d, MouseEvent e) {
  if (status == EDIT && d instanceof RectangleDragject) {
   int field = view.getFieldNumber(d.getX(), d.getY());
   if (field == -1) {
    return;
   }
   if (getSit().isOccupied(field)) {
    getSit().clearField(field);
   }
   else {
    int piece=editselectframe.getSelectedPiece();
    int color=editselectframe.getSelectedColor();
    System.out.println("set:"+piece+" "+color);
    if(color!=-1)
     getSit().setField(field, piece, color);
   }
   view.updateGround(getSit());
   return;
  }
  if (d instanceof TextDragject) {
   TextDragject td = (TextDragject) d;
   doCommand(td.getText(0));
  }
 }

 /**
  * required for DragGroundListener
  * @param d
  * @param px
  * @param py
  * @param e
  */
 public void dragged(Dragject d, int px, int py, MouseEvent e) {

  if (status != USER) {
   d.setPosition(px, py);
   return;
  }
  int from = view.getFieldNumber(px, py);
  int to = view.getFieldNumber(d.getX(), d.getY());
  Move m = getList().getFromTo(from, to);
  if (m == null) {
   d.setPosition(px, py);
   return;
  }
  if (!m.isPromotion()) {
   doMove(m, true);
  }
  else {
   tempfrom = from;
   tempto = to;
   enterPromotion();
  }
 }

 /**
  * required for DragGroundListener
  * @param d
  * @param onoff
  */
 public void mouseOver(Dragject d, boolean onoff) {
  if (d instanceof TextDragject) {
   d.setHigh(onoff);
   return;
  }

  if (status == EDIT) {
   if (d instanceof RectangleDragject) {
    d.setHigh(onoff);
   }
   return;
  }


  //d must be a field
  int f = view.getFieldNumber(d.getX(), d.getY());
  if (onoff) { //entering a field
   if (f != -1 && getSit().isOccupied(f) && getSit().getColor(f) == getSit().getPlayer()) {
    view.square[f].setHigh(true);
    MoveList l = getList();
    view.setHighLightedFields(l.getTargets(f));
   }
  }
  else {
   if (f != -1) {
    view.square[f].setHigh(false);
   }
   view.setHighLightedFields(null);
  }
 }

 /**
  * called by NewGameView to indicate a new game is requested.
  * @param newGame false if cancelled, true if committed
  */
 public synchronized void newGame(Situation sit, String whiteOption,
                                  String blackOption) {
  if (status != EDITWAIT && status !=PLAYWAIT) {
   return;
  }
  if (sit!=null) {
   reset(sit,whiteOption.equals("computer"), blackOption.equals("computer"));
  }
  else {
   if(status==PLAYWAIT)
    setPlayStatus();
   else
    setEditStatus();
  }
 }

 public synchronized void reset(Situation newsit,boolean whiteComputer, boolean blackComputer) {
  playtimer[Situation.WHITE].reset();
  playtimer[Situation.BLACK].reset();
  game=new ChessGame(newsit);
  if (whiteComputer && compplayer[Situation.WHITE] == null) {
   compplayer[Situation.WHITE] = new ComputerPlayer(book);
  }
  else if (!whiteComputer && compplayer[Situation.WHITE] != null) {
   compplayer[Situation.WHITE].destroy();
   compplayer[Situation.WHITE] = null;
  }
  if (blackComputer && compplayer[Situation.BLACK] == null) {
   compplayer[Situation.BLACK] = new ComputerPlayer(book);
  }
  else if (!blackComputer && compplayer[Situation.BLACK] != null) {
   compplayer[Situation.BLACK].destroy();
   compplayer[Situation.BLACK] = null;
  }
  situationChanged();

 }

 /**
  * this fuction is called by computer players to give their latest move.
  * @param m the move suggested by the computer player
  * @param reqnum the request number
  */
 public synchronized void answer(Move m, Situation msit) {
  if (status == COMPUTER && getSit() == msit) {
   doMove(m, false);
  }
 }

 /**********************************************************************/
 // private methods
 /**********************************************************************/
 /**
  * change the status of the controller
  * @param newstatus
  */
 private void setStatus(Object newstatus) {
  status=newstatus;
  setCommandsEnabled();
 }


 private Object getNewPlayStatus() {
  if (getSit().isFinished()) {
   return IDLE;
  }
  if (compplayer[getSit().getPlayer()] == null) {
   return USER;
  }
  else {
   return COMPUTER;
  }
 }
 /**
  * start play of the game. either let the user enter a move or
  * ask the user to enter a move.
  */
 private void setPlayStatus() {
  setStatus(getNewPlayStatus());
  System.out.println("status="+status);
  if (status== COMPUTER && !compplayer[getSit().getPlayer()].isWorkingOn(getSit())) {
   compplayer[getSit().getPlayer()].request(this, getSit());
  }
 }

 /************************************************************************/

 /**
  * returns the movelist for the given player
  * @param player WHITE or BLACK
  * @return the list of all valid moves.
  */
 private MoveList getList() {
  return getSit().getValidMoves();
 }

 private boolean isInPlayStatus(){
 return status==USER||status==COMPUTER||status==IDLE;
}

 void setCommandsEnabled(){
  exitCommand.setEnabled(applet==null);
  newGameCommand.setEnabled(isInPlayStatus());
  undoCommand.setEnabled(isInPlayStatus()&&game.canUndo());
  redoCommand.setEnabled(isInPlayStatus()&&game.canRedo());
  editCommand.setEnabled(isInPlayStatus());
  playCommand.setEnabled(status==EDIT);
 }


 /**
  * do the chosen command.
  * Supported:
  * exit: close application
  * new game: start a new game
  * Not supported:
  * undo: undo a move
  * @param s the command to do
  */
 public void doCommand(String s) {
  if (s.equals("exit")) {
   try {
    book.save(File.createTempFile("book", ".txt"));
   }
   catch (IOException ex) {
   }
   System.exit(0);
  }
  else if (s.equals("new game")) {
   setStatus(PLAYWAIT);
   newGameView.show(new Situation());
  }
  else if (s.equals("undo")) {
   undoMove();
  }
  else if (s.equals("edit")) {
   setEditStatus();
  }
  else if (s.equals("play")) {
   String error = getSit().getErrorMessage();
   if (error == null) {
    setStatus(EDITWAIT);
    newGameView.show(getSit());
   }
   else {
    view.setGameStatus(error);
   }
  }
 }


 private void situationChanged() {
  playtimer[Situation.BLACK].stop();
  playtimer[Situation.WHITE].stop();
  view.setHighField( -1);
  view.updateGround(getSit());
  view.setGameStatus(getGameStatus());
  playtimer[getSit().getPlayer()].start();
  setPlayStatus();
 }

 /**
  * do a move
  * @param m a valid move to do
  */
 private synchronized void doMove(Move m, boolean saveInBook) {
  if (saveInBook) {
   book.add(getSit().toString(), m);
  }
  game.doMove(m);
  view.movePiece(m.getFrom(),m.getTo());
  situationChanged();
  view.setHighField(m.getTo());
 }

 private synchronized void undoMove() {
  game.undoMove();
  situationChanged();
 }

 private String getGameStatus() {
  int status = getSit().getGameStatus();
  int player = getSit().getPlayer();
  String s = (player == Situation.WHITE) ? "White to move." : "Black to move.";
  if (status == Situation.CHECK) {
   s += " Check!";
  }
  else if (status == Situation.CHECKMATE) {
   s += " Checkmate!!";
  }
  else if (status == Situation.STALEMATE) {
   s += " Stalemate.";
  }
  return s;
 }

 /**
  * change status to promotion and show promotion dialogue
  */
 private void enterPromotion() {
  setStatus(PLAYWAIT);
  promotionframe[getSit().getPlayer()].show();
 }
}
