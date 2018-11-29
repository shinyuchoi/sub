class Clock extends Thread
{

    private long i;


    @Override
    public void run()
    {
        long curr = System.currentTimeMillis();

        try
        {
            while (true)
            {
                i = System.currentTimeMillis() - curr;
                Thread.sleep(1);
            }
        } catch (Exception e)
        {

        }
    }

    long getI()
    {
        return i;
    }

    void setI(long i)
    {
        this.i = i;
    }

}
