package vo;

import java.io.Serializable;
import java.util.Objects;

public class SellerOrderVo implements Serializable{
    private String sellerId;
    private float payment;

    @Override
    public String toString() {
        return "SellerOrderVo{" +
                "sellerId='" + sellerId + '\'' +
                ", payment=" + payment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerOrderVo that = (SellerOrderVo) o;
        return Float.compare(that.payment, payment) == 0 &&
                Objects.equals(sellerId, that.sellerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sellerId, payment);
    }

    public String getSellerId() {

        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }
}
