import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Build JPanel for Help/Instruction
 */
public class HelpPanel extends JPanel {
    HelpPanel(String version) {

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);
        //add(new JLabel(new ImageIcon(new ImageIcon("Introduction.png").getImage().getScaledInstance((int) (1920 * 0.8), (int) (320 * 0.8), Image.SCALE_DEFAULT))));
        add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getClassLoader().getResource("Introduction.png")).getScaledInstance((int) (1920 * 0.8), (int) (320 * 0.8), Image.SCALE_DEFAULT))));


        JLabel jLabel = new JLabel("<html><h1 align=\"center\">###########사용법###########</h1>" +
                "<h3>1. 글씨 크기와 자막 위치를 조절합니다.<br>플레이 중에도 우클릭으로 메뉴를 열어 조절 가능</h3>" +
                "<h3>2. 열기 버튼을 클릭 후, 자막 파일을 선택합니다<br>" +
                "&nbsp;&nbsp;### Easy Start 체크시###<br>&nbsp;&nbsp;2-1. 시작 하고자 하는 자막을 &lt;이전 버튼, 다음&gt; 버튼으로 선택합니다.<br>&nbsp;&nbsp;2-2.자막이 나오는 시간에 맞춰 GO를 클릭합니다.\n" +
                "<h3>3. 즐겁게 감상합니다.</html>", SwingConstants.CENTER);


        JLabel contact = new JLabel("<html> <h4>버그 및 에러제보 : <a href=\"\">shinyu.choi@tum.de</a><h4></html>", SwingConstants.CENTER);
        contact.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().mail(new URI("mailto:shinyu.choi@tum.de?subject=BugReport"));
                } catch (URISyntaxException | IOException ex) {
                }
            }
        });


        JLabel website = new JLabel("<html> <h4>GitHub : <a href=\"\">https://github.com/shinyuchoi/sub</a></h4></html>", SwingConstants.CENTER);
        website.setCursor(new Cursor(Cursor.HAND_CURSOR));
        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/shinyuchoi/sub"));
                } catch (URISyntaxException | IOException ex) {
                }
            }
        });


        JLabel copyright = new JLabel("<html><h5>" + version + "<br>Copyright (c) 2020, Choi shin-yu <br>All rights reserved.</h5></html", SwingConstants.CENTER);



        add(jLabel);
        add(contact);
        add(website);
        add(copyright);
    }


}
