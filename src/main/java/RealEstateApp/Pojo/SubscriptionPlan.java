package RealEstateApp.Pojo;

public enum SubscriptionPlan {
	STARTER("Starter", 30, 10),
    GROWTH("Growth", 60, 30),
    PRO("Pro", 150, 100);
    
    private final String displayName;
    private final int monthlyUsd;          // 30, 60, 150
    private final int maxProperties;       // example limits

    SubscriptionPlan(String displayName, int monthlyUsd, int maxProperties) {
        this.displayName = displayName;
        this.monthlyUsd = monthlyUsd;
        this.maxProperties = maxProperties;
    }

    public String getDisplayName() { return displayName; }
    public int getMonthlyUsd() { return monthlyUsd; }
    public int getMaxProperties() { return maxProperties; }

    // helpful for UI
    public String getPriceLabel() { return "$" + monthlyUsd + "/mo"; }
}