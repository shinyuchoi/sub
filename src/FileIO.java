import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FileIO {

    private ArrayList<String> sub;
    private ArrayList<String> subUnder;
    private ArrayList<Integer> timeStamp;

    /**
     * Make all sub text in one line
     * @param filePath : filePath
     * @return : String s. All subText in one line
     * @throws IOException IO Exception
     */
    String fileToOneLine(String filePath) throws IOException {

        //file path
        File file = new File(filePath);
        //Charset of subtitle
        String subCharset = new UniversalDetector().detectCharset(file);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), subCharset));

        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {

            line.replaceAll("(\r\n|\r|\n|\n\r)", " ");

            //Remove Unnecessary Tags
            if (line.toUpperCase().contains("<FONT"))
                line = line.replace(line.substring(line.toUpperCase().indexOf("<FONT"), line.indexOf('>', line.toUpperCase().indexOf("<FONT")) + 1), "");
            if (line.toUpperCase().contains("</FONT"))
                line = line.replace(line.substring(line.toUpperCase().indexOf("</FONT"), line.indexOf('>', line.toUpperCase().indexOf("</FONT")) + 1), "");
            if (line.toUpperCase().contains("</I"))
                line = line.replace(line.substring(line.toUpperCase().indexOf("</I"), line.indexOf('>', line.toUpperCase().indexOf("</I")) + 1), "");
            if (line.toUpperCase().contains("<I"))
                line = line.replace(line.substring(line.toUpperCase().indexOf("<I"), line.indexOf('>', line.toUpperCase().indexOf("<I")) + 1), "");


            result += line.replace("&nbsp;", " ");
        }
        bufferedReader.close();
        return result;
    }


    /**
     * Convert *.smi to Arrays to be used by SubThread.java
     * @param path_win filePath
     * @throws IOException Exception
     */
    void sim2Array(String path_win) throws IOException {
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
                    sub.add(str[str.length - 2].replace("<br", ""));
                    subUnder.add(str[str.length - 1]);
                } else {
                    timeStamp.add(Integer.parseInt(str[0].trim()));
                    sub.add(str[str.length - 1]);
                    subUnder.add(" ");
                }

            }
        }
    }

    /**
     * Read *.srt and convert it to Arrays to be used by SubThread.java
     * @param path_win filepath
     * @throws IOException IOException
     */
    void srt2Array(String path_win) throws IOException {
        sub = new ArrayList<>();
        subUnder = new ArrayList<>();
        timeStamp = new ArrayList<>();
        //file path
        File file = new File(path_win);
        //Charset of subtitle
        String subCharset = new UniversalDetector().detectCharset(file);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path_win), subCharset));

        String line;
        String[] timeString;


        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("\n")) {

            } else if (line.matches("\uFEFF[0-9]+|[0-9]+")) {
                timeString = bufferedReader.readLine().split("-->");
                timeStamp.add(time2Long(timeString[0].trim()));
                timeStamp.add(time2Long(timeString[1].trim()));

                line = bufferedReader.readLine();
                sub.add(line);

                if ((line = bufferedReader.readLine()) != null) {
                    if (line.equals("\n"))
                        subUnder.add(" ");
                    else {
                        subUnder.add(line);
                    }
                }

                sub.add(" ");
                subUnder.add(" ");
            }
        }

        bufferedReader.close();
    }


    /**
     * Convert String HH:MM:SS,mmm to millisecond (int) for timestamp
     * @param s : String HH:MM:ss,mmm
     * @return millisecond (int)
     */
    int time2Long(String s) {
        int result = Integer.parseInt(s.substring(0, 2).trim());
        result *= 60;

        result += Integer.parseInt(s.substring(3, 5).trim());
        result *= 60;

        result += Integer.parseInt(s.substring(6, 8).trim());

        result *= 1000;
        result += Integer.parseInt(s.substring(9, 12).trim());
        return result;
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
