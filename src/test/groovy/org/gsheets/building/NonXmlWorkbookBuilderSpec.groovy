package org.gsheets.building

import org.apache.poi.ss.usermodel.Workbook

class NonXmlWorkbookBuilderSpec extends WorkbookBuilderSpec {

	protected newBuilder() { new WorkbookBuilder(false) }
	
	static void main(String[] args) {
		String filename = args.length ? args[0] : 'demo.xls'
		file filename, new NonXmlWorkbookBuilderSpec().newBuilder()
	}
	
}
