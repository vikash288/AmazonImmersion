package com.amazonbyod.listclass;

import java.util.Date;

public class Companyannouncements {
	
	String announcementId;
	String companyID;
	Date announcementDate;
	String announcemnType;
	String announcemnBy;
	String announcemnFrom;
	String documentDoc;
	
	public Companyannouncements(String announcementId, String companyID, Date announcementDate, String announcemnType,
			String announcemnBy, String announcemnFrom, String documentDoc) {
		
		this.announcementId=announcementId;
		this.companyID=companyID;
		this.announcementDate=announcementDate;
		this.announcemnType=announcemnType;
		this.announcemnBy=announcemnBy;
		this.announcemnFrom=announcemnFrom;
		this.documentDoc=documentDoc;
		
	}
	
	public String getAnnouncementId() {
		return announcementId;
	}

	public String getCompanyID() {
		return companyID;
	}

	public Date getAnnouncementDate() {
		return announcementDate;
	}

	public String getAnnouncemnType() {
		return announcemnType;
	}

	public String getAnnouncemnBy() {
		return announcemnBy;
	}

	public String getAnnouncemnFrom() {
		return announcemnFrom;
	}

	public String getDocumentDoc() {
		return documentDoc;
	}


}
