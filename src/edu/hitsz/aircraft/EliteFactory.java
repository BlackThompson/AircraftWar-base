package edu.hitsz.aircraft;

public class EliteFactory implements AircraftFactory {
    @Override
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
