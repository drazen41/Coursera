package Consensus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

    private ArrayList<Transaction> transactions;
    private Set<Candidate> nodeCandidates ;
    private ArrayList<CompliantNode> compliantNodes ;
    private Node[] nodes ;
    private boolean[] myFollowees;
    private Set<Transaction > transactionsToFollowers;
    private Set<Transaction> pendingTransactions;
    private HashSet<Candidate > proposals;
    private int numberOfRounds;
    private int round;
	public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // IMPLEMENT THIS
		numberOfRounds = numRounds;
		round = 0;
		transactionsToFollowers = new HashSet<Transaction>();
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
    		return this.pendingTransactions;
		} else {
			if (round < 9) {
				return transactionsToFollowers;
			} else {
				System.out.println("Round 10");
				return null;
			}
			
		}
    	
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        // IMPLEMENT THIS
    	
    	for (Candidate candidate : candidates) {
			if (candidate.tx == null) continue;
//    		if (myFollowees[candidate.sender] == true && pendingTransactions.contains(candidate.tx) ) {
////				if (numberOfRounds < 10) {
////					transactionsToFollowers.add(candidate.tx );
////					round++;
////				} else {
////					// make consensus (transaction to followers), i.e. where number of transaction is greater than 1
////				}
////    			System.out.println("Transaction number: " + candidate.tx.id + " from " + candidate.sender + "in my transaction.");
//    			this.transactionsToFollowers.add(candidate.tx);
//    			round++;
//			}
			if (myFollowees[candidate.sender]) {
				if (round == 0) {
					if (pendingTransactions.contains(candidate.tx)) {
						this.transactionsToFollowers.add(candidate.tx);
					}					
				}
				else {
					if (transactionsToFollowers.contains(candidate.tx)) {
//						this.transactionsToFollowers.add(candidat)
					}
				}
				
				
			}
		}
    	
    }
}
