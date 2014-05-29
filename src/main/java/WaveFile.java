import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by igor on 28.05.2014.
 */
public class WaveFile {

    private AudioInputStream stream = null;
    private AudioFormat format = null;
    private FloatControl volume = null;
    private Clip clip = null;
    private boolean released;
    private boolean playing;
    private byte[] data = null;

    public WaveFile(File file) {

        try {
            stream = AudioSystem.getAudioInputStream(file);
            format = stream.getFormat();
            clip = AudioSystem.getClip();
            clip.open(stream);

            released = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            released = false;
        }
    }

    public boolean isReleased() {
        return released;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void play() {

        if (isReleased()) {

            if (isPlaying()) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
            else {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    public void stop() {

        if (isPlaying()) {
            clip.stop();
            playing = false;
        }
    }

    public void setVolume(float vol) {
        if (vol < 0)
            vol = 0;
        else if (vol > 1)
            vol = 1;

        float min = volume.getMinimum();
        float max = volume.getMaximum();

        volume.setValue(vol * (max - min) + min);
    }

    public float getVolume() {
        float vol = volume.getValue();
        float min = volume.getMinimum();
        float max = volume.getMaximum();

        return (vol - min)/(max - min);
    }

    public void join() {
        if (!released) return;
        synchronized(clip) {
            try {
                while (playing) clip.wait();
            } catch (InterruptedException exc) {}
        }
    }

}
