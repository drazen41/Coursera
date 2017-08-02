import java.awt.List;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;



public class TxHandler {

   private UTXOPool utxoPool ;
	/**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
    	this.utxoPool = new UTXOPool(utxoPool) ;    	
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
    	boolean ok = true;   	   	
    	ArrayList<Transaction.Input> inputs = tx.getInputs();
    	ArrayList<Transaction.Output > outputs = tx.getOutputs();
    	
    	double inputSum = 0;
    	double outputSum = 0;
    	int i = 0;
    	ArrayList<Transaction.Output> usedOutputs = new ArrayList<Transaction.Output>();
    	if (inputs != null) {
    		for (Transaction.Input input  :  inputs) {
    			try {
    				UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
        			Transaction.Output output = this.utxoPool.getTxOutput(utxo);
        			if (usedOutputs.contains(output )) {
        				return false;
        			}
        			else {
        				usedOutputs.add(output);
        			}
        			inputSum += output.value ;
        			
        			Transaction.Output output2 = tx.getOutput(input.outputIndex );
        			
        			
//        			PublicKey publicKey = output2.address;
//        			byte[] message = tx.getRawDataToSign(i);
//        			byte[] signature = input.signature;
//        			try {
//						boolean verify = Crypto.verifySignature(publicKey, message, signature);
//						System.out.println(input.signature.toString() + "-> sig, " + i + "->brojac");
//					} catch (Exception e) {
//						// TODO: handle exception
//						System.out.println(e.getMessage());
//					}
        			
        				//return false;
        				
        			
				} catch (Exception e) {
					// TODO: handle exception
//					System.out.println(input.prevTxHash.toString());
					return false;
				}
    			i++;
    		}
    	}
		if (outputs != null) {
			
//			for (int j = 0; j < outputs.size(); j++) {
//				UTXO utxo = new UTXO(tx.getHash(), j);
//				if (!this.utxoPool.contains(utxo)) {
//					return false;
//				}
////				Transaction.Output output = tx.getOutput(i);
////				if (output.value < 0) return false;
////				outputSum += output.value ;
//			}
			for (Transaction.Output  output : outputs) {
//				if (output.value < 0) {
//					System.out.println(output.value );
//				}
				if (output.value < 0) return false;
				outputSum += output.value ;
				
				
			}
		}
//		
    	if (inputSum < outputSum) return false;
    	
    	return ok;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    	Transaction[] transactions = new Transaction[possibleTxs.length];
    	int i = 0;
    	UTXOPool utxoPool = new UTXOPool();
    	ArrayList<UTXO> utxos = this.utxoPool.getAllUTXO();
    	for (Transaction transaction : possibleTxs) {
			if(!isValidTx(transaction )) {
				ArrayList<Transaction.Output> outputs = transaction.getOutputs();
				for (int j = 0; j < outputs.size(); j++) {
					UTXO utxo = new UTXO(transaction.getHash(), j);
					if (this.utxoPool.contains(utxo)) {
						this.utxoPool.removeUTXO(utxo);
					}
				}
			}
			else {
				transactions[i] = transaction;
				i++;
			}
		}
    	
    	return transactions ;
    }

}
