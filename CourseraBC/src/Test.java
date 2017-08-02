import java.security.*;


public class Test {


	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair kPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey  = kPair.getPublic();
		PrivateKey privateKey = kPair.getPrivate();
		Transaction trans = new Transaction();
		trans.addOutput(5555, publicKey );
		trans.finalize();
		System.out.println(trans.getHash());
		UTXO utxo = new UTXO(trans.getHash(), 0);
		UTXOPool utxoPool = new UTXOPool();
		utxoPool.addUTXO(utxo, trans.getOutput(0));
		TxHandler handler = new TxHandler(utxoPool);
		boolean valid = handler.isValidTx(trans);
		Transaction[] transactions = new Transaction[100];
		transactions[0] = trans;
		
		Transaction[] validTransactions = handler.handleTxs(transactions );
		kPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey2 = kPair.getPublic();
//		Transaction.Input input = new Transaction.
		
		
	}

}
