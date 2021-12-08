package com.stibla.findduplicatefiles;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class MainForm extends javax.swing.JFrame implements java.awt.event.ActionListener {

    private static final long serialVersionUID = 1L;
    private static DefaultTableModel model;
    private static JTable table;
    public static javax.swing.JTextField filterText;
    private static javax.swing.JProgressBar progressBarr;
    private static int stavPB;

    MainForm() {
        super("FindDuplicateFiles");
        setDefaultCloseOperation(3);

        stavPB = 0;

        String[] columns = {"Subor", "Hash", "Velkost", "Duplicita"};
        model = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            public Class<?> getColumnClass(int column) {
                Class<?> returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    try {
                        if (getValueAt(0, column) == null) {
                            returnValue = Object.class;
                        } else /*  37 */ {
                            returnValue = getValueAt(0, column).getClass();
                        }
                    } catch (Exception e) {
                        System.out.println("Chyba returnValue = getValueAt(0, column).getClass();");
                        returnValue = Object.class;
                    }
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }

        };
        table = new JTable(model);
        javax.swing.table.TableRowSorter<javax.swing.table.TableModel> sorter = new javax.swing.table.TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);

        javax.swing.JScrollPane pane = new javax.swing.JScrollPane(table);
        add(pane, "Center");

        JPanel panel = new JPanel(new java.awt.BorderLayout());
        javax.swing.JLabel label = new javax.swing.JLabel("Adresar:");
        panel.add(label, "West");

        filterText = new javax.swing.JTextField("");
        filterText.setEditable(false);
        panel.add(filterText, "Center");

        JButton buttonSpusti = new JButton("Spustit");
        buttonSpusti.addActionListener(this);
        panel.add(buttonSpusti, "South");

        progressBarr = new javax.swing.JProgressBar();
        panel.add(progressBarr, "North");

        JButton buttonFCh = new JButton("Vyber");
        buttonFCh.addActionListener(this);
        panel.add(buttonFCh, "East");

        add(panel, "North");

        JButton button = new JButton("Zmazat");
        button.addActionListener(this);
        add(button, "South");

        setSize(1000, 500);
    }

    public void actionPerformed(ActionEvent ua) {
        if (ua.getActionCommand().equals("Zmazat")) {
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                if (table.isRowSelected(i)) {
                    try {
                        File path = new File((String) table.getValueAt(i, 0));

                        path.delete();
                        TbRemoveRowPath(path.getAbsolutePath());
                    } catch (Exception e) {
                        System.out.println("Chyba Zmazat");
                    }
                }
            }
        }

        if (ua.getActionCommand().equals("Spustit")) {
            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }
            try {
                new Thread(new RunFindDuplicateFiles()).start();
            } catch (Exception e) {
                System.out.println("Chyba new Thread(new Spusti()).start()");
            }
        }

        if (ua.getActionCommand().equals("Vyber")) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(1);
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == 0) {
                File selectedFile = fileChooser.getSelectedFile();
                filterText.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    public static void TbAddRow(Object[] row) {
        model.addRow(row);
    }

    public static Object TbGetValue(int row, int column) {
        return model.getValueAt(row, column);
    }

    public static int TbGetRowCount() {
        return model.getRowCount();
    }

    public static void TbSetValue(Object val, int row, int column) {
        model.setValueAt(val, row, column);
    }

    public static void TbRemoveRow(int row) {
        model.removeRow(row);
    }

    public static void TbRemoveRowPath(String path) {
        for (int i = model.getRowCount() - 1; i >= 0; i--) /* 143 */ {
            if (model.getValueAt(i, 0).equals(path)) {
                model.removeRow(i);
                return;
            }
        }
    }

    public static void ZvisPB() {
        stavPB += 1;
        if (stavPB > 100) /* 152 */ {
            stavPB = 0;
        }
        progressBarr.setValue(stavPB);
    }

    public static void nastavPB(int Stav) {
        progressBarr.setValue(Stav);
    }
}
