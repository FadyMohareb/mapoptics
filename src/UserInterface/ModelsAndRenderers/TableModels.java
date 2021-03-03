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
            switch (column) {
                case 1:
                case 2:
                case 3:
                case 4:
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
            switch (column) {
                case 1:
                case 5:
                case 6:
                    return Integer.class;
                case 3:
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

    public static DefaultTableModel getQryModel() {
        return QRY_MODEL;
    }

    private static final DefaultTableModel REF_MODEL = new DefaultTableModel() {
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
                case 1:
                case 2:
                case 4:
                case 5:
                    return Integer.class;
                case 3:
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
