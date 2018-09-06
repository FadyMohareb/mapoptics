package FileHandling;

import DataTypes.ContigInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author s276817
 */
public class FastaReader {

    public static LinkedHashMap<String, String> readFasta(String filename, LinkedHashMap<String, String> contigNames) {
        ArrayList<String> sequenceArray = new ArrayList();
        LinkedHashMap<String, String> sequences = new LinkedHashMap();
        String name = "";
        // Try reading file
        try {
            BufferedReader read = new BufferedReader(new FileReader(filename));
            String line;
            String sequence = "";
            // If line starts with > add sequence to array
            while ((line = read.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (sequenceArray.size() > 0) {
                        if (contigNames.containsValue(name)) {
                            sequence = concatenateStrings(sequenceArray);
                            sequences.put(name, sequence);
                        }
                        sequenceArray.clear();
                    }
                    name = line.substring(1);
                } else {
                    sequenceArray.add(line);
                }
            }
            if (sequenceArray.size() > 0) {
                sequence = concatenateStrings(sequenceArray);
                sequences.put(name, sequence);
                sequenceArray.clear();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error loading FASTA file\n\nError: " + ex,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return sequences;
    }

    public static String concatenateStrings(ArrayList<String> items) {
        int expectedSize = 0;
        for (String item : items) {
            expectedSize += item.length();
        }
        StringBuffer result = new StringBuffer(expectedSize);
        for (String item : items) {
            result.append(item);
        }
        return result.toString();
    }

    public static LinkedHashMap<String, String> readKeyFile(String filename, ArrayList<String> contigIds, LinkedHashMap<String, ContigInfo> contigInfo) {
        LinkedHashMap<String, String> contigNames = new LinkedHashMap();

        // Try reading file
        try {
            BufferedReader read = new BufferedReader(new FileReader(filename));
            String line;
            String[] fields;
            String id;
            String name;
            String length;
            while ((line = read.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("CompntId")) {
                    continue;
                }
                fields = line.split("\t");
                id = fields[0];
                name = fields[1];
                length = fields[2];

                if (contigIds.contains(id)) {
                    if (contigInfo.get(id).getContigLen() == Double.parseDouble(length)) {
                        contigNames.put(id, name);
                    }
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error loading KEY file\n\nError: " + ex,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return contigNames;
    }

}
