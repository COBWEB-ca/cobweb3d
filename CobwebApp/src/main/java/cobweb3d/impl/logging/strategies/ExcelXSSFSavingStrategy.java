package cobweb3d.impl.logging.strategies;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.logging.SavingStrategy;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class ExcelXSSFSavingStrategy implements SavingStrategy {

    @Override
    public int save(DataTable coreData, Collection<DataLoggingMutator> plugins, File file) {
        if (file == null) return 0;
        try {
            XSSFWorkbook workbook;
            try {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } catch (Exception ex2) {
                workbook = new XSSFWorkbook();
            }
            saveDataToExcelSheets(workbook, coreData, plugins);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed saving log to " + file.getAbsolutePath());
        }
        return 1;
    }

    public void saveDataToExcelSheets(XSSFWorkbook workbook, DataTable coreData, Collection<DataLoggingMutator> plugins) {
        if (coreData != null) saveDataToExcelSheet(workbook, "COBWEB base", coreData);
        for (DataLoggingMutator plugin : plugins) {
            int i = 1;
            for (DataTable dataTable : plugin.getTables()) {
                if (i >= 2) saveDataToExcelSheet(workbook, plugin.getName() + " " + i, dataTable);
                else saveDataToExcelSheet(workbook, plugin.getName(), dataTable);
                i++;
            }
        }
    }

    public void saveDataToExcelSheet(XSSFWorkbook workbook, String title, DataTable table) {
        XSSFSheet sheet = workbook.getSheet(title);
        if (sheet == null) sheet = workbook.createSheet(title);
        XSSFRow headingRow = sheet.getRow(0);
        if (headingRow == null) headingRow = sheet.createRow(0);
        for (int i : table.columnInts.keySet()) {
            headingRow.createCell(i).setCellValue(table.columnInts.get(i));
        }

        Set<Integer> keyset = new TreeSet<>(table.rowMap.keySet());
        for (int r : keyset) { //int r = 0; r < table.rowMap.size(); r++) {
            DataTable.SmartLogRow logRow = table.rowMap.get(r);
            if (logRow != null) {
                XSSFRow row = sheet.createRow(1 + r);
                for (int c : logRow.cells.keySet()) {
                    Object val = logRow.cells.get(c).value;
                    if (val instanceof Float)
                        row.createCell(c).setCellValue((Float) val);
                    else if (val instanceof Integer)
                        row.createCell(c).setCellValue((Integer) val);
                    else if (val instanceof String)
                        row.createCell(c).setCellValue((String) val);
                    else if (val instanceof Long)
                        row.createCell(c).setCellValue((Long) val);
                    else if (val instanceof Double)
                        row.createCell(c).setCellValue((Double) val);
                    else if (val instanceof Byte)
                        row.createCell(c).setCellValue((Byte) val);
                    else if (val instanceof Short)
                        row.createCell(c).setCellValue((Short) val);
                    else if (val instanceof Character)
                        row.createCell(c).setCellValue((Character) val);
                    else row.createCell(c).setCellValue(val.toString());
                }
            }
        }
        keyset.clear();
    }
}