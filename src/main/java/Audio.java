import java.util.LinkedList;

/**
 * Created by igor on 06.06.2014.
 */

public class Audio {

    private static final double border0 = 30;
    private static final double border1 = 120;
    private static final double border2 = 480;
    private static final double border3 = 960;
    private static final double border4 = 1920;
    private static final double border5 = 3480;
    private static final double border6 = 7680;
    private static final double border7 = 15360;

    private static LinkedList queueBuffer = new LinkedList<short[]>();

    public static void equalizer(Complex[] spectrumArray, double inverseTimeOfBuffer, int[] bandValueArray) {

        int n = spectrumArray.length;

        for (int i = 0; i < n; i++) {
            double freq = i * inverseTimeOfBuffer;

            if (freq < border0) {
                double gain = Math.pow(10.0, bandValueArray[0]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border1) {
                double gain = Math.pow(10.0, bandValueArray[1]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border2) {
                double gain = Math.pow(10.0, bandValueArray[2]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border3) {
                double gain = Math.pow(10.0, bandValueArray[3]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border4) {
                double gain = Math.pow(10.0, bandValueArray[4]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border5) {
                double gain = Math.pow(10.0, bandValueArray[5]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border6) {
                double gain = Math.pow(10.0, bandValueArray[6]/10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            } else if (freq < border7) {
                double gain = Math.pow(10.0, bandValueArray[7] /10.0);
                double re = spectrumArray[i].re();
                double im = spectrumArray[i].im();
                spectrumArray[i] = new Complex(re * gain, im * gain);
            }
        }
    }

    public static void delay(short[] sampleBuffer) {

        queueBuffer.add(sampleBuffer);

        if (queueBuffer.size() > 12) {
            short[] sample;
            sample = (short[])queueBuffer.remove();
            for (int i = 0; i < sampleBuffer.length; ++i) {
                sampleBuffer[i] = (short)((short)(sampleBuffer[i] * 0.5) + (short)(sample[i] * 0.5));
            }
        }
    }

    public static void vibrato(short[] sampleBuffer) {

        for (int i = 0; i < sampleBuffer.length; ++i) {
            sampleBuffer[i] = (short) (sampleBuffer[i] * (Math.cos(Math.PI / 2 * i) + 2));
        }
    }
}
