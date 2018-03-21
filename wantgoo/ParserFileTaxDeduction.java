package wantgoo;

import java.io.*;
import java.sql.*;

import stock5.conn.ConnectionDAO;

/*
 * (2011)100年殖利率扣抵稅率
 */
public class ParserFileTaxDeduction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String year = "101";
			String filePath = "C:/tax2012.csv";
			FileReader fr= new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
	
			Connection conn = ConnectionDAO.getConnection();
			
			PreparedStatement PS1 = null, PS2=null, PS3=null;
			ResultSet RS1 = null;
			
			String sqlTaxQuery = "select stock_id from wantgoo_tax_deductions where year=? and stock_id=?";
			PS1 = conn.prepareStatement(sqlTaxQuery);
			
			String sqlTaxInsert = "insert into wantgoo_tax_deductions(stock_id, stock_name, year, tax_deduction, create_date) " +
								" values(?,?,?,round(?*100,2),now())";
//			String sqlTaxInsert = "insert into wantgoo_tax_deductions(stock_id, stock_name, year, tax_deduction, create_date) " +
//			" values(?,?,?,?,now())";
			PS3 = conn.prepareStatement(sqlTaxInsert);
			
			String sqlTaxUpdate = "update wantgoo_tax_deductions set tax_deduction=round(?*100,2), create_date=now() where year=? and stock_id=?";
//			String sqlTaxUpdate = "update wantgoo_tax_deductions set tax_deduction=?, create_date=now() where year=? and stock_id=?";
			PS2 = conn.prepareStatement(sqlTaxUpdate);
			
			String strLine = "";
			while ((strLine = br.readLine()) != null) {
				String[] strSplit = strLine.split(",");
				
				if(strSplit.length<3) {
					continue;
				}
				
				String stockName = strSplit[1];
				String stockId = strSplit[0];
				String stockFax = strSplit[2]+"";
				
				
				PS1.setString(1, year);
				PS1.setString(2, stockId);
				RS1 = PS1.executeQuery();
				
				if(RS1.next()) {
					PS2.setString(1, stockFax);
					PS2.setString(2, year);
					PS2.setString(3, stockId);
					PS2.executeUpdate();
					
				}
				else {
					PS3.setString(1, stockId);
					PS3.setString(2, stockName);
					PS3.setString(3, year);
					PS3.setString(4, stockFax);
					PS3.executeUpdate();
					
				}
				
			}
			
			if(RS1 !=null) {RS1.close();}
			if(PS1 !=null) {PS1.close();}
			if(PS2 !=null) {PS2.close();}
			if(PS3 !=null) {PS3.close();}
			if(conn !=null) {conn.close();}
			
			if(br !=null) {br.close();}
			if(fr !=null) {fr.close();}
			
			System.err.println("Finish!!");
		}
		catch(Exception ex) {
			System.err.println(ex.toString());
		}
		
		
	}
}
