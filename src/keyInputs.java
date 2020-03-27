import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class keyInputs extends KeyAdapter {

    UI ui;


    keyInputs(UI ui) {
        this.ui = ui;
    }


    void q() {
        ui.timeControl -= 5000;
        ui.arrangeIndex();
    }

    void w() {
        ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() - 10);
    }

    void e() {
        ui.timeControl += 5000;
        ui.arrangeIndex();
    }

    void a() {
        ui.timeControl -= 1000;
        ui.arrangeIndex();
    }

    void s() {
        ui.jFrame.setLocation(ui.jFrame.getX(), ui.jFrame.getY() + 10);
    }

    void d() {
        ui.timeControl += 1000;
        ui.arrangeIndex();
    }

    void r() {
        if (!ui.jPopupMenu.isVisible()) {
            ui.jPopupMenu.show(null, 10, 10);

        } else {
            ui.jPopupMenu.setVisible(false);
        }
    }

    void esc() {
        if (!ui.jPopupMenu.isVisible()) {
            ui.jPopupMenu.show(null, 10, 10);
            System.out.println("esc");

        } else {
            ui.jPopupMenu.setVisible(false);
            System.out.println("esc!!");

        }
    }

    /**
     * q:-5, w:up  e:+5
     * a:-1, s:down    d:+1, ,
     */
    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'q':
                q();
                break;

            case 'w':
                w();
                break;

            case 'e':
                e();
                break;

            case 'a':
                a();
                break;

            case 's':
                s();
                break;

            case 'd':
                d();
                break;

            case 27://esc
                esc();
                break;

            case 'r':
                r();
                break;
        }
    }
}
