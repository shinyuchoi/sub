import java.util.ArrayList;

public class SubThread implements Runnable {

    UI ui;


    SubThread(UI ui) {
        this.ui = ui;
    }

    @Override
    public void run() {
        while (ui.getFilePath() == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ui.timeControl = 0;
        ArrayList<String> sub = ui.sub;
        ArrayList<String> subUnder = ui.subUnder;
        ArrayList<Integer> timeStamp = ui.timeStamp;
        ui.setIndex(0);
        ui.startTime = System.currentTimeMillis();
        long startTime = ui.startTime;
        long tmpTime;

        int i;
        ui.setTextLabels("파일을 성공적으로", "읽었습니다.");
        ui.removeLabels();

        while (ui.isPlaying()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i = ui.getIndex();
            tmpTime = System.currentTimeMillis();
            if (timeStamp.get(i) + ui.timeControl < tmpTime - startTime) {

                if (sub.get(i).trim().equals("&nbsp;")) {
                    ui.setTextLabels(" ", ".");
                } else {
                    ui.setTextLabels(sub.get(i), subUnder.get(i));
                }
                ui.jFrame.repaint();
                //index++;
                ui.indexPlusPlus();
                if (i == timeStamp.size() - 1) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

        }
        System.out.println("끝");
    }
}
