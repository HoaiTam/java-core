package practice2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * Lớp trừu tượng chứa thông tin và hành vi chung của mọi loại hàng hóa.
 */
public abstract class Goods {
    private final String productCode;
    private final String name;
    private int inventoryQuantity;
    private final BigDecimal unitPrice;

    protected Goods(
            String productCode,
            String name,
            int inventoryQuantity,
            BigDecimal unitPrice) {
        this.productCode = requireText(productCode, "Mã hàng").toUpperCase(Locale.ROOT);
        this.name = requireText(name, "Tên hàng");
        setInventoryQuantity(inventoryQuantity);
        this.unitPrice = requirePositive(unitPrice, "Đơn giá");
    }

    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        if (inventoryQuantity < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho phải lớn hơn hoặc bằng 0.");
        }
        this.inventoryQuantity = inventoryQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getInventoryValue() {
        return unitPrice.multiply(BigDecimal.valueOf(inventoryQuantity));
    }

    public abstract BigDecimal getVatRate();

    public BigDecimal calculateVat() {
        return getInventoryValue()
                .multiply(getVatRate())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public ConsumptionRating evaluateConsumption() {
        return evaluateConsumption(LocalDate.now());
    }

    public abstract ConsumptionRating evaluateConsumption(LocalDate referenceDate);

    protected String baseInformation() {
        return "code='%s', name='%s', quantity=%d, unitPrice=%s, vat=%s, rating='%s'"
                .formatted(
                        productCode,
                        name,
                        inventoryQuantity,
                        unitPrice.toPlainString(),
                        calculateVat().toPlainString(),
                        evaluateConsumption().getDescription());
    }

    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

    private static BigDecimal requirePositive(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " không được null.");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(fieldName + " phải lớn hơn 0.");
        }
        return value;
    }
}
