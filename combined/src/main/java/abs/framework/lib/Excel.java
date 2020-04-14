package abs.framework.lib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class has methods to interact with the excel files. Methods to create
 * connection, run queries and return responses and close connections
 *
 * @author Vaibhav Narkhede
 * @version 1.0
 * @since 2016-04-22
 */

public class Excel {

	private static Connection dbConnection = null;
	private static ResultSet resultSet = null;

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	public Excel(String path) throws Exception {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}

	}

	// public static LoggerManager loggerManger;

	/**
	 * Close all.
	 *
	 * @name closeAll
	 * @description
	 * @author
	 * @return void ||description:
	 * @throws SQLException
	 * @jiraId
	 */
	public void closeAll() throws SQLException {
		try {
			if (resultSet != null)
				resultSet.close();
			if (dbConnection != null)
				dbConnection.close();
		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * Read data.
	 *
	 * @name readData
	 * @description Creates connection with the Excel sheet name passed and runs the
	 *              query , returns the result set
	 * @author Vaibhav Narkhede
	 * @param filename ||description: String Name of the Excel sheet ||allowedRange:
	 * @param query    ||description: String query that needs to be run on the excel
	 *                 sheet ||allowedRange:
	 * @return ResultSet ||description: Result set of records returned by the Query
	 * @throws Exception
	 * @jiraId
	 */
	public ResultSet readData(String filename, String query) {
		if (dbConnection == null)
			dbConnection = getExcelConnect(filename);
		try {
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			resultSet = stmt.executeQuery(query);
			// c.close();

		} catch (Exception e) {
//			loggerManger.logger.info("Execption at readData(String filename, String query) in Excel.java:\n"+e.getMessage());
		}

		return resultSet;
	}

	/**
	 * Get excel connect.
	 *
	 * @name getExcelConnect
	 * @description Creates a connection to run queries on excel
	 * @author ShantanuJ
	 * @param filename ||description: String Name/Path of the excel file to connect
	 *                 ||allowedRange:
	 * @return Connection ||description: Connection object with the connection to
	 *         the file
	 * @throws Exception
	 * @jiraId
	 */
	public Connection getExcelConnect(String filename) {
		try {
			Class.forName("com.googlecode.sqlsheet.Driver");
			filename = filename.replace("\\", "/");
			dbConnection = DriverManager.getConnection("jdbc:xls:file:" + filename);
		} catch (Exception e) {
//			loggerManger.logger.info("Execption at getExcelConnect(String filename) in Excel.java:\n"+e.getMessage());
		}
		return dbConnection;
	}

	/**
	 * Excel read.
	 *
	 * @name excelRead
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as
	 *              List<Object[]>
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to read
	 *                   ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file
	 *                   to work ||allowedRange:
	 * @return List ||description: List<Object[]>, Object[] contains
	 *         LinkedHashMap<String,String>
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	public static List<Object[]> excelRead(String sExcelPath, String sSheetName) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<Object[]> DataList = new ArrayList();

		try {
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}
					DataList.add(new Object[] { RowData });
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
//			loggerManger.logger.info("Execption at excelRead(String sExcelPath, String sSheetName) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}

	/**
	 * F Excel read hash map.
	 *
	 * @name excelReadHashMap
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as
	 *              List<LinkedHashMap<"ColumnHeader", "RowValue">>
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to read
	 *                   ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file
	 *                   to work ||allowedRange:
	 * @return List ||description: List of LinkedHashMap<"ColumnHeader", "RowValue">
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	public static List<LinkedHashMap<String, String>> excelReadHashMap(String sExcelPath, String sSheetName) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<LinkedHashMap<String, String>> DataList = new ArrayList<>();
		try {
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;

			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}

			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}
					DataList.add(RowData);
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DataList;
	}

	/**
	 * Excel read.
	 *
	 * @name excelRead
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as
	 *              List<Obejct[]> based on the condition column & its value
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to
	 *                   connect ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file
	 *                   to work ||allowedRange:
	 * @param sCondCol   ||description: String Name of the column in the excel file
	 *                   to apply condition ||allowedRange:
	 * @param sCondVal   ||description: String Condition value to apply for valid
	 *                   test data ||allowedRange:
	 * @return List ||description: List of Object[], Object[] contains
	 *         HashMap<Column Header, Value>
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	public static List<Object[]> excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<Object[]> DataList = new ArrayList<>();
		FileInputStream oFis = null;
		Workbook workbook = null;

		try {
			oFis = new FileInputStream(sExcelPath);
			// Using XSSF for xlsx format, for xls use HSSF
			// workbook = new XSSFWorkbook(oFis);
			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}

				if (bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)) {
					workbook.close();
					throw new InvalidParameterException(
							"Condition column:" + sCondCol + "doesn't exist in the sheet:" + sSheetName);
				}

				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					RowData = new LinkedHashMap<String, String>();

					for (int i = 0; i < sHeaderKey.length; i++) {
						// RowData.put(sHeaderKey[i], sValue[i]);
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}
					if (RowData.get(sCondCol) != null
							&& RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						DataList.add(new Object[] { RowData });
					}
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
//			loggerManger.logger.info("Execption at excelRead(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}

	/**
	 * Excel read hash map.
	 *
	 * @name excelReadHashMap
	 * @description Reads data from an excel(.xlsx) sheet and returns the result as
	 *              List<LinkedHashMap<String,String>> based on the condition column
	 *              & its value
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to
	 *                   connect ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file
	 *                   to work ||allowedRange:
	 * @param sCondCol   ||description: String Name of the column in the excel file
	 *                   to apply condition ||allowedRange:
	 * @param sCondVal   ||description: String Condition value to apply for valid
	 *                   test data ||allowedRange:
	 * @return List ||description: List of LinkedHashMap<"ColumnHeader", "Value">
	 * @throws @jiraId
	 */
	public static List<LinkedHashMap<String, String>> excelReadHashMap(String sExcelPath, String sSheetName,
			String sCondCol, String sCondVal) {
		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		LinkedHashMap<String, String> RowData;
		List<LinkedHashMap<String, String>> DataList = new ArrayList<>();
		try {
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;
			// Using XSSF for xlsx format, for xls use HSSF
			// workbook = new XSSFWorkbook(oFis);

			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}

			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				sValue = new String[0];
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}
				if (bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)) {
					workbook.close();
					throw new InvalidParameterException(
							"Condition column:" + sCondCol + "doesn't exist in the sheet:" + sSheetName);
				}

				if ((sHeaderKey.length != 0) && (sValue.length != 0)) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.put(sHeaderKey[i], sValue[i]);
						} else {
							RowData.put(sHeaderKey[i], null);
						}
					}

					if (RowData.get(sCondCol) != null
							&& RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						DataList.add(RowData);
					}
				}
			}
			workbook.close();
			oFis.close();
		} catch (Exception e) {
//			loggerManger.logger.info("Execption at excelReadHashMap(String sExcelPath, String sSheetName, String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		}
		return DataList;
	}

	/**
	 * Update excel.
	 *
	 * @name updateExcel
	 * @description Updates the data to excel(.xlsx) sheet based on the condition
	 *              column & its value
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to
	 *                   connect ||allowedRange:
	 * @param sSheetName ||description: String Name of the sheet in the excel file
	 *                   to work ||allowedRange:
	 * @param sCondCol   ||description: String Name of the column in the excel file
	 *                   to apply condition ||allowedRange:
	 * @param sCondVal   ||description: String Condition value to apply for valid
	 *                   test data ||allowedRange:
	 * @param DataPair   ||description: LinkedHashMap<String,String> value to be
	 *                   updated in the sheet ||allowedRange:
	 * @return void ||description:
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	public static void updateExcel(String sExcelPath, String sSheetName, String sCondCol, String sCondVal,
			LinkedHashMap<String, String> DataPair) {

		String[] sHeaderKey = new String[0];
		try {
			FileInputStream oFis = new FileInputStream(sExcelPath);
			Workbook workbook = null;

			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);
			boolean bFlag = false;
			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					if (bFlag == true) {
						break;
					}

					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						String sCellVal;
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sCellVal = formatter.formatCellValue(cell);
						} else {
							sCellVal = "";
						}

						if ((sHeaderKey[cell.getColumnIndex()].equals(sCondCol)) && (sCellVal.equals(sCondVal))) {

							// Iterator<Cell> targetCellIterator = row.cellIterator();
							for (int i = 0; i < DataPair.size(); i++) {
								/*
								 * while (targetCellIterator.hasNext()) { Cell targetCell =
								 * targetCellIterator.next(); if
								 * (DataPair.get(sHeaderKey[targetCell.getColumnIndex()]) != null) {
								 * targetCell.setCellValue(DataPair.get(sHeaderKey[targetCell.getColumnIndex()])
								 * ); break; } }
								 */
								int j = 0;
								while (j < sHeaderKey.length) {
									// Cell targetCell = targetCellIterator.next();
									Cell cell1 = row.getCell(j, Row.RETURN_NULL_AND_BLANK);
									if (DataPair.get(sHeaderKey[j]) != null) {
										if (cell1 == null) {
											row.createCell(j, Cell.CELL_TYPE_STRING);
											cell1 = row.getCell(j, Row.RETURN_NULL_AND_BLANK);
											bFlag = true;
										}
										cell1.setCellValue(DataPair.get(sHeaderKey[j]));
										break;
									}
									j++;
								}

							}
						}

					}
				}

			}
			oFis.close();
			FileOutputStream oFos = new FileOutputStream(sExcelPath);
			workbook.write(oFos);
			workbook.close();
		} catch (Exception e) {
//			loggerManger.logger.info("Execption at updateExcel(String sExcelPath, String sSheetName, String sCondCol, String sCondVal, LinkedHashMap<String, String> DataPair) in Excel.java:\n"+e.getMessage());
		}
	}

	/**
	 * Excel read multiple sheets.
	 *
	 * @name excelReadMultipleSheets
	 * @description Reads the data from multiple sheets in XLSX excel and return
	 *              them as a List based on the condition column & primary column
	 *              reference, primary column should be configured in the
	 *              Config.properties file using the property
	 *              "env.data.primarycolumn".
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to
	 *                   connect ||allowedRange:
	 * @param sSheetName ||description: Array of String, Excel Sheet Names
	 *                   ||allowedRange:
	 * @param sCondCol   ||description: String Name of the column in the excel file
	 *                   to apply condition ||allowedRange:
	 * @param sCondVal   ||description: String Condition value to apply for valid
	 *                   test data ||allowedRange:
	 * @return List ||description: List of Object[], Object[] contains
	 *         LinkedHashMap<ColumHeader, RowValue>
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	@SuppressWarnings("resource")
	public static List<Object[]> excelReadMultipleSheets(String sExcelPath, String sSheetName[], String sCondCol,
			String sCondVal) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		ArrayList<Object[]> DataList;
		String Col_Name = null;
//		Col_Name = ConfigUtil.config.get("env.data.primarycolumn");
		// Col_Name = "SR #";

		LinkedHashMap<String, String> RowData = null;
		Row DataRow = null;
		int primaryColumnIndex = 0;
		DataList = new ArrayList<Object[]>();
		FileInputStream oFis = null;
		Workbook workbook = null;

		try {
			oFis = new FileInputStream(sExcelPath);
			// workbook = new XSSFWorkbook(oFis);

			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}

			Sheet sheet = workbook.getSheet(sSheetName[0]);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				sValue = new String[0];

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}

				if (bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)) {
					throw new InvalidParameterException(
							"Condition column:" + sCondCol + "doesn't exist in the sheet:" + sSheetName[0]);
				}

				if (sValue.length == sHeaderKey.length && row.getRowNum() != 0) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.putIfAbsent(sHeaderKey[i], sValue[i]);
						} else {
							RowData.putIfAbsent(sHeaderKey[i], null);
						}
					}

					if (RowData.get(sCondCol) != null
							&& RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						if (sSheetName.length == 1) {
							DataList.add(new Object[] { RowData });
						} else if (Col_Name.isEmpty()) {
							throw new InvalidParameterException(
									"Config parameter env.data.primarycolumn can't be empty");
						}

						// Check child sheets and add data to RowData
						int sheetCount;
						for (sheetCount = 1; sheetCount < sSheetName.length; sheetCount++) {
							Sheet sheet1 = workbook.getSheet(sSheetName[sheetCount]);

							// Get the primary Column Index in the child sheet
							Iterator<Row> rowIteratorHeader = sheet1.iterator();
							if (rowIteratorHeader.hasNext()) {
								Row childSheetRow = rowIteratorHeader.next();
								Iterator<Cell> childSheetCellIterator = childSheetRow.cellIterator();
								primaryColumnIndex = 0;

								while (childSheetCellIterator.hasNext()) {
									Cell cell = childSheetCellIterator.next();
									cell.setCellType(Cell.CELL_TYPE_STRING);
									if (cell.getStringCellValue().toString().equals(Col_Name)) {
										primaryColumnIndex = cell.getColumnIndex() + 1;
										break;
									}
								}
							} else {
								System.out.println("No valid data found in sheet:" + sheet1.getSheetName());
								// Adding the data from multiple sheets into DataList
								if (sheetCount == sSheetName.length - 1) {
									DataList.add(new Object[] { RowData });
								}
								continue;
							}

							if (primaryColumnIndex != 0) {
								// Get the row number of the data
								int rowCount = sheet1.getLastRowNum();
								DataRow = null;
								for (int i = 0; i <= rowCount; i++) {
									Cell cell;
									cell = sheet1.getRow(i).getCell(primaryColumnIndex - 1);
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String CellData = cell.getStringCellValue().toString();
									if (CellData.equalsIgnoreCase(RowData.get(Col_Name))) {
										DataRow = sheet1.getRow(i);
										break;
									}
								}

								if (DataRow != null) {
									// Read the child sheet row data
									String[] sChildHeaderKey = new String[0];
									String[] sChildValue = new String[0];
									Cell cell;
									Row childRow = sheet1.getRow(0);
									Iterator<Cell> headerRow = childRow.iterator();
									Iterator<Cell> dataRowCellIterator = DataRow.cellIterator();

									while (headerRow.hasNext()) {
										cell = headerRow.next();
										if ((cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
											sChildHeaderKey = Arrays.copyOf(sChildHeaderKey,
													sChildHeaderKey.length + 1);
											if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
												sChildHeaderKey[cell.getColumnIndex()] = formatter
														.formatCellValue(cell);
											} else {
												sChildHeaderKey[cell.getColumnIndex()] = "";
											}
										}
									}

									while (dataRowCellIterator.hasNext()) {
										cell = dataRowCellIterator.next();
										if ((sChildHeaderKey[cell.getColumnIndex()] != null)) {
											sChildValue = Arrays.copyOf(sChildValue, sChildValue.length + 1);
											if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
												sChildValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else {
												sChildValue[cell.getColumnIndex()] = "";
											}
										}
									}
									if (sChildValue.length == sChildHeaderKey.length) {
										for (int i = 0; i < sChildHeaderKey.length; i++) {
											if (i < sChildValue.length) {
												RowData.putIfAbsent(sChildHeaderKey[i], sChildValue[i]);
											} else {
												RowData.putIfAbsent(sChildHeaderKey[i], null);
											}
										}
									} else {
										System.out.println(
												"Mismatch found between no. of row values and header values in Sheet:"
														+ sheet1.getSheetName() + ", for the iteration:"
														+ RowData.get(Col_Name) + ", No. of Headers: "
														+ sChildHeaderKey.length + ", No. of Values:"
														+ sChildValue.length);
									}

								} else {
									System.out.println("Unable to find a valid data row in sheet:"
											+ sheet1.getSheetName() + ", for the iteration:" + RowData.get(Col_Name));
								}

							} else {
								System.out.println("Unable to find primary column in sheet:" + sheet1.getSheetName());
							}

							// Adding the data from multiple sheets into DataList
							if (sheetCount == sSheetName.length - 1) {
								DataList.add(new Object[] { RowData });
							}
						}

					}

				} else if (row.getRowNum() != 0) {
					System.out.println("Mismatch found between no. of row values and header values in Sheet:"
							+ sSheetName[0] + ", for the row:" + row.getRowNum() + ", No. of Headers: "
							+ sHeaderKey.length + ", No. of Values:" + sValue.length);
				}
			}

			workbook.close();
			oFis.close();
		} catch (Exception e) {

//		loggerManger.logger.info("Execption at excelReadMultipleSheets(String sExcelPath, String sSheetName[], String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());
		}

		return DataList;
	}

	/**
	 * Excel read multi sheets hash map.
	 *
	 * @name excelReadMultiSheetsHashMap
	 * @description Reads the data from multiple sheets in XLSX excel and return
	 *              them as a List<LinkedHashMap<String,String>> based on the
	 *              condition column & primary column reference, primary column
	 *              should be configured in the Config.properties file using the
	 *              property "env.data.primarycolumn".
	 * @author Vaibhav Narkhede
	 * @param sExcelPath ||description: String Name/Path of the excel file to
	 *                   connect ||allowedRange:
	 * @param sSheetName ||description: Array of String, Excel Sheet Names
	 *                   ||allowedRange:
	 * @param sCondCol   ||description: String Name of the column in the excel file
	 *                   to apply condition ||allowedRange:
	 * @param sCondVal   ||description: String Condition value to apply for valid
	 *                   test data ||allowedRange:
	 * @return List ||description: List of LinkedHashMap<String,String>, HashMap
	 *         contains <ColumHeader, RowValue>
	 * @throws Exception ||type: exception
	 * @jiraId
	 */
	@SuppressWarnings("resource")
	public static List<LinkedHashMap<String, String>> excelReadMultiSheetsHashMap(String sExcelPath,
			String sSheetName[], String sCondCol, String sCondVal) {

		String[] sHeaderKey = new String[0];
		String[] sValue = new String[0];
		ArrayList<LinkedHashMap<String, String>> DataList;
		String Col_Name = null;
/////////////////	Col_Name = ConfigUtil.config.get("env.data.primarycolumn");
		// Col_Name = "SR #";

		LinkedHashMap<String, String> RowData = null;
		Row DataRow = null;
		int primaryColumnIndex = 0;
		DataList = new ArrayList<LinkedHashMap<String, String>>();
		FileInputStream oFis = null;
		Workbook workbook = null;

		try {
			oFis = new FileInputStream(sExcelPath);
			// workbook = new XSSFWorkbook(oFis);

			if (sExcelPath.contains(".xlsx")) {
				workbook = new XSSFWorkbook(oFis);
			} else {
				workbook = new HSSFWorkbook(oFis);
			}
			Sheet sheet = workbook.getSheet(sSheetName[0]);
			Iterator<Row> rowIterator = sheet.iterator();
			DataFormatter formatter = new DataFormatter(Locale.US);

			while (rowIterator.hasNext()) {
				Boolean bHeaderRow = false;
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					bHeaderRow = true;
				}
				sValue = new String[0];

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (bHeaderRow && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
						sHeaderKey = Arrays.copyOf(sHeaderKey, sHeaderKey.length + 1);
						sHeaderKey[cell.getColumnIndex()] = formatter.formatCellValue(cell);
					} else if ((!bHeaderRow) && (sHeaderKey[cell.getColumnIndex()] != null)) {
						sValue = Arrays.copyOf(sValue, cell.getColumnIndex() + 1);
						if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							sValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
						} else {
							sValue[cell.getColumnIndex()] = null;
						}
					}
				}

				if (bHeaderRow && !Arrays.asList(sHeaderKey).contains(sCondCol)) {
					throw new InvalidParameterException(
							"Condition column:" + sCondCol + "doesn't exist in the sheet:" + sSheetName[0]);
				}

				if (sValue.length == sHeaderKey.length && row.getRowNum() != 0) {
					RowData = new LinkedHashMap<String, String>();
					for (int i = 0; i < sHeaderKey.length; i++) {
						if (i < sValue.length) {
							RowData.putIfAbsent(sHeaderKey[i], sValue[i]);
						} else {
							RowData.putIfAbsent(sHeaderKey[i], null);
						}
					}

					if (RowData.get(sCondCol) != null
							&& RowData.get(sCondCol).trim().toLowerCase().equals(sCondVal.trim().toLowerCase())) {
						if (sSheetName.length == 1) {
							DataList.add(RowData);
						} else if (Col_Name.isEmpty()) {
							throw new InvalidParameterException(
									"Config parameter env.data.primarycolumn can't be empty");
						}

						// Check child sheets and add data to RowData
						int sheetCount;
						for (sheetCount = 1; sheetCount < sSheetName.length; sheetCount++) {
							Sheet sheet1 = workbook.getSheet(sSheetName[sheetCount]);

							// Get the primary Column Index in the child sheet
							Iterator<Row> rowIteratorHeader = sheet1.iterator();
							if (rowIteratorHeader.hasNext()) {
								Row childSheetRow = rowIteratorHeader.next();
								Iterator<Cell> childSheetCellIterator = childSheetRow.cellIterator();
								primaryColumnIndex = 0;

								while (childSheetCellIterator.hasNext()) {
									Cell cell = childSheetCellIterator.next();
									cell.setCellType(Cell.CELL_TYPE_STRING);
									if (cell.getStringCellValue().toString().equals(Col_Name)) {
										primaryColumnIndex = cell.getColumnIndex() + 1;
										break;
									}
								}
							} else {
								System.out.println("No valid data found in sheet:" + sheet1.getSheetName());
								// Adding the data from multiple sheets into DataList
								if (sheetCount == sSheetName.length - 1) {
									DataList.add(RowData);
								}
								continue;
							}

							if (primaryColumnIndex != 0) {
								// Get the row number of the data
								int rowCount = sheet1.getLastRowNum();
								DataRow = null;
								for (int i = 0; i <= rowCount; i++) {
									Cell cell;
									cell = sheet1.getRow(i).getCell(primaryColumnIndex - 1);
									cell.setCellType(Cell.CELL_TYPE_STRING);
									String CellData = cell.getStringCellValue().toString();
									if (CellData.equalsIgnoreCase(RowData.get(Col_Name))) {
										DataRow = sheet1.getRow(i);
										break;
									}
								}

								if (DataRow != null) {
									// Read the child sheet row data
									String[] sChildHeaderKey = new String[0];
									String[] sChildValue = new String[0];
									Cell cell;
									Row childRow = sheet1.getRow(0);
									Iterator<Cell> headerRow = childRow.iterator();
									Iterator<Cell> dataRowCellIterator = DataRow.cellIterator();

									while (headerRow.hasNext()) {
										cell = headerRow.next();
										if ((cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
											sChildHeaderKey = Arrays.copyOf(sChildHeaderKey,
													sChildHeaderKey.length + 1);
											if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
												sChildHeaderKey[cell.getColumnIndex()] = formatter
														.formatCellValue(cell);
											} else {
												sChildHeaderKey[cell.getColumnIndex()] = "";
											}
										}
									}

									while (dataRowCellIterator.hasNext()) {
										cell = dataRowCellIterator.next();
										if ((sChildHeaderKey[cell.getColumnIndex()] != null)) {
											sChildValue = Arrays.copyOf(sChildValue, sChildValue.length + 1);
											if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
												sChildValue[cell.getColumnIndex()] = formatter.formatCellValue(cell);
											} else {
												sChildValue[cell.getColumnIndex()] = "";
											}
										}
									}

									if (sChildValue.length == sChildHeaderKey.length) {
										for (int i = 0; i < sChildHeaderKey.length; i++) {
											if (i < sChildValue.length) {
												RowData.putIfAbsent(sChildHeaderKey[i], sChildValue[i]);
											} else {
												RowData.putIfAbsent(sChildHeaderKey[i], null);
											}
										}
									} else {
										System.out.println(
												"Mismatch found between no. of row values and header values in Sheet:"
														+ sheet1.getSheetName() + ", for the iteration:"
														+ RowData.get(Col_Name) + ", No. of Headers: "
														+ sChildHeaderKey.length + ", No. of Values:"
														+ sChildValue.length);
									}

								} else {
									System.out.println("Unable to find a valid data row in sheet:"
											+ sheet1.getSheetName() + ", for the iteration:" + RowData.get(Col_Name));
								}

							} else {
								System.out.println("Unable to find primary column in sheet:" + sheet1.getSheetName());
							}

							// Adding the data from multiple sheets into DataList
							if (sheetCount == sSheetName.length - 1) {
								DataList.add(RowData);
							}
						}

					}

				} else if (row.getRowNum() != 0) {
					System.out.println("Mismatch found between no. of row values and header values in Sheet:"
							+ sSheetName[0] + ", for the row:" + row.getRowNum() + ", No. of Headers: "
							+ sHeaderKey.length + ", No. of Values:" + sValue.length);
				}
			}

			workbook.close();
			oFis.close();
		} catch (Exception e) {
//		loggerManger.logger.info("Execption at excelReadMultiSheetsHashMap(String sExcelPath, String sSheetName[], String sCondCol, String sCondVal) in Excel.java:\n"+e.getMessage());

		}

		return DataList;
	}

	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}

	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(col_Num);

			if (cell == null)
				return "";
			// System.out.println(cell.getCellType());
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());

		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colName + " does not exist in xls";
		}
	}

	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC || cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

				String cellText = String.valueOf(cell.getNumericCellValue());

				return cellText;
			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;
			}
			if (colNum == -1)
				return false;

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			// cell style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		// System.out.println("setCellData setCellData******************");
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			// System.out.println("A");
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
					colNum = i;
			}

			if (colNum == -1)
				return false;
			sheet.autoSizeColumn(colNum); // ashish
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();

			// cell style for hyperlinks
			// by default hypelrinks are blue and underlined
			CellStyle hlink_style = workbook.createCellStyle();
			XSSFFont hlink_font = workbook.createFont();
			hlink_font.setUnderline(XSSFFont.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
			// hlink_style.setWrapText(true);

			XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
			link.setAddress(url);
			cell.setHyperlink(link);
			cell.setCellStyle(hlink_style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addSheet(String sheetname) {

		FileOutputStream fileOut;
		try {
			workbook.createSheet(sheetname);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeSheet(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return false;

		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addColumn(String sheetName, String colName) {
		// System.out.println("**************addColumn*********************");

		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			if (row == null)
				row = sheet.createRow(0);

			// cell = row.getCell();
			// if (cell == null)
			// System.out.println(row.getLastCellNum());
			if (row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());

			cell.setCellValue(colName);
			cell.setCellStyle(style);

			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	public boolean removeColumn(String sheetName, int colNum) {
		try {
			if (!isSheetExist(sheetName))
				return false;
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			style.setFillPattern(HSSFCellStyle.NO_FILL);

			for (int i = 0; i < getRowCount(sheetName); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(colNum);
					if (cell != null) {
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();

	}

//String sheetName, String testCaseName,String keyword ,String URL,String message
	public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url,
			String message) {
		// System.out.println("ADDING addHyperLink******************");

		url = url.replace('\\', '/');
		if (!isSheetExist(sheetName))
			return false;

		sheet = workbook.getSheet(sheetName);

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
				// System.out.println("**caught "+(i+index));
				setCellData(sheetName, screenShotColName, i + index, message, url);
				break;
			}
		}

		return true;
	}

	public int getCellRowNum(String sheetName, String colName, String cellValue) {

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;

	}
}