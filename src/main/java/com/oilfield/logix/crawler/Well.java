package com.oilfield.logix.crawler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Object representing wells
 *
 * @author Jordan Sanderson
 */
public class Well {

    private int id;
    private String operaterName,fieldName,leaseName,rrcGasId,wellNumber,apiNo,rrcDistrictNo,completionType,wellType,county,drillingPermitNumber,wellBoreProfile,fieldNumber;
    private LocalDate submissionDate, approvalDate, completionDate;
    private List<Form> forms;

    public Well(int id, String operaterName, String fieldName, String leaseName, String rrcGasId, String rrcDistrictNo,
                String wellNumber, String apiNo, LocalDate submissionDate, LocalDate approvalDate,
                LocalDate completionDate, String completionType,
            String wellType, String county, String drillingPermitNumber, String wellBoreProfile,
            String fieldNumber) {
        this.id = id;
        this.operaterName = operaterName;
        this.fieldName = fieldName;
        this.leaseName = leaseName;
        this.rrcGasId = rrcGasId;
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
        this.forms = new ArrayList<>();
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
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

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public LocalDate getCompletionDate() {
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


    public String[] asCsvEntry() {
        String[] entry = {String.valueOf(id),operaterName, fieldName, leaseName, rrcGasId, rrcDistrictNo, wellNumber, apiNo,
                submissionDate.toString(), approvalDate.toString(), completionDate.toString(),
                completionType, wellType, county, drillingPermitNumber, wellBoreProfile, fieldNumber
                };
        return entry;
    }

    public static class Form {
        private String type;

        public void setCreation(LocalDate creation) {
            this.creation = creation;
        }

        public void setCertification(Optional<LocalDate> certification) {
            this.certification = certification;
        }

        private LocalDate creation;
        private Optional<LocalDate> certification;

        public Form(String type, LocalDate creation) {
            this.type = type;
            this.creation = creation;
            this.certification = Optional.empty();
        }

        public Form(String type, LocalDate creation, Optional<LocalDate> certification) {
            this.type = type;
            this.creation = creation;
            this.certification = certification;
        }

        public boolean isCertified() {
            return this.certification.isPresent();
        }

        public String getType() {
            return type;
        }

        public LocalDate getCreation() {
            return creation;
        }

        public Optional<LocalDate> getCertification() {
            return certification;
        }

        public String[] asCsvEntry() {
            String[] entry = {type,creation.toString(),certification.isPresent() ? "null" : certification.get().toString()};
            return entry;
        }
    }
}
