import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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

	private final String FILE_INPUT_NAME = "data/sample_data.xlsx";
	private final String FILE_OUTPUT_NAME = "data/sample_data_.xlsx";
	private final String SHEET_NAME_REGEXP = "Q\\d*";
	private final String QUESTION_NUMBER_CHAR = "#";
	private final int ANSWER_INDEX = 0;
	private final int MODEL_INDEX = 1;
	private final String RESULTS_SHEET_NAME = "Results";
	private final int RESULT_CORNER_INDEX = 0;
	private final String RESULT_CORNER_STRING = "Code";
	private final String RESULT_CELL_STRING = "Results";
	
	private CellStyle[] cellStyles = new CellStyle[2];
	
	/**
	 * Default constructor
	 */
	public EvaluationMain() {
		try {
			Workbook workbook = openWorkbook(FILE_INPUT_NAME);
			int[] evals = resolveAnswersAndCountEvaluation(workbook, loadQuestions(workbook));
			createCellStyles(workbook);
			writeEvalsToFile(FILE_OUTPUT_NAME, workbook, evals);
			System.out.println("Done!");
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
		Workbook workbook = new XSSFWorkbook(is);
		is.close();
		return workbook;
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
					tmpAnswers = tmpSheet.getRow(j).getCell(ANSWER_INDEX).getStringCellValue();
					tmpModels = tmpSheet.getRow(j).getCell(MODEL_INDEX).getStringCellValue();
					answers.add(new ModelsAndAnswer(new Answer(tmpAnswers, tmpQID), Model.parseModels(tmpModels)));
				}
				questions.add(new Question(tmpQID, answers));
			}
		}
		return questions;
	}
	
	private int[] resolveAnswersAndCountEvaluation(Workbook workbook, List<Question> questions) {
		Sheet resultsSheet = workbook.getSheet(RESULTS_SHEET_NAME);
		Row firstRow = resultsSheet.getRow(0);
		if(!firstRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().startsWith(RESULT_CORNER_STRING)) {
			System.out.println("First row of result list does not contain \"" + RESULT_CORNER_STRING + "\" on column num " + RESULT_CORNER_INDEX);
			return null;
		}
		Evaluation evaluation = null;
		List<Answer> answers = null;
		int[] evals = new int[firstRow.getPhysicalNumberOfCells() - 1];
		for(int i = 1; i < firstRow.getPhysicalNumberOfCells(); i++) {
			evaluation = new Evaluation();
			answers = getAnswersInColumn(resultsSheet, i, 1);
			if(answers != null) {
				evals[i - 1] = evaluation.evaluate(questions, answers);
			} else {
				System.out.println("Answers are null!!");
				return null;
			}
		}
		return evals;
	}
	
	private List<Answer> getAnswersInColumn(Sheet sheet, int columnIdx, int headerOffset) {
		List<Answer> values = new ArrayList<Answer>(sheet.getPhysicalNumberOfRows());
		Row tmpRow = null;
		Cell tmpCell = null;
		String tmpString = null;
		int tmpUID = 0;
		for(int i = headerOffset; i < sheet.getPhysicalNumberOfRows(); i++) {
			tmpRow = sheet.getRow(i);
			if(tmpRow == null) continue;
			if(tmpRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().isEmpty()) break;
			if(tmpRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().startsWith(RESULT_CELL_STRING)) continue;
			if(!tmpRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().startsWith(QUESTION_NUMBER_CHAR)) {
				System.out.println("Unrecognizable question number on line " + tmpRow.getRowNum());
				return null;
			}
			tmpCell = tmpRow.getCell(columnIdx);
			tmpString = tmpRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().replace(QUESTION_NUMBER_CHAR, "");
			tmpUID = Integer.valueOf(tmpString);
			if(tmpCell == null) {
				values.add(new Answer("", tmpUID));
			} else {
				values.add(new Answer(tmpRow.getCell(columnIdx).getStringCellValue(), tmpUID));
			}
		}
		return values;
	}

	private void createCellStyles(Workbook workbook) {
		cellStyles[0] = workbook.createCellStyle();
		Font tmpFont = workbook.createFont();
		tmpFont.setColor(HSSFColor.AUTOMATIC.index);
		cellStyles[0].setFont(tmpFont);
		cellStyles[1] = workbook.createCellStyle();
		tmpFont = workbook.createFont();
		tmpFont.setColor(HSSFColor.RED.index);
		cellStyles[1].setFont(tmpFont);
	}
	
	private void writeEvalsToFile(String path, Workbook workbook, int[] evals) throws IOException {
		Sheet resultsSheet = workbook.getSheet(RESULTS_SHEET_NAME);
		int resultRowNum = getResultRowNum(resultsSheet);
		if(resultRowNum == -1) {
			System.out.println("Cannot find result row in result sheet!");
			return;
		}
		if(resultsSheet.getRow(resultRowNum) == null) resultsSheet.createRow(resultRowNum);
		Row firstRow = resultsSheet.getRow(0);
		if(!firstRow.getCell(RESULT_CORNER_INDEX).getStringCellValue().startsWith(RESULT_CORNER_STRING)) {
			System.out.println("First row of result list does not contain \"" + RESULT_CORNER_STRING + "\" on column num " + RESULT_CORNER_INDEX);
			return;
		}
		Cell tmpCell = null;
		Cell resultCell = null;
		for(int i = RESULT_CORNER_INDEX + 1; i < firstRow.getPhysicalNumberOfCells() && i <= evals.length; i++) {
			tmpCell = firstRow.getCell(i);
			if(tmpCell != null && !tmpCell.getStringCellValue().isEmpty()) {
				resultCell = resultsSheet.getRow(resultRowNum).createCell(i, Cell.CELL_TYPE_STRING);
				if(evals[i - 1] < 8) resultCell.setCellStyle(cellStyles[0]);
				else resultCell.setCellStyle(cellStyles[1]);
				resultCell.setCellValue(evals[i - 1]);
			} else {
				System.out.println("Warning: student name cell null or empty! cell coord [" + 0 + ", " + i + "]");
			}
		}
		FileOutputStream os = new FileOutputStream(new File(path));
		workbook.write(os);
		os.close();
	}
	
	private int getResultRowNum(Sheet resultsSheet) {
		for(Row row : resultsSheet) {
			if(row.getCell(RESULT_CORNER_INDEX).getStringCellValue().equalsIgnoreCase(RESULT_CELL_STRING)) return row.getRowNum();
		}
		return -1;
	}

	/**
	 * @param args String
	 */
	public static void main(String[] args) {
		new EvaluationMain();
	}

}
