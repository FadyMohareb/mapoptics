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

    private static DefaultTableModel labelModel = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Double.class;
                case 2:
                    return Double.class;
                case 3:
                    return Double.class;
                case 4:
                    return Double.class;
                case 5:
                    return Double.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    };

    public static DefaultTableModel getLabelModel() {
        return labelModel;
    }

    private static DefaultTableModel qryViewRefModel = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return Double.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getQryViewRefModel() {
        return qryViewRefModel;
    }

    private static DefaultTableModel qryModel = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return String.class;
                case 3:
                    return Double.class;
                case 4:
                    return String.class;
                case 5:
                    return Integer.class;
                case 6:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getQryModel() {
        return qryModel;
    }

    private static DefaultTableModel refModel = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Integer.class;
                case 2:
                    return Integer.class;
                case 3:
                    return Double.class;
                case 4:
                    return Integer.class;
                case 5:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public static DefaultTableModel getRefModel() {
        return refModel;
    }

    private static DefaultTableModel imageModel = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 0:
                    return String.class;
                case 1:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }

    };

    public static DefaultTableModel getImageModel() {
        return imageModel;
    }
}
