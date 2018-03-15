import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */

public class NewGameView implements ActionListener{
  Frame f;
  Choice whiteChoice=new Choice();
  Choice blackChoice=new Choice();
  Button okButton=new Button("Ok");
  Button cancelButton=new Button("Cancel");
  MatchControl control;
  Checkbox resetBoard=new Checkbox("Reset board",true,null);
  Checkbox whiteStart=new Checkbox("White starts",true,null);
  Checkbox wkCastle=new Checkbox("White can castle kingside",true,null);
  Checkbox wqCastle=new Checkbox("White can castle queenside",true,null);
  Checkbox bkCastle=new Checkbox("Black can castle kingside",true,null);
  Checkbox bqCastle=new Checkbox("Black can castle queenside",true,null);

  Situation sit;

  public NewGameView(MatchControl myControl){
   control=myControl;
   f=new Frame("new game");
   whiteChoice.add("user");
   whiteChoice.add("computer");
   blackChoice.add("user");
   blackChoice.add("computer");

   f.add("North",new Label("Choose players for a new game"));
   Panel p=new Panel();
   p.setLayout(new GridLayout(1,3));
   p.add(cancelButton);
   p.add(new Label());
   p.add(okButton);
   f.add("South",p);
   p=new Panel();
   p.setLayout(new GridLayout(1,3));
   p.add(whiteChoice);
   p.add(new Label("versus"));
   p.add(blackChoice);
   f.add("North",p);
   p=new Panel();
   p.setLayout(new GridLayout(5,1));
   p.add(whiteStart);
   p.add(resetBoard);
   p.add(wkCastle);
   p.add(wqCastle);
   p.add(bkCastle);
   p.add(bqCastle);
   f.add("Center",p);
   f.pack();
   okButton.addActionListener(this);
   cancelButton.addActionListener(this);
   centerFrame(f);
  }

 public void show(Situation sit){
  this.sit=sit;
  resetBoard=new Checkbox("Reset board",true,null);
  resetBoard.setState(false);
  wkCastle.setState(sit.canCastle(true,true));
  wqCastle.setState(sit.canCastle(true,false));
  bkCastle.setState(sit.canCastle(false,true));
  bqCastle.setState(sit.canCastle(false,false));
  f.show();
 }


 public void actionPerformed(ActionEvent actionEvent) {
  f.hide();
  if(actionEvent.getSource()==okButton){
   if(resetBoard.getState())
    sit.reset();
   sit.setPlayer(whiteStart.getState()?Situation.WHITE:Situation.BLACK);
   control.newGame(sit,whiteChoice.getSelectedItem(),
                   blackChoice.getSelectedItem());
  }else if(actionEvent.getSource()==cancelButton){
   control.newGame(null,null,null);
  }
 }
 private static void centerFrame(Component f) {
  int x = (f.getToolkit().getScreenSize().width - f.getWidth()) / 2;
  int y = (f.getToolkit().getScreenSize().height - f.getHeight()) / 2;
  f.setLocation(x, y);
 }


}