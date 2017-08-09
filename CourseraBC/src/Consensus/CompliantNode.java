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
    private HashSet<Candidate > proposals;
	public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // IMPLEMENT THIS
//    	compliantNodes.add(new CompliantNode(p_graph, p_malicious, p_txDistribution, numRounds));
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
    	transactionsToFollowers = pendingTransactions;
    	
    	
    }

    public Set<Transaction> sendToFollowers() {
        // IMPLEMENT THIS

    	return transactionsToFollowers;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        // IMPLEMENT THIS
    	
    	for (Candidate candidate : candidates) {
			if (myFollowees[candidate.sender] == true) {
				transactionsToFollowers.add(candidate.tx );
			}
		}
    	
    }
}
