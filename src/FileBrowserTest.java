import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;


public class FileBrowserTest
{
    String filePath;
    JLabel label, instructionLabel;
    JFrame jFrame;
    JPanel panel, instructionPanel;
    JButton openButton;
    ArrayList<String> sub;
    ArrayList<Integer> timeStamp;

    int panelX, panelY;


    void initatFrame()
    {
        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(null);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);

        label = new JLabel("<html><h1> &lt;자막__위치> </h1></html>", SwingConstants.CENTER);

        openButton = new JButton("Open");
        openButton.setSize(100, 50);
        jFrame.add(openButton);
        openButton.setLocation(jFrame.getWidth() - 500, 100);
        openButton.addMouseListener(new OpenFile());


        panel = new JPanel();
        panel.add(label);
        jFrame.add(panel);

        panel.setBackground(Color.yellow);
        panel.setSize(1000, 150);
        label.setSize(panel.getHeight(), panel.getWidth());
        panelX = (jFrame.getWidth() - panel.getWidth()) / 2;
        panelY = jFrame.getHeight() - panel.getHeight();
        panel.setLocation(panelX, panelY);

        instructionPanel = new JPanel();
        instructionLabel = new JLabel("<html><h1> ##사용법## <br> Open을 클릭 후 파일 선택 <br>현재 smi 만 가능 <br>제작:shinyu.choi@tum.de</h1></html>", SwingConstants.CENTER);
        instructionPanel.setSize(jFrame.getWidth(), jFrame.getHeight());
        instructionLabel.setSize(300, 300);
        instructionPanel.add(instructionLabel);

        instructionPanel.setLocation(0, 300);
        jFrame.add(instructionPanel);

    }

    void start()
    {
        jFrame.remove(instructionPanel);
        jFrame.remove(openButton);
        jFrame.setBackground(new Color(0, 0, 0, 0));
        panel.setBackground(new Color(0, 0, 0, 0));
        jFrame.repaint();

        Clock clock = new Clock();
        clock.start();
        FileIO fileIO;
        try
        {

            fileIO = new FileIO(filePath);
            sub = fileIO.getSub();
            timeStamp = fileIO.getTimeStamp();


            int end = timeStamp.get((timeStamp.size() - 1))+20000;

            int index = 0;

            while (clock.getI() < end)
            {
                if (clock.getI() > timeStamp.get(index))
                {
                    label.setText(sub.get(index));
                    jFrame.repaint();
                    index++;
                }
                Thread.sleep(1);

            }


        } catch (Exception e) {return;}


    }

    public FileBrowserTest()
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
        new FileBrowserTest();
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
