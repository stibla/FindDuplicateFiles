package com.stibla.findduplicatefiles;

import java.io.File;

public class RunFindDuplicateFiles implements Runnable {

    @Override
    public void run() {
        enumerateFileDir(MainForm.filterText.getText());

        for (int i = 0; i < MainForm.TbGetRowCount(); i++) {
            String compareHash = (String) MainForm.TbGetValue(i, 1);
            for (int j = i + 1; j < MainForm.TbGetRowCount(); j++) {
                if (compareHash.equals((String) MainForm.TbGetValue(j, 1))) {
                    MainForm.TbSetValue("Duplicita", j, 3);
                    MainForm.TbSetValue("Duplicita", i, 3);
                }
            }
            MainForm.ZvisPB();
        }

        for (int i = MainForm.TbGetRowCount() - 1; i >= 0; i--) {
            if (MainForm.TbGetValue(i, 3) == null) {
                MainForm.TbRemoveRow(i);
            }
            MainForm.ZvisPB();
        }

        MainForm.nastavPB(100);
    }

    public static void enumerateFileDir(String pathdir) {
        File path = new File(pathdir);

        String[] list = path.list();
        int ilenght;
        try {
            ilenght = list.length;
        } catch (Exception e) {
            System.out.println("Chyba ilenght = list.length;");
            ilenght = 0;
        }
        for (int i = 0; i < ilenght; i++) {
            File inPath = new File(path.getAbsolutePath() + "\\" + list[i]);
            if (inPath.isDirectory()) {
                enumerateFileDir(inPath.getAbsolutePath() + "\\");
            }
            if (inPath.isFile()) {
                Object[] row = new Object[3];
                row[0] = inPath.getAbsolutePath();
                try {
                    row[1] = FileChecksum.getMD5Checksum(inPath.getAbsolutePath());
                } catch (Exception e) {
                    row[1] = "Nezistene";
                    System.out.println("row[1] = FileChecksum.getMD5Checksum(inPath.getAbsolutePath());");
                }
                row[2] = inPath.length();
                if (!row[1].equals("Nezistene"))  {
                    MainForm.TbAddRow(row);
                }
            }
            MainForm.ZvisPB();
        }
    }
}
