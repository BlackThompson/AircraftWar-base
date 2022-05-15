package edu.hitsz.prop;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


class BloodTest {

    Blood blood;

    @BeforeEach
    void setUp() {
        blood = new Blood(100, 1000, 0, 10);
    }

    @AfterEach
    void tearDown() {
        blood = null;
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("**--- BloodTest Begin ---**");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- BloodTest End ---**");
    }

    @Test
    void forward() {
        System.out.println("**--- forward Executed ---**");
        //speedY > 0 && locationY > 768 ,768为屏幕高度，时判定为出界
        blood.forward();
        assumeTrue(blood.notValid());
        System.out.println("blood is out of screen");
    }

    @Test
    void getBlood() {
        System.out.println("**--- getBlood Executed ---**");
        assertEquals(50, blood.getBlood());
    }
}