package edu.hitsz.prop;

public class BulletFactory implements PropFactory {
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new Bullet(locationX, locationY, speedX, speedY);
    }
}
