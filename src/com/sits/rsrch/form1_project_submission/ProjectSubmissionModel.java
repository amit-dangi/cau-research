/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.form1_project_submission;

import com.sits.general.Common;
import com.sits.general.General;

public class ProjectSubmissionModel extends Common {

	private String IsSubmit;
	private String fId;
	private String PiName;
	private String PiNameDesc;
	private String dept;
	private String desg;
	private String projPropsal;
	private String durPropProj;
	private String totalBudgProp;
	private String FundAgency;
	private String nameAddrCoPi;
	private String XTODATE;
	private String nonRecCost;
	private String chemAndCon;
	private String manpower;
	private String contingency;
	private String travel;
	private String outSourcingCharge;
	private String overCharg;
	private String projPropSub;
	private String projPropClear;
	private String necClearObt;
	private String finCommitUni;
	private String AttchCertif;
	private String deptName;
	private String desgName;
	private String did;
	private String doc_name;
	private String file_name;
	private String ppId;
	private String finCommitUniDetails;
	private String locationCode;
	private String ddoCode;
	private String locationName;
	private String ddoName;
	private String projPropsalIdManual;
	private String durPropProjYear;
	private String projET;
	private String extdurPropProjYear;
	private String extdurPropProjMonth;
	
	//new fields added 22 feb
	private String projtype;
	private String erptype;
	private String projterm;
	private String IsApprovalReq;
	
	//new fields added 19 jun
	private String proj_start_date;
	
