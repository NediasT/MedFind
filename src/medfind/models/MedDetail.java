package medfind.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MedDetail {

    private MedItem item;
    private int amount;

    public BigDecimal getPrice(){
        return new BigDecimal(amount).multiply(item.getPrice());
    }

    @Override
    public String toString() {
        return this.item.getName();
    }
}
