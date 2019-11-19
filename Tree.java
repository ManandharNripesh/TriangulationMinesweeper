import java.util.Iterator;
import java.util.ArrayList;

import java.io.*;
  
public class Tree implements Iterable 
{
   private TreeIterator ti;
   
   private TreeNode myRoot;
   
   private int length = 0;
   
   private boolean changed = false;
   
   public Tree()
   {
      myRoot = null;
   }
   
   public int size() {
      return length;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:adds x to the tree such that the tree is still an in-order Binary Search Tree
   
   public boolean add(Comparable x)
   {
      changed = false;
      myRoot = addHelper(myRoot, x);
      if(changed) {
         length++;
      }
      return changed;
   }
   
   private TreeNode addHelper(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root == null) {
         root = new TreeNode(x);
         changed = true;
      }
      else {
         if(x.compareTo(root.getValue()) < 0)
            root.setLeft(addHelper(root.getLeft(), x));
         else if(x.compareTo(root.getValue()) > 0)
            root.setRight(addHelper(root.getRight(), x));
         else
            return root;
      }
   //************************************************************           
      return root;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:removes x from the tree such that the tree is still an in-order Binary Search Tree
   
   public boolean remove(Comparable x)
   {
      changed = false;
      myRoot = removeHelper(myRoot, x);
      if(changed && length > 0)
         length--;
      return changed;
   }
   
   private TreeNode removeHelper(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null) {
         if(contains(x)) {
            changed = true;
            TreeNode toRemove = searchHelper(root, x);
            TreeNode parent = searchParent(root, x);
         
            if(toRemove != null) {
               if(isLeaf(toRemove)) {
                  if(toRemove == root) {
                     root = null;
                  }
                  else if(parent.getLeft() == toRemove) {
                     parent.setLeft(null);
                  }
                  else {      //if(parent.getRight() == toRemove) {
                     parent.setRight(null);
                  }
                  return root;
               }
               else if(oneKid(toRemove)) {
                  if(toRemove == root) {
                     if(toRemove.getLeft() != null) {
                        root = toRemove.getLeft();
                     }
                     else {      //if(toRemove.getRight() != null) {
                        root = toRemove.getRight();
                     }
                  }
                  else {
                     if(parent.getLeft() == toRemove) {
                        if(toRemove.getLeft() != null) {
                           parent.setLeft(toRemove.getLeft());
                        }
                        else {      //if(toRemove.getRight() != null) {
                           parent.setLeft(toRemove.getRight());
                        }
                     }
                     else {      //if(parent.getRight() == toRemove) {
                        if(toRemove.getLeft() != null) {
                           parent.setRight(toRemove.getLeft());
                        }
                        else {      //if(toRemove.getRight() != null) {
                           parent.setRight(toRemove.getRight());
                        }
                     }
                  }
               }
               else {      //if toRemove has two children
                  TreeNode m = toRemove.getLeft();
                  Comparable temp;
                  if(m.getRight() != null) {
                     if(m.getRight().getRight() != null) {
                        m = removeHelper(m.getRight(), x);
                     }
                     temp = m.getRight().getValue();
                  }
                  else {
                     temp = m.getValue();
                  }
                  m.setRight(removeHelper(m.getRight(), temp));
                  toRemove.setValue(temp);
               }
            }
            else {      //if toRemove == null   (removing with two children)
               if(root.getRight().getRight() != null) {
                  root = removeHelper(root.getRight(), x);
               }
            }
         }
      }
   //************************************************************           
      return root;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:returns whether or not x is found in the tree
   
   public boolean contains(Comparable x)
   {
      if (searchHelper(myRoot, x)==null)
         return false;
      return true;
   }
   
   private TreeNode searchHelper(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null) {
         if(root.getValue().compareTo(x) == 0) {
            return root;
         }
         else if(root.getValue().compareTo(x) > 0) {
            return searchHelper(root.getLeft(), x);
         }
         else {
            return searchHelper(root.getRight(), x);
         }
      }
   //************************************************************  
      return null;
   }
   
   //pre: root points to an in-order Binary Search Tree
   //post:returns a reference to the parent of the node that contains x, returns null if no such node exists
   //THIS WILL BE CALLED IN THE METHOD removeRecur
   private TreeNode searchParent(TreeNode root, Comparable x)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null) {
         if(root.getLeft() != null && x.compareTo(root.getLeft().getValue()) == 0 || 
            root.getRight() != null && x.compareTo(root.getRight().getValue()) == 0) {
            return root;
         }
         else if(x.compareTo(root.getValue()) < 0) {
            return searchParent(root.getLeft(), x);
         }
         else {
            return searchParent(root.getRight(), x);
         }
      }
   //************************************************************  
      return null;
   }
   
   //post: determines if root is a leaf or not O(1)
   private boolean isLeaf(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null && root.getLeft() == null && root.getRight() == null) {
         return true;
      }
   //************************************************************  
      return false;
   }
      
   //post: returns true if only one child O(1)
   private boolean oneKid(TreeNode root)
   {
   //************COMPLETE THIS METHOD*****************************
      if(root != null && (root.getLeft() == null ^ root.getRight() == null)) {
         return true;
      }
   //************************************************************  
      return false;
   }
   
   public Comparable get(Comparable x) {
      TreeNode temp = searchHelper(myRoot, x);
      if(temp != null) {
         return temp.getValue();
      }
      else {
         return null;
      }
   }
   
   public Iterator<Comparable> iterator() {
      ti = new TreeIterator();
      iteratorHelper(myRoot);
      return ti;
   }
   
   private TreeNode iteratorHelper(TreeNode root) {
      if(root == null) {
         return null;
      }
      
      if(root.getRight() != null)
         iteratorHelper(root.getRight()).getValue();
      
      ti.add(root.getValue());
      
      if(root.getLeft() != null)
         iteratorHelper(root.getLeft()).getValue();
      
      return root;
   }

   private class TreeIterator implements Iterator {
      private Comparable[] list;
      int i;
      int j;
      
      public TreeIterator() {
         list = new Comparable[length];
         i = 0;
         j = 0;
      }
      
      public boolean hasNext() {
         return i < list.length;
      }
      
      public Comparable next() {
         if(i < list.length) {
            return list[i++];
         }
         return null;
      }
      
      private void add(Comparable c) {
         if(j < list.length)
            list[j++] = c;
      }
   }
}