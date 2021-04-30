package FileHandling;

import javax.swing.*;
import java.io.PrintWriter;

/*
 * @author Josie
 */
public class ConflictFileWriter {

    static public void writeConflictFile(String filename, JTable conflictsTable, String[] firstRows) {
        String line = "";
        try {
            PrintWriter writer = new PrintWriter(filename + ".txt", "UTF-8");
            writer.println(firstRows[0]);
            writer.println(firstRows[1]);
            for (int i = 0; i < conflictsTable.getRowCount(); i++) {
                for (int j = 0; j < 9; j++) {
                    if (j == 0 && (i & 1) == 0) {
                        line += conflictsTable.getValueAt(i, j).toString();
                    } else if (j != 0) {
                        line += "\t" + conflictsTable.getValueAt(i, j).toString();
                    }  
                }
                if ((i & 1) != 0) {
                writer.println(line);
                line = "";
                }
            }
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
