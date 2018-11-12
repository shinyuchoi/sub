import java.util.ArrayList;
class Clock extends Thread
{
    int i ;
    @Override
    public void run()
    {
        i = 0 ;
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
}
class MyThread extends Thread
{
    int i, pointer;
    ArrayList<Integer> timeStamp;
    ArrayList<String> sub;

    MyThread(ArrayList<Integer> timeStamp, ArrayList<String> sub)
    {
        i = 0;
        this.timeStamp = timeStamp;
        this.sub = sub;
        pointer = 0;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {

            }
        } catch (Exception e)
        {

        }
    }
}

public class test
{
    public static void main(String[] args) throws Exception
    {
        FileIO fi = new FileIO("C:/Users/Choi/Desktop/test.smi");

        ArrayList<Integer> timeStamp = fi.getTimeStamp();
        ArrayList<String> sub = fi.getSub();
        MyThread t = new MyThread(timeStamp, sub);
        t.start();
        Clock c = new Clock();
        c.start();
    }

}
