package practice2;

public enum ConsumptionRating {
    SELLABLE("Bán được"),
    HARD_TO_SELL("Khó bán"),
    SLOW_SELLING("Bán chậm"),
    NOT_EVALUATED("Không đánh giá");

    private final String description;

    ConsumptionRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
