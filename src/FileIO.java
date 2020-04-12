import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileIO {


    private ArrayList<String> sub;
    private ArrayList<String> subUnder;
    private ArrayList<Integer> timeStamp;

    //파일을 읽어서 한줄로 만들어줌
    String fileToOneLine(String filePath) throws Exception {

        //file path
        File file = new File(filePath);
        //Charset of subtitle
        String subCharset = new UniversalDetector().detectCharset(file);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), subCharset));

        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            line.replaceAll("(\r\n|\r|\n|\n\r)", " ");
            result += line;
        }
        bufferedReader.close();
        return result;
    }

    void sim2Array(String path_win) throws Exception {
        sub = new ArrayList<>();
        timeStamp = new ArrayList<>();
        subUnder = new ArrayList<>();

        String[] str = fileToOneLine(path_win).split("[sS][yY][Nn][Cc] [sS][tT][aA][rR][tT]");
        for (String s : str) {
            s = s.substring(1, s.length() - 1);
            str = s.split(">");
            str[1] = str[1].toUpperCase();
            if ((str[0].matches("[0-9]*") && (str[1].contains("KRCC")))) {
                if (str.length == 4) {
                    timeStamp.add(Integer.parseInt(str[0].trim()));
                    sub.add(str[str.length - 2].replace("<br", "").replace("<i", ""));
                    subUnder.add(str[str.length - 1]);
                } else {
                    timeStamp.add(Integer.parseInt(str[0].trim()));
                    sub.add(str[str.length - 1]);
                    subUnder.add(" ");
                }

            }
        }

    }


    void srt2Array(String path_win) throws Exception {
        sub = new ArrayList<String>();
        timeStamp = new ArrayList<Integer>();
        //file path
        File file = new File(path_win);
        //Charset of subtitle
        String subCharset = new UniversalDetector().detectCharset(file);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path_win), subCharset));

        String line = "";
        int i = 1;
        while ((line = bufferedReader.readLine()) != null) {
            while (!line.trim().equals(i + "")) {
                bufferedReader.readLine();
            }

        }
        bufferedReader.close();
    }

    public ArrayList<String> getSubUnder() {
        return subUnder;
    }

    public ArrayList<Integer> getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<String> getSub() {
        return sub;
    }


}
