package com.oilfield.logix.crawler;

/**
 * Object representing wells
 *
 * @author Jordan Sanderson
 */
public class Well {

    private int id;
    private String operaterName;
    private String fieldName;
    private String leaseName;
    private String rrcGasId;
    private String wellNumber;
    private String apiNo;
    private String rrcDistrictNo;
    private String completionType;
    private String wellType;
    private String county;
    private String drillingPermitNumber;
    private String wellBoreProfile;
    private String fieldNumber;
    private String filingPurpose;
    private String submissionDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setLeaseName(String leaseName) {
        this.leaseName = leaseName;
    }

    public void setRrcGasId(String rrcGasId) {
        this.rrcGasId = rrcGasId;
    }

    public void setWellNumber(String wellNumber) {
        this.wellNumber = wellNumber;
    }

    public void setApiNo(String apiNo) {
        this.apiNo = apiNo;
    }

    public void setRrcDistrictNo(String rrcDistrictNo) {
        this.rrcDistrictNo = rrcDistrictNo;
    }

    public void setCompletionType(String completionType) {
        this.completionType = completionType;
    }

    public void setWellType(String wellType) {
        this.wellType = wellType;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setDrillingPermitNumber(String drillingPermitNumber) {
        this.drillingPermitNumber = drillingPermitNumber;
    }

    public void setWellBoreProfile(String wellBoreProfile) {
        this.wellBoreProfile = wellBoreProfile;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public void setFilingPurpose(String filingPurpose) {
        this.filingPurpose = filingPurpose;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    private String approvalDate;
    private String completionDate;
    private String w2Date;
    private String w15Date;
    private String l1HeaderDate;
    private String directionalSurveyMWDDate;
    private String directionSurveyGyroDate;

    public Well(int id, String approvalDate, String operaterName, String completionType,
            String fieldName, String completionDate, String leaseName, String filingPurpose,
            String rrcDistrictNo, String wellType, String rrcGasId, String county,
            String wellNumber, String drillingPermitNumber, String apiNo, String wellBoreProfile,
                String submissionDate, String fieldNumber) {
        this.id = id;
        this.operaterName = operaterName;
        this.fieldName = fieldName;
        this.leaseName = leaseName;
        this.rrcGasId = rrcGasId;
        this.filingPurpose = filingPurpose;
        this.wellNumber = wellNumber;
        this.apiNo = apiNo;
        this.rrcDistrictNo = rrcDistrictNo;
        this.completionType = completionType;
        this.wellType = wellType;
        this.county = county;
        this.drillingPermitNumber = drillingPermitNumber;
        this.wellBoreProfile = wellBoreProfile;
        this.fieldNumber = fieldNumber;
        this.submissionDate = submissionDate;
        this.approvalDate = approvalDate;
        this.completionDate = completionDate;
    }

    public Well(int id, String approvalDate, String operaterName, String completionType,
                String fieldName, String completionDate, String leaseName, String filingPurpose,
                String rrcDistrictNo, String wellType, String rrcGasId, String county,
                String wellNumber, String drillingPermitNumber, String apiNo, String wellBoreProfile,
            String submissionDate, String fieldNumber, String w2Date, String w15Date,
            String l1HeaderDate, String directionalSurveyMWDDate, String directionSurveyGyroDate) {
        this.id = id;
        this.operaterName = operaterName;
        this.fieldName = fieldName;
        this.leaseName = leaseName;
        this.rrcGasId = rrcGasId;
        this.filingPurpose = filingPurpose;
        this.wellNumber = wellNumber;
        this.apiNo = apiNo;
        this.rrcDistrictNo = rrcDistrictNo;
        this.completionType = completionType;
        this.wellType = wellType;
        this.county = county;
        this.drillingPermitNumber = drillingPermitNumber;
        this.wellBoreProfile = wellBoreProfile;
        this.fieldNumber = fieldNumber;
        this.submissionDate = submissionDate;
        this.approvalDate = approvalDate;
        this.completionDate = completionDate;
        this.w2Date = w2Date;
        this.w15Date = w15Date;
        this.l1HeaderDate = l1HeaderDate;
        this.directionalSurveyMWDDate = directionalSurveyMWDDate;
        this.directionSurveyGyroDate = directionSurveyGyroDate;

    }

    public int getId() {
        return id;
    }

    public String getOperaterName() {
        return operaterName;
    }

    public String getRrcDistrictNo() {
        return rrcDistrictNo;
    }

    public String getApiNo() {
        return apiNo;
    }

    public String getCompletionType() {
        return completionType;
    }

    public String getWellType() {
        return wellType;
    }

    public String getWellBoreProfile() {
        return wellBoreProfile;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getLeaseName() {
        return leaseName;
    }

    public String getRrcGasId() {
        return rrcGasId;
    }

    public String getWellNumber() {
        return wellNumber;
    }

    public String getCounty() {
        return county;
    }

    public String getDrillingPermitNumber() {
        return drillingPermitNumber;
    }

    public String getFieldNumber() {
        return fieldNumber;
    }

    public String getFilingPurpose() {
        return filingPurpose;
    }

    public String getW2Date() {
        return w2Date == null ? "null" : w2Date;
    }

    public String getL1HeaderDate() {
        return l1HeaderDate == null ? "null" : l1HeaderDate;
    }

    public void setL1HeaderDate(String l1HeaderDate) {
        this.l1HeaderDate = l1HeaderDate;
    }

    public void setW2Date(String w2Date) {
        this.w2Date = w2Date;
    }

    public String getW15Date() {
        return w15Date == null ? "null" : w15Date;
    }

    public void setW15Date(String w15Date) {
        this.w15Date = w15Date;
    }

    public String getDirectionalSurveyMWDDate() {
        return directionalSurveyMWDDate == null ? "null" : directionalSurveyMWDDate;
    }

    public void setDirectionalSurveyMWDDate(String directionalSurveyMWDDate) {
        this.directionalSurveyMWDDate = directionalSurveyMWDDate;
    }

    public String getDirectionSurveyGyroDate() {
        return directionSurveyGyroDate == null ? "null" : directionSurveyGyroDate;
    }

    public void setDirectionSurveyGyroDate(String directionSurveyGyroDate) {
        this.directionSurveyGyroDate = directionSurveyGyroDate;
    }



    public String[] asCsvEntry() {
        String[] entry = {String.valueOf(id), approvalDate, operaterName, completionType,
                fieldName, completionDate, leaseName, filingPurpose,
                rrcDistrictNo, wellType, rrcGasId, county,
                wellNumber, drillingPermitNumber, apiNo, wellBoreProfile,
                submissionDate, fieldNumber, getW2Date(), getW15Date(), getL1HeaderDate(), getDirectionalSurveyMWDDate(), getDirectionSurveyGyroDate()
        };
        return entry;
    }

    public enum Forms {
        W2("W-2"), G2("G-2"), W15("W-15"), L1HEADER("L-1 Header"), DIRECTIONAL_SURVEY_MWD(
                "Directional Survey - MWD"), DIRECTIONAL_SURVEY_GYRO("Directional Survey - Gyro");

        public String type;

        Forms(String type) {
            this.type = type;
        }

        //From String method will return you the Enum for the provided input string
        public static Forms fromString(String parameterName) {
            if (parameterName != null) {
                for (Forms objType : Forms.values()) {
                    if (parameterName.equalsIgnoreCase(objType.type)) {
                        return objType;
                    }
                }
            }
            return null;
        }
    }
}
