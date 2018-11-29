class Clock extends Thread
{
    long i, curr;

    Clock(long curr)
    {
        this.curr = curr;
    }

    @Override
    public void run()
    {
        i = 0;
        int sec = 0;
        try
        {
            while (true)
            {
                i = curr - System.currentTimeMillis();
                Thread.sleep(1);
            }
        } catch (Exception e)
        {

        }
    }

    public long getI()
    {
        return i;
    }
}
