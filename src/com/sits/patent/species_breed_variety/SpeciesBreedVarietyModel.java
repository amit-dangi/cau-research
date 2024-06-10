package com.sits.patent.species_breed_variety;

import com.sits.general.Common;

public class SpeciesBreedVarietyModel extends Common{
	
	private String s_id;
	private String relsing_no;
	public String getRelsing_no() {
		return relsing_no;
	}
	public void setRelsing_no(String relsing_no) {
		this.relsing_no = relsing_no;
	}
	public String getFn_agency() {
		return fn_agency;
	}
	public void setFn_agency(String fn_agency) {
		this.fn_agency = fn_agency;
	}
	private String fn_agency;
	private String rel_date;
	private String location;
	private String ddo;
	private String location_id;
	private String ddo_id;
	private String uploaded_file;
	private String uploaded_proceeding;
	private String pi_name;
	private String sts;
	
	

	
	
	public String getS_id() {
		return s_id;
	}
	public void setS_id(String s_id) {
		this.s_id = s_id;
	}
	public String getPi_name() {
		return pi_name;
	}
	public void setPi_name(String pi_name) {
		this.pi_name = pi_name;
	}
	public String getSts() {
		return sts;
	}
	public void setSts(String sts) {
		this.sts = sts;
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
	
	public String getRel_date() {
		return rel_date;
	}
	public void setRel_date(String rel_date) {
		this.rel_date = rel_date;
	}
	public String getUploaded_proceeding() {
		return uploaded_proceeding;
	}
	public void setUploaded_proceeding(String uploaded_proceeding) {
		this.uploaded_proceeding = uploaded_proceeding;
	}
	public String getLocation_id() {
		return location_id;
	}
	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}
	public String getDdo_id() {
		return ddo_id;
	}
	public void setDdo_id(String ddo_id) {
		this.ddo_id = ddo_id;
	}
	
}
