import sas.swing.plaf.MultiLineShadowUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class UI extends JFrame {

    final String version = "version 1.0";

    //UI
    JFrame jFrame;
    JLabel subTextLabel, subTextUnderLabel, fontSizeExplainLabel, infoLabel, frameLocationLabel;
    JButton open, exit, fontSmaller, fontBigger, moveUp, moveDown, helpButton;
    JCheckBox easyStartCheckBox;


    //Values
    private boolean playing, fileSelected;
    int fontSize, frameHeight, frameWidth;
    boolean isTransparent;

    ArrayList<JButton> jButtonList;

    Dimension bigButtonSize, smallButtonSize, screenSize;
    JPopupMenu jPopupMenu;

    //class
    SubThread subThread;
    Thread thread;

    UI() {
        initValues();
        initFrame();
        initButtons();
        initLabels();
        initClasses();
        start();

    }

    /**
     * bigButtonSize :  (70, 50)
     * smallButtonSize : (50, 35);
     */
    void initValues() {
        jButtonList = new ArrayList<>();
        fontSize = 50;
        fileSelected = false;
        playing = false;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = screenSize.height;
        frameWidth = screenSize.width;
        bigButtonSize = new Dimension(70, 50);
        smallButtonSize = new Dimension(50, 35);
        isTransparent = false;
    }

    void initFrame() {
        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        //jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setSize(frameWidth, 300);
        jFrame.setLocation(0, frameHeight - jFrame.getHeight() - 50);
        //jFrame.setLocation(0, frameHeight - jFrame.getHeight() - 550);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);
        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent m) {
                if (SwingUtilities.isRightMouseButton(m)) {
                    jPopupMenu.show(m.getComponent(), m.getX(), m.getY());
                }
            }
        });
    }

    void initButtons() {
        open = createButton(50, 20, bigButtonSize, "열기", new OpenFile());
        exit = createButton(jFrame.getWidth() - 70, 0, smallButtonSize, "X", null);
        exit.addActionListener(e -> System.exit(0));


        fontBigger = createButton(150, 20, smallButtonSize, "+", e -> {
            fontSize += 2;
            subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
            subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        });


        fontSmaller = createButton(fontBigger.getX() + bigButtonSize.width + smallButtonSize.width, 20, smallButtonSize, "-", e -> {
            fontSize -= 2;
            subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
            subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        });

        moveUp = createButton(fontSmaller.getX() + fontSmaller.getWidth() + 20, 20, new Dimension(smallButtonSize.width, bigButtonSize.height), "" + (char) 9650, e -> {
            jFrame.setLocation(jFrame.getX(), jFrame.getY() - 10);
        });

        moveDown = createButton(moveUp.getX() + bigButtonSize.width + smallButtonSize.width, 20, new Dimension(smallButtonSize.width, bigButtonSize.height), "" + (char) 9660, e -> {
            jFrame.setLocation(jFrame.getX(), jFrame.getY() + 10);

        });
        helpButton = createButton((jFrame.getWidth() / 2) - 50, 20, new Dimension(200, bigButtonSize.height), "사용법/도움말", e -> JOptionPane.showMessageDialog(null,
                new HelpPanel(version),
                "사용법, 도움말",
                JOptionPane.PLAIN_MESSAGE));

        easyStartCheckBox = new JCheckBox("Easy Start");
        easyStartCheckBox.setSelected(true);
        easyStartCheckBox.setSize(100, 50);
        easyStartCheckBox.setLocation(helpButton.getX() + helpButton.getWidth() + 50, 20);
        jFrame.add(easyStartCheckBox);

    }

    void initLabels() {
        subTextLabel = createLabel(0, 0, frameWidth, (int) ((frameHeight * 0.20)) / 2, "test", Color.lightGray);
        subTextLabel.setLocation(0, jFrame.getHeight() - (subTextLabel.getHeight() * 2));
        subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
        subTextLabel.setText("자막의 위치입니다. 우클릭: 메뉴열기");

        subTextUnderLabel = createLabel(0, subTextLabel.getHeight(), frameWidth, (int) ((frameHeight * 0.20)) / 2, "test", Color.lightGray);
        subTextUnderLabel.setLocation(0, jFrame.getHeight() - subTextLabel.getHeight());
        subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        subTextUnderLabel.setText("<열기>버튼을 클릭 후, 파일을 선택해 주세요.");


        fontSizeExplainLabel = createLabel(fontBigger.getX() + smallButtonSize.width, 20, bigButtonSize.width, smallButtonSize.height, "글씨 크기", Color.LIGHT_GRAY);
        frameLocationLabel = createLabel(moveUp.getX() + smallButtonSize.width, 20, bigButtonSize.width, smallButtonSize.height, "위치 조절", Color.LIGHT_GRAY);
        infoLabel = createLabel(jFrame.getWidth() - 150, 35, 150, 40, "", new Color(0, 0, 0, 0));
        setInfoLabelText("싱크표시창");
    }

    void initClasses() {
        jPopupMenu = new popupMenus(this);
        subThread = new SubThread(this);
        thread = new Thread(subThread);

    }
    
    void start() {
        while (!fileSelected) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        playing = true;
        thread.start();
    }


    /**
     * Restart application
     * @param sec : Sec to restart
     * @throws IOException  : if problem occurs by reading file
     * @throws URISyntaxException  : problem with ProcessBuilder
     * @throws InterruptedException : problem with thread
     */
    void restart(int sec) throws IOException, URISyntaxException, InterruptedException {
        for (int j = 0; j < sec; j++) {
            subTextUnderLabel.setText("프로그램을 " + (sec - j) + "초후 재실행합니다.");
            Thread.sleep(1000);
        }
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-jar", new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath());
        pb.start();
        System.exit(0);
    }

    void removeButtons() {
        for (JButton jButton : jButtonList) {
            jFrame.remove(jButton);
        }
        jFrame.remove(easyStartCheckBox);
    }

    void doTransparentLabels() {

        infoLabel.setText("");
        subTextLabel.setBackground(new Color(0, 0, 0, 0));
        subTextUnderLabel.setBackground(new Color(0, 0, 0, 0));
        jFrame.setBackground(new Color(0, 0, 0, 1));
        fontSizeExplainLabel.setBackground(new Color(0, 0, 0, 0));
        fontSizeExplainLabel.setText("");
        frameLocationLabel.setBackground(new Color(0, 0, 0, 0));
        frameLocationLabel.setText("");
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * setText for both subLabels
     * @param up : Text for upper Label
     * @param down : Text for under Label
     */
    void setTextLabels(String up, String down) {
        subTextLabel.setText(up);
        subTextUnderLabel.setText(down);
        jFrame.repaint();
    }

    /**
     * SetText Infolabel
     * @param s String Text
     */
    void setInfoLabelText(String s) {
        infoLabel.setText(s);
    }

    class OpenFile implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame window = new JFrame();
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Downloads"));
            FileNameExtensionFilter smi = new FileNameExtensionFilter("자막파일(smi,srt)", "smi", "srt");
            jfc.addChoosableFileFilter(smi);

            jfc.setFileFilter(smi);

            int result = jfc.showOpenDialog(window);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                subThread.setFilePath(selectedFile.toString());
                fileSelected = true;
                //System.out.println(selectedFile.toString());
            }
        }

    }


    /**
     * @param x              : x-coordinate
     * @param y              : y-coordinate
     * @param d              : Dimension
     * @param text           : text on button
     * @param actionListener : listener
     * @return new JButton
     */
    JButton createButton(int x, int y, Dimension d, String text, ActionListener actionListener) {
        JButton jButton = new JButton(text);
        jFrame.add(jButton);
        jButton.setSize(d);
        jButton.setLocation(x, y);
        jButton.addActionListener(actionListener);
        jButtonList.add(jButton);
        return jButton;
    }

    /**
     * @param x:x-coordinate
     * @param y:y-coordinate
     * @param width:             size-Width
     * @param height:size-height
     * @param text:Text          on label
     * @param c:Color
     * @return JLabel
     */
    JLabel createLabel(int x, int y, int width, int height, String text, Color c) {
        JLabel jLabel = new JLabel(text, SwingConstants.CENTER);
        jFrame.add(jLabel);
        jLabel.setSize(width, height);
        jLabel.setLocation(x, y);
        jLabel.setOpaque(true);
        jLabel.setBackground(c);
        jLabel.setText(text);
        jLabel.setForeground(Color.white);
        jLabel.setUI(MultiLineShadowUI.labelUI);
        return jLabel;
    }
}
