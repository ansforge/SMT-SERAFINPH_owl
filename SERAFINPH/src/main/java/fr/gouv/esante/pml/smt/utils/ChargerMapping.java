package fr.gouv.esante.pml.smt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChargerMapping {
	
	public static HashMap<String, List<String>> listConceptsSerafin = new HashMap<String, List<String>>();

	public static  void  chargeExcelConceptToList(final String xlsFile) throws IOException, ParseException {
		
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
       // String xlsxRihnFileName = PropertiesUtil.getProperties("xlsxEmaFile");
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheetBesoin = workbook.getSheet("Besoin");
		XSSFSheet sheetPrestationDirecte = workbook.getSheet("PrestationDirecte");
		XSSFSheet sheetPrestationIndirecte = workbook.getSheet("PrestationIndirecte");
		
		Iterator<Row> rowIteratorBesoin = sheetBesoin.iterator();
		Iterator<Row> rowIteratorPrestationDirecte = sheetPrestationDirecte.iterator();
		Iterator<Row> rowIteratorPrestationIndirecte = sheetPrestationIndirecte.iterator();
		
		rowIteratorBesoin.next();
		rowIteratorPrestationDirecte.next(); 
		rowIteratorPrestationIndirecte.next(); 
		
		 
		
		while (rowIteratorBesoin.hasNext()) {
			 
			 
			 Row row = rowIteratorBesoin.next();
	    	 Cell niveau = row.getCell(0); 
		     Cell code = row.getCell(1); 
		     Cell label = row.getCell(2); 
		     Cell definition = row.getCell(3); 
		     Cell parent = row.getCell(4); 
		     Cell dateCreation = row.getCell(5); 
		     Cell dateModification = row.getCell(6); 
		     Cell statusActif = row.getCell(7); 

		     
		     List<String> listedonnees= new ArrayList<>();
		     
		    if(niveau!=null && niveau.getCellType() == Cell.CELL_TYPE_STRING) {
		    	//System.out.println("code :"+code.getStringCellValue());
		     listedonnees.add(0,  niveau.getStringCellValue());
		    }
		    else if(niveau!=null && niveau.getCellType() == Cell.CELL_TYPE_NUMERIC) {
		    	//System.out.println("*code :"+code.getStringCellValue());
		    	Double doubleNiveau=new Double(niveau.getNumericCellValue());
		    	int intNiveau=doubleNiveau.intValue();
		        listedonnees.add(0,  String.valueOf(intNiveau) );
		    }
		    else {
		     listedonnees.add(0,  "");
		    }
			 
		     listedonnees.add(1, label.getStringCellValue());
			 
		     if(definition!=null)
			  listedonnees.add(2, definition.getStringCellValue());
			 else
			   listedonnees.add(2, "");	 
			 
			 
			 listedonnees.add(3, parent.getStringCellValue());
			 listedonnees.add(4, formatter.format(dateCreation.getDateCellValue()) );
			 
			 if(dateModification!=null && dateModification.getCellType() != Cell.CELL_TYPE_BLANK && dateModification.getCellType() != Cell.CELL_TYPE_STRING ) {
				 System.out.println("*** code "+code.getStringCellValue() +"date modif "+dateModification.getCellType());
			  listedonnees.add(5, formatter.format(dateModification.getDateCellValue()) );
			 }
			 else {
			   listedonnees.add(5, "");
			 }
			 
			 listedonnees.add(6, String.valueOf(statusActif.getBooleanCellValue()) );
			  
		     
		   
			 listConceptsSerafin.put(code.getStringCellValue(), listedonnees);
		     
		   
		}
		
		while (rowIteratorPrestationDirecte.hasNext()) {
			 
			 
			 Row row = rowIteratorPrestationDirecte.next();
	    	 Cell niveau = row.getCell(0); 
		     Cell code = row.getCell(1); 
		     Cell label = row.getCell(2); 
		     Cell definition = row.getCell(3); 
		     Cell parent = row.getCell(4); 
		     Cell dateCreation = row.getCell(5); 
		     Cell dateModification = row.getCell(6); 
		     Cell statusActif = row.getCell(7); 

		     
		     List<String> listedonnees= new ArrayList<>(); 
		    
		     listedonnees.add(0,  niveau.getStringCellValue());
			 listedonnees.add(1, label.getStringCellValue());
			 if(definition!=null)
			  listedonnees.add(2, definition.getStringCellValue());
			 else
			   listedonnees.add(2, "");	 
			 
			 
			 listedonnees.add(3, parent.getStringCellValue());
			 listedonnees.add(4, formatter.format(dateCreation.getDateCellValue()) );
			 
			 if(dateModification!=null && dateModification.getCellType() != Cell.CELL_TYPE_BLANK && dateModification.getCellType() != Cell.CELL_TYPE_STRING ) {
				 System.out.println("*** code "+code.getStringCellValue() +"date modif "+dateModification.getCellType());
			  listedonnees.add(5, formatter.format(dateModification.getDateCellValue()) );
			 }
			 else {
			   listedonnees.add(5, "");
			 }
			 
			 
			 listedonnees.add(6, String.valueOf(statusActif.getBooleanCellValue()) );
			  
		     
		   
			 listConceptsSerafin.put(code.getStringCellValue(), listedonnees);
		     
		   
		}
		
		
		
		while (rowIteratorPrestationIndirecte.hasNext()) {
			 
			 
			 Row row = rowIteratorPrestationIndirecte.next();
	    	 Cell niveau = row.getCell(0); 
		     Cell code = row.getCell(1); 
		     Cell label = row.getCell(2); 
		     Cell definition = row.getCell(3); 
		     Cell parent = row.getCell(4); 
		     Cell dateCreation = row.getCell(5); 
		     Cell dateModification = row.getCell(6); 
		     Cell statusActif = row.getCell(7); 

		     
		     List<String> listedonnees= new ArrayList<>(); 
		    
		     listedonnees.add(0,  niveau.getStringCellValue());
			 listedonnees.add(1, label.getStringCellValue());
			 if(definition!=null)
			  listedonnees.add(2, definition.getStringCellValue());
			 else
			   listedonnees.add(2, "");	 
			 
			 
			 listedonnees.add(3, parent.getStringCellValue());
			 listedonnees.add(4, formatter.format(dateCreation.getDateCellValue()) );
			 
			 
			 
			 if(dateModification!=null && dateModification.getCellType() != Cell.CELL_TYPE_BLANK && dateModification.getCellType() != Cell.CELL_TYPE_STRING ) {
				 System.out.println("*** code "+code.getStringCellValue() +"date modif "+dateModification.getCellType());
			  listedonnees.add(5, formatter.format(dateModification.getDateCellValue()) );
			 }
			 else {
			   listedonnees.add(5, "");
			 }
			// listedonnees.add(5, formatter.format(dateModification.getDateCellValue()));
			 
			 
			 
			 listedonnees.add(6, String.valueOf(statusActif.getBooleanCellValue()) );
			  
		     
		   
			 listConceptsSerafin.put(code.getStringCellValue(), listedonnees);
		     
		   
		}
		
	    

	}

}
