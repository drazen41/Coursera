import java.math.BigInteger;
import java.security.*;

public class Test {


	public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException {
		/*
         * Generate key pairs
         */
        KeyPair pk_scrooge = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair pk_alice   = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair pk_bob = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        
        // Root transaction
        Main.Tx tx = new Main.Tx();
        tx.addOutput(10, pk_scrooge.getPublic());
        // This value has no meaning, but tx.getRawDataToSign(0) will access it in prevTxHash;
        byte[] initialHash = BigInteger.valueOf(0).toByteArray();
        tx.addInput(initialHash, 0);
        tx.signTx(pk_scrooge.getPrivate(), 0);
     // The transaction output of the root transaction is the initial unspent output.
        UTXOPool utxoPool = new UTXOPool();
        UTXO utxo = new UTXO(tx.getHash(),0);
        System.out.println("Genesis utxo: " + tx.getHash() + ", " + utxo.getIndex());
        utxoPool.addUTXO(utxo, tx.getOutput(0));
        
        /*  
         * Set up a test Transaction
         */
        Main.Tx tx2 = new Main.Tx();

        // the Transaction.Output of tx at position 0 has a value of 10
        tx2.addInput(tx.getHash(), 0);      
        // I split the coin of value 10 into 3 coins and send all of them for simplicity to
        // the same address (Alice)
        tx2.addOutput(5, pk_alice.getPublic());
        tx2.addOutput(3, pk_alice.getPublic());
        tx2.addOutput(2, pk_alice.getPublic());        
        // Signing transaction input from tx2 at position 0
        tx2.signTx(pk_scrooge.getPrivate(), 0);
        UTXO utxo2 = new UTXO(tx2.getHash(),1); // 5 coins of Alice
        System.out.println("OK utxo: " + tx2.getHash() + ", " + utxo2.getIndex());
        utxoPool.addUTXO(utxo2, tx2.getOutput(0));
        UTXO utxo3 = new UTXO(tx2.getHash(),2); // 3 coins of Alice
        System.out.println("OK utxo: " + tx2.getHash() + ", " + utxo3.getIndex());
        utxoPool.addUTXO(utxo3, tx2.getOutput(1));
        UTXO utxo4 = new UTXO(tx2.getHash(),3); // 2 coins of Alice
        System.out.println("OK utxo: " + tx2.getHash() + ", " + utxo4.getIndex());
        utxoPool.addUTXO(utxo4, tx2.getOutput(2));
//        UTXO utxo6 = new UTXO(tx2.getHash(),4); 
//        System.out.println("Bad utxo: " + tx2.getHash() + ", " + utxo6.getIndex());
//        utxoPool.addUTXO(utxo6, tx2.getOutput(2)); //utxo6 claims output by utxo4 in same tx
        
        // Double spend transaction - trying to spend coins from tx which tx2 already had spent        
        Main.Tx tx3 = new Main.Tx();
        tx3.addInput(tx.getHash(), 0);
        tx3.addOutput(5, pk_bob.getPublic());
        tx3.signTx(pk_scrooge.getPrivate(), 0);
        UTXO utxo5 = new UTXO(tx3.getHash(),4); 
        System.out.println("Double spend utxo: " + tx3.getHash() + ", " + utxo5.getIndex());
//        utxoPool.addUTXO(utxo5, tx3.getOutput(0));
        
        // Transaction can reference another in the same block        
        Main.Tx k = new Main.Tx();
        k.addInput(tx2.getHash(), 0);
        k.addOutput(0.5, pk_scrooge.getPublic());
        k.addOutput(0.3, pk_bob.getPublic());
        
        
        
        Main.Tx q = new Main.Tx();
        q.addInput(tx2.getHash(), 0);
       
        
        
        Transaction[] transactions = new Transaction[10];
        transactions[0] = tx;
        transactions[1] = tx2;
        transactions[2] = tx3;
        
        /*
         * Start the test
         */
        // Remember that the utxoPool contains a single unspent Transaction.Output which is
        // the coin from Scrooge.
        TxHandler txHandler = new TxHandler(utxoPool);
        
        System.out.println("txHandler.isValidTx(tx3) returns: " + txHandler.isValidTx(tx2));
        System.out.println("TxHandlers UTXOpool  contains: " + txHandler.utxoPool.getAllUTXO().size() + " UTXO(s)");
        System.out.println("txHandler.handleTxs(transactions) returns: " +
                txHandler.handleTxs(transactions).length +  " transaction(s)");
        System.out.println("TxHandlers UTXOpool contains: " + txHandler.utxoPool.getAllUTXO().size() + " UTXO(s)");
//        System.out.println("Second call of txHandler.handleTxs(transactions) returns: " + txHandler.handleTxs(transactions).length +  " transaction(s)");
//        System.out.println("Second call TxHandlers UTXOpool contains: " + txHandler.utxoPool.getAllUTXO().size() + " UTXO(s)");
	}
	
}
