
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class TimeControllerJOptional implements ActionListener {
    UI ui;
    JLabel currentPlayTimeLabel;
    int v;
    long pauseStart;

    public TimeControllerJOptional(UI ui) {
        this.ui = ui;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (ui.isPlaying()) {
            pauseStart = System.currentTimeMillis();

            currentPlayTimeLabel = new JLabel();
            currentPlayTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            setCurrentPlayTimeLabelText(labelTimer(ui.subThread.getPlayingTime()), labelTimer(ui.subThread.getPlayingTime()));
            ui.setPlaying(false);
            ui.jPopupMenu.setVisible(false);
            JOptionPane jOptionPane = new JOptionPane();
            UIManager.put("OptionPane.minimumSize", new Dimension(ui.frameWidth, 200));
            if (jOptionPane.showConfirmDialog(ui, new Object[]{currentPlayTimeLabel, getSlider(),}, "플레이타임 설정", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (ui.subThread.getPlayingTime() > v) {
                    ui.subThread.timeControl -= Math.abs(ui.subThread.getPlayingTime() - v);
                } else {
                    ui.subThread.timeControl += Math.abs(ui.subThread.getPlayingTime() - v);
                }
            }
            ui.subThread.arrangeIndex();
            ui.setPlaying(true);

            ui.subThread.addPauseTime(System.currentTimeMillis() - pauseStart);

        } else {
            ui.jPopupMenu.setVisible(false);
        }
    }


    void setCurrentPlayTimeLabelText(String currentTime, String choosenTime) {
        currentPlayTimeLabel.setText(String.format("현재 플레이타임: %s >>>> 선택된 플레이타임: %s", currentTime, choosenTime));
    }

    JSlider getSlider() {
        JSlider slider = new JSlider(0, ui.subThread.timeStamp.get(ui.subThread.timeStamp.size() - 1) + 20000);

        Hashtable position = new Hashtable();
        for (int i = 0; i < slider.getMaximum(); i += 300000) {

            position.put(i, new JLabel(labelTimer(i)));

        }
        slider.setValue((int) ui.subThread.getPlayingTime());
        slider.setPaintLabels(true);
        slider.setLabelTable(position);
        slider.setMajorTickSpacing(300000);
        slider.setPaintTicks(true);

        ChangeListener changeListener = changeEvent -> {
            JSlider theSlider = (JSlider) changeEvent.getSource();
            if (!theSlider.getValueIsAdjusting()) {
                v = theSlider.getValue();
                setCurrentPlayTimeLabelText(labelTimer(ui.subThread.getPlayingTime()), labelTimer(v));
            }
        };
        slider.addChangeListener(changeListener);
        return slider;
    }

    String labelTimer(long n) {
        n /= 1000;
        return String.format("%02d:%02d:%02d", n / 3600, (n % 3600) / 60, (n % 3600) % 60);
    }
}
