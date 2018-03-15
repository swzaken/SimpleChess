import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ImageDragject extends Dragject
    implements ImageObserver
{
  Image im,imhigh;
  boolean knowProperties;

  public ImageDragject(Image img,Image imghigh){
   im=img;
   imhigh=imghigh;
   tryGetSize();
  }

  public void draw(Graphics g,DragGround dg,long time){
   if(!knowProperties)
    tryGetSize();
   if(isHigh||imhigh==null)
    g.drawImage(im,getTopX(),getTopY(),dg);
   else
    g.drawImage(imhigh,getTopX(),getTopY(),dg);
 }

 public Image getImage(){
  return im;
 }

  public boolean imageUpdate(Image img,
                                int infoflags,
                                int x,
                                int y,
                                int width,
                                int height)
  {
    int myflags=(ImageObserver.WIDTH|ImageObserver.HEIGHT);
    if((infoflags&myflags)==myflags){
    sizey=img.getHeight(null);
    sizex=img.getWidth(null);
    return false;
   }
   return true;
  }

  private void tryGetSize(){
   sizex=im.getWidth(null);
   sizey=im.getHeight(null);
   if(sizex==-1||sizey==-1){
     knowProperties=false;
     sizex=20;sizey=20;
   }else
    knowProperties=true;
  }

}