public class ReportDefinition {
    private Long topPerformersThreshold;
    private boolean userExperienceMultiplier;
    private Long periodLimit;

    public ReportDefinition() {
    }

    public Long getTopPerformersThreshold() {
        return topPerformersThreshold;
    }

    public void setTopPerformersThreshold(Long topPerformersThreshold) {
        this.topPerformersThreshold = topPerformersThreshold;
    }

    public boolean isUserExperienceMultiplier() {
        return userExperienceMultiplier;
    }

    public void setUserExperienceMultiplier(boolean userExperienceMultiplier) {
        this.userExperienceMultiplier = userExperienceMultiplier;
    }

    public Long getPeriodLimit() {
        return periodLimit;
    }

    public void setPeriodLimit(Long periodLimit) {
        this.periodLimit = periodLimit;
    }
}
