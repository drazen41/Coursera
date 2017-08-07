import java.math.BigInteger;
import java.security.*;


public class Test2 {
	public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException {
		// Key pairs
		KeyPair pk_scrooge = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair pk_alice   = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair pk_bob = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        // Genesis
        Main.Tx tx0 = new Main.Tx();
        tx0.addOutput(10, pk_scrooge.getPublic());        
        // This value has no meaning, but tx.getRawDataToSign(0) will access it in prevTxHash;
        byte[] initialHash = BigInteger.valueOf(0).toByteArray();
        tx0.addInput(initialHash, 0);
        tx0.signTx(pk_scrooge.getPrivate(), 0);
        /* ****************** Scenario 1. - The transaction output of the root transaction is the initial unspent output. */
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(tx0.getHash(),0);
        System.out.println("UTXO to pool: " + utxo.getTxHash() + ", " + utxo.getIndex());
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        Main.Tx tx1 = new Main.Tx();
        // the Transaction.Output of tx at position 0 has a value of 10
        tx1.addInput(tx0.getHash(), 0); 
        tx1.addOutput(5, pk_alice.getPublic());
        tx1.addOutput(3, pk_alice.getPublic());
        tx1.addOutput(2, pk_alice.getPublic());
        tx1.signTx(pk_scrooge.getPrivate(), 0);
        // 1. Remove rootUtxo from utxoPool and addOutputs from tx1 to utxoPool
        /*
         * Start the test
         */
        // Remember that the utxoPool contains a single unspent Transaction.Output which is
        // the coin from Scrooge.
        TxHandler txHandler = new TxHandler(utxoPool);
        System.out.println("txHandler.isValidTx(tx1) returns: " + txHandler.isValidTx(tx1));
        for (UTXO utxo2 : txHandler.utxoPool.getAllUTXO()) {
			Transaction.Output output = txHandler.utxoPool.getTxOutput(utxo2);
			System.out.println(output.address.toString() + ": " + output.value);
//        	System.out.println("UTXO in pool before: " + utxo2.getTxHash() + ", " + utxo2.getIndex());
		}
        System.out.println("txHandler.handleTxs(new Transaction[]{tx1}) returns: " +
            txHandler.handleTxs(new Transaction[]{tx1}).length + " transaction(s)");
        for (UTXO utxo2 : txHandler.utxoPool.getAllUTXO()) {
			Transaction.Output output = txHandler.utxoPool.getTxOutput(utxo2);
			System.out.println(output.address.toString() + ": " + output.value);
//        	System.out.println("UTXO in pool after: " + utxo2.getTxHash() + ", " + utxo2.getIndex());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
