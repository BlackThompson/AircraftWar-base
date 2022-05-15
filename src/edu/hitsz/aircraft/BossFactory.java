package edu.hitsz.aircraft;

public class BossFactory implements AircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new Boss(locationX, locationY, speedX, speedY, hp);
    }
}
