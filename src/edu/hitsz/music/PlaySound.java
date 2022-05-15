package edu.hitsz.music;

/**
 * @author Black 
 */
public class PlaySound {
    /**
     *     for bgm and boss bgm, load their instances first
     *     
     */
    private static MusicPlayer BGM = new MusicPlayer("src/audios/bgm.wav", true);
    private static MusicPlayer bossBGM = new MusicPlayer("src/audios/bgm_boss.wav", true);

    public static void startPlayBGM() {
        BGM.startPlaying();
    }

    public static void stopPlayBGM() {
        BGM.stopPlaying();
    }

    public static void startPlayBossBGM() {
        bossBGM.startPlaying();
    }

    public static void stopPlayBossBGM() {
        bossBGM.stopPlaying();
    }

    public static void playBulletSound() {
        new MusicPlayer("src/audios/bullet.wav", false).startPlaying();
    }

    public static void playBulletHitSound() {
        new MusicPlayer("src/audios/bullet_hit.wav", false).startPlaying();
    }

    public static void playGameOverSound() {
        new MusicPlayer("src/audios/game_over.wav", false).startPlaying();
    }

    public static void playBombExplosionSound() {
        new MusicPlayer("src/audios/bomb_explosion.wav", false).startPlaying();
    }

    public static void playGetSupplySound() {
        new MusicPlayer("src/audios/get_supply.wav", false).startPlaying();
    }
}
