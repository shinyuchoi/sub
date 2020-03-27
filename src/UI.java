import sas.swing.plaf.MultiLineShadowUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI extends JFrame {

    //UI
    JFrame jFrame;
    JLabel subTextLabel, subTextUnderLabel, fontSizeExplainLabel, infoLabel;
    JButton open, exit, fontSmaller, fontBigger;


    //Values
    private String filePath;
    private boolean playing;


    private int index;

    int fontSize, frameHeight, frameWidth;
    long startTime, timeControl;

    Dimension bigButtonSize, smallButtonSize, screenSize;
    ArrayList<String> sub, subUnder;
    JPopupMenu jPopupMenu;
    ArrayList<Integer> timeStamp;
    JMenuItem[] jMenuItems;


    //class
    FileIO fileIO;
    SubThread subThread;
    Thread thread;

    UI() throws Exception {

        initValues();
        initFrame();
        initButtons();
        initLabels();
        setJPopupMenu();


        start();

    }


    synchronized void start() {
        playing = true;

        //start Thread
        subThread = new SubThread(this);
        thread = new Thread(subThread);
        thread.start();
    }

    void initValues() {
        fontSize = 50;
        playing = false;
        timeControl = 0;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = screenSize.height;
        frameWidth = screenSize.width;
        bigButtonSize = new Dimension(70, 50);
        smallButtonSize = new Dimension(50, 35);

        fileIO = new FileIO();
    }

    void initFrame() {
        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);

        //jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setSize(frameWidth, 400);
        //jFrame.setLocation(0, frameHeight - jFrame.getHeight() - 50);
        jFrame.setLocation(0, frameHeight - jFrame.getHeight() - 550);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);
        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent m) {
                if (SwingUtilities.isRightMouseButton(m)) {
                    doPop(m);

                }
            }


        });
        jFrame.addKeyListener(new keyInputs(this));
        jFrame.setFocusable(true);
    }

    void doPop(MouseEvent e) {
        jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    void setJPopupMenu() {
        String[] menus = {"초기화", "시간 설정", "재생 정보", "투명화 해제", "투명화", "도움말", "종료"};
        jPopupMenu = new JPopupMenu();
        jMenuItems = new JMenuItem[menus.length];

        for (int i = 0; i < menus.length; i++) {
            jMenuItems[i] = new JMenuItem(menus[i]);
            jPopupMenu.add(jMenuItems[i]);
            jPopupMenu.addSeparator();
        }

        //초기화
        jMenuItems[0].addActionListener(evt -> {
            if (isPlaying()) {
                playing = false;
                thread.interrupt();

                initValues();
                initButtons();
                subTextLabel.setBackground(Color.lightGray);
                subTextUnderLabel.setBackground(Color.lightGray);
                jFrame.setBackground(Color.lightGray);
                fontSizeExplainLabel.setBackground(Color.gray);
                fontSizeExplainLabel.setText("글씨 크기");
                jPopupMenu.setVisible(false);
                jFrame.repaint();
                subTextLabel.setText("싱크조절: q(-5),a(-1),d(+1),e(+5)");
                subTextUnderLabel.setText("자막위치: w(위),s(아래) // ESC(메뉴열기)");
                jFrame.repaint();
                setFilePath(null);
                start();

            } else {
                jPopupMenu.setVisible(false);

            }
        });


        //시간 설정 명령
        jMenuItems[1].addActionListener(evt -> {
            String tmp = JOptionPane.showInputDialog(jFrame, "HMMSS 예> 1:23:45=>12345//0:12:34=>1234", getTimetoString(timeControl / 1000));
            if (tmp != null) {
                StringBuilder name = new StringBuilder(tmp);
                Pattern p = Pattern.compile("[0-9]{1,5}");
                Matcher m = p.matcher(name.toString());

                if (name.length() < 6 && m.find()) {
                    for (int i = name.length(); i < 5; i++) {
                        name.insert(0, "0");
                    }
                    timeControl = getTimeDiff(name.toString().trim());
                }
                if (timeStamp != null)
                    arrangeIndex();
            }
        });

        jMenuItems[2].addActionListener(e -> {
            JOptionPane.showMessageDialog(jFrame,
                    "재생중:\t" + getFilePath() + "\n싱크:\t" + (timeControl / 1000) + "초",
                    "재생 정보",
                    JOptionPane.PLAIN_MESSAGE);
        });

        //투명화 해제
        jMenuItems[3].addActionListener(e -> {
            initButtons();
            subTextLabel.setBackground(Color.lightGray);
            subTextUnderLabel.setBackground(Color.lightGray);
            jFrame.setBackground(Color.lightGray);
            fontSizeExplainLabel.setBackground(Color.gray);
            fontSizeExplainLabel.setText("글씨 크기");
            jPopupMenu.setVisible(false);
            jFrame.repaint();

        });

        //투명화
        jMenuItems[4].addActionListener(e -> {
            removeButtons();
            removeLabels();
            jFrame.repaint();
        });
        jMenuItems[5].addActionListener(e -> {
            JOptionPane.showMessageDialog(jFrame,
                    "싱크조절\n5초:q(-)   e(+)\n1초:a(-1)   d(+1)\n\n자막위치\nw(위로),s(아래로)",
                    "도움말",
                    JOptionPane.PLAIN_MESSAGE);
        });

        //종료
        jMenuItems[jMenuItems.length - 1].addActionListener(e -> System.exit(0));

    }

    void arrangeIndex() {
        for (int i = 0; i < timeStamp.size(); i++) {
            if (timeStamp.get(i) > timeControl + System.currentTimeMillis() - startTime) {
                setIndex(i);
                break;
            }
        }
    }

    void initButtons() {
        //create buttons
        open = createButton(50, 50, bigButtonSize, "열기", new OpenFile());
        exit = createButton(jFrame.getWidth() - 70, 0, smallButtonSize, "X", null);
        exit.addActionListener(e -> System.exit(0));


        fontBigger = createButton(150, 15, smallButtonSize, "+", e -> {
            fontSize += 5;
            subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
            subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        });


        fontSmaller = createButton(150, 100, smallButtonSize, "-", e -> {
            fontSize -= 5;
            subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
            subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        });
    }

    void removeButtons() {
        jFrame.remove(open);
        jFrame.remove(exit);
        jFrame.remove(fontBigger);
        jFrame.remove(fontSmaller);
    }

    void removeLabels() {
        infoLabel.setText("");
        subTextLabel.setBackground(new Color(0, 0, 0, 0));
        subTextUnderLabel.setBackground(new Color(0, 0, 0, 0));
        jFrame.setBackground(new Color(0, 0, 0, 0));
        fontSizeExplainLabel.setBackground(new Color(0, 0, 0, 0));
        fontSizeExplainLabel.setText("");
    }

    void initLabels() {
        subTextLabel = createLabel(0, 0, frameWidth, (int) ((frameHeight * 0.20) - 15) / 2, "test", Color.lightGray);
        subTextLabel.setLocation(0, jFrame.getHeight() - (subTextLabel.getHeight() * 2));
        subTextLabel.setFont(new Font(null, Font.BOLD, fontSize));
        subTextLabel.setText("싱크조절: q(-5),a(-1),d(+1),e(+5)");

        subTextUnderLabel = createLabel(0, subTextLabel.getHeight(), frameWidth, (int) ((frameHeight * 0.20) - 15) / 2, "test", Color.lightGray);
        subTextUnderLabel.setLocation(0, jFrame.getHeight() - subTextLabel.getHeight());
        subTextUnderLabel.setFont(new Font(null, Font.BOLD, fontSize));
        subTextUnderLabel.setText("자막위치: w(위),s(아래) // ESC(메뉴열기)");


        fontSizeExplainLabel = createLabel((int) (fontBigger.getX() + ((0.5) * smallButtonSize.width) - 50), 50, 100, 50, "글씨 크기", Color.LIGHT_GRAY);
        infoLabel = createLabel(jFrame.getWidth() - 150, 35, 150, 40, "", new Color(0, 0, 0, 0));
        setInfoLabel("정보표시창");
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
                setFilePath(selectedFile.toString());
                try {
                    fileIO.sim2Array(filePath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                sub = fileIO.getSub();
                subUnder = fileIO.getSubUnder();
                timeStamp = fileIO.getTimeStamp();
                sub.add("종료되었습니다.");
                subUnder.add("사용해주셔서 감사합니다.");

                timeStamp.add(timeStamp.get(timeStamp.size() - 1) + 2000);
            }
        }
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
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

    String getTimetoString(long l) {
        String ret;
        ret = l / 3600 + "";
        l %= 3600;
        ret += l / 60 + "";
        //l %= 60;
        return ret + (l % 60);
    }

    long getTimeDiff(String st) {
        long ret = 0;
        ret += Integer.parseInt(st.substring(0, 1)) * 3600;
        ret += Integer.parseInt(st.substring(1, 3)) * 60;
        ret += Integer.parseInt(st.substring(3, 5));

        return ret * 1000;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void indexPlusPlus() {
        this.index++;
    }
}
