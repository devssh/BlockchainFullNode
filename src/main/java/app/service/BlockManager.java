package app.service;

import app.model.Block;
import app.model.BlockDetails;
import app.model.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Block.BlockSign;
import static app.service.ContractManager.ContractUTXOs;
import static app.service.ContractManager.Contracts;
import static app.service.ContractManager.CreateContracts;
import static app.utils.DateUtil.GetDateTimeNow;
import static app.utils.FileUtil.StoreBlockchainBlock;
import static app.utils.FileUtil.StoreTransactionsInBlock;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static app.utils.SignatureUtils.Verify;

public class BlockManager {
    public static final ConcurrentMap<Integer, Block> BLOCKCHAIN = new ConcurrentHashMap<>();
    public static final ConcurrentMap<Integer, String> BLOCKCHAIN_TRANSACTIONS = new ConcurrentHashMap<>();

    public static void MineBlock() {
        //Extract to mine node
        if (ContractUTXOs.keySet().size() == 0) {
            return;
        }

        int blockDepth = BLOCKCHAIN.keySet().size();

        Set<String> keySet = ContractUTXOs.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);

        List<String> merkleDataList = new ArrayList<>();
        List<Contract> contracts = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            Contract contract = new Contract(ContractUTXOs.get(keys[i]), blockDepth, i);
            contracts.add(contract);
            //TODO: Test to see if stream map collect gives same data in sequence
            merkleDataList.add(ToJSON(contract));
        }
        String merkleData = ToJSON(merkleDataList.toArray());


//      same miner as the one who mines the block inside block constructor
        String merkleRoot = BlockSign(merkleData);
        String genesisStringToProveMinimumDateTime = "Mark Zuckerberg answers to congress over Cambridge Analytica";
        String previousSign = blockDepth == 0 ? genesisStringToProveMinimumDateTime : BLOCKCHAIN.get(blockDepth - 1).sign;

        BlockDetails blockDetails = new BlockDetails(0, blockDepth, GetDateTimeNow(),
                previousSign, merkleRoot, 3, "me", keys.length);

        //Mining logic here
        int difficulty = 3;
        String difficultyString = "";
        String difficultyCharacter = "0";
        for (int i = 0; i < difficulty - 1; i++) {
            difficultyString = difficultyString + difficultyCharacter;
        }
        difficultyString = difficultyString + "=";
        int incrementNonceBy = 1;


        String data = ToJSON(blockDetails);
        Block block = new Block(data);
        while (!block.sign.substring(block.sign.length() - difficulty, block.sign.length()).equals(difficultyString)) {
            blockDetails.nonce = blockDetails.nonce + incrementNonceBy;
            blockDetails.blockCreatedAt = GetDateTimeNow();
            data = ToJSON(blockDetails);
            block = new Block(data);
        }


        keySet.forEach(ContractUTXOs::remove);
        BLOCKCHAIN.putIfAbsent(blockDepth, block);
        BLOCKCHAIN_TRANSACTIONS.putIfAbsent(blockDepth, merkleData);
        //if the above 3 lines are successful do things like sending email here if in contract

        CreateContracts(contracts);
        StoreBlockchainBlock(block);
        StoreTransactionsInBlock(contracts);


    }

    public static void CreateBlockAndVerify(String jsonBlock) {
        Block block = FromJSON(jsonBlock, Block.class);
        BlockDetails blockDetails = block.blockData;
        block.data = ToJSON(block.blockData);
        BLOCKCHAIN.putIfAbsent(blockDetails.depth, block);
        if (!Verify(block)) {
            throw new IllegalArgumentException("Blocks don't match up, blockchain in inconsistent state, re-sync from external source");
        }
    }

    public static void CreateAndVerifyTransactions(String merkleData) {
        Contract[] contracts = FromJSON(merkleData, Contract[].class);
        Integer blockDepth = contracts[0].address.blockDepth;
        BLOCKCHAIN_TRANSACTIONS.putIfAbsent(blockDepth, merkleData);
        for (int i = 0; i < contracts.length; i++) {
            Contract contract = contracts[i];
            Contracts.putIfAbsent(contract.name, contract);
            if (contract.address.transactionDepth != i) {
                throw new IllegalArgumentException("Transactions don't match up, blockchain in inconsistent state, re-sync from external source");
            }
        }

        //Make sure blockchain is populated with CreateBlockAndVerify before running this part
        Block block = BLOCKCHAIN.get(blockDepth);
        BlockDetails blockDetails = FromJSON(block.data, BlockDetails.class);

        if (Verify(merkleData, block.publicKey, blockDetails.merkleRoot)) {
            throw new IllegalArgumentException("Merkle roots don't match up, blockchain in inconsistent state, re-sync from external source");
        }
    }

}
