import java.awt.List;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;



public class TxHandler {

   public UTXOPool utxoPool ;
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
    	int i = 0;
    	double inputSum = 0;
    	double outputSum = 0;
    	
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
//        				if (input.prevTxHash.length == 0) {
//							return true;
//						}
        				if (output == null) {
//							System.out.println("Input.prevTxHash.length: " + input.prevTxHash.length + "Input.outputIndex: " + input.outputIndex);
        					if (input.prevTxHash.length == 1) {
								return true; // genesis 
							}
						}
        				usedOutputs.add(output);
        			}
        			inputSum += output.value ;
        			
        			Transaction.Output output2 = tx.getOutput(input.outputIndex );
        			
        			
        			PublicKey publicKey = output.address;
        			byte[] message = tx.getRawDataToSign(i);
        			byte[] signature = input.signature;
        			try {
						boolean verify = Crypto.verifySignature(publicKey, message, signature);
						if (!verify) return false;
//						System.out.println(input.signature.toString() + "-> sig, " + i + "->brojac");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e.getMessage());
					}
        			
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
			ArrayList<Transaction.Output > outputs2 = new ArrayList<Transaction.Output>();
//			int outputIndex = 0;
			for (Transaction.Output  output : outputs) {
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
    	// 1. Find output in utxoPool and add to spentUtxos for removing from utxoPool, outputs
    	
    	ArrayList<Transaction> goodTransactions = new ArrayList<Transaction>();
    	UTXOPool utxoPoolAdd = new UTXOPool();
    	UTXOPool utxoPoolRemove = new UTXOPool();
    	int outputIndex = 0;
    	for (Transaction transaction : possibleTxs) {
    		if (isValidTx(transaction)) {
				// Find output in utxoPool
    			for (Transaction.Input input  : transaction.getInputs()) {
    				UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

    				// Removing utxo from pool
    				if (this.utxoPool.contains(utxo)) {
//    					System.out.println("Removing utxo from pool: " + utxo.getTxHash() + ", " + utxo.getIndex());
    					this.utxoPool.removeUTXO(utxo);
    					goodTransactions.add(transaction);
    				}

    				
    				
    			}
    			for (Transaction.Output output : transaction.getOutputs()) {
    				UTXO utxo = new UTXO(transaction.getHash(), outputIndex);
    				utxoPoolAdd.addUTXO(utxo, output );
//    				this.utxoPool.addUTXO(utxo, output);
    				
    				
    				
    				outputIndex++;					
    			}
				
				
				
				
			} 
    		
  		
		}
    	
    	
    	Transaction[] transactions = new Transaction[goodTransactions.size()];
    	int i = 0;
    	for (Transaction transaction2 : goodTransactions) {
			transactions[i] = transaction2 ;
    		i++;
		}
    	
    	
    	return transactions ;
    }

}
