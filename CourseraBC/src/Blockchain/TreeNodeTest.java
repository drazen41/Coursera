package Blockchain;



import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import Blockchain.Main.Tx;

public class TreeNodeTest {
	public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		 KeyPair pk_scrooge = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     KeyPair pk_alice   = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     KeyPair pk_bob     = KeyPairGenerator.getInstance("RSA").generateKeyPair();
	     
	     
	    Block genesis = new Block(null, pk_scrooge.getPublic());
	 	genesis.finalize();
	 	TreeNode root = new TreeNode(genesis);
	 	
	 	 
	 	Block block1 = new Block(genesis.getHash(), pk_alice.getPublic());
	 	Tx tx1 = new Tx();
       tx1.addInput(genesis.getCoinbase().getHash(), 0);
       tx1.addOutput(5, pk_alice.getPublic());
       tx1.addOutput(10, pk_alice.getPublic());
       tx1.addOutput(7, pk_alice.getPublic());
       tx1.signTx(pk_scrooge.getPrivate(), 0);
		block1.addTransaction(tx1);
		block1.finalize();
		TreeNode treeNode1  = new TreeNode(block1);
		root.setChild(treeNode1);
		
		Block block11 = new Block(block1.getHash(), pk_bob.getPublic());
	 	Tx tx2 = new Tx();
       tx2.addInput(block1.getCoinbase().getHash(), 0); 
       tx2.addOutput(2, pk_scrooge.getPublic());
       tx2.addOutput(3, pk_scrooge.getPublic());
       tx2.addOutput(4, pk_scrooge.getPublic());
       tx2.signTx(pk_alice.getPrivate(), 0);
		block11.addTransaction(tx2);
		block11.finalize();
		TreeNode treeNode11  = new TreeNode(block11);
		treeNode1.setChild(treeNode11);
		
		Block block12 = new Block(block1.getHash(), pk_bob.getPublic()); //fork, double-spend ???
	 	Tx tx3 = new Tx();
       tx3.addInput(block1.getCoinbase().getHash(), 0); 
       tx3.addOutput(5, pk_scrooge.getPublic());
       tx3.addOutput(6, pk_scrooge.getPublic());
       tx3.addOutput(7, pk_scrooge.getPublic());
       tx3.signTx(pk_alice.getPrivate(), 0);
		block12.addTransaction(tx3);
		block12.finalize();
		TreeNode treeNode12  = new TreeNode(block12);
		treeNode1.setChild(treeNode12);
		
		Block block111 = new Block(block11.getHash(), pk_scrooge.getPublic()); // height 4, 
	 	Tx tx4 = new Tx();
       tx4.addInput(block11.getCoinbase().getHash(), 0); 
       tx4.addOutput(1, pk_alice.getPublic());
       tx4.addOutput(3, pk_alice.getPublic());
       tx4.addOutput(5, pk_alice.getPublic());
       tx4.signTx(pk_bob.getPrivate(), 0);
		block111.addTransaction(tx4);
		block111.finalize();
		TreeNode treeNode111  = new TreeNode(block111);
		treeNode11.setChild(treeNode111);
		
		Block block2 = new Block(genesis.getHash(), pk_bob.getPublic()); // height 2
	 	Tx tx5 = new Tx();
       tx5.addInput(genesis.getCoinbase().getHash(), 0);
       tx5.addOutput(1, pk_alice.getPublic());
       tx5.addOutput(2, pk_alice.getPublic());      
       tx5.signTx(pk_scrooge.getPrivate(), 0);
		block2.addTransaction(tx5);
		block2.finalize();
		TreeNode treeNode2  = new TreeNode(block2);
		root.setChild(treeNode2);
		
		Block block21 = new Block(block2.getHash(), pk_scrooge.getPublic()); // height 3
	 	Tx tx6 = new Tx();
       tx6.addInput(block2.getCoinbase().getHash(), 0);
       tx6.addOutput(1, pk_alice.getPublic());
       tx6.addOutput(7, pk_alice.getPublic());      
       tx6.signTx(pk_bob.getPrivate(), 0);
		block21.addTransaction(tx6);
		block21.finalize();
		TreeNode treeNode21  = new TreeNode(block21);
		treeNode2.setChild(treeNode21);
		
		Block block211 = new Block(block21.getHash(), pk_alice.getPublic()); // height 4
	 	Tx tx7 = new Tx();
       tx7.addInput(block21.getCoinbase().getHash(), 0);
       tx7.addOutput(2, pk_scrooge.getPublic());
       tx7.addOutput(8, pk_scrooge.getPublic());      
       tx7.signTx(pk_bob.getPrivate(), 0);
		block211.addTransaction(tx7);
		block211.finalize();
		TreeNode treeNode211  = new TreeNode(block211);
		treeNode21.setChild(treeNode211);
		
		
		
		TreeNode<Block> returnNode = root.getMaxHeightNode(root);
		Integer maxHeight = returnNode.getMaxHeight(); // ???
		Integer maxHeight2 = root.getHeight(root); // ok
		Block maxHeightBlock = returnNode.getBlock();
		UTXOPool maxUtxoPool = returnNode.txHandler.getUTXOPool();
	}
	
	
	
}
