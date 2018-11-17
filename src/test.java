import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

import java.io.File;
import java.util.ArrayList;
import javax.swing.event.*;




public class test extends JFrame
{
    Container contentPane;
    JLabel b1, b2, b3, b4;
    String filePath;
    JFrame jFrame;

    int fontSize;

    JMenuBar menu;

    String LabelsetText(String sub)
    {

        return null;
    }

    test() throws Exception
    {

        /*jFrame = new JFrame("frame");
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(200, 200);
        jFrame.setVisible(true);

*/

        JPanel panel = new JPanel();


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setSize(500,500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(new Color(0, 0, 0, 0));
        setVisible(true);
        //메뉴바


        b1 = new JLabel();
        b1.setFont(new Font("Serif", Font.PLAIN, 30));

        add(b1, JLabel.CENTER);


        while (true)
        {
            System.out.println("hi");
            Thread.sleep(1000);
            if (filePath != null)
            {

                break;
            }
        }
        FileIO fi = new FileIO(filePath);
        ArrayList<Integer> timeStamp = fi.getTimeStamp();
        ArrayList<String> sub = fi.getSub();
        int index = 0;
        Clock c = new Clock();
        c.start();

        while (true)
        {
            if (index == timeStamp.size())
                return;
            if (timeStamp.get(index) < c.getI())
            {
                b1.setText(sub.get(index));
                System.out.println(sub.get(index));
                index++;
                b1.repaint();
            }
            Thread.sleep(1);
        }
    }



    private void create()
    {
        jFrame = new JFrame("frame");
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(200, 200);
        jFrame.setVisible(true);

    }
    public static void main(String[] args) throws Exception
    {

        JFrame.setDefaultLookAndFeelDecorated(true);

        new test();
    }


    //menu



}
