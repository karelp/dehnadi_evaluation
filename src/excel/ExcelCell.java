package excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * @author KarelPetranek
 * Represents a cell in an Excel document
 */
public class ExcelCell {
	Cell cell;
	
	/**
	 * Create an empty cell
	 */
	public ExcelCell()  {
		cell = null;
	}
	
	/**
	 * Create a cell from the given POI cell
	 * @param cell POI cell
	 */
	public ExcelCell(Cell cell)  {
		this.cell = cell;
	}
	
	/**
	 * Returns the cell content represented as string. If the cell is not of string type,
	 * it is converted automatically. Never returns null, check for an empty string instead.
	 * @return String content of the cell
	 */
	public String asString()  {
		if (cell == null)
			return "";
		
		String res = "";
		switch (cell.getCellType())  {
		case Cell.CELL_TYPE_BOOLEAN:
			res = Boolean.toString(cell.getBooleanCellValue());
		break;
		case Cell.CELL_TYPE_NUMERIC:
			res = Double.toString(cell.getNumericCellValue());
		break;
		case Cell.CELL_TYPE_STRING:
			res = cell.getStringCellValue();
		break;
		}
		
		return res;		
	}
	
	/**
	 * Interprets the cell as a number. If the cell cannot be interpreted as
	 * a number (either is not numeric or its string value is not convertible to double), 0 is returned.
	 * @return Numeric representation of the cell
	 */
	public double asNumber()  {
		if (cell == null)
			return 0;
		
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			return cell.getNumericCellValue();
		if (cell.getCellType() == Cell.CELL_TYPE_STRING)  {
			try {
				return Double.valueOf(cell.getStringCellValue());
			} catch (NumberFormatException e) { }
		}
		return 0;
	}
	
	/**
	 * Returns column index of the cell (x coordinate)
	 * @return Column index
	 */
	public int getX()  {
		return cell == null ? 0 : cell.getColumnIndex();
	}
	
	/**
	 * Returns row index of the cell (y coordinate)
	 * @return Row index
	 */
	public int getY()  {
		return cell == null ? 0 : cell.getRowIndex();
	}
	
	/**
	 * Returns true if the cell is blank
	 * @return True if the cell is blank
	 */
	public boolean isEmpty()  {
		return cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK;
	}
	
	/**
	 * Sets cell font properties
	 * @param color Font color
	 * @param bold Specifies if the font should be bold
	 * @param italic Specifies if the font should be italic
	 * @param underline Specifies if the font should be underlined
	 */
	public void setFont(HSSFColor color, boolean bold, boolean italic, boolean underline)  {
		// TODO: this creates a new style for each cell (doesn't reuse styles)
		CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
		Font font = cell.getSheet().getWorkbook().createFont();
		font.setColor(color.getIndex());
		if (bold)
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		if (italic)
			font.setItalic(true);
		if (underline)
			font.setUnderline(Font.U_SINGLE);
		style.setFont(font);
		cell.setCellStyle(style);
	}
	
	/**
	 * Sets cell value to the given string
	 * @param text The text to set
	 */
	public void setValue(String text)  {
		cell.setCellValue(text == null ? "" : text);
	}
	
	/**
	 * Sets cell value to the given integer
	 * @param num The integer to set
	 */
	public void setValue(int num)  {
		cell.setCellValue(num);
	}
	
	/**
	 * Sets cell value to the given number
	 * @param num The number to set
	 */
	public void setValue(double num)  {
		cell.setCellValue(num);
	}
	
	@Override
	public String toString()  {
		return asString();
	}
}
