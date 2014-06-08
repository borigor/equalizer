import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by igor on 29.05.2014.
 */
public class eqGUI extends Thread{

    static final int NUM_BANDS = 8;

    static final int MIN_VALUE_EQ = -10;
    static final int MAX_VALUE_EQ = 10;
    static final int INIT_VALUE_EQ = 0;

    static final int MIN_VALUE_VOL = 0;
    static final int MAX_VALUE_VOL = 10;
    static final int INIT_VALUE_VOL = 5;

    private int bandValues[] = new int[NUM_BANDS];
    private int volumeValue = INIT_VALUE_VOL;
    private boolean delayFlag = false;
    private boolean vibratoFlag = false;

    private static File file;
    private static WaveFile waveFile;
    private eqGUI gui;


    private JLabel addressFile;
    private JButton chooserButton;

    private JCheckBox delayCheckBox;
    private JCheckBox vibratoCheckBox;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JSlider volumeControl;
    private JLabel volumeLabel;

    private JSlider band1;
    private JSlider band2;
    private JSlider band3;
    private JSlider band4;
    private JSlider band5;
    private JSlider band6;
    private JSlider band7;
    private JSlider band8;

    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;

    public int[] getBandValues() {
        return bandValues;
    }

    public float getVolumeValue() {
        return volumeValue;
    }

    public boolean getDelayFlag() {
        return delayFlag;
    }

    public boolean getVibratoFlag() {
        return vibratoFlag;
    }

