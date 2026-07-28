package practice2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class Food extends Goods {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.05");

    private final LocalDate manufactureDate;
    private final LocalDate expirationDate;
    private final String supplier;

    public Food(
            String productCode,
            String name,
            int inventoryQuantity,
            BigDecimal unitPrice,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            String supplier) {
        super(productCode, name, inventoryQuantity, unitPrice);
        this.manufactureDate = Objects.requireNonNull(
                manufactureDate, "Ngày sản xuất không được null.");
        this.expirationDate = Objects.requireNonNull(
                expirationDate, "Ngày hết hạn không được null.");
        if (!expirationDate.isAfter(manufactureDate)) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau ngày sản xuất.");
        }
        this.supplier = requireText(supplier, "Nhà cung cấp");
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getSupplier() {
        return supplier;
    }

    @Override
    public BigDecimal getVatRate() {
        return VAT_RATE;
    }

    @Override
    public ConsumptionRating evaluateConsumption(LocalDate referenceDate) {
        Objects.requireNonNull(referenceDate, "Ngày đánh giá không được null.");
        if (getInventoryQuantity() > 0 && expirationDate.isBefore(referenceDate)) {
            return ConsumptionRating.HARD_TO_SELL;
        }
        return ConsumptionRating.NOT_EVALUATED;
    }

    @Override
    public String toString() {
        return "Food{%s, manufactureDate=%s, expirationDate=%s, supplier='%s'}"
                .formatted(baseInformation(), manufactureDate, expirationDate, supplier);
    }
}
