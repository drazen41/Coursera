import java.util.ArrayList;

public class MaxFeeTxHandler {
	public UTXOPool utxoPool;
	public MaxFeeTxHandler(UTXOPool utxoPool) {
		this.utxoPool = utxoPool ;
	}
	public Transaction[] handleTxs(Transaction[] possibleTxs) {
		Transaction[] transactions = new Transaction[1];
		double txFee = 0;
		double maxFee = 0;
		for (Transaction transaction : possibleTxs) {
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
			if (txFee > maxFee) {
				transactions[0] = transaction;
			}
		}
		
		
		
		return transactions;
	}
}
