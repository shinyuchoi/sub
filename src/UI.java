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

    //UI
    JFrame jFrame;
    JLabel subTextLabel, subTextUnderLabel, fontSizeExplainLabel, infoLabel, frameLocationLabel;
    JButton open, exit, fontSmaller, fontBigger, moveUp, moveDown;

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
    String help = "1. 글씨 크기와 자막 위치를 조절합니다. \n플레이 중에도 우클릭으로 메뉴를 열어 조절 할 수 있습니다." +
            "\n2. 열기 버튼을 클릭 후, 자막 파일을 선택합니다. 현재 지원 자막:smi\n" +
            "3. 즐겁게 감상합니다.\n\n\n버그 및 에러 제보: shinyu.choi@tum.de\nVersion 1.0\nCopyright (c) 2020, Choi shin-yu \n" +
            "All rights reserved.";

    UI() {
        initValues();
        initFrame();
        initButtons();
        initLabels();
        initClasses();
        start();

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

    void restart(int sec) throws IOException, URISyntaxException, InterruptedException {
        for (int j = 0; j < sec; j++) {
            subTextUnderLabel.setText("프로그램을 " + (sec - j) + "초후 재실행합니다.");
            Thread.sleep(1000);
        }
        ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home") + "/bin/java", "-jar", new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getAbsolutePath());
        pb.start();
        System.exit(0);
    }


    void initButtons() {
        //create buttons
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
        createButton((int) (jFrame.getWidth() / 2) - 50, 20, new Dimension(200, bigButtonSize.height), "사용법/도움말", e -> JOptionPane.showMessageDialog(jFrame, help
                , "사용법/도움말",
                JOptionPane.INFORMATION_MESSAGE));

    }


    void removeButtons() {
        for (JButton jButton : jButtonList) {
            jFrame.remove(jButton);
        }
    }

    void removeLabels() {

        infoLabel.setText("");
        subTextLabel.setBackground(new Color(0, 0, 0, 0));
        subTextUnderLabel.setBackground(new Color(0, 0, 0, 0));
        jFrame.setBackground(new Color(0, 0, 0, 1));
        fontSizeExplainLabel.setBackground(new Color(0, 0, 0, 0));
        fontSizeExplainLabel.setText("");
        frameLocationLabel.setBackground(new Color(0, 0, 0, 0));
        frameLocationLabel.setText("");
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
        setInfoLabel("싱크표시창");
    }


    void setInfoLabel(String s) {
        infoLabel.setText(s);
    }

    /**
     * @param x              : x좌표
     * @param y              : y좌표
     * @param d              : Dimension
     * @param text           : 버튼 출력 내용
     * @param actionListener : 리스너
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
     * @param x:x좌표
     * @param y:y좌표
     * @param width:가로
     * @param height:높이
     * @param text:텍스트
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
            //if (result == jfc.APPROVE_OPTION) {
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                subThread.setFilePath(selectedFile.toString());
                fileSelected = true;
                /*try {
                    fileIO.sim2Array(filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sub = fileIO.getSub();
                subUnder = fileIO.getSubUnder();
                timeStamp = fileIO.getTimeStamp();
                sub.add("종료되었습니다.");
                subUnder.add("사용해주셔서 감사합니다.");
                timeStamp.add(timeStamp.get(timeStamp.size() - 1) + 2000);*/
            }
        }

    }


    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    void setTextLabels(String up, String down) {
        subTextLabel.setText(up);
        subTextUnderLabel.setText(down);
    }
}
