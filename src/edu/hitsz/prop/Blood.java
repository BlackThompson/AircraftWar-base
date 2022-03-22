package edu.hitsz.prop;

/**
 * @author Black
 */
public class Blood extends AbstractProp {

    private int blood = 50;

    public Blood(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public int getBlood(){return blood;}
}
