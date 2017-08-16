package Blockchain;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import Blockchain.Main.Tx;

public class Test {
	public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		 KeyPair pk_scrooge = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     KeyPair pk_alice   = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     KeyPair pk_bob     = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     
	     
	    Block genesis = new Block(null, pk_scrooge.getPublic());
	 	genesis.finalize();
	 	BlockChain bc = new BlockChain(genesis);
	 	BlockHandler bh = new BlockHandler(bc);
	 	
	 	 
	 	Block block1 = bh.createBlock(pk_alice.getPublic());
	 	Tx tx1 = new Tx();

        // the genesis block has a value of 25
        tx1.addInput(genesis.getCoinbase().getHash(), 0);

        tx1.addOutput(5, pk_alice.getPublic());
        tx1.addOutput(10, pk_alice.getPublic());
        tx1.addOutput(10, pk_alice.getPublic());

        // There is only one (at position 0) Transaction.Input in tx2
        // and it contains the coin from Scrooge, therefore I have to sign with the private key from Scrooge
        tx1.signTx(pk_scrooge.getPrivate(), 0);

		block1.addTransaction(tx1);
		block1.finalize();
		
		System.out.println("Block1 Added ok: " + bh.processBlock(block1)); // Ovdje kreirati UTXO pool
	}
}
