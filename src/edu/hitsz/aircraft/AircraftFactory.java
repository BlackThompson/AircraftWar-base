package edu.hitsz.aircraft;

/**
 * All the aircraft factories should implement the interface.
 *
 * @author Black
 */
public interface AircraftFactory {
    /**
     * Create variable kinds of enemy aircraft.
     *
     * @param locationX:x轴坐标
     * @param locationY:y轴坐标
     * @param speedX:x轴速度
     * @param speedY:y轴速度
     * @param hp:血量
     * @return 无返回值
     */
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp);
}
