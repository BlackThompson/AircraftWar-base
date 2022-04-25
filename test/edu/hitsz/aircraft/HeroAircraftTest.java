package edu.hitsz.aircraft;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HeroAircraftTest {

    HeroAircraft heroAircraft;

    @BeforeEach
    void beforeEach() {
        heroAircraft = HeroAircraft.getHeroInstance();
    }

    @AfterEach
    void afterEach() {
        heroAircraft = null;
    }

    @BeforeAll
    static void setup() {
        System.out.println("**--- HeroAircraftTest Begin ---**");
    }

    @AfterAll
    static void tearDown() {
        System.out.println("**--- HeroAircraftTest End ---**");
    }

    @Test
    void getHeroInstance() {
        System.out.println("**--- getHeroInstance Executed ---**");
        //heroAircraft = HeroAircraft.getHeroInstance(100, 100, 0, 0, 100);
        assertNotNull(heroAircraft);
    }

    @Test
    void increaseHp() {
        System.out.println("**--- increaseHp Executed ---**");
        heroAircraft.increaseHp(10);
        assertEquals(100, heroAircraft.getHp());
    }

}