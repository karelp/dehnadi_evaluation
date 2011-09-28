import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 */

/**
 * Evaluation quick-hacked tool for Dehnadi programming evaluation test
 * @author Pavel Janecka, Karel Petranek
 */
public class EvaluationMain {

	private final String FILE_NAME = "data/sample_data.xlsx";
	private final String SHEET_NAME_REGEXP = "Q\\d*";
	private final String QUESTION_NUMBER_CHAR = "#";
	private final int answerIndex = 0;
	private final int modelIndex = 1;
	
	/**
	 * Default constructor
	 */
	public EvaluationMain() {
		try {
			Workbook workbook = openWorkbook(FILE_NAME);
			loadQuestions(workbook);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open {@link Workbook} on selected path (if possible)
	 * @param filePath String path of workbook
	 * @return {@link Workbook} instance
	 * @throws IOException
	 */
	private Workbook openWorkbook(String filePath) throws IOException {
		InputStream is = new FileInputStream(new File(filePath));
		return new XSSFWorkbook(is); 
	}
	
	/**
	 * Loads answers from selected {@link Workbook} instance
	 * @param workbook {@link Workbook} instance
	 * @return {@link List} of {@link Question} instances
	 */
	private List<Question> loadQuestions(Workbook workbook) {
		List<Question> questions = new ArrayList<Question>();
		List<ModelsAndAnswer> answers = null;
		Sheet tmpSheet = null;
		Row tmpRow = null;
		String tmpString = null;
		String tmpAnswers = null;
		String tmpModels = null;
		int tmpQID = 0;
		for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
			tmpSheet = workbook.getSheetAt(i);
			if(tmpSheet.getSheetName().matches(SHEET_NAME_REGEXP)) {
				tmpRow = tmpSheet.getRow(0);
				if(tmpRow == null) {
					System.out.println("Sheet " + tmpSheet.getSheetName() + " does not have 0th row");
					continue;
				}
				tmpString = tmpRow.getCell(0).getStringCellValue();
				if(!tmpString.startsWith(QUESTION_NUMBER_CHAR)) {
					System.out.println("Question number have incompatible value: " + tmpString);
					continue;
				}
				tmpString = tmpString.replace(QUESTION_NUMBER_CHAR, "");
				tmpQID = Integer.valueOf(tmpString);
				answers = new ArrayList<ModelsAndAnswer>();
				for(int j = 1; j < tmpSheet.getPhysicalNumberOfRows(); j++) {
					tmpAnswers = tmpSheet.getRow(j).getCell(answerIndex).getStringCellValue();
					tmpModels = tmpSheet.getRow(j).getCell(modelIndex).getStringCellValue();
					answers.add(new ModelsAndAnswer(new Answer(tmpAnswers, tmpQID), Model.parseModels(tmpModels)));
				}
				questions.add(new Question(tmpQID, answers));
			}
		}
		return questions;
	}
	
	/**
	 * @param args String
	 */
	public static void main(String[] args) {
		new EvaluationMain();
	}

}
