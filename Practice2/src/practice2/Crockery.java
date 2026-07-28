package practice2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class Crockery extends Goods {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.10");

    private final String manufacturer;
    private final LocalDate arrivalDate;

    public Crockery(
            String productCode,
            String name,
            int inventoryQuantity,
            BigDecimal unitPrice,
            String manufacturer,
            LocalDate arrivalDate) {
        super(productCode, name, inventoryQuantity, unitPrice);
        this.manufacturer = requireText(manufacturer, "Nhà sản xuất");
        this.arrivalDate = Objects.requireNonNull(arrivalDate, "Ngày nhập kho không được null.");
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    @Override
    public BigDecimal getVatRate() {
        return VAT_RATE;
    }

    @Override
    public ConsumptionRating evaluateConsumption(LocalDate referenceDate) {
        Objects.requireNonNull(referenceDate, "Ngày đánh giá không được null.");
        long storageDays = ChronoUnit.DAYS.between(arrivalDate, referenceDate);
        if (getInventoryQuantity() > 50 && storageDays > 10) {
            return ConsumptionRating.SLOW_SELLING;
        }
        return ConsumptionRating.NOT_EVALUATED;
    }

    @Override
    public String toString() {
        return "Crockery{%s, manufacturer='%s', arrivalDate=%s}"
                .formatted(baseInformation(), manufacturer, arrivalDate);
    }
}
