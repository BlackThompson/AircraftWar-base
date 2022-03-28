package edu.hitsz.prop;

public class BloodFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new Blood(locationX, locationY, speedX, speedY);
    }
}
