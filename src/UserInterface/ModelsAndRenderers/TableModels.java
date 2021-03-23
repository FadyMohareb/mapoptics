/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface.ModelsAndRenderers;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Josie
 */
public class TableModels {

    private static final DefaultTableModel LABEL_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            return switch (column) {
                case 1, 2, 3, 4, 5 -> Double.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    };

    public static DefaultTableModel getLabelModel() {
        return LABEL_MODEL;
    }

    private static final DefaultTableModel QRY_VIEW_REF_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            if (column == 2) {
                return Double.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getQryViewRefModel() {
        return QRY_VIEW_REF_MODEL;
    }

    private static final DefaultTableModel QRY_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            return switch (column) {
                case 0, 5, 6 -> Integer.class;
                case 1, 3 -> Double.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getQryModel() {
        return QRY_MODEL;
    }

    // Add SV Model table, setting column classes
    private static final DefaultTableModel SV_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            if (column > 0 && column < 8) {
                return Integer.class;
            } else {
                return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getSVModel() {
        return SV_MODEL;
    }


    private static final DefaultTableModel REF_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            return switch (column) {
                case 0, 2, 4, 5 -> Integer.class;
                case 1, 3 -> Double.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getRefModel() {
        return REF_MODEL;
    }

    private static final DefaultTableModel IMAGE_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            if (column == 1) {
                return Boolean.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }

    };

    public static DefaultTableModel getImageModel() {
        return IMAGE_MODEL;
    }
}
