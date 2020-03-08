class Timer extends Thread {
    public void run() {
        long start ,stop,end;
        int n =0;
        start  = System.currentTimeMillis();
        while(true){
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop = System.currentTimeMillis()-start ;
            if(stop %1000==0)
                System.out.println(stop);
        }
    }
}