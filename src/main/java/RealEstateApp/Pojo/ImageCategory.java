package RealEstateApp.Pojo;

public enum ImageCategory {
    PROFILE,          // tenant profile pic
    MAINTENANCE,      // maintenance request images (contextId = requestId)
    PROPERTY,         // property photos (contextId = propertyId)
    COMPANY_LOGO,     // company logo
    COMPANY_BRANDING  // background / other branding
}
