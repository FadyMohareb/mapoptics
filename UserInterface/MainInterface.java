package UserInterface;

import UserInterface.ModelsAndRenderers.*;
import FileHandling.*;
import Algorithms.*;
import Datasets.UserEdited.*;
import Datasets.Default.*;
import DataTypes.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleAnchor;

/*
 * @author Josie
 */
public class MainInterface extends JFrame {

    public MainInterface() {
        System.setProperty("sun.java2d.opengl", "true");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();

        setRefContigTable();
        setQryContigTable();
        setLabelTable();
        setQryViewRefTable();
        setImageTable();
        setConflictsTable();

        ButtonGroup buttongroup = new ButtonGroup();
        buttongroup.add(styleMatch);
        buttongroup.add(styleCoverage);
        buttongroup.add(styleChim);

        fileLoader.setVisible(false);
        fileLoader.pack();

        fastaLoader.setVisible(false);
        fastaLoader.pack();

        confidenceSettings.setVisible(false);
        confidenceSettings.pack();

        coverageSettings.setVisible(false);
        coverageSettings.pack();

        chimSettings.setVisible(false);
        chimSettings.pack();

        conflictsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // when conflicts table is clicked, set ref and qry
                if ((conflictsTable.getSelectedRow() & 1) == 0) {
                    conflictsTable.setRowSelectionInterval(conflictsTable.getSelectedRow(), conflictsTable.getSelectedRow() + 1);
                } else {
                    conflictsTable.setRowSelectionInterval(conflictsTable.getSelectedRow(), conflictsTable.getSelectedRow() - 1);
                }

                // get selected ref and qry
                String chosenRef = conflictsTable.getValueAt(conflictsTable.getSelectedRow(), 2).toString();
                String chosenQry = conflictsTable.getValueAt(conflictsTable.getSelectedRow() + 1, 2).toString();
                changeRef(chosenRef);
                changeQry(chosenQry);
                repaint();
            }
        });

        selectAllImages.setText("Select all images  ");
        selectAllImages.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // select all images
                if (selectAllImages.isSelected()) {
                    for (int i = 0; i < imageTable.getRowCount(); i++) {
                        imageTable.setValueAt(true, i, 1);
                    }
                }
                if (!selectAllImages.isSelected()) {
                    for (int i = 0; i < imageTable.getRowCount(); i++) {
                        imageTable.setValueAt(false, i, 1);
                    }
                }
            }
        });

        imageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // set chosen ref
                String chosenRef = imageTable.getValueAt(imageTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });

        repaint();
    }

    private JTable conflictsTable = new JTable();
    private JTable imageTable = new JTable();
    private JCheckBox selectAllImages = new JCheckBox();

    private String xmapFilename = "";
    private String refCmapFilename = "";
    private String qryCmapFilename = "";
    private String refCmapDataset = "";
    private String qryCmapDataset = "";

    // set variables for panel widths and heights so when resized, drawing can be resized relatively
    private double refViewHeight = 0.0;
    private double refViewWidth = 0.0;
    private double sumViewHeight = 0.0;
    private double sumViewWidth = 0.0;
    private double qryViewHeight = 0.0;
    private double qryViewWidth = 0.0;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileLoader = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        xmapFile = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        browseXmap = new javax.swing.JButton();
        browseRef = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        qryFile = new javax.swing.JTextField();
        browseQry = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        runAnalysis = new javax.swing.JButton();
        refFile = new javax.swing.JTextField();
        confidenceSettings = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lowConf = new javax.swing.JSpinner();
        highConf = new javax.swing.JSpinner();
        saveConfThresholds = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        coverageSettings = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        lowCov = new javax.swing.JSpinner();
        highCov = new javax.swing.JSpinner();
        saveCovThresholds = new javax.swing.JButton();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        chimSettings = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        lowQual = new javax.swing.JSpinner();
        highQual = new javax.swing.JSpinner();
        saveQualitySettings = new javax.swing.JButton();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        fastaLoader = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        keyFile = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        browseKey = new javax.swing.JButton();
        browseFasta = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        fastaFile = new javax.swing.JTextField();
        loadFastaFile = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        refOrQry = new javax.swing.JComboBox<>();
        tabPane = new javax.swing.JTabbedPane();
        summaryPane = new javax.swing.JLayeredPane();
        refContigTableScroll = new javax.swing.JScrollPane();
        refContigTable = new javax.swing.JTable();
        summaryView = new UserInterface.SummaryView();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        refDataset = new javax.swing.JTextField();
        qryDataset = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        referencesGraph = new javax.swing.JPanel();
        labelDensityGraph = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        refViewPane = new javax.swing.JLayeredPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        referenceView = new UserInterface.ReferenceView();
        exportRefButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        reCentre = new javax.swing.JButton();
        zoomIn = new javax.swing.JButton();
        zoomOut = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        styleMatch = new javax.swing.JRadioButton();
        styleCoverage = new javax.swing.JRadioButton();
        styleChim = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        confidenceSetting = new javax.swing.JCheckBox();
        overlapSetting = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        reOrientate = new javax.swing.JButton();
        deleteContig = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        alignLeft = new javax.swing.JButton();
        alignRight = new javax.swing.JButton();
        save = new javax.swing.JButton();
        tabPaneFiles = new javax.swing.JTabbedPane();
        refViewTableScroll = new javax.swing.JScrollPane();
        qryContigTable = new javax.swing.JTable();
        queryViewPane = new javax.swing.JLayeredPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        queryViewTableScroll = new javax.swing.JScrollPane();
        labelTable = new javax.swing.JTable();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        queryView = new UserInterface.QueryView();
        exportQryButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        qryIdSearch = new javax.swing.JTextField();
        refIdSearch = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        search = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        regionSearch = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        regionType = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        qryViewRefTable = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loadMaps = new javax.swing.JMenuItem();
        fastaLoad = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        manualConflict = new javax.swing.JMenuItem();
        saveConflictFile = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        chooseImages = new javax.swing.JMenuItem();
        exportImages = new javax.swing.JMenuItem();
        close = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        swapContigs = new javax.swing.JMenuItem();
        orientateContigs = new javax.swing.JMenuItem();
        saveAllContigs = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        confidenceSet = new javax.swing.JMenuItem();
        coverageSet = new javax.swing.JMenuItem();
        chimqualSet = new javax.swing.JMenuItem();

        fileLoader.setTitle("Load Maps");
        fileLoader.setLocation(new java.awt.Point(100, 100));
        fileLoader.setName("Load Files"); // NOI18N
        fileLoader.setPreferredSize(new java.awt.Dimension(280, 300));

        jLabel2.setText("XMAP:");

        browseXmap.setText("Browse...");
        browseXmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseXmapActionPerformed(evt);
            }
        });

        browseRef.setText("Browse...");
        browseRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseRefActionPerformed(evt);
            }
        });

        jLabel6.setText("Reference CMAP:");

        browseQry.setText("Browse...");
        browseQry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseQryActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel8.setText("Load XMAP file and corresponding CMAP files");

        jLabel7.setText("Query CMAP:");

        runAnalysis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        runAnalysis.setText("Run");
        runAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runAnalysisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xmapFile, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseXmap))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(refFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseRef))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(qryFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseQry))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(24, 24, 24))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(runAnalysis)
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xmapFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseXmap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseRef)
                    .addComponent(refFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseQry)
                    .addComponent(qryFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(runAnalysis)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout fileLoaderLayout = new javax.swing.GroupLayout(fileLoader.getContentPane());
        fileLoader.getContentPane().setLayout(fileLoaderLayout);
        fileLoaderLayout.setHorizontalGroup(
            fileLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fileLoaderLayout.setVerticalGroup(
            fileLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        fileLoader.getAccessibleContext().setAccessibleName("");
        fileLoader.getAccessibleContext().setAccessibleDescription("");

        confidenceSettings.setTitle("Confidence Threshold Settings");
        confidenceSettings.setLocation(new java.awt.Point(100, 100));

        jLabel17.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel17.setText("Change thresholds of the confidence alignment view:");

        jLabel18.setText("Low Confidence");

        jLabel19.setText("Medium Confidence");

        jLabel20.setText("High Confidence");

        jLabel21.setText("<");

        jLabel22.setText("<");

        lowConf.setPreferredSize(new java.awt.Dimension(60, 25));
        lowConf.setValue(20);
        lowConf.setVerifyInputWhenFocusTarget(false);

        highConf.setPreferredSize(new java.awt.Dimension(60, 25));
        highConf.setValue(40);

        saveConfThresholds.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saveConfThresholds.setText("Save Changes");
        saveConfThresholds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConfThresholdsActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel23.setText("Default thresholds are set to :");

        jLabel24.setText("Low Confidence  < 20");

        jLabel25.setText("20 <= Medium Confidence <= 40 ");

        jLabel26.setText(" High Confidence > 40");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel21)
                                        .addGap(9, 9, 9)
                                        .addComponent(jLabel19)
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel20))
                                    .addComponent(jLabel23)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(71, 71, 71)
                                        .addComponent(lowConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(63, 63, 63)
                                        .addComponent(highConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 47, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(saveConfThresholds)
                .addGap(43, 43, 43))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lowConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(highConf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveConfThresholds)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout confidenceSettingsLayout = new javax.swing.GroupLayout(confidenceSettings.getContentPane());
        confidenceSettings.getContentPane().setLayout(confidenceSettingsLayout);
        confidenceSettingsLayout.setHorizontalGroup(
            confidenceSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        confidenceSettingsLayout.setVerticalGroup(
            confidenceSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confidenceSettingsLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        coverageSettings.setTitle("Coverage Threshold Settings");
        coverageSettings.setLocation(new java.awt.Point(100, 100));

        jLabel47.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel47.setText("Change thresholds of the confidence alignment view:");

        jLabel48.setText("Low Coverage");

        jLabel49.setText("Medium Coverage");

        jLabel50.setText("High Coverage");

        jLabel51.setText("<");

        jLabel52.setText("<");

        lowCov.setPreferredSize(new java.awt.Dimension(60, 25));
        lowCov.setValue(20);
        lowCov.setVerifyInputWhenFocusTarget(false);

        highCov.setPreferredSize(new java.awt.Dimension(60, 25));
        highCov.setValue(50);

        saveCovThresholds.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saveCovThresholds.setText("Save Changes");
        saveCovThresholds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCovThresholdsActionPerformed(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel53.setText("Default thresholds are set to :");

        jLabel54.setText("Low Coverage  < 20");

        jLabel55.setText("20 <= Medium Coverage <= 50 ");

        jLabel56.setText(" High Coverage > 50");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(saveCovThresholds)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel53)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addGap(71, 71, 71)
                                            .addComponent(lowCov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(63, 63, 63)
                                            .addComponent(highCov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addComponent(jLabel48)
                                            .addGap(26, 26, 26)
                                            .addComponent(jLabel51)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel49)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel52)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel50))))
                                .addGap(0, 123, Short.MAX_VALUE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55)
                            .addComponent(jLabel56))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50)
                    .addComponent(jLabel51)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lowCov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(highCov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveCovThresholds)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout coverageSettingsLayout = new javax.swing.GroupLayout(coverageSettings.getContentPane());
        coverageSettings.getContentPane().setLayout(coverageSettingsLayout);
        coverageSettingsLayout.setHorizontalGroup(
            coverageSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        coverageSettingsLayout.setVerticalGroup(
            coverageSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coverageSettingsLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        chimSettings.setTitle("Chimeric Quality Threshold Settings");
        chimSettings.setLocation(new java.awt.Point(100, 100));

        jLabel57.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel57.setText("Change thresholds of the label chimeric quality view:");

        jLabel58.setText("Low Quality");

        jLabel59.setText("Medium Quality");

        jLabel60.setText("High Quality");

        jLabel61.setText("<");

        jLabel62.setText("<");

        lowQual.setPreferredSize(new java.awt.Dimension(60, 25));
        lowQual.setValue(20);
        lowQual.setVerifyInputWhenFocusTarget(false);

        highQual.setPreferredSize(new java.awt.Dimension(60, 25));
        highQual.setValue(90);

        saveQualitySettings.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saveQualitySettings.setText("Save Changes");
        saveQualitySettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveQualitySettingsActionPerformed(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel63.setText("Default thresholds are set to :");

        jLabel64.setText("Low Quality  < 20");

        jLabel65.setText("20 <= Medium Quality <= 90 ");

        jLabel66.setText(" High Quality > 90");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel63)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addGap(71, 71, 71)
                                        .addComponent(lowQual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(63, 63, 63)
                                        .addComponent(highQual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(jLabel61)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel59)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel62)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel60)
                                .addGap(17, 17, 17))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65)
                            .addComponent(jLabel66))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveQualitySettings)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60)
                    .addComponent(jLabel61)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lowQual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(highQual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel64)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel65)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveQualitySettings)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout chimSettingsLayout = new javax.swing.GroupLayout(chimSettings.getContentPane());
        chimSettings.getContentPane().setLayout(chimSettingsLayout);
        chimSettingsLayout.setHorizontalGroup(
            chimSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chimSettingsLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 73, Short.MAX_VALUE))
        );
        chimSettingsLayout.setVerticalGroup(
            chimSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chimSettingsLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 59, Short.MAX_VALUE))
        );

        fastaLoader.setTitle("Load Fasta and Key");
        fastaLoader.setLocation(new java.awt.Point(100, 100));
        fastaLoader.setName("Load Files"); // NOI18N

        jLabel29.setText("KEY file:");

        browseKey.setText("Browse...");
        browseKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseKeyActionPerformed(evt);
            }
        });

        browseFasta.setText("Browse...");
        browseFasta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseFastaActionPerformed(evt);
            }
        });

        jLabel30.setText("FASTA file:");

        jLabel31.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel31.setText("Load FASTA file and corresponding KEY file");

        loadFastaFile.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        loadFastaFile.setText("Load");
        loadFastaFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFastaFileActionPerformed(evt);
            }
        });

        jLabel32.setText("Which contig is the fasta relative to?");

        refOrQry.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reference", "Query" }));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(loadFastaFile))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(keyFile, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseKey))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel31)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(fastaFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseFasta))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel30)
                                    .addComponent(refOrQry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(24, 24, 24))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keyFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseKey))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseFasta)
                    .addComponent(fastaFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(refOrQry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(loadFastaFile)
                .addContainerGap())
        );

        javax.swing.GroupLayout fastaLoaderLayout = new javax.swing.GroupLayout(fastaLoader.getContentPane());
        fastaLoader.getContentPane().setLayout(fastaLoaderLayout);
        fastaLoaderLayout.setHorizontalGroup(
            fastaLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        fastaLoaderLayout.setVerticalGroup(
            fastaLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MapOptics");

        refContigTable.setAutoCreateRowSorter(true);
        refContigTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        refContigTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        refContigTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        refContigTable.setShowVerticalLines(false);
        refContigTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refContigTableMouseClicked(evt);
            }
        });
        refContigTableScroll.setViewportView(refContigTable);

        summaryView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        summaryView.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                summaryViewComponentResized(evt);
            }
        });

        javax.swing.GroupLayout summaryViewLayout = new javax.swing.GroupLayout(summaryView);
        summaryView.setLayout(summaryViewLayout);
        summaryViewLayout.setHorizontalGroup(
            summaryViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        summaryViewLayout.setVerticalGroup(
            summaryViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 205, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Reference Dataset:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Query Dataset:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refDataset, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(qryDataset))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(refDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(qryDataset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        referencesGraph.setBackground(new java.awt.Color(255, 255, 255));
        referencesGraph.setLayout(new javax.swing.BoxLayout(referencesGraph, javax.swing.BoxLayout.LINE_AXIS));

        labelDensityGraph.setBackground(new java.awt.Color(255, 255, 255));
        labelDensityGraph.setLayout(new javax.swing.BoxLayout(labelDensityGraph, javax.swing.BoxLayout.LINE_AXIS));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Reference Graphs:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labelDensityGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(referencesGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(referencesGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelDensityGraph, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        summaryPane.setLayer(refContigTableScroll, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(summaryView, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        summaryPane.setLayer(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout summaryPaneLayout = new javax.swing.GroupLayout(summaryPane);
        summaryPane.setLayout(summaryPaneLayout);
        summaryPaneLayout.setHorizontalGroup(
            summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryPaneLayout.createSequentialGroup()
                .addComponent(refContigTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(summaryView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        summaryPaneLayout.setVerticalGroup(
            summaryPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(summaryPaneLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(refContigTableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
        );

        tabPane.addTab("Summary View", summaryPane);

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        referenceView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        referenceView.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                referenceViewMouseMoved(evt);
            }
        });
        referenceView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                referenceViewMouseClicked(evt);
            }
        });
        referenceView.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                referenceViewComponentResized(evt);
            }
        });

        exportRefButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        exportRefButton.setText("Export Image");
        exportRefButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportRefButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout referenceViewLayout = new javax.swing.GroupLayout(referenceView);
        referenceView.setLayout(referenceViewLayout);
        referenceViewLayout.setHorizontalGroup(
            referenceViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, referenceViewLayout.createSequentialGroup()
                .addContainerGap(618, Short.MAX_VALUE)
                .addComponent(exportRefButton)
                .addContainerGap())
        );
        referenceViewLayout.setVerticalGroup(
            referenceViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(referenceViewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exportRefButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Display tools:");

        reCentre.setText("reCentre");
        reCentre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reCentreActionPerformed(evt);
            }
        });

        zoomIn.setText("+");
        zoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInActionPerformed(evt);
            }
        });

        zoomOut.setText("-");
        zoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel1.setText("Label style:");

        styleMatch.setSelected(true);
        styleMatch.setText("Matches");
        styleMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                styleMatchActionPerformed(evt);
            }
        });

        styleCoverage.setText("Coverage");
        styleCoverage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                styleCoverageActionPerformed(evt);
            }
        });

        styleChim.setText("Chim Quality");
        styleChim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                styleChimActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel5.setText("View:");

        confidenceSetting.setText("Confidence");
        confidenceSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confidenceSettingActionPerformed(evt);
            }
        });

        overlapSetting.setText("Overlap");
        overlapSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overlapSettingActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Contig tools:");

        reOrientate.setText("reOrientate");
        reOrientate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reOrientateActionPerformed(evt);
            }
        });

        deleteContig.setText("delete");
        deleteContig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteContigActionPerformed(evt);
            }
        });

        resetButton.setText("RESET");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        alignLeft.setText("<");
        alignLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignLeftActionPerformed(evt);
            }
        });

        alignRight.setText(">");
        alignRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alignRightActionPerformed(evt);
            }
        });

        save.setText("SAVE");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reCentre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(alignLeft)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(alignRight))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(zoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(zoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1)
                            .addComponent(styleMatch)
                            .addComponent(styleCoverage)
                            .addComponent(styleChim)
                            .addComponent(jLabel5)
                            .addComponent(confidenceSetting)
                            .addComponent(overlapSetting)
                            .addComponent(jLabel3)
                            .addComponent(reOrientate, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(deleteContig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reCentre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zoomIn)
                    .addComponent(zoomOut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(styleMatch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(styleCoverage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(styleChim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confidenceSetting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overlapSetting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reOrientate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteContig)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alignRight)
                    .addComponent(alignLeft))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(save)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLayeredPane2.setLayer(referenceView, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane2.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addComponent(referenceView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(referenceView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane2.setLeftComponent(jLayeredPane2);

        qryContigTable.setAutoCreateRowSorter(true);
        qryContigTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        qryContigTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        qryContigTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qryContigTableMouseClicked(evt);
            }
        });
        refViewTableScroll.setViewportView(qryContigTable);

        tabPaneFiles.addTab("Query Contigs", refViewTableScroll);

        jSplitPane2.setRightComponent(tabPaneFiles);

        refViewPane.setLayer(jSplitPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout refViewPaneLayout = new javax.swing.GroupLayout(refViewPane);
        refViewPane.setLayout(refViewPaneLayout);
        refViewPaneLayout.setHorizontalGroup(
            refViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2)
        );
        refViewPaneLayout.setVerticalGroup(
            refViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
        );

        tabPane.addTab("Reference View", refViewPane);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        labelTable.setAutoCreateRowSorter(true);
        labelTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        labelTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        labelTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelTableMouseClicked(evt);
            }
        });
        queryViewTableScroll.setViewportView(labelTable);

        jSplitPane1.setBottomComponent(queryViewTableScroll);

        queryView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        queryView.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                queryViewMouseMoved(evt);
            }
        });
        queryView.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                queryViewComponentResized(evt);
            }
        });

        exportQryButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        exportQryButton.setText("Export Image");
        exportQryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportQryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout queryViewLayout = new javax.swing.GroupLayout(queryView);
        queryView.setLayout(queryViewLayout);
        queryViewLayout.setHorizontalGroup(
            queryViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queryViewLayout.createSequentialGroup()
                .addContainerGap(442, Short.MAX_VALUE)
                .addComponent(exportQryButton)
                .addContainerGap())
        );
        queryViewLayout.setVerticalGroup(
            queryViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queryViewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exportQryButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Search");

        jLabel10.setText("Reference ID :");

        jLabel11.setText("Query ID :");

        search.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        jLabel12.setText("Region :");

        jLabel13.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel13.setText("Format region search as start-end in bp e.g. 20-200");

        regionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Reference", "Query" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(search))
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(regionSearch, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(regionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(refIdSearch)
                            .addComponent(qryIdSearch))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(refIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(qryIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))))
                        .addGap(38, 38, 38))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(regionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(regionSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(search)
                .addContainerGap())
        );

        qryViewRefTable.setAutoCreateRowSorter(true);
        qryViewRefTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        qryViewRefTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        qryViewRefTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qryViewRefTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(qryViewRefTable);

        jLayeredPane1.setLayer(queryView, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(queryView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
            .addComponent(queryView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jLayeredPane1);

        queryViewPane.setLayer(jSplitPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout queryViewPaneLayout = new javax.swing.GroupLayout(queryViewPane);
        queryViewPane.setLayout(queryViewPaneLayout);
        queryViewPaneLayout.setHorizontalGroup(
            queryViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        queryViewPaneLayout.setVerticalGroup(
            queryViewPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
        );

        tabPane.addTab("Query View", queryViewPane);

        jMenu1.setText("File");

        loadMaps.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadMaps.setText("Load Maps");
        loadMaps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMapsActionPerformed(evt);
            }
        });
        jMenu1.add(loadMaps);

        fastaLoad.setText("Load FASTA File");
        fastaLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastaLoadActionPerformed(evt);
            }
        });
        jMenu1.add(fastaLoad);

        jMenu5.setText("Manual Conflict Resolution");

        manualConflict.setText("Load conflicts_cut_status file");
        manualConflict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualConflictActionPerformed(evt);
            }
        });
        jMenu5.add(manualConflict);

        saveConflictFile.setText("Save manual conflict resolution file");
        saveConflictFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveConflictFileActionPerformed(evt);
            }
        });
        jMenu5.add(saveConflictFile);

        jMenu1.add(jMenu5);

        jMenu2.setText("Export images");

        chooseImages.setText("Choose images to export");
        chooseImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseImagesActionPerformed(evt);
            }
        });
        jMenu2.add(chooseImages);

        exportImages.setText("Export chosen images");
        exportImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImagesActionPerformed(evt);
            }
        });
        jMenu2.add(exportImages);

        jMenu1.add(jMenu2);

        close.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        close.setText("Close");
        close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeActionPerformed(evt);
            }
        });
        jMenu1.add(close);

        menuBar.add(jMenu1);

        jMenu3.setText("Quick-tools");

        swapContigs.setText("Swap reference and query");
        swapContigs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                swapContigsActionPerformed(evt);
            }
        });
        jMenu3.add(swapContigs);

        orientateContigs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        orientateContigs.setText("Orientate all contigs");
        orientateContigs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orientateContigsActionPerformed(evt);
            }
        });
        jMenu3.add(orientateContigs);

        saveAllContigs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveAllContigs.setText("Save view for all contigs");
        saveAllContigs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllContigsActionPerformed(evt);
            }
        });
        jMenu3.add(saveAllContigs);

        menuBar.add(jMenu3);

        jMenu4.setText("Settings");

        confidenceSet.setText("Confidence thresholds");
        confidenceSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confidenceSetActionPerformed(evt);
            }
        });
        jMenu4.add(confidenceSet);

        coverageSet.setText("Coverage thresholds");
        coverageSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageSetActionPerformed(evt);
            }
        });
        jMenu4.add(coverageSet);

        chimqualSet.setText("Chimeric quality thresholds");
        chimqualSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chimqualSetActionPerformed(evt);
            }
        });
        jMenu4.add(chimqualSet);

        menuBar.add(jMenu4);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refContigTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refContigTableMouseClicked
        // get selected contig from reference table
        if (refContigTable.getRowCount() != 0) {
            String chosenRef = refContigTable.getValueAt(refContigTable.getSelectedRow(), 0).toString();
            changeRef(chosenRef);
        }
    }//GEN-LAST:event_refContigTableMouseClicked

    private void qryContigTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qryContigTableMouseClicked
        // get selected contig from query table
        if (qryContigTable.getRowCount() != 0) {
            String chosenQry = qryContigTable.getValueAt(qryContigTable.getSelectedRow(), 0).toString();
            changeQry(chosenQry);
        }
    }//GEN-LAST:event_qryContigTableMouseClicked

    private void summaryViewComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_summaryViewComponentResized
        // when panel resized, resize image
        double newHeight = summaryView.getHeight();
        double newWidth = summaryView.getWidth();
        double heightChange = newHeight / sumViewHeight;
        double widthChange = newWidth / sumViewWidth;
        summaryView.zoomPanel(widthChange, heightChange);
        sumViewHeight = newHeight;
        sumViewWidth = newWidth;
        summaryView.repaint();
    }//GEN-LAST:event_summaryViewComponentResized

    private void queryViewComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_queryViewComponentResized
        // when panel resized resize image
        double newHeight = queryView.getHeight();
        double newWidth = queryView.getWidth();
        double heightChange = newHeight / qryViewHeight;
        double widthChange = newWidth / qryViewWidth;
        queryView.zoomPanel(widthChange, heightChange);
        qryViewHeight = newHeight;
        qryViewWidth = newWidth;
        queryView.repaint();
    }//GEN-LAST:event_queryViewComponentResized

    private void orientateContigsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orientateContigsActionPerformed
        // orientates all query contigs that are negatively oriented
        UserRefData.setQueries(SortOrientation.sortAllOrientation(UserRefData.getReferences(), UserRefData.getQueries()));
        UserRefData.setPanelLength(refViewWidth);
        UserRefData.setPanelHeight(refViewHeight);
        for (String refId : UserRefData.getReferences().keySet()) {
            UserRefData.reCentreView(refId);
        }
        referenceView.repaint();
    }//GEN-LAST:event_orientateContigsActionPerformed

    private void browseXmapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseXmapActionPerformed
        // browse for xmap file
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Open XMAP File", FileDialog.LOAD);
        fileBox.setVisible(true);
        if (fileBox.getFile() != null) {
            String fileDirectory = fileBox.getDirectory();
            String filename = fileDirectory.concat(fileBox.getFile());

            // set text field to display name
            xmapFile.setText(filename);
        }
    }//GEN-LAST:event_browseXmapActionPerformed

    private void browseRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseRefActionPerformed
        // browse for ref cmap file
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Open Reference CMAP File", FileDialog.LOAD);
        fileBox.setVisible(true);
        if (fileBox.getFile() != null) {
            String fileDirectory = fileBox.getDirectory();
            String filename = fileDirectory.concat(fileBox.getFile());

            // set text field to display name
            refFile.setText(filename);
        }
    }//GEN-LAST:event_browseRefActionPerformed

    private void browseQryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseQryActionPerformed
        // browse for qry cmap file
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Open Query CMAP File", FileDialog.LOAD);
        fileBox.setVisible(true);
        if (fileBox.getFile() != null) {
            String fileDirectory = fileBox.getDirectory();
            String filename = fileDirectory.concat(fileBox.getFile());

            // set text field to display name
            qryFile.setText(filename);
        }
    }//GEN-LAST:event_browseQryActionPerformed

    private void runAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runAnalysisActionPerformed
        fileLoader.setVisible(false);
        // reset all data
        resetData();
        XmapReader.setSwap(false);

        if (!qryFile.getText().equals("") && !xmapFile.getText().equals("") && !refFile.getText().equals("")) {
            xmapFilename = xmapFile.getText();
            refCmapFilename = refFile.getText();
            qryCmapFilename = qryFile.getText();

            refCmapDataset = refFile.getText().substring(refFile.getText().lastIndexOf("\\") + 1);
            qryCmapDataset = qryFile.getText().substring(refFile.getText().lastIndexOf("\\") + 1);

            // extract data from files
            RawFileData.setAlignmentInfo(XmapReader.xmapToHashMap(xmapFilename));
            RawFileData.setRefContigs(CmapReader.cmapToHashMap(refCmapFilename));
            RawFileData.setQryContigs(CmapReader.cmapToHashMap(qryCmapFilename));

            refDataset.setText(refCmapDataset);
            qryDataset.setText(qryCmapDataset);
            ReferenceView.setRefDataset(refCmapDataset);
            ReferenceView.setQryDataset(qryCmapDataset);

            setAllData();

        } else {
            JOptionPane.showMessageDialog(null, "Not all files have been declared", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_runAnalysisActionPerformed

    private void loadMapsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMapsActionPerformed
        // displays menu
        fileLoader.setVisible(true);
    }//GEN-LAST:event_loadMapsActionPerformed

    private void swapContigsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swapContigsActionPerformed
        // swap the query and the reference around
        if (!qryCmapFilename.equals("") && !xmapFilename.equals("") && !refCmapFilename.equals("")) {
            int swap = JOptionPane.showConfirmDialog(null, "Are you sure you would like to swap the query and the reference dataset? The data will reset to default", "Swap Contigs", JOptionPane.YES_NO_OPTION);
            if (swap == JOptionPane.YES_OPTION) {
                // reset all data
                resetData();

                // reread the files but swapping the variables
                if (XmapReader.isSwap()) {
                    XmapReader.setSwap(false);
                    // extract data from files
                    RawFileData.setAlignmentInfo(XmapReader.xmapToHashMap(xmapFilename));
                    RawFileData.setRefContigs(CmapReader.cmapToHashMap(refCmapFilename));
                    RawFileData.setQryContigs(CmapReader.cmapToHashMap(qryCmapFilename));
                    String refData = refDataset.getText();
                    refDataset.setText(qryDataset.getText());
                    qryDataset.setText(refData);
                    ReferenceView.setRefDataset(qryDataset.getText());
                    ReferenceView.setQryDataset(refData);
                } else {
                    XmapReader.setSwap(true);
                    // extract data from files
                    RawFileData.setAlignmentInfo(XmapReader.xmapToHashMap(xmapFilename));
                    RawFileData.setRefContigs(CmapReader.cmapToHashMap(qryCmapFilename));
                    RawFileData.setQryContigs(CmapReader.cmapToHashMap(refCmapFilename));
                    String refData = refDataset.getText();
                    refDataset.setText(qryDataset.getText());
                    qryDataset.setText(refData);
                    ReferenceView.setRefDataset(qryDataset.getText());
                    ReferenceView.setQryDataset(refData);
                }
                setAllData();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Not all files have been declared - load files first", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_swapContigsActionPerformed

    private void qryViewRefTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qryViewRefTableMouseClicked
        // get selected contig
        if (qryViewRefTable.getRowCount() != 0) {
            String chosenRef = qryViewRefTable.getValueAt(qryViewRefTable.getSelectedRow(), 0).toString();
            changeRef(chosenRef);
            repaint();
        }
    }//GEN-LAST:event_qryViewRefTableMouseClicked

    private void labelTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelTableMouseClicked
        // set label and draw on diagram
        if (labelTable.getRowCount() != 0) {
            String chosenLabel = labelTable.getValueAt(labelTable.getSelectedRow(), 0).toString();
            QueryView.setChosenLabel(chosenLabel);
            repaint();
        }
    }//GEN-LAST:event_labelTableMouseClicked

    private void referenceViewComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_referenceViewComponentResized
        // when panel resized resize image
        double newHeight = referenceView.getHeight();
        double newWidth = referenceView.getWidth();
        double heightChange = newHeight / refViewHeight;
        double widthChange = newWidth / refViewWidth;
        referenceView.zoomPanel(widthChange, heightChange);
        refViewHeight = newHeight;
        refViewWidth = newWidth;
        repaint();
    }//GEN-LAST:event_referenceViewComponentResized

    private void referenceViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referenceViewMouseClicked
        // Set clicked contig
        Rectangle2D qry;
        boolean qryMatch = false;
        String chosenRef = ReferenceView.getChosenRef();
        if (!chosenRef.equals("")) {
            for (String qryId : UserRefData.getReferences(chosenRef).getConnections()) {
                qry = UserRefData.getQueries(chosenRef + "-" + qryId).getRectangle();
                if (qry.contains(evt.getPoint())) {
                    qryMatch = true;
                    changeQry(qryId);
                }
            }
            if (!qryMatch) {
                changeQry("");
            }
            repaint();
        }
    }//GEN-LAST:event_referenceViewMouseClicked

    private void zoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInActionPerformed
        // zoom in reference view
        if (!ReferenceView.getChosenRef().equals("")) {
            ReferenceView.zoom(1.2, referenceView.getWidth());
            referenceView.repaint();
        }
    }//GEN-LAST:event_zoomInActionPerformed

    private void zoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutActionPerformed
        // zoom out reference view
        if (!ReferenceView.getChosenRef().equals("")) {
            ReferenceView.zoom(0.8, referenceView.getWidth());
            referenceView.repaint();
        }
    }//GEN-LAST:event_zoomOutActionPerformed

    private void styleMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_styleMatchActionPerformed
        // when button clicked set style in reference view
        if (styleMatch.isSelected()) {
            ReferenceView.setStyle("match");
            QueryView.setStyle("match");
            repaint();
        }
    }//GEN-LAST:event_styleMatchActionPerformed

    private void styleCoverageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_styleCoverageActionPerformed
        // when button clicked set style in reference view
        if (styleCoverage.isSelected()) {
            ReferenceView.setStyle("coverage");
            QueryView.setStyle("coverage");
            repaint();
        }
    }//GEN-LAST:event_styleCoverageActionPerformed

    private void styleChimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_styleChimActionPerformed
        // set style to chimeric quality values
        if (styleChim.isSelected()) {
            ReferenceView.setStyle("chimQual");
            QueryView.setStyle("chimQual");
            repaint();
        }
    }//GEN-LAST:event_styleChimActionPerformed

    private void confidenceSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confidenceSettingActionPerformed
        // when check box ticked, colour query contigs by confidence score
        boolean checked = confidenceSetting.isSelected();
        QueryView.setConfidenceView(checked);
        ReferenceView.setConfidenceView(checked);
        repaint();
    }//GEN-LAST:event_confidenceSettingActionPerformed

    private void reOrientateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reOrientateActionPerformed
        // reorientate chosen contig
        if (!ReferenceView.getChosenRef().equals("") && !ReferenceView.getChosenQry().equals("")) {
            String refqryId = ReferenceView.getChosenRef() + "-" + ReferenceView.getChosenQry();
            QryContig sortedContig = SortOrientation.sortOneOrientation(UserRefData.getQueries(refqryId), UserRefData.getReferences(ReferenceView.getChosenRef()));
            UserRefData.getQueries().put(refqryId, sortedContig);
            repaint();
        }
    }//GEN-LAST:event_reOrientateActionPerformed

    private void deleteContigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteContigActionPerformed
        if (!ReferenceView.getChosenRef().equals("") && !ReferenceView.getChosenQry().equals("")) {
            int delete = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete this query contig?", "Delete", JOptionPane.YES_NO_OPTION);
            if (delete == JOptionPane.YES_OPTION) {
                DeleteConflicts.deleteOne(ReferenceView.getChosenRef(), ReferenceView.getChosenQry());
                repaint();
            }
        }
    }//GEN-LAST:event_deleteContigActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // check the user would really like to reset
        if (!ReferenceView.getChosenRef().equals("")) {
            Object[] choices = {"Default", "Last saved", "Cancel"};

            int n = JOptionPane.showOptionDialog(null,
                    "Would you like to reset to default? Or last saved?",
                    "Reset Referene View",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    choices,
                    choices[2]);

            switch (n) {
                case 0:
                    // reset view to default overlap
                    UserRefData.resetDataToDefault();
                    UserQryData.resetDataToDefault();
                    break;
                case 1:
                    // reset view to last saved
                    UserRefData.setHorZoom(refViewWidth / sumViewWidth);
                    UserRefData.setVertZoom(refViewHeight / sumViewHeight);
                    UserRefData.resetDataToLastSaved();
                    UserQryData.resetDataToLastSaved();
                    break;
                default:
                    break;
            }
            repaint();
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    private void alignLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignLeftActionPerformed
        // align contig to left
        if (!ReferenceView.getChosenRef().equals("") && !ReferenceView.getChosenQry().equals("")) {
            String refId = ReferenceView.getChosenRef();
            String qryId = ReferenceView.getChosenQry();
            UserRefData.align(refId, qryId, true);
            repaint();
        }
    }//GEN-LAST:event_alignLeftActionPerformed

    private void alignRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alignRightActionPerformed
        // align contig to right
        if (!ReferenceView.getChosenRef().equals("") && !ReferenceView.getChosenQry().equals("")) {
            String refId = ReferenceView.getChosenRef();
            String qryId = ReferenceView.getChosenQry();
            UserRefData.align(refId, qryId, false);
            repaint();
        }
    }//GEN-LAST:event_alignRightActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // data is saved to summary view
        if (!ReferenceView.getChosenRef().equals("")) {
            int save = JOptionPane.showConfirmDialog(null, "Would you like to save changes and update Summary View?", "Save Changes", JOptionPane.YES_NO_OPTION);
            if (save == JOptionPane.YES_OPTION) {
                SavedRefData.setHorZoom(sumViewWidth / refViewWidth);
                SavedRefData.setVertZoom(sumViewHeight / refViewHeight);
                SavedRefData.saveOneData(ReferenceView.getChosenRef());
                SavedQryData.saveOneData(ReferenceView.getChosenRef());
                repaint();
            }
        }
    }//GEN-LAST:event_saveActionPerformed

    private void reCentreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reCentreActionPerformed
        // recentre the view
        if (!ReferenceView.getChosenRef().equals("")) {
            UserRefData.setPanelLength(refViewWidth);
            UserRefData.setPanelHeight(refViewHeight);
            String chosenRef = ReferenceView.getChosenRef();
            UserRefData.reCentreView(chosenRef);
            referenceView.repaint();
        }
    }//GEN-LAST:event_reCentreActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        // set IDs of reference and query to that searched
        boolean refMatch = false;
        boolean qryMatch = false;
        boolean regionMatch = false;
        String refSearch = refIdSearch.getText();
        String qrySearch = qryIdSearch.getText();
        String region = regionSearch.getText();
        String type = regionType.getSelectedItem().toString();

        // check if qry Id or ref Id exist otherwise give error message
        if (!refSearch.equals("")) {
            for (String refId : RawFileData.getRefContigs().keySet()) {
                if (refId.equals(refSearch)) {
                    changeRef(refId);
                    refMatch = true;
                }
            }
        }
        if (!qrySearch.equals("")) {
            for (String qryId : RawFileData.getQryContigs().keySet()) {
                if (qryId.equals(qrySearch)) {
                    changeQry(qryId);
                    qryMatch = true;
                }
            }
        }

        if (refMatch && qryMatch) {
            if (!region.equals("")) {
                try {
                    String[] regions = region.split("-");
                    if (type.equals("Query")) {
                        double actualWidth = RawFileData.getQryContigs(qrySearch).getContigLen();
                        if (Double.parseDouble(regions[0]) >= 0 && Double.parseDouble(regions[1]) <= actualWidth) {
                            regionMatch = true;
                            double rectWidth = UserQryData.getQueries(refSearch + "-" + qrySearch).getRectangle().getWidth();
                            double scale = rectWidth / actualWidth;
                            // shift relative to start pos
                            double shift = 100 + (Double.parseDouble(regions[0]) * scale);
                            // zoom relative to width
                            double zoom = queryView.getWidth() / (Double.parseDouble(regions[1]) * scale - Double.parseDouble(regions[0]) * scale);
                            SearchRegionData.setRegion(shift, zoom);
                            QueryView.setRegionView(true);
                            repaint();
                        }
                    } else if (type.equals("Reference")) {
                        double actualWidth = RawFileData.getRefContigs(refSearch).getContigLen();
                        if (Double.parseDouble(regions[0]) >= 0 && Double.parseDouble(regions[1]) <= actualWidth) {
                            regionMatch = true;
                            double rectWidth = UserQryData.getReferences(refSearch + "-" + qrySearch).getRectangle().getWidth();
                            double scale = rectWidth / actualWidth;
                            // shift relative to start pos
                            double shift = 100 - UserQryData.getQueries(refSearch + "-" + qrySearch).getRefAlignStart() + UserQryData.getQueries(refSearch + "-" + qrySearch).getQryAlignStart() + (Double.parseDouble(regions[0]) * scale);
                            // zoom relative to width
                            double zoom = queryView.getWidth() / (Double.parseDouble(regions[1]) * scale - Double.parseDouble(regions[0]) * scale);
                            SearchRegionData.setRegion(shift, zoom);
                            QueryView.setRegionView(true);
                            repaint();
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Region is not formatted correctly", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (!refMatch) {
            JOptionPane.showMessageDialog(null, "No match with Reference ID", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        if (!qryMatch) {
            JOptionPane.showMessageDialog(null, "No match with Query ID", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        if (!regionMatch) {
            JOptionPane.showMessageDialog(null, "Region out of bounds", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_searchActionPerformed

    private void referenceViewMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_referenceViewMouseMoved
        // when mouse is hovered over, display the position
        if (!ReferenceView.getChosenRef().equals("")) {
            double positionScale;
            String position = "";
            String refId = ReferenceView.getChosenRef();
            Rectangle2D ref = UserRefData.getReferences(refId).getRectangle();
            if (ref.contains(evt.getPoint())) {
                // display position
                positionScale = RawFileData.getRefContigs(refId).getContigLen() / ref.getWidth();
                position = String.format("%.2f", (evt.getPoint().getX() - ref.getMinX()) * positionScale);
            }
            Rectangle2D qry;
            for (String qryId : UserRefData.getReferences(refId).getConnections()) {
                qry = UserRefData.getQueries(refId + "-" + qryId).getRectangle();
                if (qry.contains(evt.getPoint())) {
                    // display position
                    positionScale = RawFileData.getQryContigs(qryId).getContigLen() / qry.getWidth();
                    position = String.format("%.2f", (evt.getPoint().getX() - qry.getMinX()) * positionScale);
                }
            }

            ReferenceView.setPosition(position);
            ReferenceView.setMouseX(evt.getX());
            ReferenceView.setMouseY(evt.getY());

            referenceView.repaint(evt.getX() - 500, evt.getY() - 500, 1000, 1000);
        }
    }//GEN-LAST:event_referenceViewMouseMoved

    private void queryViewMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_queryViewMouseMoved
        // when mouse is hovered over, display the position
        if (!QueryView.getChosenRef().equals("") && !QueryView.getChosenQry().equals("")) {
            double positionScale;
            String position = "";
            String refId = QueryView.getChosenRef();
            String qryId = QueryView.getChosenQry();
            Rectangle2D ref;
            Rectangle2D qry;
            if (QueryView.isRegionView()) {
                ref = SearchRegionData.getRef().getRectangle();
                qry = SearchRegionData.getQry().getRectangle();
            } else {
                ref = UserQryData.getReferences(refId + "-" + qryId).getRectangle();
                qry = UserQryData.getQueries(refId + "-" + qryId).getRectangle();
            }
            if (ref.contains(evt.getPoint())) {
                // display position
                positionScale = RawFileData.getRefContigs(refId).getContigLen() / ref.getWidth();
                position = String.format("%.2f", (evt.getPoint().getX() - ref.getMinX()) * positionScale);
            }

            if (qry.contains(evt.getPoint())) {
                // display position
                positionScale = RawFileData.getQryContigs(qryId).getContigLen() / qry.getWidth();
                position = String.format("%.2f", (evt.getPoint().getX() - qry.getMinX()) * positionScale);
            }

            QueryView.setPosition(position);
            QueryView.setMouseX(evt.getX());
            QueryView.setMouseY(evt.getY());

            queryView.repaint(evt.getX() - 500, evt.getY() - 500, 1000, 1000);
        }
    }//GEN-LAST:event_queryViewMouseMoved

    private void confidenceSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confidenceSetActionPerformed
        // show confidence settings
        confidenceSettings.setVisible(true);
    }//GEN-LAST:event_confidenceSetActionPerformed

    private void coverageSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageSetActionPerformed
        // show coverage settings
        coverageSettings.setVisible(true);
    }//GEN-LAST:event_coverageSetActionPerformed

    private void chimqualSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chimqualSetActionPerformed
        // show chimeric quality settings
        chimSettings.setVisible(true);
    }//GEN-LAST:event_chimqualSetActionPerformed

    private void saveConfThresholdsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfThresholdsActionPerformed
        // save all thresholds set in confidence settings
        ReferenceView.setLowConf((int) lowConf.getValue());
        ReferenceView.setHighConf((int) highConf.getValue());
        confidenceSettings.setVisible(false);
        repaint();
    }//GEN-LAST:event_saveConfThresholdsActionPerformed

    private void saveCovThresholdsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCovThresholdsActionPerformed
        // save all thresholds in coverage settings
        ReferenceView.setLowCov((int) lowCov.getValue());
        ReferenceView.setHighCov((int) highCov.getValue());
        coverageSettings.setVisible(false);
        repaint();
    }//GEN-LAST:event_saveCovThresholdsActionPerformed

    private void saveQualitySettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveQualitySettingsActionPerformed
        // save all thresholds in chim quality settings
        ReferenceView.setLowQual((int) lowQual.getValue());
        ReferenceView.setHighQual((int) highQual.getValue());
        chimSettings.setVisible(false);
        repaint();
    }//GEN-LAST:event_saveQualitySettingsActionPerformed

    private void saveAllContigsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllContigsActionPerformed
        // save the view of all contigs
        if (!RawFileData.getRefContigs().isEmpty()) {
            int saveAll = JOptionPane.showConfirmDialog(null, "Are you sure you would like to save the view of all contigs?", "Save All Contigs", JOptionPane.YES_NO_OPTION);
            if (saveAll == JOptionPane.YES_OPTION) {
                SavedRefData.setHorZoom(sumViewWidth / refViewWidth);
                SavedRefData.setVertZoom(sumViewHeight / refViewHeight);
                SavedQryData.saveAllData();
                SavedRefData.saveAllData();
                repaint();
            }
        } else {
            JOptionPane.showMessageDialog(null, "No files loaded", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveAllContigsActionPerformed

    private void overlapSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overlapSettingActionPerformed
        // when check box ticked, colour regions of overlap
        boolean checked = overlapSetting.isSelected();
        ReferenceView.setOverlapView(checked);
        repaint();
    }//GEN-LAST:event_overlapSettingActionPerformed

    private void chooseImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseImagesActionPerformed
        if (tabPaneFiles.indexOfTab("Choose Images") == -1) {
            fillImageTable();
            JScrollPane scrollPane = new JScrollPane();
            tabPaneFiles.addTab("Choose Images", scrollPane);
            scrollPane.getViewport().add(imageTable);
            imageTable.getTableHeader().setLayout(new BorderLayout());
            imageTable.getColumnModel().getColumn(1).setHeaderRenderer(new EditableHeaderRenderer(selectAllImages));
        } else {
            JOptionPane.showMessageDialog(null, "Choose Images tab exists already in Reference View - use this to choose images to export", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_chooseImagesActionPerformed

    private void exportQryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportQryButtonActionPerformed
        // export chosen image into chosen directory
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Save PNG of query alignment view", FileDialog.SAVE);
        fileBox.setVisible(true);

        if (fileBox.getFile() != null) {
            String chosenPath = fileBox.getDirectory();
            String chosenFile = fileBox.getFile();

            BufferedImage image = new BufferedImage(queryView.getWidth(), queryView.getHeight(), BufferedImage.TYPE_INT_RGB);
            exportQryButton.setVisible(false);
            queryView.paint(image.createGraphics());
            try {
                ImageIO.write(image, "png", new File(chosenPath + chosenFile + ".png"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving image to file", "Error", JOptionPane.ERROR_MESSAGE);
            }
            exportQryButton.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No filename given", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportQryButtonActionPerformed

    private void exportRefButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportRefButtonActionPerformed
        // export chosen image into chosen directory
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Save PNG of reference alignment view", FileDialog.SAVE);
        fileBox.setVisible(true);

        if (fileBox.getFile() != null) {
            String chosenPath = fileBox.getDirectory();
            String chosenFile = fileBox.getFile();

            BufferedImage image = new BufferedImage(referenceView.getWidth(), referenceView.getHeight(), BufferedImage.TYPE_INT_RGB);
            exportRefButton.setVisible(false);
            referenceView.paint(image.createGraphics());
            try {
                ImageIO.write(image, "png", new File(chosenPath + chosenFile + ".png"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error saving image to file", "Error", JOptionPane.ERROR_MESSAGE);
            }
            exportRefButton.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No filename given", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportRefButtonActionPerformed

    private void manualConflictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualConflictActionPerformed
        // Opens a dialog box for user to choose directory of file
        if (tabPaneFiles.indexOfTab("Conflict Resolution") == -1) {
            FileDialog fileBox;
            fileBox = new FileDialog(this, "Open conflicts_cut_status File", FileDialog.LOAD);
            fileBox.setVisible(true);

            if (fileBox.getFile() != null) {
                // Get file name and directory
                String fileDirectory = fileBox.getDirectory();
                String filename = fileDirectory.concat(fileBox.getFile());

                ConflictFileReader.readConflictFile(conflictsTable, filename);
                JScrollPane scrollPane = new JScrollPane();
                tabPaneFiles.addTab("Conflict Resolution", scrollPane);
                scrollPane.getViewport().add(conflictsTable);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Conflict resolution file already open", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_manualConflictActionPerformed

    private void fastaLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastaLoadActionPerformed
        // Open dialog for fasta loading
        fastaLoader.setVisible(true);
    }//GEN-LAST:event_fastaLoadActionPerformed

    private void closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeActionPerformed
        // close program
        System.exit(0);
    }//GEN-LAST:event_closeActionPerformed

    private void browseKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseKeyActionPerformed
        // browse for key file
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Open KEY File", FileDialog.LOAD);
        fileBox.setVisible(true);

        if (fileBox.getFile() != null) {
            // Get file name and directory
            String fileDirectory = fileBox.getDirectory();
            String filename = fileDirectory.concat(fileBox.getFile());

            keyFile.setText(filename);
        }
    }//GEN-LAST:event_browseKeyActionPerformed

    private void browseFastaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFastaActionPerformed
        // browse for fasta file
        // Opens a dialog box for user to choose directory of file
        FileDialog fileBox;
        fileBox = new FileDialog(this, "Open FASTA File", FileDialog.LOAD);
        fileBox.setVisible(true);

        if (fileBox.getFile() != null) {
            // Get file name and directory
            String fileDirectory = fileBox.getDirectory();
            String filename = fileDirectory.concat(fileBox.getFile());

            fastaFile.setText(filename);
        }
    }//GEN-LAST:event_browseFastaActionPerformed

    private void loadFastaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFastaFileActionPerformed
        // load both files into the program
        if (!fastaFile.getText().equals("")) {
            fastaLoader.setVisible(false);

            String refQry = refOrQry.getSelectedItem().toString();

            if (refQry.equals("Reference")) {
                ArrayList<String> refIds = new ArrayList<>(RawFileData.getReferences().keySet());
                LinkedHashMap<String, String> names = FastaReader.readKeyFile(keyFile.getText(), refIds, RawFileData.getRefContigs());
                LinkedHashMap<String, String> sequences = FastaReader.readFasta(fastaFile.getText(), names);

                // add sequences to contigs
                QueryView.setRefSequenceView(true);
                UserQryData.addSequences(names, sequences, "ref");

            } else if (refQry.equals("Query")) {
                ArrayList<String> qryIds = new ArrayList();
                for (String refqryId : RawFileData.getQueries().keySet()) {
                    String qryId = refqryId.split("-")[1];
                    if (!qryIds.contains(qryId)) {
                        qryIds.add(qryId);
                    }
                }
                LinkedHashMap<String, String> names = FastaReader.readKeyFile(keyFile.getText(), qryIds, RawFileData.getQryContigs());
                LinkedHashMap<String, String> sequences = FastaReader.readFasta(fastaFile.getText(), names);

                // add sequences to contigs
                QueryView.setQrySequenceView(true);
                UserQryData.addSequences(names, sequences, "qry");
            }
            repaint();
        } else {
            JOptionPane.showMessageDialog(null, "No FASTA file has be chosen", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loadFastaFileActionPerformed

    private void saveConflictFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConflictFileActionPerformed
        // save conflict file with changes if tab is open
        if (tabPaneFiles.indexOfTab("Conflict Resolution") != -1) {
            // Opens a dialog box for user to save file
            FileDialog fileBox;
            fileBox = new FileDialog(this, "Open conflicts_cut_status File", FileDialog.SAVE);
            fileBox.setVisible(true);

            // Get file name and directory
            if (fileBox.getFile() != null) {
                String fileDirectory = fileBox.getDirectory();
                String filename = fileDirectory.concat(fileBox.getFile());

                ConflictFileWriter.writeConflictFile(filename, conflictsTable, ConflictFileReader.getFirstRows());

                tabPaneFiles.remove(tabPaneFiles.indexOfTab("Conflict Resolution"));

            } else {
                JOptionPane.showMessageDialog(null, "File name not entered", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No conflict resolution file is loaded", "No file found", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_saveConflictFileActionPerformed

    private void exportImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImagesActionPerformed
        // export chosen images into chosen directory
        if (tabPaneFiles.indexOfTab("Choose Images") != -1) {
            // Opens a dialog box for user to choose directory of file
            FileDialog fileBox;
            fileBox = new FileDialog(this, "Choose a directory to save images", FileDialog.SAVE);
            fileBox.setVisible(true);

            String chosenPath = fileBox.getDirectory();
            String chosenDir = fileBox.getFile();

            if (chosenDir != null) {

                new File(chosenPath + chosenDir).mkdirs();

                exportRefButton.setVisible(false);
                // loop through all chosen images
                for (int i = 0; i < imageTable.getRowCount(); i++) {
                    imageTable.setRowSelectionInterval(i, i);
                    boolean chosen = imageTable.getValueAt(imageTable.getSelectedRow(), 1).toString().equals("true");
                    if (chosen) {
                        String refId = imageTable.getValueAt(imageTable.getSelectedRow(), 0).toString();
                        changeRef(refId);
                        repaint();

                        BufferedImage image = new BufferedImage(referenceView.getWidth(), referenceView.getHeight(), BufferedImage.TYPE_INT_RGB);
                        referenceView.paint(image.createGraphics());
                        try {
                            ImageIO.write(image, "png", new File(chosenPath + chosenDir + "\\reference_" + refId + "_alignments.png"));
                            tabPaneFiles.remove(tabPaneFiles.indexOfTab("Choose Images"));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error saving image to file", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                exportRefButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Directory name not entered", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No images chosen - choose images before exporting", "No images chosen", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_exportImagesActionPerformed

    public static void main(String args[]) {
        // Set the look and feel
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainInterface().setVisible(true);
            }
        });
    }

    private void setRefContigTable() {
        DefaultTableModel refModel = TableModels.getRefModel();
        refModel.addColumn("Ref ID");
        refModel.addColumn("Length");
        refModel.addColumn("Labels");
        refModel.addColumn("Density");
        refModel.addColumn("Alignments");
        refModel.addColumn("Overlaps");

        refContigTable.setModel(refModel);

        refContigTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "refUp");
        refContigTable.getActionMap().put("refUp", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (refContigTable.getSelectedRow() != 0) {
                    refContigTable.setRowSelectionInterval(refContigTable.getSelectedRow() - 1, refContigTable.getSelectedRow() - 1);
                }
                // get selected contig
                String chosenRef = refContigTable.getValueAt(refContigTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });
        refContigTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "refDown");
        refContigTable.getActionMap().put("refDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (refContigTable.getSelectedRow() != refContigTable.getRowCount() - 1) {
                    refContigTable.setRowSelectionInterval(refContigTable.getSelectedRow() + 1, refContigTable.getSelectedRow() + 1);
                }
                // get selected contig
                String chosenRef = refContigTable.getValueAt(refContigTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });

    }

    private void setQryContigTable() {
        DefaultTableModel qryModel = TableModels.getQryModel();
        qryModel.addColumn("Query ID");
        qryModel.addColumn("Length");
        qryModel.addColumn("Orientation");
        qryModel.addColumn("Confidence");
        qryModel.addColumn("HitEnum");
        qryModel.addColumn("Num Labels");
        qryModel.addColumn("Num Matches");
        qryContigTable.setModel(qryModel);

        qryContigTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "qryUp");
        qryContigTable.getActionMap().put("qryUp", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (qryContigTable.getSelectedRow() != 0) {
                    qryContigTable.setRowSelectionInterval(qryContigTable.getSelectedRow() - 1, qryContigTable.getSelectedRow() - 1);
                }
                // get selected contig
                String chosenQry = qryContigTable.getValueAt(qryContigTable.getSelectedRow(), 0).toString();
                changeQry(chosenQry);
                repaint();
            }
        });
        qryContigTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "qryDown");
        qryContigTable.getActionMap().put("qryDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (qryContigTable.getSelectedRow() != qryContigTable.getRowCount() - 1) {
                    qryContigTable.setRowSelectionInterval(qryContigTable.getSelectedRow() + 1, qryContigTable.getSelectedRow() + 1);
                }
                // get selected contig
                String chosenQry = qryContigTable.getValueAt(qryContigTable.getSelectedRow(), 0).toString();
                changeQry(chosenQry);
                repaint();
            }
        });

    }

    private void setQryViewRefTable() {
        // Construct empty table model and set headings for protein table
        DefaultTableModel qryViewRefModel = TableModels.getQryViewRefModel();
        qryViewRefModel.addColumn("Reference ID");
        qryViewRefModel.addColumn("Orientation");
        qryViewRefModel.addColumn("Confidence");
        qryViewRefTable.setModel(qryViewRefModel);
        qryViewRefTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "qryUp");
        qryViewRefTable.getActionMap().put("qryUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (qryViewRefTable.getSelectedRow() != 0) {
                    qryViewRefTable.setRowSelectionInterval(qryViewRefTable.getSelectedRow() - 1, qryViewRefTable.getSelectedRow() - 1);
                }
                // get selected contig
                String chosenRef = qryViewRefTable.getValueAt(qryViewRefTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });
        qryViewRefTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "qryMDown");
        qryViewRefTable.getActionMap().put("qryMDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (qryViewRefTable.getSelectedRow() != qryViewRefTable.getRowCount() - 1) {
                    qryViewRefTable.setRowSelectionInterval(qryViewRefTable.getSelectedRow() + 1, qryViewRefTable.getSelectedRow() + 1);
                }
                // get selected contig
                String chosenRef = qryViewRefTable.getValueAt(qryViewRefTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });
    }

    private void setLabelTable() {
        DefaultTableModel labelModel = TableModels.getLabelModel();
        labelModel.addColumn("Site ID");
        labelModel.addColumn("Position");
        labelModel.addColumn("Coverage");
        labelModel.addColumn("Occurance");
        labelModel.addColumn("ChimQuality");
        labelModel.addColumn("Std Dev");
        labelTable.setModel(labelModel);
        labelTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "lUp");
        labelTable.getActionMap().put("lUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (labelTable.getSelectedRow() != 0) {
                    labelTable.setRowSelectionInterval(labelTable.getSelectedRow() - 1, labelTable.getSelectedRow() - 1);
                }
                // get selected label
                String chosenLabel = labelTable.getValueAt(labelTable.getSelectedRow(), 0).toString();
                QueryView.setChosenLabel(chosenLabel);
                repaint();
            }
        });
        labelTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "lDown");
        labelTable.getActionMap().put("lDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (labelTable.getSelectedRow() != labelTable.getRowCount() - 1) {
                    labelTable.setRowSelectionInterval(labelTable.getSelectedRow() + 1, labelTable.getSelectedRow() + 1);
                }
                // get selected label
                String chosenLabel = labelTable.getValueAt(labelTable.getSelectedRow(), 0).toString();
                QueryView.setChosenLabel(chosenLabel);
                repaint();
            }
        });
    }

    private void setConflictsTable() {
        DefaultTableModel conflictModel = new DefaultTableModel();
        conflictModel.addColumn("xMapId");
        conflictModel.addColumn("refQry");
        conflictModel.addColumn("Id");
        conflictModel.addColumn("leftRefBkpt");
        conflictModel.addColumn("rightRefBkpt");
        conflictModel.addColumn("alignmentOrientation");
        conflictModel.addColumn("leftBkpt-toCut");
        conflictModel.addColumn("rightBkpt_toCut");
        conflictModel.addColumn("toDiscard");
        conflictsTable.setModel(conflictModel);
        conflictsTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "cUp");
        conflictsTable.getActionMap().put("cUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (conflictsTable.getSelectedRow() != 0) {
                    conflictsTable.setRowSelectionInterval(conflictsTable.getSelectedRow() - 2, conflictsTable.getSelectedRow() - 1);
                }
                // get selected ref and qry
                String chosenRef = conflictsTable.getValueAt(conflictsTable.getSelectedRow(), 2).toString();
                String chosenQry = conflictsTable.getValueAt(conflictsTable.getSelectedRow() + 1, 2).toString();
                changeRef(chosenRef);
                changeQry(chosenQry);
                repaint();
            }
        });
        conflictsTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "cDown");
        conflictsTable.getActionMap().put("cDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (conflictsTable.getSelectedRow() != conflictsTable.getRowCount() - 2) {
                    conflictsTable.setRowSelectionInterval(conflictsTable.getSelectedRow() + 2, conflictsTable.getSelectedRow() + 3);
                }
                // get selected ref and qry
                String chosenRef = conflictsTable.getValueAt(conflictsTable.getSelectedRow(), 2).toString();
                String chosenQry = conflictsTable.getValueAt(conflictsTable.getSelectedRow() + 1, 2).toString();
                changeRef(chosenRef);
                changeQry(chosenQry);
                repaint();
            }
        });
    }

    private void setImageTable() {
        DefaultTableModel imageModel = TableModels.getImageModel();
        imageModel.addColumn("Reference ID");
        imageModel.addColumn("Export Image");

        imageTable.setModel(imageModel);
        imageTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "iUp");
        imageTable.getActionMap().put("iUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageTable.getSelectedRow() != 0) {
                    imageTable.setRowSelectionInterval(imageTable.getSelectedRow() - 1, imageTable.getSelectedRow() - 1);
                }
                // get selected contig
                String chosenRef = imageTable.getValueAt(imageTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });
        imageTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "iDown");
        imageTable.getActionMap().put("iDown", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (imageTable.getSelectedRow() != imageTable.getRowCount() - 1) {
                    imageTable.setRowSelectionInterval(imageTable.getSelectedRow() + 1, imageTable.getSelectedRow() + 1);
                }
                // get selected contig
                String chosenRef = imageTable.getValueAt(imageTable.getSelectedRow(), 0).toString();
                changeRef(chosenRef);
                repaint();
            }
        });
    }

    private ChartPanel makeLengthChartPanel(ArrayList<Double> lengths, String refId, boolean ref) {
        // Create dataset with sorted length array
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < lengths.size(); i++) {
            dataset.addValue(lengths.get(i) / 1000, "", Integer.toString(i));
        }
        // Create horizontal bar chart using dataset
        JFreeChart chart = ChartFactory.createBarChart("", "", "Contig Length (kbp)",
                dataset, PlotOrientation.VERTICAL, false, false, false);
        // Alter parameters of plot
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.getDomainAxis().setVisible(false);
        plot.setBackgroundPaint(new Color(244, 244, 244));

        plot.setRenderer(new MyChartRenderer());
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
        ((BarRenderer) plot.getRenderer()).setShadowVisible(false);
        plot.getRenderer().setSeriesPaint(0, Color.black);

        // if chosen contig, draw line to show what length
        if (!refId.equals("")) {
            int selectedBar = lengths.indexOf(RawFileData.getRefContigs(refId).getContigLen());
            ((MyChartRenderer) plot.getRenderer()).setSelectedBar(selectedBar);
            Marker line = new ValueMarker(RawFileData.getRefContigs(refId).getContigLen() / 1000);
            line.setPaint(new Color(97, 204, 10));
            line.setLabel(" ID: " + refId + " ");
            line.setLabelFont(new Font("Tahoma", Font.BOLD, 10));
            line.setLabelAnchor(RectangleAnchor.CENTER);
            line.setLabelBackgroundColor(new Color(244, 244, 244));
            line.setLabelPaint(new Color(0, 153, 0));
            plot.addRangeMarker(line);

        }
        // Create panel for this chart
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private ChartPanel makeDensityChartPanel(ArrayList<Double> densities, String refId, boolean ref) {
        // Create dataset with sorted length array
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < densities.size(); i++) {
            dataset.addValue(densities.get(i), "", Integer.toString(i));
        }
        // Create horizontal bar chart using dataset
        JFreeChart chart = ChartFactory.createLineChart("", "", "Label Density (/100 kbp)",
                dataset, PlotOrientation.VERTICAL, false, false, false);
        // Alter parameters of plot
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.getDomainAxis().setVisible(false);
        plot.setBackgroundPaint(new Color(244, 244, 244));
        plot.getRenderer().setSeriesPaint(0, Color.black);

        Marker goodZone = new IntervalMarker(10, 20, new Color(204, 255, 179), new BasicStroke(2.0f), null, null, 0.5f);
        goodZone.setPaint(new Color(204, 255, 179));
        plot.addRangeMarker(goodZone);

        // if chosen contig, draw line to show what length
        if (!refId.equals("")) {
            double labDense = ((RawFileData.getRefContigs(refId).getLabelInfo().length - 1) / RawFileData.getRefContigs(refId).getContigLen()) * 100000;
            Marker line = new ValueMarker(labDense);
            line.setPaint(new Color(97, 204, 10));
            line.setLabel(" Label Density: " + String.format("%.2f", labDense) + " ");
            line.setLabelFont(new Font("Tahoma", Font.BOLD, 10));
            line.setLabelAnchor(RectangleAnchor.CENTER);
            line.setLabelBackgroundColor(new Color(244, 244, 244));
            line.setLabelPaint(new Color(0, 153, 0));
            plot.addRangeMarker(line);
        }
        // Create panel for this chart
        ChartPanel chartPanel = new ChartPanel(chart);
        return chartPanel;
    }

    private void fillQryTable(String refId) {
        // Empty table
        DefaultTableModel qryModel = (DefaultTableModel) qryContigTable.getModel();
        qryModel.setRowCount(0);
        // Add rows to query table
        if (!refId.equals("")) {
            for (String qryId : RawFileData.getReferences(refId).getConnections()) {
                qryModel.addRow(new Object[]{
                    qryId,
                    (int) RawFileData.getQryContigs(qryId).getContigLen(),
                    RawFileData.getAlignmentInfo(refId + "-" + qryId).getOrientation(),
                    Double.parseDouble(RawFileData.getAlignmentInfo(refId + "-" + qryId).getConfidence()),
                    RawFileData.getAlignmentInfo(refId + "-" + qryId).getHitEnum(),
                    (int) RawFileData.getQryContigs(qryId).getLabelInfo().length - 1,
                    (int) RawFileData.getQueries(refId + "-" + qryId).getAlignments().length
                });
            }
        }
    }

    private void fillRefTable(LinkedHashMap<String, Integer> numOverlaps) {
        // Format table to list all contigs with matches
        DefaultTableModel tmRefContigs = (DefaultTableModel) refContigTable.getModel();
        // Empty table
        tmRefContigs.setRowCount(0);
        // Add rows to table
        for (String refId : RawFileData.getReferences().keySet()) {
            tmRefContigs.addRow(new Object[]{
                refId,
                (int) RawFileData.getRefContigs(refId).getContigLen(),
                (int) RawFileData.getRefContigs(refId).getLabelInfo().length - 1,
                (double) ((RawFileData.getRefContigs(refId).getLabelInfo().length - 1) / RawFileData.getRefContigs(refId).getContigLen()) * 100000,
                (int) RawFileData.getReferences(refId).getConnections().length,
                (int) numOverlaps.get(refId)
            });
        }
    }

    private void fillImageTable() {
        // Format table to list all contigs with matches
        DefaultTableModel imageModel = (DefaultTableModel) imageTable.getModel();
        // Empty table
        imageModel.setRowCount(0);
        // Add rows to table
        for (String refId : RawFileData.getReferences().keySet()) {
            imageModel.addRow(new Object[]{
                refId,
                false
            });
        }
    }

    private void fillQryViewRefTable(String qryId) {
        DefaultTableModel tmQryMatch = (DefaultTableModel) qryViewRefTable.getModel();
        // Empty table
        tmQryMatch.setRowCount(0);
        // Add rows to table
        if (!qryId.equals("")) {
            for (String refId : QueryViewData.getQryConnections(qryId)) {
                tmQryMatch.addRow(new Object[]{
                    refId,
                    RawFileData.getAlignmentInfo(refId + "-" + qryId).getOrientation(),
                    Double.parseDouble(RawFileData.getAlignmentInfo(refId + "-" + qryId).getConfidence())
                });
            }
        }

    }

    private void fillLabelTable(String qryId) {
        DefaultTableModel labelModel = (DefaultTableModel) labelTable.getModel();
        // Empty table
        labelModel.setRowCount(0);
        // Add rows to table
        if (!qryId.equals("")) {
            for (int i = 0; i < RawFileData.getQryContigs(qryId).getLabelInfo().length - 1; i++) {
                Double chimQual;
                LabelInfo label = RawFileData.getQryContigs(qryId).getLabelInfo()[i];
                if (label.getChimQuality() == null) {
                    chimQual = 0.0;
                } else {
                    chimQual = Double.parseDouble(label.getChimQuality());
                }
                labelModel.addRow(new Object[]{
                    Integer.toString(i),
                    Double.parseDouble(label.getLabelPos()),
                    Double.parseDouble(label.getCoverage()),
                    Double.parseDouble(label.getOccurance()),
                    chimQual,
                    Double.parseDouble(label.getStdDev())
                });
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton alignLeft;
    private javax.swing.JButton alignRight;
    private javax.swing.JButton browseFasta;
    private javax.swing.JButton browseKey;
    private javax.swing.JButton browseQry;
    private javax.swing.JButton browseRef;
    private javax.swing.JButton browseXmap;
    private javax.swing.JDialog chimSettings;
    private javax.swing.JMenuItem chimqualSet;
    private javax.swing.JMenuItem chooseImages;
    private javax.swing.JMenuItem close;
    private javax.swing.JMenuItem confidenceSet;
    private javax.swing.JCheckBox confidenceSetting;
    private javax.swing.JDialog confidenceSettings;
    private javax.swing.JMenuItem coverageSet;
    private javax.swing.JDialog coverageSettings;
    private javax.swing.JButton deleteContig;
    private javax.swing.JMenuItem exportImages;
    private javax.swing.JButton exportQryButton;
    private javax.swing.JButton exportRefButton;
    private javax.swing.JTextField fastaFile;
    private javax.swing.JMenuItem fastaLoad;
    private javax.swing.JDialog fastaLoader;
    private javax.swing.JDialog fileLoader;
    private javax.swing.JSpinner highConf;
    private javax.swing.JSpinner highCov;
    private javax.swing.JSpinner highQual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextField keyFile;
    private javax.swing.JPanel labelDensityGraph;
    private javax.swing.JTable labelTable;
    private javax.swing.JButton loadFastaFile;
    private javax.swing.JMenuItem loadMaps;
    private javax.swing.JSpinner lowConf;
    private javax.swing.JSpinner lowCov;
    private javax.swing.JSpinner lowQual;
    private javax.swing.JMenuItem manualConflict;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem orientateContigs;
    private javax.swing.JCheckBox overlapSetting;
    private javax.swing.JTable qryContigTable;
    private javax.swing.JTextField qryDataset;
    private javax.swing.JTextField qryFile;
    private javax.swing.JTextField qryIdSearch;
    private javax.swing.JTable qryViewRefTable;
    private UserInterface.QueryView queryView;
    private javax.swing.JLayeredPane queryViewPane;
    private javax.swing.JScrollPane queryViewTableScroll;
    private javax.swing.JButton reCentre;
    private javax.swing.JButton reOrientate;
    private javax.swing.JTable refContigTable;
    private javax.swing.JScrollPane refContigTableScroll;
    private javax.swing.JTextField refDataset;
    private javax.swing.JTextField refFile;
    private javax.swing.JTextField refIdSearch;
    private javax.swing.JComboBox<String> refOrQry;
    private javax.swing.JLayeredPane refViewPane;
    private javax.swing.JScrollPane refViewTableScroll;
    private UserInterface.ReferenceView referenceView;
    private javax.swing.JPanel referencesGraph;
    private javax.swing.JTextField regionSearch;
    private javax.swing.JComboBox<String> regionType;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton runAnalysis;
    private javax.swing.JButton save;
    private javax.swing.JMenuItem saveAllContigs;
    private javax.swing.JButton saveConfThresholds;
    private javax.swing.JMenuItem saveConflictFile;
    private javax.swing.JButton saveCovThresholds;
    private javax.swing.JButton saveQualitySettings;
    private javax.swing.JButton search;
    private javax.swing.JRadioButton styleChim;
    private javax.swing.JRadioButton styleCoverage;
    private javax.swing.JRadioButton styleMatch;
    private javax.swing.JLayeredPane summaryPane;
    private UserInterface.SummaryView summaryView;
    private javax.swing.JMenuItem swapContigs;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTabbedPane tabPaneFiles;
    private javax.swing.JTextField xmapFile;
    private javax.swing.JButton zoomIn;
    private javax.swing.JButton zoomOut;
    // End of variables declaration//GEN-END:variables

    private void setAllData() {

        // generate raw data
        RawFileData.setData();
        // set overlap data from raw data
        SortOverlap.sortOverlaps(RawFileData.getReferences(), RawFileData.getQueries(), 10000);

        // set reference view data
        refViewHeight = referenceView.getHeight();
        refViewWidth = referenceView.getWidth();
        RefViewData.setPanelLength(refViewWidth);
        RefViewData.setPanelHeight(refViewHeight);
        RefViewData.setData();

        // set summary view data
        sumViewHeight = summaryView.getHeight();
        sumViewWidth = summaryView.getWidth();
        SummaryViewData.setHorZoom(sumViewWidth / refViewWidth);
        SummaryViewData.setVertZoom(sumViewHeight / refViewHeight);
        SummaryViewData.setData();

        // set query view data
        qryViewHeight = queryView.getHeight();
        qryViewWidth = queryView.getWidth();
        QueryViewData.setPanelLength(qryViewWidth);
        QueryViewData.setPanelHeight(qryViewHeight);
        QueryViewData.setData();

        // set usr altered data
        UserRefData.setData();
        UserQryData.setData();
        SavedRefData.setData();
        SavedQryData.setData();

        LinkedHashMap<String, Integer> numOverlaps = CalculateOverlaps.countAllOverlaps(RefViewData.getReferences(), RefViewData.getQueries());

        fillRefTable(numOverlaps);

        // Displays graph of reference contigs
        referencesGraph.removeAll();
        ChartPanel refChartPanel = makeLengthChartPanel(RawFileData.getRefLengths(), ReferenceView.getChosenRef(), true);
        referencesGraph.add(refChartPanel, BorderLayout.CENTER);
        referencesGraph.setVisible(true);

        // Displays graph of query contigs
        labelDensityGraph.removeAll();
        ChartPanel labDenseChartPanel = makeDensityChartPanel(RawFileData.getRefDensity(), ReferenceView.getChosenRef(), false);
        labelDensityGraph.add(labDenseChartPanel, BorderLayout.CENTER);
        labelDensityGraph.setVisible(true);
    }

    private void resetData() {
        RawFileData.resetData();
        RefViewData.resetData();
        SummaryViewData.resetData();
        QueryViewData.resetData();
        QueryView.setQrySequenceView(false);
        QueryView.setRefSequenceView(false);

        UserRefData.resetData();
        UserQryData.resetData();
        SavedRefData.resetData();
        SavedQryData.resetData();
        SearchRegionData.resetData();

        changeQry("");
        changeRef("");
    }

    private void changeRef(String refId) {
        ReferenceView.setRefDataset(refDataset.getText());
        ReferenceView.setQryDataset(qryDataset.getText());
        ReferenceView.setChosenRef(refId);
        SummaryView.setChosenRef(refId);
        QueryView.setChosenRef(refId);
        fillQryTable(refId);
        QueryView.setRegionView(false);
        QueryView.setChosenLabel("");
        SearchRegionData.resetData();
        // Redraw the graph with contig marked
        referencesGraph.removeAll();
        ChartPanel chartPanel1 = makeLengthChartPanel(RawFileData.getRefLengths(), refId, true);
        referencesGraph.add(chartPanel1, BorderLayout.CENTER);
        referencesGraph.setVisible(true);
        // Redraw the graph with contig marked
        labelDensityGraph.removeAll();
        ChartPanel chartPanel2 = makeDensityChartPanel(RawFileData.getRefDensity(), refId, true);
        labelDensityGraph.add(chartPanel2, BorderLayout.CENTER);
        labelDensityGraph.setVisible(true);
        refIdSearch.setText(refId);
        repaint();
    }

    private void changeQry(String qryId) {
        ReferenceView.setRefDataset(refDataset.getText());
        ReferenceView.setQryDataset(qryDataset.getText());
        ReferenceView.setChosenQry(qryId);
        QueryView.setChosenQry(qryId);
        QueryView.setRegionView(false);
        QueryView.setChosenLabel("");
        SearchRegionData.resetData();
        fillLabelTable(qryId);
        fillQryViewRefTable(qryId);
        qryIdSearch.setText(qryId);
        repaint();
    }
}
