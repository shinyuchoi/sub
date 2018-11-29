import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;


public class Main
{
    String filePath, fontStart, fontEnd;
    JLabel label, instructionLabel;
    JFrame jFrame;
    JPanel panel, instructionPanel;
    JButton openButton;
    ArrayList<String> sub;
    ArrayList<Integer> timeStamp;
    Clock clock;
    int panelX, panelY, fontSize;
    float sync = 0;
    boolean syncVisiable = false;

    void initatFrame()
    {
        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);
        fontSize = 80;
        fontStart = "<html><strong><font size = " + fontSize + " color=white>";
        fontEnd = " </font> </html>";
        label = new JLabel(fontStart + "&lt;자막__위치><br>&lt;자막__위치>" + fontEnd, SwingConstants.CENTER);

        openButton = new JButton("Open");
        openButton.setSize(100, 50);
        jFrame.add(openButton);
        openButton.setLocation(jFrame.getWidth() - 500, 100);
        openButton.addMouseListener(new OpenFile());


        panel = new JPanel();
        panel.add(label);
        jFrame.add(panel);

        panel.setBackground(Color.blue);
        panel.setSize(1000, 150);
        label.setSize(panel.getHeight(), panel.getWidth());
        panelX = (jFrame.getWidth() - panel.getWidth()) / 2;
        panelY = jFrame.getHeight() - panel.getHeight();
        panel.setLocation(panelX, panelY);


        instructionPanel = new JPanel();
        instructionLabel = new JLabel("<html><h1> ##사용법## <br> Open을 클릭 후 파일 선택 <br>현재 smi 만 가능 <br>제작:shinyu.choi@tum.de</h1></html>", SwingConstants.CENTER);
        instructionPanel.setSize(300, 150);
        instructionLabel.setSize(300, 150);
        instructionPanel.add(instructionLabel);
        instructionPanel.setLocation(panelX, panelY - 200);
        jFrame.add(instructionPanel);


    }

    void start()
    {
        instructionLabel.setText("");
        instructionPanel.setBackground(new Color(0, 0, 0, 0));
        jFrame.remove(openButton);
        jFrame.setBackground(new Color(0, 0, 0, 0));
        panel.setBackground(new Color(0, 0, 0, 0));
        jFrame.repaint();

        clock = new Clock();
        clock.start();

        ShowSync ss = new ShowSync();
        ss.start();

        jFrame.addKeyListener(new MyKeyListener());
        FileIO fileIO;
        try
        {

            fileIO = new FileIO(filePath);
            sub = fileIO.getSub();
            timeStamp = fileIO.getTimeStamp();


            int end = timeStamp.get((timeStamp.size() - 1)) + 20000;
            int index = 0;

            while (clock.getI() < end)
            {
                if (clock.getI() > timeStamp.get(index))
                {
                    label.setText(fontStart + sub.get(index) + fontEnd);
                    jFrame.repaint();
                    index++;
                }
                Thread.sleep(2);

            }


        } catch (Exception e) {return;}


    }

    public Main()
    {
        initatFrame();
        while (filePath == null)
            try
            {
                Thread.sleep(1000);
            } catch (Exception e) {}
        start();
    }


    public static void main(String[] args)
    {
        new Main();
    }

    class ShowSync extends Thread
    {
        @Override
        public void run()
        {
            String txt;
            while (true)
            {
                try
                {
                    txt = "<html><font size =20 color = white >싱크 " + (sync/1000) + " 초</font></html>";
                    if (syncVisiable)
                    {
                        instructionLabel.setText(txt);
                        jFrame.repaint();
                        Thread.sleep(100);
                        instructionLabel.setText("");
                        syncVisiable= false;
                        jFrame.repaint();
                    }
                    Thread.sleep(100);

                    } catch (Exception e)
                {
                }
            }
        }
    }

    class MyKeyListener extends KeyAdapter
    {
        public void keyPressed(KeyEvent evt)
        {
            switch (evt.getKeyCode())
            {
                case 74:
                    // 74 j
                    clock.setI(clock.getI() - 100);
                    sync -= 100;
                    syncVisiable = true;
                    break;
                case 76:
                    // 76 l
                    clock.setI(clock.getI() + 100);
                    sync += 100;
                    syncVisiable = true;
                    break;
                case 79:
                    // 79 o
                    clock.setI(clock.getI() + 1000);
                    sync += 1000;
                    syncVisiable = true;
                    break;
                case 85:
                    // 85 u
                    clock.setI(clock.getI() - 1000);
                    sync -= 1000;
                    syncVisiable = true;
                    break;

            }
        }


    }

    class OpenFile implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            JFrame window = new JFrame();
            JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Downloads"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(".smi", "smi");
            jfc.addChoosableFileFilter(filter);
            int result = jfc.showOpenDialog(window);
            if (result == jfc.APPROVE_OPTION)
            {
                File selectedFile = jfc.getSelectedFile();
                filePath = selectedFile.toString();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

    }
}
