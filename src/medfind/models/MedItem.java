package medfind.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class MedItem {

    private String name;
    private String abbr;
    private BigDecimal price;

    @Override
    public String toString() {
        return this.name;
    }
}
