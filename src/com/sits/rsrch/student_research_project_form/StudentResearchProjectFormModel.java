package com.sits.rsrch.student_research_project_form;

import com.sits.general.Common;

public class StudentResearchProjectFormModel extends Common{
	
	private String m_id;
	private String proj_id;
	private String LOCATION_CODE;
	private String DDO_ID;
	
	private String stu_name;
	private String ICAR_USID;
	private String cau_regno;
	private String course;
	private String discipline;
	private String research_thrust_area;
	private String research_sub_thrust_area;
	private String proj_type;
	private String proj_title;
	private String objective;
	private String guide_name;
	private String external_guide_name;
	
	//New coloum added given by ajay 1 nov as per Feature #12676
	private String fin_yr;
	private String stu_status;	
	
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getICAR_USID() {
		return ICAR_USID;
	}
	public void setICAR_USID(String iCAR_USID) {
		ICAR_USID = iCAR_USID;
	}
	public String getCau_regno() {
		return cau_regno;
	}
	public void setCau_regno(String cau_regno) {
		this.cau_regno = cau_regno;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getResearch_thrust_area() {
		return research_thrust_area;
	}
	public void setResearch_thrust_area(String research_thrust_area) {
		this.research_thrust_area = research_thrust_area;
	}
	public String getResearch_sub_thrust_area() {
		return research_sub_thrust_area;
	}
	public void setResearch_sub_thrust_area(String research_sub_thrust_area) {
		this.research_sub_thrust_area = research_sub_thrust_area;
	}
	public String getProj_type() {
		return proj_type;
	}
	public void setProj_type(String proj_type) {
		this.proj_type = proj_type;
	}
	public String getProj_title() {
		return proj_title;
	}
	public void setProj_title(String proj_title) {
		this.proj_title = proj_title;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getGuide_name() {
		return guide_name;
	}
	public void setGuide_name(String guide_name) {
		this.guide_name = guide_name;
	}
	public String getExternal_guide_name() {
		return external_guide_name;
	}
	public void setExternal_guide_name(String external_guide_name) {
		this.external_guide_name = external_guide_name;
	}
	private String mou_type;
	private String inst_name;
	private String coll_type;
	private String coll_area;
	private String signed_on;
	private String validity_period;
	private String valid_upto;
	private String signed_by;
	private String pi_name;
	private String m_status;
	private String uploaded_file ;
	private String location;
	private String ddo;
	

	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
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
	public String getMou_type() {
		return mou_type;
	}
	public void setMou_type(String mou_type) {
		this.mou_type = mou_type;
	}
	public String getInst_name() {
		return inst_name;
	}
	public void setInst_name(String inst_name) {
		this.inst_name = inst_name;
	}
	public String getColl_type() {
		return coll_type;
	}
	public void setColl_type(String coll_type) {
		this.coll_type = coll_type;
	}
	public String getColl_area() {
		return coll_area;
	}
	public void setColl_area(String coll_area) {
		this.coll_area = coll_area;
	}
	public String getSigned_on() {
		return signed_on;
	}
	public void setSigned_on(String signed_on) {
		this.signed_on = signed_on;
	}
	public String getValidity_period() {
		return validity_period;
	}
	public void setValidity_period(String validity_period) {
		this.validity_period = validity_period;
	}
	public String getValid_upto() {
		return valid_upto;
	}
	public void setValid_upto(String valid_upto) {
		this.valid_upto = valid_upto;
	}
	public String getSigned_by() {
		return signed_by;
	}
	public void setSigned_by(String signed_by) {
		this.signed_by = signed_by;
	}
	public String getPi_name() {
		return pi_name;
	}
	public void setPi_name(String pi_name) {
		this.pi_name = pi_name;
	}
	public String getM_status() {
		return m_status;
	}
	public void setM_status(String m_status) {
		this.m_status = m_status;
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
	public String getProj_id() {
		return proj_id;
	}
	public void setProj_id(String proj_id) {
		this.proj_id = proj_id;
	}
	public String getFin_yr() {
		return fin_yr;
	}
	public void setFin_yr(String fin_yr) {
		this.fin_yr = fin_yr;
	}
	public String getStu_status() {
		return stu_status;
	}
	public void setStu_status(String stu_status) {
		this.stu_status = stu_status;
	}
	
}
