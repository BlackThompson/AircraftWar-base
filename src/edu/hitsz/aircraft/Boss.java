package edu.hitsz.aircraft;

import edu.hitsz.prop.*;

import java.util.List;
import java.util.Random;

public class Boss extends AbstractAircraft {

    private Random random = new Random();
    private PropFactory propFactory;
    private AbstractProp prop;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public void propGenerate(List<AbstractProp> abstractProps, AbstractAircraft boss) {

        //boss机掉落道具的x轴偏移量
        int x_offset = random.nextInt(10)*10 - 50;

        int flag = random.nextInt(2);
        if (flag == 1) {
            propFactory = new BloodFactory();
            prop = propFactory.createProp(boss.getLocationX() + x_offset,
                    boss.getLocationY(),
                    0,
                    5);
            abstractProps.add(prop);
        }

        flag = random.nextInt(2);
        x_offset = random.nextInt(10)*10 - 50;
        if (flag == 1) {
            propFactory = new BombFactory();
            prop = propFactory.createProp(boss.getLocationX() + x_offset,
                    boss.getLocationY(),
                    0,
                    5);
            abstractProps.add(prop);
        }

        flag = random.nextInt(2);
        x_offset = random.nextInt(10)*10 - 50;
        if (flag == 1) {
            propFactory = new BulletFactory();
            prop = propFactory.createProp(boss.getLocationX() + x_offset,
                    boss.getLocationY(),
                    0,
                    5);
            abstractProps.add(prop);
        }
    }
}
