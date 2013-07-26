package org.gsheets.parsing

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.gsheets.NonXmlWorkbookSupport
import org.gsheets.WorkbookSupport
import org.gsheets.XmlWorkbookSupport

class WorkbookParser {

	private final WorkbookSupport support
	
	Workbook workbook
	
	int rows
	
	int startRowIndex
	
	Map columns = [:]
	
	WorkbookParser(boolean xml) {
		support = xml ? new XmlWorkbookSupport() : new NonXmlWorkbookSupport()
	}
	
	List<Map> grid(Workbook workbook, Closure closure) {
		assert workbook
		assert closure
		
		this.workbook = workbook
		closure.delegate = this
		closure.call()
		
		data(workbook)
	}
	
	void header(int rows) { startRowIndex = rows }
	
	void columns(Map columns) { this.columns = columns }
	
	private List<Map> data(Workbook workbook) {
		List data = []
		Sheet sheet = workbook.getSheetAt(0)
		rows.times {
			Row row = sheet.getRow(it + startRowIndex)
			Map rowData = rowData(row)
			data << rowData
		}
		data
	}
	
	private Map rowData(Row row) {
		Map data = [:]
		columns.eachWithIndex { name, type, index ->
			Cell cell = row.getCell(index)
			def cellData
			if (type == String) { cellData = cellAsString(cell) }
			else if (type == BigDecimal) { cellData = cellAsBigDecimal(cell) }
			else if (type == Double) { cellData = cellAsDouble(cell) }
			else if (type == Float) { cellData = cellAsFloat(cell) }
			else if (type == Long) { cellData = cellAsLong(cell) }
			else if (type == Integer) { cellData = cellAsInteger(cell) }
			else if (type == Boolean) { cellData = cellAsBoolean(cell) }
			else if (type == Date) { cellData = cellAsDate(cell) }
			data[name] = cellData
		}
		data
	}
	
	private String cellAsString(Cell cell) { cell.toString() }
	
	private Boolean cellAsBoolean(Cell cell) { cell.booleanCellValue }
	
	private BigDecimal cellAsBigDecimal(Cell cell) { new BigDecimal(cell.toString()) }

	private Double cellAsDouble(Cell cell) { cell.numericCellValue}
	
	private Float cellAsFloat(Cell cell) { cellAsDouble(cell).floatValue() }
	
	private Long cellAsLong(Cell cell) { cellAsBigDecimal(cell).longValue() }
	
	private Integer cellAsInteger(Cell cell) { cellAsBigDecimal(cell).intValue() }
	
	private Date cellAsDate(Cell cell) { cell.dateCellValue }
	
}
