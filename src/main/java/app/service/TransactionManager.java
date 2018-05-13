package app.service;

import app.model.Transaction;
import app.model.dto.CreateContract;
import app.model.utxo.TransactionUTXO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.utxo.TransactionUTXO.MakeTransactionUTXO;

public class TransactionManager {

    public static final ConcurrentMap<String, Transaction> Transactions = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, TransactionUTXO> TransactionUTXOs = new ConcurrentHashMap<>();

    public static void CreateTransactionUTXO(CreateContract createContract) {
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(createContract, "Create-");
        TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
    }
    public static void CreateCreateTransactionUTXO(CreateContract createContract) {
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(createContract, "Create-");
        TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
    }
    public static void CreateCompleteTransactionUTXO(CreateContract createContract) {
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(createContract, "Complete-");
        TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
    }

    public static void CreateTransactions(List<Transaction> transactions) {
        transactions.forEach(transaction -> Transactions.putIfAbsent(transaction.contractName, transaction));
    }
}
