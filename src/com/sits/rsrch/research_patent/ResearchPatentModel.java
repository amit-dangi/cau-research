package com.sits.rsrch.research_patent;

import com.sits.general.Common;

public class ResearchPatentModel extends Common{
	private String id;
	private String pat_type;
	private String pat_status;
	private String location;
	private String ddo;
	private String app_name;
	private String pat_title;
	private String pat_app_num;
	private String filing_date;
	private String resh_category;
	private String sub_category;
	private String url ;
	private String pat_awd_by;
	private String pat_grnt_date;
	private String pat_publ_grnt_num;
	private String ass_name;
	private String upload ;
	private String ddo_id ;
	private String location_id ;
	
	private String pat_cat;
	private String PiName;
	private String deptId;
	//New coloum added given by ajay 1 nov as per Feature #12676
		private String fin_yr;
	public String getPiName() {
		return PiName;
	}
	public void setPiName(String piName) {
		PiName = piName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPat_type() {
		return pat_type;
	}
	public void setPat_type(String pat_type) {
		this.pat_type = pat_type;
	}
	public String getPat_status() {
		return pat_status;
	}
	public void setPat_status(String pat_status) {
		this.pat_status = pat_status;
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
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getPat_title() {
		return pat_title;
	}
	public void setPat_title(String pat_title) {
		this.pat_title = pat_title;
	}
	public String getPat_app_num() {
		return pat_app_num;
	}
	public void setPat_app_num(String pat_app_num) {
		this.pat_app_num = pat_app_num;
	}
	public String getFiling_date() {
		return filing_date;
	}
	public void setFiling_date(String filing_date) {
		this.filing_date = filing_date;
	}
	public String getResh_category() {
		return resh_category;
	}
	public void setResh_category(String resh_category) {
		this.resh_category = resh_category;
	}
	public String getSub_category() {
		return sub_category;
	}
	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPat_awd_by() {
		return pat_awd_by;
	}
	public void setPat_awd_by(String pat_awd_by) {
		this.pat_awd_by = pat_awd_by;
	}
	public String getPat_grnt_date() {
		return pat_grnt_date;
	}
	public void setPat_grnt_date(String pat_grnt_date) {
		this.pat_grnt_date = pat_grnt_date;
	}
	public String getPat_publ_grnt_num() {
		return pat_publ_grnt_num;
	}
	public void setPat_publ_grnt_num(String pat_publ_grnt_num) {
		this.pat_publ_grnt_num = pat_publ_grnt_num;
	}
	public String getAss_name() {
		return ass_name;
	}
	public void setAss_name(String ass_name) {
		this.ass_name = ass_name;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public String getDdo_id() {
		return ddo_id;
	}
	public void setDdo_id(String ddo_id) {
		this.ddo_id = ddo_id;
	}
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public String getPat_cat() {
		return pat_cat;
	}
	public void setPat_cat(String pat_cat) {
		this.pat_cat = pat_cat;
	}
	public String getDeptid() {
		return deptId;
	}
	public void setDeptid(String deptid) {
		this.deptId = deptid;
	}
	public String getFin_yr() {
		return fin_yr;
	}
	public void setFin_yr(String fin_yr) {
		this.fin_yr = fin_yr;
	}
	
}