    public void run() {
        gui = this;
        JFrame frame = new JFrame("equalizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel controlPanel = new JPanel();
        JPanel effectPanel = new JPanel();
        JPanel playPanel = new JPanel();
        final JPanel volumePanel = new JPanel();
        JPanel filePanel = new JPanel();
        JPanel slidersPanel = new JPanel();

        JPanel slider1 = new JPanel();
        JPanel slider2 = new JPanel();
        JPanel slider3 = new JPanel();
        JPanel slider4 = new JPanel();
        JPanel slider5 = new JPanel();
        JPanel slider6 = new JPanel();
        JPanel slider7 = new JPanel();
        JPanel slider8 = new JPanel();

        addressFile = new JLabel("Choose audiofile");
        chooserButton = new JButton("...");

        delayCheckBox = new JCheckBox("delay");
        vibratoCheckBox = new JCheckBox("vibrato");

        playButton = new JButton("play");
        pauseButton = new JButton("pause");
        stopButton = new JButton("stop");

        volumeLabel = new JLabel("volume");
        volumeControl = new JSlider(MIN_VALUE_VOL, MAX_VALUE_VOL, INIT_VALUE_VOL);

        volumeControl.setMajorTickSpacing(1);
        volumeControl.setMinorTickSpacing(1);
        volumeControl.setPaintTicks(true);

        band1 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band2 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band3 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band4 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band5 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band6 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band7 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);
        band8 = new JSlider(JSlider.VERTICAL, MIN_VALUE_EQ, MAX_VALUE_EQ, INIT_VALUE_EQ);

        band1.setMajorTickSpacing(2);
        band1.setMinorTickSpacing(2);
        band1.setPaintTicks(true);

        band2.setMajorTickSpacing(2);
        band2.setMinorTickSpacing(2);
        band2.setPaintTicks(true);

        band3.setMajorTickSpacing(2);
        band3.setMinorTickSpacing(2);
        band3.setPaintTicks(true);

        band4.setMajorTickSpacing(2);
        band4.setMinorTickSpacing(2);
        band4.setPaintTicks(true);

        band5.setMajorTickSpacing(2);
        band5.setMinorTickSpacing(2);
        band5.setPaintTicks(true);

        band6.setMajorTickSpacing(2);
        band6.setMinorTickSpacing(2);
        band6.setPaintTicks(true);

        band7.setMajorTickSpacing(2);
        band7.setMinorTickSpacing(2);
        band7.setPaintTicks(true);

        band8.setMajorTickSpacing(2);
        band8.setMinorTickSpacing(2);
        band8.setPaintTicks(true);

        label1 = new JLabel("band1");
        label2 = new JLabel("band2");
        label3 = new JLabel("band3");
        label4 = new JLabel("band4");
        label5 = new JLabel("band5");
        label6 = new JLabel("band6");
        label7 = new JLabel("band7");
        label8 = new JLabel("band8");


        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                    waveFile = new WaveFile(file, gui);
                    addressFile.setText(file.getAbsolutePath());
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!waveFile.isPlaying()) {
                    waveFile.start();
                } else {
                    waveFile.resume();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waveFile.isPlaying()) {
                    waveFile.stop();
                    waveFile = new WaveFile(file, gui);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waveFile.isPlaying()) {
                    waveFile.suspend();
                }
            }
        });

        delayCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                delayFlag = delayCheckBox.isSelected();
            }
        });

        vibratoCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                vibratoFlag = vibratoCheckBox.isSelected();
            }
        });

        volumeControl.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!volumeControl.getValueIsAdjusting()) {
                    volumeValue = volumeControl.getValue();
                }
            }
        });

        band1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band1.getValueIsAdjusting()) {
                    bandValues[0] = band1.getValue();
                }
            }
        });

        band2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band2.getValueIsAdjusting()) {
                    bandValues[1] = band2.getValue();
                }
            }
        });

        band3.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band3.getValueIsAdjusting()) {
                    bandValues[2] = band3.getValue();
                }
            }
        });

        band4.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band4.getValueIsAdjusting()) {
                    bandValues[3] = band4.getValue();
                }
            }
        });

        band5.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band5.getValueIsAdjusting()) {
                    bandValues[4] = band5.getValue();
                }
            }
        });

        band6.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band6.getValueIsAdjusting()) {
                    bandValues[5] = band6.getValue();
                }
            }
        });

        band7.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band7.getValueIsAdjusting()) {
                    bandValues[6] = band7.getValue();
                }
            }
        });

        band8.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!band8.getValueIsAdjusting()) {
                    bandValues[7] = band8.getValue();
                }
            }
        });

        slider1.setLayout(new BorderLayout());
        slider2.setLayout(new BorderLayout());
        slider3.setLayout(new BorderLayout());
        slider4.setLayout(new BorderLayout());
        slider5.setLayout(new BorderLayout());
        slider6.setLayout(new BorderLayout());
        slider7.setLayout(new BorderLayout());
        slider8.setLayout(new BorderLayout());

        slider1.add(band1, BorderLayout.NORTH);
        slider1.add(label1, BorderLayout.SOUTH);
        slider2.add(band2, BorderLayout.NORTH);
        slider2.add(label2, BorderLayout.SOUTH);
        slider3.add(band3, BorderLayout.NORTH);
        slider3.add(label3, BorderLayout.SOUTH);
        slider4.add(band4, BorderLayout.NORTH);
        slider4.add(label4, BorderLayout.SOUTH);
        slider5.add(band5, BorderLayout.NORTH);
        slider5.add(label5, BorderLayout.SOUTH);
        slider6.add(band6, BorderLayout.NORTH);
        slider6.add(label6, BorderLayout.SOUTH);
        slider7.add(band7, BorderLayout.NORTH);
        slider7.add(label7, BorderLayout.SOUTH);
        slider8.add(band8, BorderLayout.NORTH);
        slider8.add(label8, BorderLayout.SOUTH);

        filePanel.add(addressFile);
        filePanel.add(chooserButton);

        effectPanel.add(delayCheckBox);
        effectPanel.add(vibratoCheckBox);

        playPanel.add(playButton);
        playPanel.add(stopButton);
        playPanel.add(pauseButton);

        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.PAGE_AXIS));
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeControl);

        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        controlPanel.add(volumePanel);
        controlPanel.add(playPanel);
        controlPanel.add(effectPanel);

        slidersPanel.add(slider1);
        slidersPanel.add(slider2);
        slidersPanel.add(slider3);
        slidersPanel.add(slider4);
        slidersPanel.add(slider5);
        slidersPanel.add(slider6);
        slidersPanel.add(slider7);
        slidersPanel.add(slider8);

        frame.add(filePanel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.EAST);
        frame.add(slidersPanel, BorderLayout.WEST);

        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        eqGUI gui = new eqGUI();
        gui.start();

    }
}
