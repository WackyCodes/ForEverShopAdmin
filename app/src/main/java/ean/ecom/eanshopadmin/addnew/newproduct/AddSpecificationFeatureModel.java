package ean.ecom.eanshopadmin.addnew.newproduct;

public class AddSpecificationFeatureModel {
    private String featureName;
    private String featureDetails;

    public AddSpecificationFeatureModel(String featureName, String featureDetails) {
        this.featureName = featureName;
        this.featureDetails = featureDetails;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getFeatureDetails() {
        return featureDetails;
    }

    public void setFeatureDetails(String featureDetails) {
        this.featureDetails = featureDetails;
    }
}
