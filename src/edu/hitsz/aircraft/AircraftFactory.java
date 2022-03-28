package edu.hitsz.aircraft;

public interface AircraftFactory {
    public AbstractAircraft createAircraft(int locationX, int locationY, int speedX, int speedY, int hp);
}
