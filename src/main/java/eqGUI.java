import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by igor on 29.05.2014.
 */
public class eqGUI extends JFrame{

    private static File file;
    private static WaveFile waveFile;

    private JFileChooser fileChooser;
    private JButton playButton;
    private JButton stopButton;
    private JTextField addressFile;
    private JButton chooserButton;


    public eqGUI() {
        super("equalizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JPanel playPanel = new JPanel();
        JPanel filePanel = new JPanel();

        fileChooser = new JFileChooser();
        addressFile = new JTextField();
        chooserButton = new JButton("...");

        playButton = new JButton("play");
        stopButton = new JButton("stop");

        chooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                    waveFile = new WaveFile(file);
                }
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waveFile.play();

            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waveFile.stop();
            }
        });

        filePanel.add(addressFile);
        filePanel.add(chooserButton);

        playPanel.add(playButton);
        playPanel.add(stopButton);

        panel.setLayout(new GridLayout(2,1,5,10));
        panel.add(filePanel);
        panel.add(playPanel);

        getContentPane().add(panel);

        pack();
        setVisible(true);

    }

    public static void main(String[] args) {

        new eqGUI();

    }
}
