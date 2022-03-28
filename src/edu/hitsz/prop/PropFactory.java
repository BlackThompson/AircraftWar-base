package edu.hitsz.prop;

public interface PropFactory {
   public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY);
}