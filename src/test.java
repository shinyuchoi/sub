import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Clock extends Thread
{
    int i;

    @Override
    public void run()
    {
        i = 0;
        int sec = 0;
        try
        {
            while (true)
            {
                i++;
                Thread.sleep(1);
            }
        } catch (Exception e)
        {

        }
    }

    public int getI()
    {
        return i;
    }
}


public class test extends JFrame
{
    Container contentPane;
    JLabel b1, b2, b3, b4;
    enum language {KRCC, ENCC}

    int fontSize;



    String LabelsetText(String sub)
    {

        return null;
    }

    test() throws Exception
    {
        setTitle("Swing Ex1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());
        setSize(500, 200);

        setVisible(true);


        b1 = new JLabel();
        b1.setFont(new Font("Serif",Font.PLAIN,30));
        b1.setText("hi");
        contentPane.add(b1,JLabel.CENTER);

        FileIO fi = new FileIO("C:/Users/Choi/Desktop/test.smi");

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


    public static void main(String[] args) throws Exception
    {
        new test();
    }

}
