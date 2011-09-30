package excel;

import java.util.ArrayList;

/**
 * @author KarelPetranek
 * Represents an Excel cell column
 */
public class ExcelColumn extends ArrayList<ExcelCell> {
	private int index;
	
	/**
	 * Creates an empty column
	 * @param index Column index
	 */
	public ExcelColumn(int index)  {
		super();
		this.index = index;
	}
	
	/**
	 * Creates an empty column and reserves the given cell count in memory.
	 * @param index Column index
	 * @param count Size to reserve
	 */
	public ExcelColumn(int index, int count)  {
		super(count);
		this.index = index;
	}
	
	/**
	 * Index of this column (zero-based)
	 * @return Column index
	 */
	public int getIndex()  {
		return index;
	}
	
	@Override
	public void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}

	@Override
	public String toString() {
		return "ExcelColumn [index=" + index + ", cells=" + super.toString() + " ]";
	}
	
}
