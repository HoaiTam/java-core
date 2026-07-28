package practice2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Danh sách hàng hóa được lưu bằng mảng theo yêu cầu của bài tập.
 */
public final class DSHH {
    private static final int DEFAULT_CAPACITY = 10;

    private Goods[] goods;
    private int size;

    public DSHH() {
        this(DEFAULT_CAPACITY);
    }

    public DSHH(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Kích thước ban đầu phải lớn hơn 0.");
        }
        goods = new Goods[initialCapacity];
    }

    /**
     * @return true nếu thêm thành công; false nếu mã hàng đã tồn tại.
     */
    public boolean addGoods(Goods newGoods) {
        Objects.requireNonNull(newGoods, "Hàng hóa không được null.");
        if (findByProductCode(newGoods.getProductCode()).isPresent()) {
            return false;
        }
        ensureCapacity();
        goods[size++] = newGoods;
        return true;
    }

    public Optional<Goods> findByProductCode(String productCode) {
        if (productCode == null) {
            return Optional.empty();
        }
        for (int index = 0; index < size; index++) {
            if (goods[index].getProductCode().equalsIgnoreCase(productCode.trim())) {
                return Optional.of(goods[index]);
            }
        }
        return Optional.empty();
    }

    public int getSize() {
        return size;
    }

    public Goods[] getAll() {
        return Arrays.copyOf(goods, size);
    }

    public int getInventoryQuantity(Class<? extends Goods> goodsType) {
        Objects.requireNonNull(goodsType, "Loại hàng không được null.");
        int total = 0;
        for (int index = 0; index < size; index++) {
            if (goodsType.isInstance(goods[index])) {
                total += goods[index].getInventoryQuantity();
            }
        }
        return total;
    }

    public BigDecimal getVatAmount(Class<? extends Goods> goodsType) {
        Objects.requireNonNull(goodsType, "Loại hàng không được null.");
        BigDecimal total = BigDecimal.ZERO;
        for (int index = 0; index < size; index++) {
            if (goodsType.isInstance(goods[index])) {
                total = total.add(goods[index].calculateVat());
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private void ensureCapacity() {
        if (size == goods.length) {
            goods = Arrays.copyOf(goods, goods.length * 2);
        }
    }
}