	// fields added 28 aug
	private String fn_agency;
	private String proj_obj;
	private String thrust_area;
	private String sub_thrust_area;
	private String retirePiName;
	private String budgetHeads;
	private String inst_charges;
	private String previous_pi_Name;
	private String applied_date;
	// field added 13 march mail
	private String projunder;
	
	
	public String getPrevious_pi_Name() {
		return previous_pi_Name;
	}
	public void setPrevious_pi_Name(String previous_pi_Name) {
		this.previous_pi_Name = previous_pi_Name;
	}
	public String getApplied_date() {
		return applied_date;
	}
	public void setApplied_date(String applied_date) {
		this.applied_date = applied_date;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getDoc_name() {
		return doc_name;
	}
	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getPiName() {
		return PiName;
	}
	public void setPiName(String piName) {
		PiName = piName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getDesg() {
		return desg;
	}
	public void setDesg(String desg) {
		this.desg = desg;
	}
	public String getProjPropsal() {
		return projPropsal;
	}
	public void setProjPropsal(String projPropsal) {
		this.projPropsal = projPropsal;
	}
	public String getDurPropProj() {
		return durPropProj;
	}
	public void setDurPropProj(String durPropProj) {
		this.durPropProj = durPropProj;
	}
	public String getTotalBudgProp() {
		return totalBudgProp;
	}
	public void setTotalBudgProp(String totalBudgProp) {
		this.totalBudgProp = totalBudgProp;
	}
	public String getFundAgency() {
		return FundAgency;
	}
	public void setFundAgency(String fundAgency) {
		FundAgency = fundAgency;
	}
	public String getNameAddrCoPi() {
		return nameAddrCoPi;
	}
	public void setNameAddrCoPi(String nameAddrCoPi) {
		this.nameAddrCoPi = nameAddrCoPi;
	}
	public String getXTODATE() {
		return XTODATE;
	}
	public void setXTODATE(String xTODATE) {
		XTODATE = xTODATE;
	}
	public String getNonRecCost() {
		return nonRecCost;
	}
	public void setNonRecCost(String nonRecCost) {
		this.nonRecCost = nonRecCost;
	}
	public String getChemAndCon() {
		return chemAndCon;
	}
	public void setChemAndCon(String chemAndCon) {
		this.chemAndCon = chemAndCon;
	}
	public String getManpower() {
		return manpower;
	}
	public void setManpower(String manpower) {
		this.manpower = manpower;
	}
	public String getContingency() {
		return contingency;
	}
	public void setContingency(String contingency) {
		this.contingency = contingency;
	}
	public String getTravel() {
		return travel;
	}
	public void setTravel(String travel) {
		this.travel = travel;
	}
	public String getOutSourcingCharge() {
		return outSourcingCharge;
	}
	public void setOutSourcingCharge(String outSourcingCharge) {
		this.outSourcingCharge = outSourcingCharge;
	}
	public String getOverCharg() {
		return overCharg;
	}
	public void setOverCharg(String overCharg) {
		this.overCharg = overCharg;
	}
	public String getProjPropSub() {
		return projPropSub;
	}
	public void setProjPropSub(String projPropSub) {
		this.projPropSub = projPropSub;
	}
	public String getProjPropClear() {
		return projPropClear;
	}
	public void setProjPropClear(String projPropClear) {
		this.projPropClear = projPropClear;
	}
	public String getNecClearObt() {
		return necClearObt;
	}
	public void setNecClearObt(String necClearObt) {
		this.necClearObt = necClearObt;
	}
	public String getFinCommitUni() {
		return finCommitUni;
	}
	public void setFinCommitUni(String finCommitUni) {
		this.finCommitUni = finCommitUni;
	}
	public String getAttchCertif() {
		return AttchCertif;
	}
	public void setAttchCertif(String attchCertif) {
		AttchCertif = attchCertif;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDesgName() {
		return desgName;
	}
	public void setDesgName(String desgName) {
		this.desgName = desgName;
	}
	public String getfId() {
		return fId;
	}
	public void setfId(String fId) {
		this.fId = fId;
	}
	public String getIsSubmit() {
		return IsSubmit;
	}
	public void setIsSubmit(String isSubmit) {
		IsSubmit = isSubmit;
	}
	public String getPpId() {
		return ppId;
	}
	public void setPpId(String ppId) {
		this.ppId = ppId;
	}
	public String getFinCommitUniDetails() {
		return finCommitUniDetails;
	}
	public void setFinCommitUniDetails(String finCommitUniDetails) {
		this.finCommitUniDetails = finCommitUniDetails;
	}
	public String getPiNameDesc() {
		return PiNameDesc;
	}
	public void setPiNameDesc(String piNameDesc) {
		PiNameDesc = piNameDesc;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getDdoCode() {
		return ddoCode;
	}
	public void setDdoCode(String ddoCode) {
		this.ddoCode = ddoCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getDdoName() {
		return ddoName;
	}
	public void setDdoName(String ddoName) {
		this.ddoName = ddoName;
	}
	public String getProjPropsalIdManual() {
		return projPropsalIdManual;
	}
	public void setProjPropsalIdManual(String projPropsalIdManual) {
		this.projPropsalIdManual = projPropsalIdManual;
	}
	public String getDurPropProjYear() {
		return durPropProjYear;
	}
	public void setDurPropProjYear(String durPropProjYear) {
		this.durPropProjYear = durPropProjYear;
	}
	public String getProjET() {
		return projET;
	}
	public void setProjET(String projET) {
		this.projET = projET;
	}
	public String getExtdurPropProjYear() {
		return extdurPropProjYear;
	}
	public void setExtdurPropProjYear(String extdurPropProjYear) {
		this.extdurPropProjYear = extdurPropProjYear;
	}
	public String getExtdurPropProjMonth() {
		return extdurPropProjMonth;
	}
	public void setExtdurPropProjMonth(String extdurPropProjMonth) {
		this.extdurPropProjMonth = extdurPropProjMonth;
	}
	public String getProjtype() {
		return projtype;
	}
	public void setProjtype(String projtype) {
		this.projtype = projtype;
	}
	public String getErptype() {
		return erptype;
	}
	public void setErptype(String erptype) {
		this.erptype = erptype;
	}
	public String getProjterm() {
		return projterm;
	}
	public void setProjterm(String projterm) {
		this.projterm = projterm;
	}
	public String getIsApprovalReq() {
		return IsApprovalReq;
	}
	public void setIsApprovalReq(String isApprovalReq) {
		IsApprovalReq = isApprovalReq;
	}	
	
	public String getProj_start_date() {
		return proj_start_date;
	}
	public void setProj_start_date(String proj_start_date) {
		this.proj_start_date = proj_start_date;
	}
	public String getFn_agency() {
		return fn_agency;
	}
	public void setFn_agency(String fn_agency) {
		this.fn_agency = fn_agency;
	}
	public String getProj_obj() {
		return proj_obj;
	}
	public void setProj_obj(String proj_obj) {
		this.proj_obj = proj_obj;
	}
	public String getThrust_area() {
		return thrust_area;
	}
	public void setThrust_area(String thrust_area) {
		this.thrust_area = thrust_area;
	}
	public String getSub_thrust_area() {
		return sub_thrust_area;
	}
	public void setSub_thrust_area(String sub_thrust_area) {
		this.sub_thrust_area = sub_thrust_area;
	}
	public String getRetirePiName() {
		return retirePiName;
	}
	public void setRetirePiName(String retirePiName) {
		this.retirePiName = retirePiName;
	}
	public String getBudgetHeads() {
		return budgetHeads;
	}
	public void setBudgetHeads(String budgetHeads) {
		this.budgetHeads = budgetHeads;
	}
	public String getInst_charges() {
		return inst_charges;
	}
	public void setInst_charges(String inst_charges) {
		this.inst_charges = inst_charges;
	}
	public String getProjunder() {
		return projunder;
	}
	public void setProjunder(String projunder) {
		this.projunder = projunder;
	}
	
}