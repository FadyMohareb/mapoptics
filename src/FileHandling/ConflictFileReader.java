<<<<<<< HEAD
package FileHandling;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * @author Josie
 */
public class ConflictFileReader {
    
    private static String[] firstRows = new String[2];

    public static String[] readConflictFile(JTable conflictsTable, String filename) {
        try {
            firstRows = new String[2];
            int count = 0;
            String line;
            String[] fields;
            DefaultTableModel conflictModel = (DefaultTableModel) conflictsTable.getModel();
            conflictModel.setRowCount(0);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    firstRows[0] = line;
                    count++;
                    continue;
                } else if (count == 1) {
                    firstRows[1] = line;
                    count++;
                    continue;
                }

                fields = line.split("\t");
                conflictModel.addRow(new Object[]{
                    fields[0],
                    fields[1],
                    fields[2],
                    fields[3],
                    fields[4],
                    fields[5],
                    fields[6],
                    fields[7],
                    fields[8]
                });

                conflictModel.addRow(new Object[]{
                    fields[0],
                    fields[9],
                    fields[10],
                    fields[11],
                    fields[12],
                    fields[13],
                    fields[14],
                    fields[15],
                    fields[16]
                });
                count++;
            }

        } catch (Exception ex) {
            //Show error message if no file found
            JOptionPane.showMessageDialog(null,
                    "Error loading cut_conflict_status file\n\nError: " + ex,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return firstRows;
    }

    public static String[] getFirstRows() {
        return firstRows;
    }
}
=======
package FileHandling;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * @author Josie
 */
public class ConflictFileReader {
    
    private static String[] firstRows = new String[2];

    public static String[] readConflictFile(JTable conflictsTable, String filename) {
        try {
            firstRows = new String[2];
            int count = 0;
            String line;
            String[] fields;
            DefaultTableModel conflictModel = (DefaultTableModel) conflictsTable.getModel();
            conflictModel.setRowCount(0);
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    firstRows[0] = line;
                    count++;
                    continue;
                } else if (count == 1) {
                    firstRows[1] = line;
                    count++;
                    continue;
                }

                fields = line.split("\t");
                conflictModel.addRow(new Object[]{
                    fields[0],
                    fields[1],
                    fields[2],
                    fields[3],
                    fields[4],
                    fields[5],
                    fields[6],
                    fields[7],
                    fields[8]
                });

                conflictModel.addRow(new Object[]{
                    fields[0],
                    fields[9],
                    fields[10],
                    fields[11],
                    fields[12],
                    fields[13],
                    fields[14],
                    fields[15],
                    fields[16]
                });
                count++;
            }

        } catch (Exception ex) {
            //Show error message if no file found
            JOptionPane.showMessageDialog(null,
                    "Error loading cut_conflict_status file\n\nError: " + ex,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return firstRows;
    }

    public static String[] getFirstRows() {
        return firstRows;
    }
}
>>>>>>> mapOpticsv2/master
