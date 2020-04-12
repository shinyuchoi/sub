import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SubThread implements Runnable {

    UI ui;
    long startTime;
    long timeControl;
    long pauseTime;
    ArrayList<String> sub, subUnder;
    ArrayList<Integer> timeStamp;

    long playingTime;
    int subIndex;
    boolean indicatorInfolabel;

    SubThread(UI ui) {
        this.ui = ui;
    }

    FileIO fileIO;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath;

    @Override
    public void run() {
        fileIO = new FileIO();
        try {
            fileIO.sim2Array(filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sub = fileIO.getSub();
        subUnder = fileIO.getSubUnder();
        timeStamp = fileIO.getTimeStamp();


        timeControl = 0;
        startTime = System.currentTimeMillis();
        subIndex = 0;

        if (timeStamp.size() == 0) {
            try {
                ui.restart(3);
            } catch (InterruptedException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }


        ui.setTextLabels(filePath, "파일을 성공적으로 읽었습니다.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ui.setTextLabels("", "");
        int counter = 0;
        ui.isTransparent = true;
        ui.removeButtons();
        ui.removeLabels();
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;

            }
            if (ui.isPlaying()) {

                setPlayingtime(System.currentTimeMillis());

                if (timeStamp.get(subIndex) < getPlayingTime()) {


                    if (sub.get(subIndex).trim().equals("&nbsp;")) {
                        ui.setTextLabels(" ", " ");
                    } else {
                        ui.setTextLabels(sub.get(subIndex), subUnder.get(subIndex));
                    }
                    ui.jFrame.repaint();

                    subIndex++;

                    if (subIndex == timeStamp.size() - 1) {
                        try {
                            ui.restart(5);
                        } catch (InterruptedException | IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
                if (indicatorInfolabel) {
                    ui.infoLabel.setText("싱크: " + (timeControl / 1000) + "초");
                    counter++;
                    if (counter == 3000) {
                        indicatorInfolabel = false;
                        counter = 0;
                    }

                } else {
                    ui.infoLabel.setText("");
                }
            }

        }
    }

    public void addPauseTime(long pauseTime) {
        this.pauseTime += pauseTime;
    }

    public void syncConrol(double n) {
        if (ui.isPlaying()) {

            timeControl += (n * 1000);
            arrangeIndex();
            ui.infoLabel.setText("싱크: " + (timeControl / 1000) + "초");
            indicatorInfolabel = true;

        }
    }

    public long getPlayingTime() {
        return timeControl + playingTime - startTime - pauseTime;
    }

    public void setPlayingtime(long playingTime) {
        this.playingTime = playingTime;
    }

    public void arrangeIndex() {

        for (int i = 0; i < timeStamp.size(); i++) {
            if (timeStamp.get(i) > getPlayingTime()) {
                subIndex = i;
                break;
            }
        }
        if (sub.get(subIndex).trim().equals("&nbsp;")) {
            ui.setTextLabels(" ", " ");
        } else {
            ui.setTextLabels(sub.get(subIndex), subUnder.get(subIndex));
        }
        ui.jFrame.repaint();
    }
}