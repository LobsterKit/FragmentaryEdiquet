package com.lobsterkit.fragmentary.conversation;

import java.util.HashMap;
import java.util.Set;

public class ConversationTree<ContentType, BranchType>
{
	public class ConversationNode
	{
		HashMap<BranchType, ConversationNode> branches;
		ContentType content;
		
		public ConversationNode(ContentType content)
		{
			super();
			this.branches = new HashMap<>();
			this.content = content;
			nodeFromID.put(idCounter, this);
			idsFromNode.put(this, idCounter);
			idCounter++;
		}
		
		public boolean hasBranch(BranchType branch)
		{
			return branches.keySet().contains(branch);
		}
		
		public boolean isEnd()
		{
			return branches.isEmpty();
		}
	}
	
	private int idCounter;
	private HashMap<ConversationNode, Integer> idsFromNode;
	private HashMap<Integer, ConversationNode> nodeFromID;
	private ConversationNode rootNode;
	private ConversationNode currentNode;
	
	public ConversationTree(ContentType rootContent)
	{
		this.idsFromNode = new HashMap<>();
		this.nodeFromID = new HashMap<>();
		this.rootNode = new ConversationNode(rootContent);
		this.currentNode = this.rootNode;
	}
	
	private int addBranch(BranchType branch, ConversationNode node)
	{
		currentNode.branches.put(branch, node);
		return idsFromNode.get(currentNode);
	}
	
	public int addBranch(BranchType branch, ContentType content)
	{
		ConversationNode node = new ConversationNode(content);
		return addBranch(branch, node);
	}
	
	public int addBranch(BranchType branch, int contentID)
	{
		ConversationNode node = nodeFromID.get(contentID);
		return addBranch(branch, node);
	}
	
	private void validateNonNull(Object object, String message)
	{
		if (object == null)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	public ContentType enterBranch(BranchType branch)
	{
		ConversationNode branchNode = currentNode.branches.get(branch);
		validateNonNull(branchNode, "Cannot enter null branch");
		
		this.currentNode = branchNode;
		return branchNode.content;
	}
	
	public ContentType peekBranch(BranchType branch)
	{
		ConversationNode branchNode = currentNode.branches.get(branch);
		validateNonNull(branchNode, "Branch is null or does not exist");
		return branchNode.content;
	}
	
	public boolean currentNodeHasBranch(BranchType branch)
	{
		return currentNode.hasBranch(branch);
	}
	
	public boolean atEndNode()
	{
		return currentNode.isEnd();
	}
	
	public Set<BranchType> getAvailableBranches()
	{
		return currentNode.branches.keySet();
	}
	
	public ContentType getContent()
	{
		return currentNode.content;
	}
	
	public int getCurrentID()
	{
		return idsFromNode.get(currentNode);
	}
	
	public void enterNode(int id)
	{
		this.currentNode = nodeFromID.get(id);
	}
}
