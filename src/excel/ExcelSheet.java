package excel;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author KarelPetranek
 * Represents one sheet in Excel workbook
 */
public class ExcelSheet {
	Sheet sheet;
	
	/**
	 * Creates a sheet from the given POI sheet
	 * @param sheet The POI sheet
	 */
	public ExcelSheet(Sheet sheet)  {
		this.sheet = sheet;
	}
	
	/**
	 * String value of the cell at the given coordinates. If the cell is not of string type,
	 * a conversion is performed automatically. Never returns null.
	 * @param x Column
	 * @param y Row
	 * @return String representation of the given cell
	 */
	public String stringAt(int x, int y)  {
		return cellAt(x, y).asString();
	}
	
	/**
	 * Returns a cell at the given coordinates
	 * @param x Column
	 * @param y Row
	 * @return Cell at the given coordinates
	 */
	public ExcelCell cellAt(int x, int y)  {
		if (sheet == null)
			return new ExcelCell();
		
		Row row = sheet.getRow(y);
		if (row == null)
			return new ExcelCell();
		return new ExcelCell(row.getCell(x));
	}
	
	/**
	 * Returns row at the given index. The row contains only elements between the first and last non-blank cells.
	 * @param index Index of the row
	 * @return Row at the given index
	 */
	public ExcelRow getRow(int index)  {
		ExcelRow res = new ExcelRow(index);
		if (sheet == null)
			return res;
		
		Row row = sheet.getRow(index);
		if (row == null)
			return res;
		
		int lastNonEmptyCell = -1, i = 0;
		Iterator<Cell> iter = row.cellIterator();
		while (iter.hasNext())  {
			Cell c = iter.next();
			ExcelCell ec = new ExcelCell(c);
			if (!ec.isEmpty())
				lastNonEmptyCell = i;
			res.add(ec);
			i++;
		}
		
		res.removeRange(lastNonEmptyCell, res.size());
		
		return res;
	}
	
	/**
	 * Returns column at the given index. The column contains only elements between the first and last non-blank cells.
	 * @param index Column index
	 * @return Column at the given index
	 */
	public ExcelColumn getColumn(int index)  {
		ExcelColumn res = new ExcelColumn(index);
		if (sheet == null)
			return res;
		
		int lastNonEmptyCell = -1;
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++)  {
			ExcelCell c = cellAt(index, i);
			if (!c.isEmpty())
				lastNonEmptyCell = i;
		}
		
		res.removeRange(lastNonEmptyCell + 1, res.size());
		
		return res;		
	}
	
	/**
	 * Gets sheet name
	 * @return Sheet name
	 */
	public String getName()  {
		return sheet == null ? "" : sheet.getSheetName();
	}
	
	/**
	 * Returns true if the sheet actually exists in the workbook. Use this to check that the sheet
	 * returned by various find methods in ExcelWorkbook was found.
	 * @return True if the sheet is valid
	 */
	public boolean isValid()  {
		return sheet != null;
	}
	
	/**
	 * Returns true if this sheet is hidden or invalid
	 * @return True if this sheet is hidden or invalid
	 */
	public boolean isHidden()  {
		if (sheet == null)
			return true;
		return sheet.getWorkbook().isSheetHidden(sheet.getWorkbook().getSheetIndex(sheet));
	}
	
	@Override
	public String toString()  {
		return getName();
	}
}
