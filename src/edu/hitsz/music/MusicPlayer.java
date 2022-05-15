package edu.hitsz.music;

import edu.hitsz.config.Media;

import javax.sound.sampled.*;
import java.io.*;


/**
 * @author Black
 */
public class MusicPlayer {
    private String fileName;

    private boolean isLoop;
    private boolean isPlaying;

    private MusicThread musicThread = null;

    public MusicPlayer(String fileName, boolean isLoop) {
        this.fileName = fileName;
        this.isLoop = isLoop;
    }

    public void startPlaying() {
        if (Media.music) {
            isPlaying = true;
            if (musicThread == null) {
                musicThread = new MusicThread(fileName);
            }
            musicThread.start();
        }
    }

    public void stopPlaying() {
        isPlaying = false;
        if (musicThread != null) {
            musicThread = null;
        }
    }

    public class MusicThread extends Thread {

        private String fileName;
        private AudioFormat audioFormat;
        private byte[] samples;
        private InputStream stream;

        public MusicThread(String fileName) {
            this.fileName = fileName;
        }

        private void sampleMusicFromFile() {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(new File(fileName));
                audioFormat = stream.getFormat();
                samples = sampleMusicStream(stream);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }

        private byte[] sampleMusicStream(AudioInputStream stream) {
            int size = (int) (stream.getFrameLength() * audioFormat.getFrameSize());
            byte[] samples = new byte[size];
            DataInputStream dataInputStream = new DataInputStream(stream);
            try {
                dataInputStream.readFully(samples);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return samples;
        }

        public void play(InputStream source) {
            SourceDataLine dataLine = null;
            int size = (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate());
            byte[] buffer = new byte[size];
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                dataLine = (SourceDataLine) AudioSystem.getLine(info);
                dataLine.open(audioFormat, size);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            assert dataLine != null;
            dataLine.start();
            try {
                int numBytesRead = 0;
                while (numBytesRead != -1 && isPlaying) {
                    numBytesRead = source.read(buffer, 0, buffer.length);
                    if (numBytesRead != -1) {
                        dataLine.write(buffer, 0, numBytesRead);
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dataLine.drain();
            dataLine.close();
        }

        @Override
        public void run() {
            do {
                sampleMusicFromFile();
                stream = new ByteArrayInputStream(samples);
                play(stream);
            } while (isLoop && isPlaying);

        }
    }
}
