import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UI {
    JFrame jFrame;
    JLabel subTextLabel;
    JButton open, exit;
    private String filePath;


    UI() {
        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameHeight = screenSize.height, frameWidth = screenSize.width;


        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);

        open = new JButton("열기");
        open.setSize(70, 50);
        open.setLocation(frameWidth - open.getWidth() - 50, open.getHeight() + 100);
        open.addActionListener(new OpenFile());
        jFrame.add(open);


        exit = new JButton("종료");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exit.setSize(70, 50);
        exit.setLocation(frameWidth - open.getWidth() - 50, open.getHeight() + 200);
        jFrame.add(exit);


        subTextLabel = new JLabel("@@", SwingConstants.CENTER);
        subTextLabel.setSize(frameWidth, (int) (frameHeight * 0.15));
        subTextLabel.setLocation(0, frameHeight - (int) (frameHeight * 0.15));
        subTextLabel.setBackground(Color.GREEN);
        subTextLabel.setOpaque(true);
        jFrame.add(subTextLabel);
        subTextLabel.setText("<html><strong><font size = 30 color=white> &lt;자막__위치><br>&lt;자막__위치></font> </html>");
    }

    void play() {
        Timer timer = new Timer();
        timer.run();

        
    }


    class OpenFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame window = new JFrame();
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Downloads"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".smi", "smi");
            jfc.addChoosableFileFilter(filter);
            int result = jfc.showOpenDialog(window);
            if (result == jfc.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                setFilePath(selectedFile.toString());
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
