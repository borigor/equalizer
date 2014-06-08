import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by igor on 28.05.2014.
 */
public class WaveFile extends Thread {

    static final int BUFFER_SIZE = 2048;

    private AudioInputStream audioInputStream = null;
    private AudioFormat audioFormat = null;
    private FloatControl volume = null;
    private float floatVolumeValue;

    private int sampleSize;
    private int sampleCountInBuffer;
    private double inverseTimesOfBuffer;

    private boolean released;
    private boolean playing;

    private eqGUI gui;

    public WaveFile(File file, eqGUI gui) {

        this.gui = gui;

        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
            audioFormat = audioInputStream.getFormat();

            sampleSize = audioFormat.getSampleSizeInBits()/8;
            sampleCountInBuffer = BUFFER_SIZE/sampleSize;
            inverseTimesOfBuffer = audioFormat.getSampleRate()/sampleCountInBuffer;

            released = true;
        } catch (UnsupportedAudioFileException | IOException e) {
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

    public void bufferToSamples(byte[] buffer, short[] samplesBuffer) {

        byte[] sampleBytes = new byte[sampleSize];
        int j = 0;

        for (int i = 0; i < sampleCountInBuffer; i++) {
            for (int k = 0; k < sampleSize; k++) {
                sampleBytes[k] = buffer[j];
                j++;
            }
            samplesBuffer[i] = ByteBuffer.wrap(sampleBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        }
    }

    public void sampleToBuffer(byte[] buffer, short[] samplesBuffer) {
        int j = 0;
        for (int i = 0; i < sampleCountInBuffer; i++) {
            byte[] sampleBytes = ByteBuffer.allocate(sampleSize).order(ByteOrder.LITTLE_ENDIAN).putShort(samplesBuffer[i]).array();

            for (int k = 0; k < sampleSize; k++) {
                buffer[j] = sampleBytes[k];
                j++;
            }
        }
    }

    public void run() {

        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);
            audioLine.open(audioFormat);
            volume = (FloatControl)audioLine.getControl(FloatControl.Type.MASTER_GAIN);
            audioLine.start();
            playing = true;

            System.out.println("Playback started.");

            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            int[] bandValueArray;
            short[] sampleBuffer = new short[sampleCountInBuffer];

            while (bytesRead != -1) {

                floatVolumeValue = (float) (gui.getVolumeValue()/10.0);
                floatVolumeValue += 0.3;
                setVolume(floatVolumeValue);

                bytesRead = audioInputStream.read(bytesBuffer, 0, bytesBuffer.length);
                if (bytesRead >= 0) {
                    bufferToSamples(bytesBuffer, sampleBuffer);

                    Complex[] x = ComplexConverter.shortArrayToComplex(sampleBuffer);
                    Complex[] spectrumArray = FFT.fft(x);

                    bandValueArray = gui.getBandValues();

                    Audio.equalizer(spectrumArray, inverseTimesOfBuffer, bandValueArray);

                    x = FFT.ifft(spectrumArray);
                    sampleBuffer = ComplexConverter.ComplexArrayToShort(x);

                    if (gui.getDelayFlag()) {
                        Audio.delay(sampleBuffer);
                    }

                    if (gui.getVibratoFlag()) {
                        Audio.vibrato(sampleBuffer);
                    }

                    sampleToBuffer(bytesBuffer, sampleBuffer);
                    audioLine.write(bytesBuffer, 0, bytesRead);
                }
            }

            audioLine.drain();
            audioLine.close();
            audioInputStream.close();

            System.out.println("Playback completed.");

        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    public void setVolume(float vol) {
        if (vol < 0.0)
            vol = 0;
        else if (vol > 1.0)
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
}
