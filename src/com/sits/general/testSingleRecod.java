package com.sits.general;

import java.sql.ResultSet;
import java.sql.SQLException;

public class testSingleRecod {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//private ResultSet resultSet = null;
		QueryUtil aq =new  QueryUtil();
		String query ="select PS_MID,projtype,LOCATION_CODE from cau_research.rsrch_form1_mast limit 1";
		try {
			ResultSet ResultSet=aq.selectData(query);
			System.out.println("ResultSet||"+ResultSet.toString());
			ResultSet.getString(1);
			ResultSet.getString(2);
			ResultSet.getString(3);
			System.out.println("ResultSet 1|"+ResultSet.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
