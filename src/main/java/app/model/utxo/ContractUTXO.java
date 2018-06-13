package app.model.utxo;

import app.model.Coupon;
import app.model.dto.CreateContract;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@AllArgsConstructor
@ToString
public class ContractUTXO {
    public final String name;
    public final String createdAt;
    public final Coupon coupon;

    public ContractUTXO(CreateContract other) {
        this.name = other.name;
        String[] fields = Arrays.stream(Arrays.copyOf(other.fields, other.fields.length))
            .map(x -> x.replaceAll(" ", "").replaceAll(",", ""))
            .filter(x -> x != null && !x.equals(""))
            .toArray(String[]::new);
        ArrayList<String> emails = new ArrayList<>() ;
        for(int j = 2; j < fields.length - 1; j++){
            emails.add(fields[j]);
        }
        this.coupon = new Coupon(fields[0], fields[1], emails.toArray(new String[fields.length - 3]), fields[fields.length - 1]);


        this.createdAt = LocalDateTime.now().toString();
    }

}
