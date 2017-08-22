package Blockchain;

import java.awt.List;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.NodeList;

// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.

public class BlockChain_No {
    
	public static final int CUT_OFF_AGE = 10;
//    private TreeMap<String ,Block> blocks;
    public TreeNode<Block> blockChain;
    private TxHandler txHandler = null;
    TransactionPool transactionPool = null;
    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain_No(Block genesisBlock) {
        // IMPLEMENT THIS
    	transactionPool = new TransactionPool();
    	Transaction transaction = genesisBlock.getCoinbase();
    	addTransaction(transaction );
    	UTXOPool utxoPool = new UTXOPool();
    	UTXO utxo = new UTXO(transaction.getHash(), 0);
    	utxoPool.addUTXO(utxo,transaction.getOutput(0));   	
    	this.txHandler = new TxHandler(utxoPool);
    	
    	

    	
//    	blockChain.setTxHandler(utxoPool);
    	
    	
    	
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        // IMPLEMENT THIS
    	TreeNode<Block> node = null;
    	return this.blockChain.getHeightFromNode(blockChain);
    	
    	
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS

    	return this.txHandler.getUTXOPool();
    	
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
        // IMPLEMENT THIS
    	return this.transactionPool;
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     * 
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     * 
     * @return true if block is successfully added
     */
    // Blockchain height ????
    public boolean addBlock(Block block) {
        // IMPLEMENT THIS
    	
//    	
    	return true;
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    	if (tx == null) {
			return;
		}
    	try {
    		this.transactionPool.addTransaction(tx);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
    }
}
final class TreeNode_No<T> {
	private Block block = null;
	private TreeNode<T> firstChild = null;
	private TreeNode<T> nextSibling = null;
	private ArrayList<TreeNode<T>> children = new ArrayList<>();
	int nodeHeight = 0;
	int treeHeight = 0;
	public TreeNode_No(Block block) {
		this.block = block; 
	}
	public void setFirstChild(TreeNode<T> firstChild) {
		this.firstChild = firstChild;
		this.children.add(firstChild);
	}
	public void setNextSibling(TreeNode<T> nextSibling) {
		this.nextSibling = nextSibling;
	}
	public int getHeightFromNode(TreeNode<T> node) {
		int height=0;
		node.nodeHeight++;
		for (int i = 0; i < node.children.size(); i++) {
			
		}
		return height;
	}
	public Block getMaxHeightBlock(TreeNode<T> root) {
		Block block = null;
		for (TreeNode<T> treeNode : root.children) {
			
		}
		
		return block;
	}
}
