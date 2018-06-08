package app.model.utxo;

import app.model.Address;
import app.model.Coupon;
import app.model.dto.CreateContract;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;

import static app.model.Keyz.GenerateHash;
import static app.utils.DateUtil.GetDateTimeNow;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"contractAddress", "contractName", "createdAt", "coupon"})
public class TransactionUTXO {
    public final Address contractAddress;
    public final String contractName;
    public final String createdAt;
    public final Coupon coupon;

    public TransactionUTXO(CreateContract createTransaction) {
        this.contractAddress = new Address(0, 0);
        this.contractName = createTransaction.name;
        this.createdAt = GetDateTimeNow();

        //for this transaction values are emails
        String[] fields = Arrays.stream(Arrays.copyOf(createTransaction.fields, createTransaction.fields.length))
            .map(x -> x.replaceAll(" ", "").replaceAll(",", ""))
            .filter(x -> x != null && !x.equals(""))
            .toArray(String[]::new);
        ArrayList<String> emails = new ArrayList<>() ;
        for(int j = 2; j < fields.length - 1; j++){
            emails.add(fields[j]);
        }
        this.coupon = new Coupon(fields[0], fields[1], emails.toArray(new String[fields.length - 3]), fields[fields.length - 1]);

    }

    public static TransactionUTXO MakeTransactionUTXO(CreateContract createContract, String type) {
        return new TransactionUTXO(new CreateContract("tw@gmail.com", GenerateHash("tw@gmail.com", 6), type + createContract.name, createContract.fields));
    }
}
