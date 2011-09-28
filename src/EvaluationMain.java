import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 */

/**
 * Evaluation quick-hacked tool for Dehnadi programming evaluation test
 * @author Pavel Janecka, Karel Petranek
 */
public class EvaluationMain {

	/**
	 * Default constructor
	 */
	public EvaluationMain() {
		try {
			InputStream is = new FileInputStream(new File("data/sample_data.xlsx"));
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			Sheet results = workbook.getSheetAt(0); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args String
	 */
	public static void main(String[] args) {
		new EvaluationMain();
	}

}
