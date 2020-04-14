import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SubSelect implements ActionListener {
    UI ui;

    SubSelect(UI ui) {
        this.ui = ui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.setPlaying(false);

        UIManager.put("OptionPane.minimumSize", new Dimension(600, 500));
        UIManager.put("OptionPane.font", new Font("Arial", Font.PLAIN, 25));

        ArrayList<String> subArray = sub2Array(ui.subThread.timeStamp, ui.subThread.sub, ui.subThread.subUnder);

        long startTime = System.currentTimeMillis();
        int before = ui.subThread.subIndex;
        Object ob = (JOptionPane.showInputDialog(ui, String.format("%-20s||    < %s >    < %s > ", "시간", "< 자막(위) >", "< 자막 (아래) >"), "자막 선택하기",
                JOptionPane.QUESTION_MESSAGE, null, subArray.toArray(), subArray.get(before)));

        if (ob != null) {
            int choice = subArray.indexOf(ob.toString());
            if (before == 0) {
                ui.subThread.addPauseTime(ui.subThread.getPlayingTime()-ui.subThread.timeStamp.get(choice) );

            } else if (before != choice) {
                ui.subThread.addPauseTime(ui.subThread.timeStamp.get(before) - ui.subThread.timeStamp.get(choice));
                ui.subThread.arrangeIndex();
            }
        }

        ui.subThread.addPauseTime((System.currentTimeMillis() - startTime));
        ui.setPlaying(true);

    }

    ArrayList<String> sub2Array(ArrayList<Integer> timer, ArrayList over, ArrayList under) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < timer.size(); i++) {
            result.add(String.format("%-20s||    < %s >    < %s >", ui.subThread.labelTimer(timer.get(i)), over.get(i), under.get(i)).replace("< &nbsp; >", " "));
        }
        return result;
    }
}
