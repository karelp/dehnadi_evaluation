package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author KarelPetranek
 * Represents an Excel workbook
 */
public class ExcelWorkbook {
	private Workbook workbook;
	private HashMap<String, CellStyle> fontRegister = new HashMap<String, CellStyle>();
	
	/**
	 * Loads an Excel workbook from the given input stream
	 * @param is Input stream to load from
	 * @throws IOException when there are problems with reading the stream
	 */
	public ExcelWorkbook(InputStream is) throws IOException  {
		workbook = new XSSFWorkbook(is);
	}
	
	/**
	 * Loads an Excel workbook from the given file
	 * @param filePath Path to the Excel file
	 * @throws IOException when the file cannot be loaded
	 */
	public ExcelWorkbook(String filePath) throws IOException  {
		InputStream is = new FileInputStream(new File(filePath));
		workbook = new XSSFWorkbook(is);
		is.close();
	}
	
	/**
	 * Creates and register font for current workbook
	 * @param colorIndex {@link HSSFColor} index
	 * @param bold boolean flag bold style
	 * @param italic boolean flag italic style
	 * @param underline boolean flag underline style
	 * @param FontID String ID of font
	 * 
	 */
	public void registrFont(short colorIndex, boolean bold, boolean italic, boolean underline, String FontID) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setColor(colorIndex);
		if (bold)
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		if (italic)
			font.setItalic(true);
		if (underline)
			font.setUnderline(Font.U_SINGLE);
		style.setFont(font);
		fontRegister.put(FontID, style);
	}
	
	/**
	 * Return font for selected FontID or null if font with selected FontID is not registered in current workbook
	 * @param FontID String
	 * @return {@link CellStyle} instance
	 * @see #registrFont(short, boolean, boolean, boolean, String)
	 * @see ExcelCell#setFont(CellStyle font)
	 */
	public CellStyle getRegistredFont(String FontID) {
		return fontRegister.get(FontID);
	}
	
	/**
	 * Writes the workbook to the given output stream
	 * @param os The output stream to write the workbook to
	 * @throws IOException when the output stream cannot be written
	 */
	public void write(OutputStream os) throws IOException  {
		workbook.write(os);
	}
	
	/**
	 * Writes the workbook to the given file
	 * @param filePath Path to the output file
	 * @throws IOException if the file cannot be written
	 */
	public void write(String filePath) throws IOException  {
		OutputStream os = new FileOutputStream(new File(filePath));
		write(os);
		os.close();
	}
	
	/**
	 * Gets all sheets in the workbook
	 * @return List of all sheets
	 */
	public List<ExcelSheet> getSheets()  {
		List<ExcelSheet> result = new ArrayList<ExcelSheet>(workbook.getNumberOfSheets());
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)  {
			result.add(new ExcelSheet(workbook.getSheetAt(i)));
		}
		return result;
	}
	
	/**
	 * Gets all sheets in the workbook whose name matches the given regex pattern
	 * @param regex The pattern to match
	 * @return List of all matching workbooks, an empty list when no matches were found
	 */
	public List<ExcelSheet> getSheetsByPattern(String regex)  {
		List<ExcelSheet> result = new ArrayList<ExcelSheet>(workbook.getNumberOfSheets());
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)  {
			Sheet s = workbook.getSheetAt(i);
			if (s.getSheetName().matches(regex))
				result.add(new ExcelSheet(s));
		}
		return result;		
	}
	
	/**
	 * Returns an Excel sheet based on its name. If the sheet with the given name doesn't
	 * exist, the returned sheet is invalid (not null, so you can still work with the instance).
	 * @param name The name to look for
	 * @return Corresponding Excel sheet or an invalid sheet if such sheet doesn't exist
	 * @see ExcelSheet
	 */
	public ExcelSheet getSheetByName(String name)  {
		return new ExcelSheet(workbook.getSheet(name));
	}
}
