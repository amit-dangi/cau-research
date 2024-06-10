<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page import="com.sits.general.ReadProps"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.awt.Color"%>
<%@ page import="com.itextpdf.text.Image"%>
<%@ page import="com.itextpdf.text.BaseColor"%>
<%@ page import="com.itextpdf.text.Document"%>
<%@ page import="com.itextpdf.text.DocumentException"%>
<%@ page import="com.itextpdf.text.Element"%>
<%@ page import="com.itextpdf.text.Font"%>
<%@ page import="com.itextpdf.text.FontFactory"%>
<%@ page import="com.itextpdf.text.Font.FontFamily"%>
<%@ page import="com.itextpdf.text.PageSize"%>
<%@ page import="com.itextpdf.text.Rectangle"%>
<%@ page import="com.itextpdf.text.Paragraph"%>
<%@ page import="com.itextpdf.text.Phrase"%>
<%@ page import="com.itextpdf.text.pdf.PdfPCell"%>
<%@ page import="com.itextpdf.text.pdf.PdfPTable"%>
<%@ page import="com.itextpdf.text.pdf.PdfWriter"%>
<%@ page import="com.itextpdf.text.pdf.BaseFont"%>
<%@ page import="com.itextpdf.text.Rectangle"%>
<%@ page import="com.itextpdf.text.pdf.PdfPageEventHelper"%>
<%@ page import="com.itextpdf.text.pdf.ColumnText"%>
<%@ page import="com.itextpdf.text.Chunk"%>
<%@ page import="com.sits.general.ReportUtil"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.apache.commons.lang.WordUtils"%>
<%@ page import="com.sits.general.*"%>
<%@page import="com.sits.rsrch.reports.research_proposal_report.ResearchProposalReportManager"%>
<%@page import="com.sits.rsrch.reports.research_proposal_report.ResearchProposalReportModel"%>
<head>
<script src="././assests/jquery-3.4.1.min.js"></script>
<script src="././assests/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../js/research/research-proposal-details-report.js"></script>
</head>
<%
	try {
			ServletContext servletContext = request.getServletContext();
			DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
			DecimalFormat df1 = new DecimalFormat("##########.##");
			DecimalFormat df_num = new DecimalFormat("##########.##");
			Document document = new Document(PageSize.A4, 15, 15, 15, 15); //1.Left, 2.Right, 3.Top, 4.Bottom
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, buffer);
			
			String fyear = "", fmonth = "";
			String verdanab_file = "verdanab.ttf";
			verdanab_file = request.getRealPath("/fonts/" + verdanab_file);
			String verdana_file = "verdana.ttf";
			verdana_file = request.getRealPath("/fonts/" + verdana_file);
			
		    String fstatus 	   = General.checknull(request.getParameter("fstatus"));
			String fd		   = General.checknull(request.getParameter("fDate"));
			String td		   = General.checknull(request.getParameter("tDate"));
			String empId	   = General.checknull(request.getParameter("empId"));
			String deptId	   = General.checknull(request.getParameter("dept"));
			String desId	   = General.checknull(request.getParameter("des"));
			
			ArrayList<ResearchProposalReportModel> list = new ArrayList<ResearchProposalReportModel>();
			list = ResearchProposalReportManager.getList(fstatus, fd, td, empId, deptId, desId);
			
		if (list.size() == 0) {%>
		<script type="text/javascript">window.parent.revertBack();</script>
		<%}else{

		response.setHeader("Content-Disposition", "attachment; filename=\"" + "Research_Proposal_Details_Report.pdf\"");
		String headerName = "Research Proposal Details Report";
		String path = File.separator + "images" + File.separator+ "hlogo.png";
		String contextPath = servletContext.getRealPath(path);
		FontFactory.register(verdanab_file, "my_varadana_bold_font");
		Font myVerdanabFontM12 = FontFactory.getFont("my_varadana_bold_font", 12.0f, new BaseColor(128, 0, 0));
		Font myVerdanabFontB12 = FontFactory.getFont("my_varadana_bold_font", 12.0f, new BaseColor(0, 0, 0));
		Font myVerdanabFontM10 = FontFactory.getFont("my_varadana_bold_font", 10.0f, new BaseColor(128, 0, 0));
		Font myVerdanabFontB10 = FontFactory.getFont("my_varadana_bold_font", 10.0f, new BaseColor(0, 0, 0));
		Font myVerdanabFontM08 = FontFactory.getFont("my_varadana_bold_font", 8.0f, new BaseColor(128, 0, 0));
		Font myVerdanabFontB08 = FontFactory.getFont("my_varadana_bold_font", 8.0f, new BaseColor(0, 0, 0));
		Font myVerdanabFontM07 = FontFactory.getFont("my_varadana_bold_font", 7.0f, new BaseColor(128, 0, 0));
		Font myVerdanabFontB07 = FontFactory.getFont("my_varadana_bold_font", 7.0f, new BaseColor(0, 0, 0));
		Font myVerdanabFontM06 = FontFactory.getFont("my_varadana_bold_font", 6.0f, new BaseColor(128, 0, 0));
		Font myVerdanabFontB06 = FontFactory.getFont("my_varadana_bold_font", 6.0f, new BaseColor(0, 0, 0));

		FontFactory.register(verdana_file, "my_varadana_normal_font");
		Font myVerdanaFontM12 = FontFactory.getFont("my_varadana_normal_font", 12.0f, new BaseColor(128, 0, 0));
		Font myVerdanaFontB12 = FontFactory.getFont("my_varadana_normal_font", 12.0f, new BaseColor(0, 0, 0));
		Font myVerdanaFontM10 = FontFactory.getFont("my_varadana_normal_font", 10.0f, new BaseColor(128, 0, 0));
		Font myVerdanaFontB10 = FontFactory.getFont("my_varadana_normal_font", 10.0f, new BaseColor(0, 0, 0));
		Font myVerdanaFontBL10 = FontFactory.getFont("my_varadana_normal_font", 10.0f, new BaseColor(0, 34, 255));
		Font myVerdanaFontM08 = FontFactory.getFont("my_varadana_normal_font", 8.0f, new BaseColor(128, 0, 0));
		Font myVerdanaFontB08 = FontFactory.getFont("my_varadana_normal_font", 8.0f, new BaseColor(0, 0, 0));
		Font myVerdanaFontBL08 = FontFactory.getFont("my_varadana_normal_font", 8.0f, new BaseColor(0, 34, 255));
		Font myVerdanaFontM07 = FontFactory.getFont("my_varadana_normal_font", 7.0f, new BaseColor(128, 0, 0));
		Font myVerdanaFontB07 = FontFactory.getFont("my_varadana_normal_font", 7.0f, new BaseColor(0, 0, 0));
		Font myVerdanaFontBL07 = FontFactory.getFont("my_varadana_normal_font", 7.0f, new BaseColor(0, 34, 255));
		Font myVerdanaFontM06 = FontFactory.getFont("my_varadana_normal_font", 6.0f, new BaseColor(128, 0, 0));
		Font myVerdanaFontB06 = FontFactory.getFont("my_varadana_normal_font", 6.0f, new BaseColor(0, 0, 0));
		Font myVerdanaFontBL06 = FontFactory.getFont("my_varadana_normal_font", 6.0f, new BaseColor(0, 34, 255));

		Font italics = new Font(FontFamily.TIMES_ROMAN, 12.0f, Font.ITALIC, BaseColor.BLACK);

		float[] headerWidth = {1.2f, 8f};
		PdfPTable headerTable = new PdfPTable(headerWidth);
		headerTable.setWidthPercentage(95f);

		float[] feeConfigWidth = {1.5f, 3f, 3f, 3f, 3f, 3f};
		PdfPTable feeConfigTable = new PdfPTable(feeConfigWidth);
		feeConfigTable.setWidthPercentage(95f);

		document.open();
		ReportUtil.addPageBorder(document, Rectangle.BOX, 1.0f, BaseColor.BLACK, 10.0f, PageSize.A4.getHeight() - 10.0f, PageSize.A4.getWidth() - 10.0f, 10.0f);

		PdfPCell cell;

							// ============   Start headerWidth ================================= 

							Image image1 = Image.getInstance(contextPath);
							image1.setAlignment(Image.RIGHT);
							image1.scaleAbsolute(60.0f, 70.0f);
							cell = new PdfPCell(new Phrase("", myVerdanabFontB10));
							cell.addElement(image1);
							cell.setRowspan(7);
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setBorderColor(BaseColor.WHITE);
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase("", myVerdanabFontB10));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase(ReadProps.getkeyValue("welcome.header", "sitsResource"), myVerdanabFontB10));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase(ReadProps.getkeyValue("comp.add1", "sitsResource"), myVerdanaFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase(headerName, myVerdanabFontB10));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase(" ", myVerdanabFontB10));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							cell = new PdfPCell(new Phrase(" ", myVerdanabFontB10));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							headerTable.addCell(cell);

							// ============   End headerWidth =================================  

							// ============   START fee Config Table ================================= 

							cell = new PdfPCell(new Phrase(" ", myVerdanabFontB08));
							cell.setColspan(6);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setBorderColor(new BaseColor(255, 255, 255));
							feeConfigTable.addCell(cell);

							Phrase content = new Phrase("S.No.", myVerdanabFontB08);
							cell = new PdfPCell(content);
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							Float fontSize = content.getFont().getSize();
							Float capHeight = content.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);
							Float padding = 5f;
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(1);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);

							cell = new PdfPCell(new Phrase("Project Title", myVerdanabFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);

							cell = new PdfPCell(new Phrase("PI Name", myVerdanabFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);
							
							cell = new PdfPCell(new Phrase("Funding Agency Name", myVerdanabFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);
							
							cell = new PdfPCell(new Phrase("Proposal Submitted Date", myVerdanabFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);

							cell = new PdfPCell(new Phrase("Status", myVerdanabFontB08));
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setBorderColor(new BaseColor(0, 0, 0));
							cell.setBackgroundColor(new BaseColor(226, 226, 226));
							cell.setPadding(padding);
							cell.setPaddingTop(capHeight - fontSize + padding);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(1);
							cell.setBorderWidthTop(1);
							cell.setBorderWidthBottom(1);
							feeConfigTable.addCell(cell);
							int i=0;
							for (int k = 0; k < list.size(); k++) {
								
								cell = new PdfPCell(new Phrase(++i +"" , myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(1);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);

								cell = new PdfPCell(new Phrase(list.get(k).getTproj(), myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);

								cell = new PdfPCell(new Phrase(list.get(k).getEmpName(), myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);
								
								cell = new PdfPCell(new Phrase(list.get(k).getFa_Name(), myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);
								
								cell = new PdfPCell(new Phrase(list.get(k).getSubmitted_date(), myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);

								cell = new PdfPCell(new Phrase(list.get(k).getStatus(), myVerdanaFontB08));
								cell.setColspan(1);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(padding);
								cell.setPaddingTop(capHeight - fontSize + padding);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(1);
								cell.setBorderWidthTop(1);
								cell.setBorderWidthBottom(1);
								feeConfigTable.addCell(cell);	
							}
							
							// ============   END fee Config Table ================================= 

							PdfPTable breakLine = new PdfPTable(1);
							breakLine.setWidthPercentage(95f);
							breakLine.getDefaultCell().setFixedHeight(2);
							breakLine.getDefaultCell().setBorder(Rectangle.BOTTOM);
							breakLine.getDefaultCell().setBorderColor(BaseColor.BLACK);
							breakLine.addCell(new Phrase("", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

							document.add(headerTable);
							document.add(breakLine);
							document.add(feeConfigTable);
							
							document.newPage();
							
							headerTable = null;
							breakLine = null;
							feeConfigTable = null;
						}
				
			if (list.size() > 0) {
				document.close();

				response.setContentType("application/pdf");
				DataOutput dataOutput = new DataOutputStream(response.getOutputStream());
				byte[] bytes = buffer.toByteArray();
				response.setContentLength(bytes.length);
				for (int f = 0; f < bytes.length; f++) {
					dataOutput.writeByte(bytes[f]);
				}

				out.clear(); // where out is a JspWriter
				out = pageContext.pushBody();
				response.flushBuffer();

			}

	} catch (Exception e) {
		System.out.println("research_proposal_details_report_2pdf Exception " + e.getMessage());		
	}
%>
