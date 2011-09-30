package excel;

import java.util.ArrayList;

/**
 * @author KarelPetranek
 * Represents an Excel row
 */
public class ExcelRow extends ArrayList<ExcelCell> {
	private int index;
	
	/**
	 * Creates an empty row with the given index
	 * @param index Row index
	 */
	public ExcelRow(int index)  {
		super();
		this.index = index;
	}
	
	/**
	 * Creates an empty row with the given index and reserves the given cell count in memory.
	 * @param index Row index
	 * @param count Size to reserve
	 */
	public ExcelRow(int index, int count)  {
		super(count);
		this.index = index;
	}
	
	/**
	 * Index of this row (zero-based)
	 * @return Row index
	 */
	public int getIndex()  {
		return index;
	}
	
	@Override
	public void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
	}	
}
