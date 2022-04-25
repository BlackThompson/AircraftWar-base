package edu.hitsz.aircraft;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class EliteEnemyTest {

    EliteEnemy eliteEnemy;

    @BeforeEach
    void setUp() {
        eliteEnemy = new EliteEnemy(100, 100,
                0, 0, 100);
    }

    @AfterEach
    void tearDown() {
        eliteEnemy = null;
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("**--- EliteEnemyTest Begin ---**");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("**--- EliteEnemyTest End ---**");
    }

    @Test
    void decreaseHp() {
        System.out.println("**--- decreaseHp Executed ---**");
        eliteEnemy.decreaseHp(50);
        assertEquals(50, eliteEnemy.getHp());
    }

    @Test
    void crash() {
        System.out.println("**--- crash Executed ---**");
        HeroAircraft heroAircraft = HeroAircraft.getHeroInstance();
        assumeTrue(eliteEnemy.crash(heroAircraft));
        System.out.println("Crashed!");
    }
}