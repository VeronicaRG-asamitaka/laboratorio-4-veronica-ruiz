package mx.com.axity.parque_dinosaurio.zone;

public enum ExperienceType {
    BASIC(1, 3, 20, 10.0),
    PREMIUM(2, 4, 12, 30.0),
    VIP(3, 5, 5, 75.0);

    private final int minScore;
    private final int maxScore;
    private final int maxVisitors;
    private final double entryFee;

    ExperienceType(int minScore, int maxScore, int maxVisitors, double entryFee) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.maxVisitors = maxVisitors;
        this.entryFee = entryFee;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getMaxVisitors() {
        return maxVisitors;
    }

    public double getEntryFee() {
        return entryFee;
    }
}