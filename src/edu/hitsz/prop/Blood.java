package edu.hitsz.prop;

/**
 * @author Black
 */
public class Blood extends AbstractProp {

    public Blood(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    private int hpIncrease = 50;

    public int getBlood() {
        return hpIncrease;
    }

}