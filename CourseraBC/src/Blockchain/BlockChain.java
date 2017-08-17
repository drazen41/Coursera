package Blockchain;

import java.awt.List;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;
    private TreeMap<String ,Block> blocks;
    private TreeNode<Block> blockChain;
//    private TxHandler txHandler = null;
    TransactionPool transactionPool = null;
    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        // IMPLEMENT THIS
    	transactionPool = new TransactionPool();
    	Transaction transaction = genesisBlock.getCoinbase();
    	addTransaction(transaction );
    	UTXOPool utxoPool = new UTXOPool();
    	UTXO utxo = new UTXO(transaction.getHash(), 0);
    	utxoPool.addUTXO(utxo,transaction.getOutput(0));   	
//    	txHandler = new TxHandler(utxoPool);
    	
    	
    	blocks = new TreeMap<String ,Block>();  	
    	blocks.put(genesisBlock.getHash().toString(),genesisBlock);
    	blockChain = new TreeNode<Block>(genesisBlock );
    	blockChain.setTxHandler(utxoPool);
    	
    	
    	
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        // IMPLEMENT THIS
//    	Block block = new Block(prevHash, address);
    	blockChain.getHeight(blockChain);
    	Map.Entry<String , Block> block =  blocks.lastEntry();
    	return block.getValue();
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
    	return null;
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
    	boolean ok = false;
    	Transaction[] transactions  = new Transaction[block.getTransactions().size()];
    	if (block.getPrevBlockHash() == null) {
			return false;
		}
    	// maintain a UTXO pool corresponding to every block on top of which a new block might be created - znaèi ZA SVAKI BLOK
    	// 1. put block on top
    	blocks.put(block.getHash().toString(), block);
    	// 2. 
    	int i = 0;
    	for (Transaction transaction : block.getTransactions()) {
			if (!this.blockChain.txHandler.isValidTx(transaction))
				return false;
    		transactions[i] = transaction;
    		i++;
		}
    	
    	
//    	for (Transaction transaction : this.txHandler.handleTxs(transactions )) {
//			transactionPool.removeTransaction(transaction.getHash());
//		} 
    	
    	ok = true;
    	return ok;
    }

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        // IMPLEMENT THIS
    	this.transactionPool.addTransaction(tx);
    }
}
final class TreeNode<T> {
	private ArrayList<TreeNode<Block>> children = new ArrayList<TreeNode<Block>>();
	private Block data = null;
//	private UTXOPool utxoPool;
	public TxHandler txHandler = null;
	private LocalDateTime localDateTime = null;
	private LocalDateTime tempDate = null;
	private int treeHeight = 0;
	private int maxHeight = 0;
	public TreeNode<Block> lastNode = null;
	public TreeNode(Block data) {
		this.data = data;
		this.localDateTime = LocalDateTime.now();
	}
	public ArrayList<TreeNode<Block>> getChildren(){
		return children;
	}
	public void setChild(TreeNode<Block> child) {
		this.children.add(child);
	}
	public void setTxHandler(UTXOPool utxoPool) {
		this.txHandler = new TxHandler(utxoPool);
	}
	public Integer getHeight(TreeNode<Block> root) {
		if (root == null) {
			return 0;
		}
		Integer hInteger = 0;
		for(TreeNode<Block> n : root.getChildren()){
            hInteger = Math.max(hInteger, getHeight(n));
        }
        return hInteger+1;
	}
	public Block getBlock() {
		return this.data;
	}
	public TreeNode<Block> getMaxHeightBlock(TreeNode<Block> root) {
//		LocalDateTime tempDate = null;		
//		ArrayList<TreeNode> blocks  = new ArrayList<TreeNode>();
//		for(TreeNode<Block> n : root.getChildren()){
//           blocks.add(n);
//           
//        }
//		for (TreeNode treeNode : blocks) {
//			
//		}
//		TreeNode<Block> node = lastNode;
//		int maxHeight = 0;
//		int treeHeight = 0;
		treeHeight++;
		for (TreeNode<Block> treeNode : root.getChildren()) {
				
			if (treeNode.getChildren().size() > 0) {
				
				lastNode = getMaxHeightBlock(treeNode);
			} else {
				
				if (treeHeight > maxHeight) {
					treeHeight++;
					maxHeight = treeHeight;
					lastNode = treeNode;
					tempDate = treeNode.localDateTime;
				} else {
					if (tempDate == null) {
						tempDate = treeNode.localDateTime;
						lastNode = treeNode;
					} else {
						if (treeNode.localDateTime.isAfter(tempDate)) {
							tempDate = treeNode.localDateTime;
							lastNode = treeNode;
						} else {

						}
					}
				}
			}
			
		}
		return lastNode;
	}
	
	
	
	
	
	
}