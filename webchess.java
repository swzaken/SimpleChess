import java.applet.Applet;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 * This class starts blueRing chess as an applet. Its name is in
 * lowercase to conform with the 'all lowercase' standard we employ
 * in html files and scripting file names.
 */

public class webchess extends Applet {
 MatchControl control;

 public void init(){
  control=new MatchControl(this);
  this.setLayout(new BorderLayout());
  this.add(control.getView().getComponent());
 }
}
