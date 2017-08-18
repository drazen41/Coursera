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

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;
//    private TreeMap<String ,Block> blocks;
    private TreeNode<Block> blockChain;
    private TxHandler txHandler = null;
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
    	this.txHandler = new TxHandler(utxoPool);
    	
    	
//    	blocks = new TreeMap<String ,Block>();  	
//    	blocks.put(genesisBlock.getHash().toString(),genesisBlock);
    	blockChain = new TreeNode<Block>(genesisBlock );
//    	blockChain.setTxHandler(utxoPool);
    	
    	
    	
    }

    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        // IMPLEMENT THIS
//    	Block block = new Block(prevHash, address);
//    	blockChain.getHeight(blockChain);
//    	Map.Entry<String , Block> block =  blocks.lastEntry();
//    	return block.getValue();
    	TreeNode<Block> node = null;
    	int height = this.blockChain.getHeight(blockChain);
    	if (height == 1) {
    		return this.blockChain.getBlock();
		} else {
			node = this.blockChain.getMaxHeightNode(blockChain);
		}
    	 
    	return node.getBlock();
    	
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        // IMPLEMENT THIS
//    	TreeNode<Block> node = null;
//    	int height = this.blockChain.getHeight(blockChain);
//    	if (height == 1) {
//    		node = this.blockChain;
//		} else {
//			node = this.blockChain.getMaxHeightNode(blockChain);
//		}
//    	return node.txHandler.getUTXOPool();
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
    	boolean ok = false;
    	Transaction[] transactions  = new Transaction[block.getTransactions().size()];
    	if (block.getPrevBlockHash() == null) {
			return false;
		}
    	// maintain a UTXO pool corresponding to every block on top of which a new block might be created - 
    	// 1. put block on top
//    	blocks.put(block.getHash().toString(), block);
    	// 2. 
    	int i = 0;
    	for (Transaction transaction : block.getTransactions()) {
			if (!this.txHandler.isValidTx(transaction))
				return false;
    		
			
			
			transactions[i] = transaction;
    		i++;
		}
    	// Add block to block with previous hash, provjeri da li je maksimalna visina blockchaina - cut_off-age odgovara visini na kojoj je parent + 1
    	// Ne moze se dodati block ispod parenta ako uvjet iznad nije zadovoljen (pronaci visinu parenta u blochainu)
    	TreeNode<Block> treeNode = new TreeNode<Block>(block);
    	int maxHeight = this.blockChain.getHeight(blockChain);
        ByteArrayWrapper parentWrapper = new ByteArrayWrapper(block.getPrevBlockHash());
        TreeNode<Block> parent = treeNode.getParentTreeNode(parentWrapper, blockChain);
        int parentBlockHeight = parent.getBlockHeight();
        if ((maxHeight-CUT_OFF_AGE) > (parentBlockHeight+1)) {
			ok = false;
		} else {
			// Dodaj block parentu
			treeNode.addTreeNodeToParent(parent);
			if (parentBlockHeight >= maxHeight) {
				this.txHandler.handleTxs(transactions);
			}
			// Transaction pool
//			TransactionPool tPool = new TransactionPool();			
//			for (Transaction tx1 : transactionPool.getTransactions()) {
//				for (Transaction tx2 : parent.getBlock().getTransactions()) {
//					if (tx1.equals(tx2)) {
//						continue;
//					} else {
//						tPool.addTransaction(tx2);
//					}
//				}
//			}
//			this.transactionPool = tPool;
			Block block2 = parent.getBlock();
			Transaction block2Coinbase = transactionPool.getTransaction(block2.getCoinbase().getHash());
			if (block2Coinbase != null) {
				transactionPool.removeTransaction(block2Coinbase.getHash());
			}	
			
			for (Transaction tx : block2.getTransactions()) {
				Transaction transaction = transactionPool.getTransaction(tx.getHash());
				if (transaction != null) {
					transactionPool.removeTransaction(tx.getHash());
				}
			}
			
			addTransaction(block.getCoinbase());
			for (Transaction tx : block.getTransactions()) {
				addTransaction(tx);
			}
			
			ok = true;
		}
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
	private int blockHeight = 0;
	public TreeNode<Block> lastNode = null;
	private TreeNode<Block> parentNode = null;
	private ArrayList<Transaction> transactions = null;
	public TreeNode(Block data) {
		this.data = data;
		this.localDateTime = LocalDateTime.now();
		this.transactions = data.getTransactions();
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
//		return this.maxHeight;
	}
	public Block getBlock() {
		
		
		return this.data;
	}
	public TreeNode<Block> getMaxHeightNode(TreeNode<Block> root) {

		treeHeight++;
		for (TreeNode<Block> treeNode : root.getChildren()) {
				
			if (treeNode.getChildren().size() > 0) {
				
				lastNode = getMaxHeightNode(treeNode);
			} else {
				
				if (treeHeight > maxHeight) {
					treeHeight++;
					maxHeight = treeHeight;
					lastNode = treeNode;
					lastNode.maxHeight = treeHeight;
					tempDate = treeNode.localDateTime;
				} else {
					if (tempDate == null) {
						tempDate = treeNode.localDateTime;
						lastNode = treeNode;
					} else {
						if (treeHeight >= maxHeight && treeNode.localDateTime.isAfter(tempDate)) {
							tempDate = treeNode.localDateTime;
							lastNode = treeNode;
						} else {

						}
					}
				}
			}
			
		}
		treeHeight--;
		
		return lastNode;
	}
	
	public Integer	getMaxHeight() {
		if (lastNode != null) {
			return lastNode.maxHeight;
		} else {
			return 0;
		}
	}
	
	public boolean addTreeNodeToBlockchain(int cut_off_age, TreeNode<Block> root) {
		boolean added = false;
		if (root.getChildren().size() < cut_off_age) {
			root.setChild((TreeNode<Block>)this);
			added = true;
		} else {
			if (!added) {
				for (TreeNode<Block> treeNode : root.getChildren()) {
					addTreeNodeToBlockchain(cut_off_age, treeNode);
				}
			}
			
			
		}
		return added;
	}
	public TreeNode<Block> getParentTreeNode(ByteArrayWrapper parent, TreeNode<Block> root) {
		
		blockHeight++;
		if (root.getChildren().size() > 0) {
			for (TreeNode<Block> treeNode : root.getChildren()) {			
				Block block = treeNode.getBlock();
				ByteArrayWrapper current = new ByteArrayWrapper(block.getHash());
				if (current.equals(parent)) {
					parentNode = treeNode;
				} else {
				//	blockHeight = getParentBlockHeight(parent, treeNode);
					parentNode = getParentTreeNode(parent, treeNode);
				}
				
				
			}
			blockHeight--;	
		} else {
			parentNode = root;
		}
		
		parentNode.blockHeight = this.blockHeight;
//		parentNode.transactions = this.transactions;
		return parentNode;
	}
	public Integer getBlockHeight() {
		return this.blockHeight;
	}
	public boolean addTreeNodeToParent(TreeNode<Block> parent) {
		boolean added = false;
		parent.setChild((TreeNode<Block>)this);
		return true;
	}
	
}