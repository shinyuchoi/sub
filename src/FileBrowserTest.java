import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


public class FileBrowserTest
{
    JLabel[] labels;
    JPanel[] panels;
    String filePath;
    JFrame jFrame;
    int numOfLabels;

    JMenuBar menuBar()
    {
        JMenuBar jb = new JMenuBar();

        JMenu open = new JMenu("Open");
        JMenu slow = new JMenu("1초 느리게");
        JMenu fast = new JMenu("1초 빠르게");
        jb.add(open);
        jb.add(slow);
        jb.add(fast);


        open.addMenuListener(new OpenFile());


        return jb;
    }


    public FileBrowserTest()
    {
        numOfLabels = 20;

        jFrame = new JFrame();
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new GridLayout(numOfLabels, 1));
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);

        labels = new JLabel[numOfLabels];
        panels = new JPanel[numOfLabels];

        for (int i = 0; i < numOfLabels; i++)
        {
            labels[i] = new JLabel("",SwingConstants.CENTER);
            panels[i] = new JPanel();

            if(i%2==0)
                labels[i].setBackground(new Color(220,220,220));
            else
                labels[i].setBackground(new Color(195,195,195));

        }
        labels[18].setBackground(Color.red);
        labels[18].setText("<html><h1><font color='red'>^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__^__</font></h1></html>");



        for (int i = 0; i < numOfLabels; i++)
        {
            jFrame.add(labels[i]);
        }


        try
        {
            Thread.sleep(1000);
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


    class OpenFile implements MenuListener
    {
        @Override
        public void menuSelected(MenuEvent e)
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
        public void menuDeselected(MenuEvent e)
        {
        }

        @Override
        public void menuCanceled(MenuEvent e)
        {
        }
    }
}
