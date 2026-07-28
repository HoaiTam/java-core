package practice2;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Electronics extends Goods {
    private static final BigDecimal VAT_RATE = new BigDecimal("0.10");

    private final int warrantyMonths;
    private final double powerKw;

    public Electronics(
            String productCode,
            String name,
            int inventoryQuantity,
            BigDecimal unitPrice,
            int warrantyMonths,
            double powerKw) {
        super(productCode, name, inventoryQuantity, unitPrice);
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException(
                    "Thời gian bảo hành phải lớn hơn hoặc bằng 0.");
        }
        if (!Double.isFinite(powerKw) || powerKw < 0) {
            throw new IllegalArgumentException("Công suất phải lớn hơn hoặc bằng 0.");
        }
        this.warrantyMonths = warrantyMonths;
        this.powerKw = powerKw;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public double getPowerKw() {
        return powerKw;
    }

    @Override
    public BigDecimal getVatRate() {
        return VAT_RATE;
    }

    @Override
    public ConsumptionRating evaluateConsumption(LocalDate referenceDate) {
        if (getInventoryQuantity() < 3) {
            return ConsumptionRating.SELLABLE;
        }
        return ConsumptionRating.NOT_EVALUATED;
    }

    @Override
    public String toString() {
        return "Electronics{%s, warrantyMonths=%d, powerKw=%.2f}"
                .formatted(baseInformation(), warrantyMonths, powerKw);
    }
}
