public class Move{
 /****************************************************************/
 // fields
 /****************************************************************/
 /**
 * a from field number, range 0..63
 */
 private int from;
 /**
 * a to field number, range 0..63
 */
 private int to;
 /**
 * one of KNIGHT,ROOK,BISHOP,QUEEN for promotions,
 * OO,OOO for castling, or ENPASSANT
 * NORMAL otherwise.
 */
 private int option;

 /**
  * all valid option values
  */
 public final static int NORMAL=0,ROOK=2,KNIGHT=3,BISHOP=4,
        QUEEN=5,OO=7,OOO=8,ENPASSANT=9,PAWNDOUBLE=10;
 /****************************************************************/
 // constructors
 /****************************************************************/

 /**
  * make a normal move
  * @param ifrom the from field for this move
  * @param ito the to field for this move
  */
 public Move(int ifrom, int ito){
  from=ifrom;
  to=ito;
  option=NORMAL;
 }

 public Move(String s){
  s=s.trim();
  int fromx=s.charAt(0)-'a';
  int fromy=s.charAt(1)-'1';
  int tox=s.charAt(3)-'a';
  int toy=s.charAt(4)-'1';
  from=f(fromx,fromy);
  to=f(tox,toy);
  if(from==-1||to==-1){
   System.out.println("unparsable:"+s);
  }
  option=NORMAL;
  for(int i=0;i<optionNames.length;i++)
   if(optionNames[i].length()>0&&s.endsWith(optionNames[i]))
    option=i;
 }

 /**
  * construct a special move
  * @param ifrom the from field for this move
  * @param ito the to field for this move
  * @param ioption any option value
  */
 public Move(int ifrom, int ito,int ioption){
   from=ifrom;
   to=ito;
   option=ioption;
 }

 /****************************************************************/
 // public methods
 /****************************************************************/

 public int getFrom(){
  return from;
 }
 public int getTo(){
  return to;
 }
 public int getCapture(){
  if(option==ENPASSANT)
   return f(fx(from),fy(to));
  return to;
 }

 public int getOption(){
  return option;
 }
 public boolean isEnPassant(){
  return option==ENPASSANT;
 }
 public boolean isPromotion(){
  return option==QUEEN||option==KNIGHT||option==BISHOP||option==ROOK;
 }
/*******************************************************************/
 //overridden methods of object

 /**
  * returns whether wo moves are equal. This is the case if from, to and
  * option are the same.
  * @param o an object to compare to
  * @return true if o is a ove with the same from, to and option.
  * False otherwise
  */
 public boolean equals(Object o){
  if(! (o instanceof Move))
   return false;
  Move m=(Move)o;
  return m.getFrom()==from&&m.getTo()==to&&m.getOption()==option;
 }

 /**
  * show this move in a "from-to (option) format", all uppercase
  * @return a string representation for this moves
  */
 public String toString(){
  return field2String(from)+"-"+field2String(to)+option2String();
 }

 /****************************************************************/
 // private methods
 /****************************************************************/
 /**
  * translates field numbers to conventional names. For example
  * field2String(12)="E2"
  * @param i a field number
  * @return a string consisting of a character and a digit
  */
 private String field2String(int i){
  return (char)('a'+i%8)+""+(char)('1'+i/8);
 }

 private final static String[] optionNames={"",""," R"," N"," B",
      " Q",""," OO"," OOO"," EP","*"};
  /**
   * returns the correct postfix for a certain option
   * @return the affix for thismoves' option
   */
  private String option2String(){
  return optionNames[option];
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

}