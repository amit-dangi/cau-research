package com.sits.patent.manage_publication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sits.commonApi.commonAPI;
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

public class ManagePublicationManager {
		static Logger log = Logger.getLogger("exceptionlog CAU Research"); 

		public static ManagePublicationModel save(ManagePublicationModel model, String user_id, String ip) {
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rst = null;
			String query = "",msg="";
			int count = 0, i=0;
			try {
				String publication=General.checknull(model.getPub_id());
				conn = DBConnection.getConnection();
				conn.setAutoCommit(false);
				
				query = "INSERT INTO RSRCH_MANAGE_PUBLICATIONS ( LOCATION_CODE,DDO_ID,PUB_ID,author_name,publication_year_article,title,"
						+ "journal_name,volume_no,issue_no,pages,naas_rating,doi_number,year_publication,symposium_name,pagination,"
						+ "place,symposium_date,name_of_magazine,place_of_publication,name_publisher,"
						+ "connecting_word,host_document_items,edition_no,later_edition,college_name,created_by,"
						+ " CREATED_DATE,created_machine)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
				psmt = conn.prepareStatement(query);
				psmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
				psmt.setString(++i, General.checknull(model.getDDO_ID()));
				psmt.setString(++i, General.checknull(model.getPub_id()));
				psmt.setString(++i, General.checknull(model.getAuthor_name()));
				psmt.setString(++i, General.checknull(model.getPublication_year_article()));
				if(publication.equals("1")||publication.equals("2")||publication.equals("5")){
					psmt.setString(++i, General.checknull(model.getArticle_title()));
				}else if(publication.equals("3")){
					psmt.setString(++i, General.checknull(model.getPaper_tittle()));
				}else if(publication.equals("4")){
					psmt.setString(++i, General.checknull(model.getResearch_paper_title()));
				}else if(publication.equals("6")){
					psmt.setString(++i, General.checknull(model.getBook_title()));
				}else if(publication.equals("7")){
					psmt.setString(++i, General.checknull(model.getContribution_title()));
				}else if(publication.equals("8")){
					psmt.setString(++i, General.checknull(model.getBulletin_title()));
				}else if(publication.equals("9")){
					psmt.setString(++i, General.checknull(""));
				}
				psmt.setString(++i, General.checknull(model.getJournal_name()));
				psmt.setString(++i, General.checknull(model.getVolume_no()));
				psmt.setString(++i, General.checknull(model.getIssue_no()));
				psmt.setString(++i, General.checknull(model.getPages()));
				psmt.setString(++i, General.checknull(model.getNaas_rating()));
				psmt.setString(++i, General.checknull(model.getDoi_number()));
				psmt.setString(++i, General.checknull(model.getYear_publication()));
				psmt.setString(++i, General.checknull(model.getSymposium_name()));
				psmt.setString(++i, General.checknull(model.getPagination()));
				psmt.setString(++i, General.checknull(model.getPlace()));
				psmt.setString(++i, General.formatDate(model.getSymposium_date()).equals("") ? null:General.formatDate(model.getSymposium_date()));
				psmt.setString(++i, General.checknull(model.getName_of_magazine()));
				psmt.setString(++i, General.checknull(model.getPlace_of_publication()));
				psmt.setString(++i, General.checknull(model.getName_publisher()));
				psmt.setString(++i, General.checknull(model.getConnecting_word()));
				psmt.setString(++i, General.checknull(model.getHost_document_items()));
				psmt.setString(++i, General.checknull(model.getEdition_no()));
				psmt.setString(++i, General.checknull(model.getLater_edition()));
				psmt.setString(++i, General.checknull(model.getCollege_name()));
				psmt.setString(++i, user_id);
				psmt.setString(++i, ip);
				//System.out.println("psmt"+psmt);
				count = psmt.executeUpdate();

				if (count > 0) {
					conn.commit();
					model.setErrMsg(ApplicationConstants.SAVE);
					model.setValid(true);
				 } else {
					conn.rollback();
					model.setErrMsg(ApplicationConstants.FAIL);
					model.setValid(false);
				}
			} catch (Exception e) {
				msg = e.getMessage().toString();
				if (msg.indexOf("Duplicate entry") != -1)
					msg = ApplicationConstants.UNIQUE_CONSTRAINT;
				else
					msg = ApplicationConstants.EXCEPTION_MESSAGE;
				model.setErrMsg(msg);
				model.setValid(false);
				log.fatal(Logging.logException("ManagePublicationManager[save]", e.getMessage().toString()));
				System.out.println("Error in ManagePublicationManager[save] : " + e.getMessage());
			} finally {
				try {
					if (psmt != null)
						psmt.close();
					if (rst != null)
						rst.close();
					conn.close();
					msg = "";
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			return model;
		}

		public static ArrayList<ManagePublicationModel> getList(ManagePublicationModel model,String user_status,String user_id) {
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rst = null;
			ArrayList<ManagePublicationModel> al = new ArrayList<ManagePublicationModel>();
			String query = "";
			try {
				
				JSONObject jsonobjlocation=new JSONObject();
				JSONObject finalObjectlocation=new JSONObject();
				finalObjectlocation.put("tablename", "leave_location_mast");
				finalObjectlocation.put("columndesc","LOCATION_NAME");
				finalObjectlocation.put("id", "LOCATION_CODE");
				jsonobjlocation= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObjectlocation);
				JSONArray designationlocationarr = (JSONArray) jsonobjlocation.get("commondata");
				
				JSONObject jsonobjDdo=new JSONObject();
				JSONObject finalObjectDdo=new JSONObject();
				finalObjectDdo.put("tablename", "ddo");
				finalObjectDdo.put("columndesc","DDONAME");
				finalObjectDdo.put("id", "DDO_ID");
				jsonobjDdo= commonAPI.getDropDownByWebService("rest/apiServices/masterdetails", finalObjectDdo);
				JSONArray designationDtoarr = (JSONArray) jsonobjDdo.get("commondata");
				conn = DBConnection.getConnection();
				query = "select id,LOCATION_CODE,DDO_ID,(select PUB_TYP from RSRCH_PUBLICATIONS rp where rp.PUB_ID=a.PUB_ID)as PUB_ID,author_name from"
						+ " RSRCH_MANAGE_PUBLICATIONS a WHERE 1=1 ";
				if (!General.checknull(model.getLOCATION_CODE()).equals("")) {
					query += "  and a.LOCATION_CODE = '" + model.getLOCATION_CODE() + "' ";
				} if (!General.checknull(model.getDDO_ID()).equals("")) {
					query += "  and a.DDO_ID='" + model.getDDO_ID() + "' ";
				} if (!General.checknull(model.getPub_id()).equals("")) {
					query += " AND a.PUB_ID='" + model.getPub_id() + "' ";
				} if (!General.checknull(model.getAuthor_name()).equals("")) {
					query += " AND a.author_name='" + model.getAuthor_name() + "' ";
				}if(!(user_id.equals("")) && (user_status.equals("U"))){
					query+=" and a.CREATED_BY like '"+user_id+"' ";
				}
				//System.out.println("query"+query);
				psmt = conn.prepareStatement(query);
				rst = psmt.executeQuery();
				while (rst.next()) {
					ManagePublicationModel  model1 = new ManagePublicationModel();
						for(int i=0; i<designationlocationarr.size(); i++){
							JSONObject jsn=	(JSONObject) designationlocationarr.get(i);
							if(jsn.get("id").equals(rst.getString("LOCATION_CODE")))
							{
								model1.setLOCATION_CODE(General.checknull(jsn.get("desc").toString()));
							}
						}
						for(int i=0; i<designationDtoarr.size(); i++){
							JSONObject jsn=	(JSONObject) designationDtoarr.get(i);
							if(jsn.get("id").equals(rst.getString("ddo_id")))
							{
								model1.setDDO_ID(General.checknull(jsn.get("desc").toString()));
							}
						}
					model1.setPub_id(General.checknull(rst.getString("PUB_ID")));
					model1.setId(General.checknull(rst.getString("id")));
					model1.setAuthor_name(General.checknull(rst.getString("author_name")));
					al.add(model1);
				}
			} catch (Exception e) {
				System.out.println("EXCEPTION CAUSED BY:" + " " + e.getMessage().toUpperCase());
				log.fatal(Logging.logException("ManagePublicationManager[getList]", e.toString()));
			} finally {
				try {
					if (rst != null)
						rst.close();
					if (psmt != null)
						psmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException sql) {
					sql.printStackTrace();
				}
			}

			return al;
		}

		public static ManagePublicationModel EditRecord(String id) {
			ManagePublicationModel model = new ManagePublicationModel();
			String cSql = "";
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rst = null;
			int i = 1;
			try {

				conn = DBConnection.getConnection();
				cSql = "select ID, LOCATION_CODE,DDO_ID,PUB_ID,author_name,publication_year_article,title,"
						+ "journal_name,volume_no,issue_no,pages,naas_rating,doi_number,year_publication,symposium_name,pagination,"
						+ "place,date_format(symposium_date, '%d/%m/%Y') as symposium_date,name_of_magazine,place_of_publication,name_publisher,"
						+ "connecting_word,host_document_items,edition_no,later_edition,college_name from RSRCH_MANAGE_PUBLICATIONS where id=? ";
				pstmt = conn.prepareStatement(cSql);
				pstmt.setString(i++, id.trim());
				System.out.println("pstmt viewrecord"+pstmt);
				rst = pstmt.executeQuery();
				if (rst.next() == false) {
				} else {
					do {
						model = new ManagePublicationModel();
						model.setDDO_ID(General.checknull(rst.getString("DDO_ID")));
						model.setLOCATION_CODE(General.checknull(rst.getString("LOCATION_CODE")));
						model.setPub_id(General.checknull(rst.getString("PUB_ID")));
						model.setAuthor_name(General.checknull(rst.getString("author_name")));
						model.setPublication_year_article(General.checknull(rst.getString("publication_year_article")));
						model.setPaper_tittle(General.checknull(rst.getString("title")));
						model.setJournal_name(General.checknull(rst.getString("journal_name")));
						model.setVolume_no(General.checknull(rst.getString("volume_no")));
						model.setIssue_no(General.checknull(rst.getString("issue_no")));
						model.setPages(General.checknull(rst.getString("pages")));
						model.setNaas_rating(General.checknull(rst.getString("naas_rating")));
						model.setDoi_number(General.checknull(rst.getString("doi_number")));
						model.setYear_publication(General.checknull(rst.getString("year_publication")));
						model.setSymposium_name(General.checknull(rst.getString("symposium_name")));
						model.setPagination(General.checknull(rst.getString("pagination")));
						model.setPlace(General.checknull(rst.getString("place")));
						model.setSymposium_date(General.checknull(rst.getString("symposium_date")));
						model.setName_of_magazine(General.checknull(rst.getString("name_of_magazine")));
						model.setPlace_of_publication(General.checknull(rst.getString("place_of_publication")));
						model.setName_publisher(General.checknull(rst.getString("name_publisher")));
						model.setConnecting_word(General.checknull(rst.getString("connecting_word")));
						model.setHost_document_items(General.checknull(rst.getString("host_document_items")));
						model.setEdition_no(General.checknull(rst.getString("edition_no")));
						model.setLater_edition(General.checknull(rst.getString("later_edition")));
						model.setCollege_name(General.checknull(rst.getString("college_name")));
						model.setId(General.checknull(rst.getString("id")));
					} while (rst.next());
				}
			} catch (Exception e) {
				System.out.println("Error in ManagePublicationManager[EditRecord] : " + e.getMessage());
				log.fatal(Logging.logException("ManagePublicationManager[EditRecord]", e.getMessage().toString()));
			} finally {
				try {
					if (pstmt != null)
						pstmt = null;
					if (rst != null)
						rst = null;
					if (conn != null)
						conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return model;
		}

		public static ManagePublicationModel update(ManagePublicationModel model, String user_id, String ip) {

			String  qry = "",msg="";
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rst = null;
			int count1 = 0,i=0;
			JSONObject jsonObject = new JSONObject();
			try {
				conn = DBConnection.getConnection();
				conn.setAutoCommit(false);
				String publication=General.checknull(model.getPub_id());
				System.out.println("publication"+publication);
				qry = " update RSRCH_MANAGE_PUBLICATIONS set LOCATION_CODE=?,DDO_ID=?,PUB_ID=?,author_name=?,publication_year_article=?,title=?,"
						+ "journal_name=?,volume_no=?,issue_no=?,pages=?,naas_rating=?,doi_number=?,year_publication=?,symposium_name=?,pagination=?,"
						+ "place=?,symposium_date=?,name_of_magazine=?,place_of_publication=?,name_publisher=?,"
						+ "connecting_word=?,host_document_items=?,edition_no=?,later_edition=?,college_name=?";
				
				qry +=" ,UPDATED_BY=?, UPDATED_DATE=now(), UPDATED_MACHINE=? ";
				qry +=" where id=? ";
					pstmt = conn.prepareStatement(qry);
					pstmt.setString(++i, General.checknull(model.getLOCATION_CODE()));
					pstmt.setString(++i, General.checknull(model.getDDO_ID()));
					pstmt.setString(++i, General.checknull(model.getPub_id()));
					pstmt.setString(++i, General.checknull(model.getAuthor_name()));
					pstmt.setString(++i, General.checknull(model.getPublication_year_article()));
					if(publication.equals("1")||publication.equals("2")||publication.equals("5")){
						pstmt.setString(++i, General.checknull(model.getArticle_title()));
					}else if(publication.equals("3")){
						pstmt.setString(++i, General.checknull(model.getPaper_tittle()));
					}else if(publication.equals("4")){
						pstmt.setString(++i, General.checknull(model.getResearch_paper_title()));
					}else if(publication.equals("6")){
						pstmt.setString(++i, General.checknull(model.getBook_title()));
					}else if(publication.equals("7")){
						pstmt.setString(++i, General.checknull(model.getContribution_title()));
					}else if(publication.equals("8")){
						pstmt.setString(++i, General.checknull(model.getBulletin_title()));
					}else if(publication.equals("9")){
						pstmt.setString(++i, General.checknull(""));
					}
					pstmt.setString(++i, General.checknull(model.getJournal_name()));
					pstmt.setString(++i, General.checknull(model.getVolume_no()));
					pstmt.setString(++i, General.checknull(model.getIssue_no()));
					pstmt.setString(++i, General.checknull(model.getPages()));
					pstmt.setString(++i, General.checknull(model.getNaas_rating()));
					pstmt.setString(++i, General.checknull(model.getDoi_number()));
					pstmt.setString(++i, General.checknull(model.getYear_publication()));
					pstmt.setString(++i, General.checknull(model.getSymposium_name()));
					pstmt.setString(++i, General.checknull(model.getPagination()));
					pstmt.setString(++i, General.checknull(model.getPlace()));
					pstmt.setString(++i, General.formatDate(model.getSymposium_date()).equals("") ? null:General.formatDate(model.getSymposium_date()));
					pstmt.setString(++i, General.checknull(model.getName_of_magazine()));
					pstmt.setString(++i, General.checknull(model.getPlace_of_publication()));
					pstmt.setString(++i, General.checknull(model.getName_publisher()));
					pstmt.setString(++i, General.checknull(model.getConnecting_word()));
					pstmt.setString(++i, General.checknull(model.getHost_document_items()));
					pstmt.setString(++i, General.checknull(model.getEdition_no()));
					pstmt.setString(++i, General.checknull(model.getLater_edition()));
					pstmt.setString(++i, General.checknull(model.getCollege_name()));
					pstmt.setString(++i, user_id);
					pstmt.setString(++i, ip);
					pstmt.setString(++i, General.checknull(model.getId().trim()));
					count1 = pstmt.executeUpdate();
					if (count1 > 0) {
								conn.commit();
								model.setErrMsg(ApplicationConstants.UPDATED);
								model.setValid(true);
							 } else {
								conn.rollback();
								model.setErrMsg(ApplicationConstants.FAIL);
								model.setValid(false);
							}
						} catch (Exception e) {
							msg = e.getMessage().toString();
							if (msg.indexOf("Duplicate entry") != -1)
								msg = ApplicationConstants.UNIQUE_CONSTRAINT;
							else
								msg = ApplicationConstants.EXCEPTION_MESSAGE;
							model.setErrMsg(msg);
							model.setValid(false);
							log.fatal(Logging.logException("ManagePublicationManager[update]", e.getMessage().toString()));
							System.out.println("Error in ManagePublicationManager[update] : " + e.getMessage());
						} finally {
							try {
								if (pstmt != null)
									pstmt.close();
								if (rst != null)
									rst.close();
								conn.close();
								msg = "";
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						return model;
					}

		public static JSONObject deleteRecord(String id, String user_id, String ip) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			int  count = 0;
			String qry = "", status = "", str = "", flg = "N";
			JSONObject obj = new JSONObject();
			try {
				conn = DBConnection.getConnection();
				conn.setAutoCommit(false);
					qry = "delete from RSRCH_MANAGE_PUBLICATIONS  where id='" + id + "'";
					pstmt = conn.prepareStatement(qry);
					count = pstmt.executeUpdate();

				if (count > 0) {
					conn.commit();
					status = ApplicationConstants.DELETED;
					flg = "Y";
				} else {
					if (conn != null)
						conn.rollback();
					status = ApplicationConstants.FAIL;
				}

				obj.put("errMsg", status);
				obj.put("flg", flg);
			} catch (Exception e) {
				str = e.getMessage().toString();
				if (str.indexOf("Duplicate entry") != -1)
					obj.put("errMsg", ApplicationConstants.DUPLICATE);
				else
					obj.put("errMsg", ApplicationConstants.EXCEPTION_MESSAGE);
				System.out.println("Exception in ManagePublicationManager[delete]" + e.getMessage());
				log.fatal(Logging.logException("Error in ManagePublicationManager[delete]", e.getMessage().toString()));
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return obj;
		}
	}