import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SubThread implements Runnable {

    UI ui;
    FileIO fileIO;
    String filePath;

    //from TimeControllerJoptional
    long timeControl;

    //sum of Pause
    long pauseTime;

    ArrayList<String> sub, subUnder;
    ArrayList<Integer> timeStamp;


    long startTime, playingTime;
    AtomicBoolean next;
    int subIndex;

    //turn on/off Infolabel
    //turn on/off menu
    boolean indicatorInfolabel, subRunning;


    SubThread(UI ui) {
        this.ui = ui;
    }


    @Override
    public void run() {
        //Init Values
        subRunning = false;
        fileIO = new FileIO();
        try {

            if (filePath.endsWith("srt")) {
                fileIO.srt2Array(filePath);
            } else {
                fileIO.sim2Array(filePath);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        sub = fileIO.getSub();
        subUnder = fileIO.getSubUnder();
        timeStamp = fileIO.getTimeStamp();


        timeControl = 0;
        startTime = System.currentTimeMillis();
        subIndex = 0;


        //error -> restart 3
        if (timeStamp.size() == 0) {
            try {
                ui.subTextLabel.setText("파일을 읽지 못했습니다. ");
                ui.restart(3);
            } catch (InterruptedException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }

        //init Labels
        ui.setTextLabels("", "");
        AtomicInteger counter = new AtomicInteger(0);
        ui.isTransparent = true;
        ui.removeButtons();
        ui.doTransparentLabels();


        //With EasyStart
        if (ui.easyStartCheckBox.isSelected()) {
            long pauseStart = System.currentTimeMillis();
            createButtonsForEasyStart();


            while (counter.incrementAndGet() < 200) {
                try {
                    Thread.sleep(10);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ui.setTextLabels(filePath, "파일을 성공적으로 읽었습니다.");

            }

            while (!next.get()) {
                try {
                    Thread.sleep(10);
                    ui.setTextLabels(sub.get(subIndex), subUnder.get(subIndex));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            addPauseTime(System.currentTimeMillis() - pauseStart - timeStamp.get(subIndex));
        } else {
            //without Easystart
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ui.isPlaying()) {
                    ui.infoLabel.repaint();
                    setPlayingtime(System.currentTimeMillis());
                    if (getPlayingTime() < timeStamp.get(subIndex)) {
                        ui.infoLabel.setText("첫자막까지>" + labelTimer(timeStamp.get(0) - getPlayingTime()));
                        if (counter.incrementAndGet() < 200) {
                            ui.setTextLabels(filePath, "파일을 성공적으로 읽었습니다.");
                        } else {
                            ui.setTextLabels(" ", " ");
                        }

                    } else {
                        break;
                    }
                }

            }
        }
        ui.removeButtons();
        ui.infoLabel.setText("");
        counter.set(0);
        subRunning = true;

        //Compare Timestamp and playtime -> Set subText to display
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;

            }
            if (ui.isPlaying()) {

                setPlayingtime(System.currentTimeMillis());

                if (timeStamp.get(subIndex) < getPlayingTime()) {

                    /* for test
                    System.out.println("over>>"+sub.get(subIndex));
                    System.out.println("under>>"+subUnder.get(subIndex));
                    */

                    if (sub.get(subIndex).trim().equals("&nbsp;")) {
                        ui.setTextLabels(" ", " ");
                    } else {
                        ui.setTextLabels(sub.get(subIndex), subUnder.get(subIndex));
                    }

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

                //SetText for info label
                if (indicatorInfolabel) {
                    ui.infoLabel.setText("싱크: " + (timeControl / 1000) + "초");
                    if (counter.incrementAndGet() == 600) {
                        indicatorInfolabel = false;
                        counter.set(0);
                    }

                } else {
                    ui.infoLabel.setText("");
                }
            }

        }
    }

    /**
     * Convert currentTimeMillis to HH:MM:SS Form
     *
     * @param n :System.currentTimeMillis
     * @return String n-> HH:MM:SS
     */
    String labelTimer(long n) {
        n /= 1000;
        return String.format("%02d:%02d:%02d", n / 3600, (n % 3600) / 60, (n % 3600) % 60);
    }

    void createButtonsForEasyStart() {

        next = new AtomicBoolean(false);
        ui.createButton(ui.exit.getX() - 90, ui.exit.getY() + ui.smallButtonSize.height, new Dimension(70, 35), "<이전", e -> {
            if (subIndex > 0) {
                if (subIndex > 1 && (sub.get(subIndex - 1).equals(" ") && subUnder.get(subIndex - 1).equals(" ")))
                    subIndex -= 2;
                else
                    subIndex--;
            }
        });
        ui.createButton(ui.exit.getX() - 20, ui.exit.getY() + ui.smallButtonSize.height, new Dimension(70, 35), "다음>", e -> {
            if (subIndex < sub.size()) {
                if (subIndex < sub.size() - 1 && (sub.get(subIndex + 1).equals(" ") && subUnder.get(subIndex + 1).equals(" ")))
                    subIndex += 2;
                else
                    subIndex++;
            }
        });

        ui.createButton(ui.exit.getX(), ui.exit.getY(), ui.smallButtonSize, "Go", e -> {
            next.set(true);
        });
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Add paused time while opening the menu
     *
     * @param pauseTime : Period of pausetime(System.currentTimeMillis- (pauseStart = System.currentTimeMillis())
     */
    public void addPauseTime(long pauseTime) {
        this.pauseTime += pauseTime;
    }

    /**
     * Display current sync information on Infolabel
     *
     * @param n : current Sync
     */
    public void syncConrol(double n) {
        if (ui.isPlaying()) {

            timeControl += (n * 1000);
            arrangeIndex();
            ui.infoLabel.setText("싱크: " + (timeControl / 1000) + "초");
            indicatorInfolabel = true;

        }
    }

    /**
     * @return current PlayingTime(calculated)
     */
    public long getPlayingTime() {
        return timeControl + playingTime - startTime - pauseTime;
    }

    //setter for playiung time
    public void setPlayingtime(long playingTime) {
        this.playingTime = playingTime;
    }

    /**
     * set Index after playtime is changed.
     */
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