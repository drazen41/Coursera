package Consensus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private ArrayList<Transaction> transactions;
    private Set<Candidate> nodeCandidates ;
    private ArrayList<CompliantNode> compliantNodes ;
    private Node[] nodes ;
    private boolean[] myFollowees;
    private Set<Transaction > receivedTransactions;
    private Set<Transaction> consensus;
    private Set<Transaction> pendingTransactions;
    private HashSet<Candidate > proposals;
    private int numberOfRounds;
    private int round;
    HashMap<Transaction,Integer> frequencymap;
	public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // IMPLEMENT THIS
		numberOfRounds = numRounds;
		round = 0;
		receivedTransactions = new HashSet<Transaction>();
		frequencymap = new HashMap<Transaction,Integer>();
    }
    // 1. 
    public void setFollowees(boolean[] followees) {
        // IMPLEMENT THIS
    	myFollowees = followees;
    	nodes = new Node[followees.length];
    	
    	
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        // IMPLEMENT THIS
//    	transactions = new ArrayList<Transaction>(pendingTransactions);
    	
    	this.pendingTransactions = pendingTransactions;
    	
    	
    }

    public Set<Transaction> sendToFollowers() {
        // IMPLEMENT THIS
    	if (round == 0) {
    		receivedTransactions = new HashSet<Transaction>();
    		for (Transaction tx : pendingTransactions) {
//				this.transactionsToFollowers.add(tx);
    			this.receivedTransactions.add(tx);
				if (frequencymap.containsKey(tx)) {
					int value = frequencymap.get(tx).intValue() + 1;
					System.out.println("Transaction " + tx.id + ", frequency " + value);
					frequencymap.put(tx, frequencymap.get(tx).intValue() + 1);
				} else {
					frequencymap.put(tx, 1);
				}
			}
    		round++;
    		
    		return this.pendingTransactions;
		} else {
			
			
			if (round < numberOfRounds) {
				consensus = new HashSet<Transaction>();
				// 1. calculate consensus
//				HashMap<String,int> frequencymap = new HashMap<String,int>();
//				foreach(String a in animals) {
//				  if(frequencymap.containsKey(a)) {
//				    frequencymap.put(a, frequencymap.get(a)+1);
//				  }
//				  else{ frequencymap.put(a, 1); }
//				}
				
				
				for (Transaction transaction : receivedTransactions) {
					if (frequencymap.containsKey(transaction)) {
						int value = frequencymap.get(transaction).intValue() + 1;
//						System.out.println("Round :" + round);
//						System.out.println("Transaction " + transaction.id + ", frequency " +  value);
						frequencymap.put(transaction, frequencymap.get(transaction).intValue() + 1);
					} 
					else {
//						System.out.println("Transaction " + transaction.id + ", frequency " + 1);
						frequencymap.put(transaction, 1);
					}
					
				}
				for (Transaction tx : frequencymap.keySet()) {
					if (frequencymap.get(tx).intValue() > 1) {
						consensus.add(tx);
					}
					
				}
				round++;
			} else {
//				System.out.println("Consensus: ");
				for (Transaction tx	: consensus) {
					if (frequencymap.containsKey(tx)) {
						System.out.println("Tx " + tx.id + ", frequency " + frequencymap.get(tx).intValue());
					}
				}
				return consensus;
			}
			
			// 2. return consensus
			receivedTransactions = new HashSet<Transaction>();
			return consensus;
		}
    	
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        // IMPLEMENT THIS
    	
    	for (Candidate candidate : candidates) {
			if (candidate.tx == null) continue;
    		if (myFollowees[candidate.sender] == true) {
//    			this.transactionsToFollowers = new HashSet<Transaction>();
    			this.receivedTransactions.add(candidate.tx);
    			
			}

		}
    	
    }
}
