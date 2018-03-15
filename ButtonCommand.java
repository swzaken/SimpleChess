

/**
 * <p>Title: </p>
 * <p>Description: blueRing chess</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: blueRing software development</p>
 * @author Sieuwert van Otterloo
 * @version 1.0
 */
import java.awt.event.MouseEvent;

public class ButtonCommand
{
 String command;
 boolean enabled;
 CommandListener listener;

 public ButtonCommand(String command,CommandListener listener){
  this.command=command;
  this.listener=listener;
  enabled=true;
 }
 public CommandListener getListener(){
  return listener;
 }
 public boolean isEnabled(){
  return enabled;
 }
 public String getText(){
  return command;
 }
 public void setEnabled(boolean enable){
  enabled=enable;
 }


}

interface CommandListener{
 public void doCommand(String s);
}