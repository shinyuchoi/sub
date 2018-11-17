import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class FileBrowserTest
{


    private JFrame jFrame;

    public FileBrowserTest()
    {

    }

    private void create()
    {
        jFrame = new JFrame("frame");
        jFrame.setUndecorated(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(200, 200);
        jFrame.setVisible(true);

    }

    public static void main(String[] args)
    {
        new FileBrowserTest().create();
    }



/*
        JFrame window = new JFrame();

        JFileChooser jfc = new JFileChooser();

        jfc.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Downloads"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".smi", "smi");
        jfc.addChoosableFileFilter(filter);

        int result = jfc.showOpenDialog(window);
        System.out.println(result);

        if (result == jfc.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile);
        }*/


}
