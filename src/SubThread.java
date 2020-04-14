import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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


    String labelTimer(long n) {
        n /= 1000;
        return String.format("%02d:%02d:%02d", n / 3600, (n % 3600) / 60, (n % 3600) % 60);
    }

    @Override
    public void run() {
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

        if (timeStamp.size() == 0) {
            try {
                ui.subTextLabel.setText("파일을 읽지 못했습니다. ");
                ui.restart(3);
            } catch (InterruptedException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }


       /* ui.setTextLabels(filePath, "파일을 성공적으로 읽었습니다.");


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

        ui.setTextLabels("", "");
        int counter = 0;
        ui.isTransparent = true;
        ui.removeButtons();
        ui.removeLabels();
        if (ui.easyStartCheckBox.isSelected()) {
            AtomicBoolean next = new AtomicBoolean(false);
            long pauseStart = System.currentTimeMillis();

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

            while (!next.get()) {
                try {
                    Thread.sleep(1);
                    ui.setTextLabels(sub.get(subIndex), subUnder.get(subIndex));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            addPauseTime(System.currentTimeMillis() - pauseStart - timeStamp.get(subIndex));
        } else {
            // before first sub
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (ui.isPlaying()) {
                    ui.infoLabel.repaint();
                    setPlayingtime(System.currentTimeMillis());
                    if (getPlayingTime() < timeStamp.get(subIndex)) {
                        ui.infoLabel.setText("첫자막까지>" + labelTimer(timeStamp.get(0) - getPlayingTime()));
                        if (getPlayingTime() % 1000 == 0)
                            System.out.println(getPlayingTime());
                        if (counter < 1000) {
                            ui.setTextLabels(filePath, "파일을 성공적으로 읽었습니다.");
                            counter++;
                        } else {
                            ui.setTextLabels(" ", " ");
                            //ui.jFrame.repaint();
                        }

                    } else {
                        break;
                    }
                }

            }
        }
        ui.removeButtons();
        ui.infoLabel.setText("");
        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                return;

            }
            if (ui.isPlaying()) {

                setPlayingtime(System.currentTimeMillis());

                if (timeStamp.get(subIndex) < getPlayingTime()) {

                    //System.out.println("over>>"+sub.get(subIndex));
                    //System.out.println("under>>"+subUnder.get(subIndex));

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