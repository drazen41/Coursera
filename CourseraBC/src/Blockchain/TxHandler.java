package Blockchain;
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
    	int i = 0;
    	double inputSum = 0;
    	double outputSum = 0;
    	if (tx.isCoinbase()) {
			return true;
		}
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
        					else { // transaction which is using output from previos tx
								
							}
						}
        				else {
        					usedOutputs.add(output);
        					inputSum += output.value ;
						}
        				
        			}
        			
        			
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

    public boolean isValidTxClean(Transaction tx) {
        // IMPLEMENT THIS
    	boolean ok = true;   	   	
    	ArrayList<Transaction.Input> inputs = tx.getInputs();
    	ArrayList<Transaction.Output > outputs = tx.getOutputs();
    	int i = 0;
    	double inputSum = 0;
    	double outputSum = 0;
    	
    	
    	
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
    	ArrayList<Transaction> validTransactions = new ArrayList<Transaction>(); 
    	UTXOPool utxoPoolUsed = new UTXOPool();
    	UTXOPool utxoPoolPossible = new UTXOPool(this.utxoPool);
    	// Adding all outputs to poolPossible
    	for (Transaction transaction : possibleTxs) {
    		if (transaction == null) continue;
    		int outputIndex = 0;
    		for (Transaction.Output output : transaction.getOutputs()) {
				UTXO utxo = new UTXO(transaction.getHash(), outputIndex);
				utxoPoolPossible.addUTXO(utxo, transaction.getOutput(outputIndex));
				outputIndex++;
			}
			
		}
    	
    	
    	
    	boolean ok = true;
    	for (Transaction transaction : possibleTxs) {			
    		if (transaction == null) continue;		
    		int outputIndex = 0;				
    		for (Transaction.Input input : transaction.getInputs()) {
				UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
				if (utxoPoolPossible.contains(utxo)) {
					utxoPoolPossible.removeUTXO(utxo);
					utxoPoolUsed.addUTXO(utxo, transaction.getOutput(outputIndex));
					outputIndex++;
				}
				else {
					if (utxoPoolUsed.contains(utxo )) {
						UTXO utxo2 = new UTXO(transaction.getHash(), outputIndex);
						Transaction.Output output = utxoPoolPossible.getTxOutput(utxo2);
						if (utxoPoolPossible.contains(utxo2)) {
							utxoPoolPossible.removeUTXO(utxo2);
						}
						ok = false;
						outputIndex++;
						continue;
					}
					
				}
				
				
				
			}

    		
    		if (ok) {
    			goodTransactions.add(transaction);   	
    			
    		}
    		else {
				ok = true;
			}

		}
    	
    	for (Transaction tx : goodTransactions) {			
//    		validTransactions.add(tx);
    		if (isValidTx(tx)) {
    			validTransactions.add(tx);
			}
    		else {
				for (Transaction.Input input : tx.getInputs()) {
				UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
					if (utxoPoolPossible.contains(utxo)) {
						utxoPoolPossible.removeUTXO(utxo);
					}
					if (utxoPoolUsed.contains(utxo)) {
						utxoPoolUsed.removeUTXO(utxo);
					}
				}
			}
		}
    	this.utxoPool = new UTXOPool();
    	for (UTXO utxo : utxoPoolPossible.getAllUTXO()) {
    		Transaction.Output output = utxoPoolPossible.getTxOutput(utxo);
//    		System.out.println(output.address.toString() + ", " + output.value );
			this.utxoPool.addUTXO(utxo,output);
		}
    	
    	

    	
    	
    	Transaction[] transactions = new Transaction[goodTransactions.size()];
    	int i = 0;
    	for (Transaction transaction2 : validTransactions) {
			transactions[i] = transaction2 ;
    		i++;
		}
    	
    	
    	return transactions ;
    }
    public UTXOPool  getUTXOPool() {
		return this.utxoPool;
	}

}
