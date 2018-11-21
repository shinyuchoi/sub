import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class FileBrowserTest
{
    String filePath;
    JLabel label;
    JFrame jFrame;
    JPanel panel;
    JButton openButton;

    int numOfLabels, panelX, panelY;

    JMenuBar menuBar()
    {
        JMenuBar jb = new JMenuBar();

        JMenu open = new JMenu("Open");
        JMenu slow = new JMenu("1초 느리게");
        JMenu fast = new JMenu("1초 빠르게");
        jb.add(open);
        jb.add(slow);
        jb.add(fast);




        return jb;
    }

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
        openButton.setSize(100,50);
        jFrame.add(openButton);
        openButton.setLocation(jFrame.getWidth()-500,100);
        openButton.addMouseListener(new OpenFile());


        panel = new JPanel();
        panel.add(label);
        jFrame.add(panel);

        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setSize(500, 200);
        label.setSize(panel.getHeight(),panel.getWidth());
        panelX = (jFrame.getWidth() - label.getText().length() * (3)) / 2;
        panelY = 300;
        panel.setLocation(panelX, panelY);
        
    }

    public FileBrowserTest()
    {
        numOfLabels = 20;

        initatFrame();







        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        jFrame.setBackground(new Color(0, 0, 0, 0));
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
                System.out.println(filePath);
            }
        }

        @Override
        public void mousePressed(MouseEvent e){}

        @Override
        public void mouseReleased(MouseEvent e){}

        @Override
        public void mouseEntered(MouseEvent e){}
        @Override
        public void mouseExited(MouseEvent e){}

    }
}
