import java.awt.*;
import java.applet.*;
import java.net.URL;
/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class MatchView {
 /**********************************************************************/
 // fields
 /**********************************************************************/
 //constants defining the look of a MatchView
 public final static int hormargin = 100, vermargin = 40, fieldsizex = 40,
     fieldsizey = 40,hlabelplace=65,vlabelplace=vermargin+8*fieldsizey+10;
 //the DragGround containing all objects
 private DragGround ground;
 //An array containing the pieces. null if no piece at that location
 private ImageDragject[] piece = new ImageDragject[64];
 //the images. Accessed as [ROOK][WHITE] etcetra. null if type unused
 private Image[][] image = new Image[7][2];

 /** if true, the Situation is shown as if the user is white, with
   * the white pieces at the bottom. Otherwise the game is shown
   * with the black pieces at the bottom of the screen;
   */
  private boolean whiteperspective=true;

  private TextDragject gameStatusLine;

  /** The colors used by this view
   */
  private static final Color background = Color.white;
  /* my old green colors
  private static final Color darkfield = new Color(56, 112, 56);
  private static final Color darkfieldh = new Color(56, 204, 56);
  private static final Color lightfield = new Color(144, 198, 144);
  private static final Color lightfieldh = new Color(209, 233, 209);
  */
 //blue colors with orange highlight
 private static final Color darkfield = new Color(65, 127, 124);
 private static final Color darkfieldh = new Color(226, 186, 0);
 private static final Color lightfield = new Color(208, 238, 208);
 private static final Color lightfieldh = new Color(255, 228, 101);

  /**
   * The listener listening to piece drag events
   */
  private DragGroundListener listener;
  /**
   * two promotion frames that can be shown if needed
   */
  private ImageSelectFrame[] promoframe;

  /**
   * Fields that are highlighted
   */
  private int[] highLightedFields=null;
  /**
   * and another highlighted field.
   */
  private int highfield=-1;
  /**
   * the squares the pieces are standing on
   */
   RectangleDragject[] square=new RectangleDragject[64];


 /**********************************************************************/
 // constructors
 /**********************************************************************/

 /**
  *
  * @param sit
  * @param listener
  */
 public MatchView(Situation sit,DragGroundListener listener,Applet applet){
  this.listener=listener;
  loadImages(applet);
  makeGround(listener);
  updateGround(sit);
  ground.start();
 }
 /**********************************************************************/
 // public methods
 /**********************************************************************/

 /** Converts screen coordinates to a field number
   *
   * @param x coordinate in pixels
   * @param y coordinate in pixels
   * @return
   */
  public int getFieldNumber(int x, int y) {
   x = (x - hormargin) / fieldsizex;
   y = (y - vermargin) / fieldsizey;
   if (x < 0 || x > 8 || y < 0 || y > 8) {
    return -1;
   }
   if(whiteperspective)
    y=7-y;
   return x + 8 * y;
  }


  /**
   * get Rightmost x co-ordinate of a field
   * @param field the number of the field
   * @return horizontal co-ordinate in pixels
   */
  public int getScreenX(int field) {
   return getcolumn(field) * fieldsizex + hormargin + fieldsizex / 2;
  }
  /**
   * get Top co-ordinate of a field
   * @param field the number of the field
   * @return vertical co-ordinate in pixels
   */
  public int getScreenY(int field) {
   return getrow(field) * fieldsizey + vermargin + fieldsizey / 2;
  }

  public Component getComponent(){
   return ground;
  }

  /**
   * returns the image that is shown on a field
   * @param i the field
   * @return null if no piece is shown, otherwise an image.
   */
  public Image getVisibleImage(int i){
   return piece[i].getImage();
  }
  /**********************************************************************/


 private ImageSelectFrame editSelectFrame;

 public EditSelectFrame getEditSelectFrame(MatchControl control){
  return new EditSelectFrame(fieldsizex,fieldsizey,image,control);
 }
 public PromotionFrame getPromotionFrame(int color,MatchControl control){
  return new PromotionFrame(color,fieldsizex,fieldsizey,image,control);
 }

   /**
    * set a group of fields to be highlighted.
    * @param f null if nothing is to be highlighted
    */
   public void setHighLightedFields(int[] f){
    if(highLightedFields!=null){
     for(int i=0;i<highLightedFields.length;i++)
        square[highLightedFields[i]].setHigh(false);
     highLightedFields=null;
    }
    highLightedFields=f;
    if(highLightedFields!=null){
     for(int i=0;i<highLightedFields.length;i++)
      square[highLightedFields[i]].setHigh(true);
    }
   }
   public void setGameStatus(String s){
    gameStatusLine.setText(s);
   }
 public void setClickField(boolean on,DragGroundListener control){
  for(int i=0;i<64;i++){
   square[i].setDragClick(false,on,true);
   square[i].addListener(control);
  }
 }

 /**********************************************************************/
 // private methods
 /**********************************************************************/
  private int getcolumn(int field) {
    return field % 8;
   }

   private int getrow(int field) {
    if(whiteperspective)
     return 7 - field / 8;
    else
     return field / 8;
   }

 /**
  * loads all required images.
  */
 private void loadImages(Applet applet) {
 Toolkit tk = Toolkit.getDefaultToolkit();
 String names[] = {
     "", "Pawn", "Rook", "Knight", "Bishop", "Queen", "King"};
 if(applet!=null){
  URL base=applet.getDocumentBase();
  for (int type = Situation.PAWN; type <= Situation.KING; type++) {
   String name = names[type];
   try{
   image[type][Situation.WHITE] = applet.getImage(new URL(base,"images/W" + name + ".gif"));
   image[type][Situation.BLACK] = applet.getImage(new URL(base,"images/B" + name + ".gif"));
   }catch(Exception e){
    e.printStackTrace();
   }
  }
 }
 else{
  for (int type = Situation.PAWN; type <= Situation.KING; type++) {
   String name = names[type];
   image[type][Situation.WHITE] = tk.getImage("images/W" + name + ".gif");
   image[type][Situation.BLACK] = tk.getImage("images/B" + name + ".gif");
  }
 }
 }



 private Image getImage(int type, int color) {
  return image[type][color];
 }
 /**
  * returns the piece of the given image.
  * @param im an image loaded by this view
  * @return PAWN,ROOK,KNIGHT,BISHOP,QUEEN,KING or -1
  */
 public int getPiece(Image im){
  for(int piece=0;piece<image.length;piece++)
    if(image[piece][Situation.WHITE]==im||image[piece][Situation.BLACK]==im)
     return piece;
  return -1;
 }
 /**
  * returns the color of the piece on an image
  * @param im the image
  * @return BLACK, WHITE or -1
  */
 public int getColor(Image im){
  for(int piece=0;piece<image.length;piece++)
    if(image[piece][Situation.WHITE]==im)
     return Situation.WHITE;
    else if(image[piece][Situation.BLACK]==im)
     return Situation.BLACK;
  return -1;
 }


 private void makeGround(DragGroundListener listener) {
 ground = new DragGround(2, Color.white);
 ground.setSize(2 * hormargin + 8 * fieldsizex, 2 * vermargin + 8 * fieldsizey);
 /*add all the squares*/
 for (int i = 0; i < 64; i++) {
   RectangleDragject d = new RectangleDragject(fieldsizex, fieldsizey);
   square[i]=d;
   if ( ((i+i/8) % 2) == 0) {
    d.setColor(darkfield, Color.black, darkfieldh, Color.black);
   }
   else {
    d.setColor(lightfield, Color.black, lightfieldh, Color.black);
   }
   d.setPosition(getScreenX(i), getScreenY(i));
   ground.add(0, d);
 }
 /*add the column and row labels*/
 String columns="ABCDEFGH",rows="12345678";
 for(int i=0;i<8;i++){
  ground.add(0,new TextDragject(""+columns.charAt(i),
                                getScreenX(i),
                                vlabelplace));
  ground.add(0,new TextDragject(""+rows.charAt(i),hlabelplace,
                                getScreenY(8*i)));
 }
 /*create the status line*/
 gameStatusLine=new TextDragject("...",hormargin + 4 * fieldsizex,vermargin/2);
 ground.add(0,gameStatusLine);
 /*create the menu*/
}

private void addButton(String s,CompositeDragject comp,int y){
 TextDragject mytext=new TextDragject(s);
 comp.add(mytext,0,y);
 mytext.setDragClick(false,true,true);
 mytext.addListener(listener);
}

public void makeMenu(ButtonCommand[] commands){
 String[] options={"exit","new game","undo","redo","edit","play"};
 makeMenu(commands,leftmenux,200);
}

private int leftmenux=hormargin+8*fieldsizex+hormargin/2;

/**
 *
 * @param element an array containing strings or timers
 * @param x horizontal position of menu
 * @param y vertical position of menu
 */
private void makeMenu(ButtonCommand[] command,int x,int y){
 Dragject[] dragject=new Dragject[command.length];
 for(int i=0;i<command.length;i++){
   dragject[i]=new ButtonDragject(command[i]);
 }
 makeMenu(dragject,x,y);
}

private void makeMenu(Dragject[] element,int x,int y){
 int width=80,height=15;
 CompositeDragject comp=new CompositeDragject(width,height*element.length);
 RectangleDragject dj=new RectangleDragject(width,height*element.length);
 dj.setColor(lightfield,Color.black,lightfieldh,Color.black);
 comp.add(dj,0,0);
 for(int i=0;i<element.length;i++){
  comp.add(element[i],0,i*height-((element.length-1)*height)/2);
 }
 comp.setPosition(x,y);
 comp.setDragClick(true,false,false);
 comp.addAllTo(ground,0,0);
}

public void makeClock(Timer t1,Timer t2){
 Dragject[] dr=new Dragject[4];
 dr[0]=new TextDragject("white");
 dr[1]=new ClockDragject(t1);
 dr[2]=new TextDragject("black");
 dr[3]=new ClockDragject(t2);
 makeMenu(dr,leftmenux,vermargin);
}

 public void movePiece(int from,int to){
  if(piece[from]!=null){
   piece[from].moveToPosition(getScreenX(to), getScreenY(to),1000);
   if(piece[to]!=null)
    ground.remove(piece[to]);
   piece[to]=piece[from];
   piece[from]=null;
  }
 }

/**
 * update the ground to show the given situation.
 * from and to can be used for animation effects, if not -1
 * @param sit a Situation that must be shown
 *
 */
 public void updateGround(Situation sit) {
 for (int i = 0; i < 64; i++) {
  square[i].setHigh(i==highfield);
  if (!sit.isOccupied(i) && piece[i] != null) {
   ground.remove(piece[i]);
   piece[i] = null;
  }
  if (sit.isOccupied(i)) {
   Image rightImage = getImage(sit.getPiece(i), sit.getColor(i));
   if (piece[i] != null && piece[i].getImage() == rightImage) {
    piece[i].setPosition(getScreenX(i), getScreenY(i));
   }
   if (piece[i] != null && piece[i].getImage() != rightImage) {
    ground.remove(piece[i]);
    piece[i] = null;
   }
   if (piece[i] == null) {
    piece[i] = new ImageDragject(rightImage, rightImage);
    piece[i].setPosition(getScreenX(i), getScreenY(i));
    piece[i].setDragClick(true, false, true);
    piece[i].addListener(listener);
    ground.add(1,piece[i]);
   }

  }
 }
}
/**
 * show the chessmatch in a new Frame
 */
public void showInFrame() {
 ground.showInFrame("blueRing chess");
}



 public void setHighField(int f){highfield=f;}


}


