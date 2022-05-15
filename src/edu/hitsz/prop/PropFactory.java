package edu.hitsz.prop;

/**
 * All the prop factories should implement the interface.
 *
 * @author Black
 */
public interface PropFactory {
    /**
     * Create variable kinds of prop.
     *
     * @param locationX:x轴坐标
     * @param locationY:y轴坐标
     * @param speedX:x轴速度
     * @param speedY:y轴速度
     * @return 无返回值
     */
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY);
}