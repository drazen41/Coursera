import java.security.PublicKey;
import java.util.ArrayList;

public class MaxFeeTxHandler {
	public UTXOPool utxoPool;
	private Transaction[] transactions = null;
	private double maxTxFee = 0;
	public MaxFeeTxHandler(UTXOPool utxoPool) {
		this.utxoPool = utxoPool ;
		
	}
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
//	        				if (input.prevTxHash.length == 0) {
//								return true;
//							}
	        				if (output == null) {
//								System.out.println("Input.prevTxHash.length: " + input.prevTxHash.length + "Input.outputIndex: " + input.outputIndex);
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
//							System.out.println(input.signature.toString() + "-> sig, " + i + "->brojac");
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println(e.getMessage());
						}
	        			
	        				//return false;
	        				
	        			
					} catch (Exception e) {
						// TODO: handle exception
//						System.out.println(input.prevTxHash.toString());
						return false;
					}
	    			i++;
	    		}
	    	}
			if (outputs != null) {
				ArrayList<Transaction.Output > outputs2 = new ArrayList<Transaction.Output>();
//				int outputIndex = 0;
				for (Transaction.Output  output : outputs) {
					if (output.value < 0) return false;
					
					
					
					outputSum += output.value ;
					 
				}
			}
//			
	    	if (inputSum < outputSum) return false;
	    	
	    	return ok;
	    }

	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		
		double txFee = 0;
		ArrayList<Transaction> goodTransactions = new ArrayList<Transaction>();
		for (Transaction transaction : possibleTxs) {
			if (!isValidTx(transaction)) {
				continue;
			}
			goodTransactions.add(transaction);
			for (Transaction.Input input : transaction.getInputs()) {
				UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
				if (this.utxoPool.contains(utxo)) {
					Transaction.Output output = this.utxoPool.getTxOutput(utxo);
					txFee += output.value;
				}
					
			}
			for (Transaction.Output  output : transaction.getOutputs()) {
				txFee -= output.value ;
				
				
			}
			if (txFee > this.maxTxFee) {
				this.maxTxFee = txFee;
				this.transactions = new Transaction[goodTransactions.size()];
				int index = 0;
				for (Transaction tx : goodTransactions) {
					transactions[index] = tx;
					index++;
				}
				
			}
		}
		
		
		
		return transactions;
	}
}
