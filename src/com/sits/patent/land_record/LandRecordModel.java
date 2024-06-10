package com.sits.patent.land_record;

import java.util.ArrayList;

import com.sits.general.Common;

public class LandRecordModel extends Common{
	
	private String ld_id;
	private String LOCATION_CODE;
	private String DDO_ID;
	private String uploaded_file ;
	private String location;
	private String ddo;
	private String patta_no;
	private String other;
	private String revenue_date;
	private String detail_id;
	private String dept;
	private String item_des;
	private ArrayList<LandRecordModel> list;
	public String getLd_id() {
		return ld_id;
	}
	public void setLd_id(String ld_id) {
		this.ld_id = ld_id;
	}
	public String getLOCATION_CODE() {
		return LOCATION_CODE;
	}
	public void setLOCATION_CODE(String lOCATION_CODE) {
		LOCATION_CODE = lOCATION_CODE;
	}
	public String getDDO_ID() {
		return DDO_ID;
	}
	public void setDDO_ID(String dDO_ID) {
		DDO_ID = dDO_ID;
	}
	public String getUploaded_file() {
		return uploaded_file;
	}
	public void setUploaded_file(String uploaded_file) {
		this.uploaded_file = uploaded_file;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDdo() {
		return ddo;
	}
	public void setDdo(String ddo) {
		this.ddo = ddo;
	}
	public String getPatta_no() {
		return patta_no;
	}
	public void setPatta_no(String patta_no) {
		this.patta_no = patta_no;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getRevenue_date() {
		return revenue_date;
	}
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}
	public void setRevenue_date(String revenue_date) {
		this.revenue_date = revenue_date;
	}
	public ArrayList<LandRecordModel> getList() {
		return list;
	}
	public void setList(ArrayList<LandRecordModel> list) {
		this.list = list;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getItem_des() {
		return item_des;
	}
	public void setItem_des(String item_des) {
		this.item_des = item_des;
	}
	
}
