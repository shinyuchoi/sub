import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


public class FileBrowserTest
{
    JLabel b1, b2, b3, b4;
    String filePath;
    JFrame jFrame;



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
        jFrame = new JFrame("frame");
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setBackground(Color.white);
        jFrame.setVisible(true);

        
        b1 = new JLabel();
        b1.setText("hi");
        b1.setBackground(Color.red);
        b1.setSize(300,300);

        jFrame.add(b1);
        b1.setLocation(100,100);


        jFrame.setBackground(new Color(0,0,0,0));
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


/*
        JFrame window = new JFrame();

        JFileChooser jfc = new JFileChooser();

        jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Downloads"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".smi", "smi");
        jfc.addChoosableFileFilter(filter);

        int result = jfc.showOpenDialog(window);
        System.out.println(result);

        if (result == jfc.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile);
        }*/


}
