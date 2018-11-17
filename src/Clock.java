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
