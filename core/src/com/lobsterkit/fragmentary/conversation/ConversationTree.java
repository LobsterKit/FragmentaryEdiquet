package com.lobsterkit.fragmentary.conversation;

import java.util.HashMap;
import java.util.Set;

public class ConversationTree<ContentType, BranchType>
{
	public class ConversationNode
	{
		HashMap<BranchType, String> branches;
		ContentType content;
		String nodeID;
		
		public ConversationNode(ContentType content, String nodeID)
		{
			super();
			this.branches = new HashMap<>();
			this.content = content;
			this.nodeID = nodeID;
			if (nodeID == null)
				throw new IllegalArgumentException("ID cannot be null");
			register();
		}
		
		private void register()
		{
			validateRegistration();
			nodeFromID.put(nodeID, this);
			idFromNode.put(this, nodeID);
		}
		
		private void validateRegistration()
		{
			if (nodeFromID.containsKey(nodeID))
			{
				throw new IllegalStateException(
						"Node IDs must be unique. Found multiple nodes with ID: "
								+ nodeID);
			}
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
	
	private HashMap<ConversationNode, String> idFromNode;
	private HashMap<String, ConversationNode> nodeFromID;
	private ConversationNode rootNode;
	private ConversationNode currentNode;
	
	public ConversationTree(ContentType rootContent, String rootID)
	{
		this.idFromNode = new HashMap<>();
		this.nodeFromID = new HashMap<>();
		this.rootNode = new ConversationNode(rootContent, rootID);
		this.currentNode = this.rootNode;
	}
	
	public void addNewBranch(BranchType branch, String nodeID, ContentType content)
	{
		ConversationNode newNode = new ConversationNode(content, nodeID);
		addLinkedBranch(branch, newNode.nodeID);
	}
	
	public void addNewNode(String nodeID, ContentType content)
	{
		new ConversationNode(content, nodeID);
	}
	
	public void addLinkedBranch(BranchType branch, String nodeID)
	{
		currentNode.branches.put(branch, nodeID);
	}
	
	private void validateNonNull(Object object, String message)
	{
		if (object == null)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	public void jumpToNode(String nodeID)
	{
		this.currentNode = nodeFromID.get(nodeID);
	}
	
	public void jumpToRoot()
	{
		this.currentNode = this.rootNode;
	}
	
	public ContentType enterBranch(BranchType branch)
	{
		String branchID = currentNode.branches.get(branch);
		validateNonNull(branchID, "Cannot enter null branch");

		ConversationNode branchNode = nodeFromID.get(branchID);
		this.currentNode = branchNode;
		return branchNode.content;
	}
	
	public ContentType peekBranch(BranchType branch)
	{
		String branchID = currentNode.branches.get(branch);
		validateNonNull(branchID, "Branch is null or does not exist");
		
		ConversationNode branchNode = nodeFromID.get(branchID);
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
	
	public String getCurrentID()
	{
		return idFromNode.get(currentNode);
	}
	
	public void enterNode(int id)
	{
		this.currentNode = nodeFromID.get(id);
	}
}
