package jp.oops.clazz.restday.dao;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <div>Copyright (c) 2019-2020 Masahito Hemmi . </div>
 * <div>This software is released under the MIT License.</div>
 */
public final class Node {
    
    private final int type;
    
    private Node left;
    private Node current;
    private Node right;
    
    private String str;
    
    private List<String> strList;

    public static final int TRIPLE = 0;
    public static final int STRING = 1;
    public static final int COLUMN = 2;
    public static final int WHERE = 3;
    public static final int ORDER_BY = 4;

    public static Node newOrderBy(String n3) {
        Node node = new Node(ORDER_BY);
        node.str = n3;
        return node;
    }    

    
    public static Node newTriple(Node n1, Node n2, Node n3) {
        Node node = new Node(TRIPLE);
        node.left = n1;
        node.current = n2;
        node.right = n3;
        return node;
    }    

    public static Node newColumn(String s) {
        Node node = new Node(COLUMN);
        node.str = s;
        return node;
    }    
    
    
    public static Node newWhere(String s) {
        
        Node node = new Node(WHERE);
        
        String[] splits = s.split("&&", -1);

        ArrayList<String> list = new ArrayList<>();
        for (String str : splits) {
            list.add(str.trim());
        }
        node.strList = list;

        return node;
    }    
    
    public static Node newString(String s) {
        Node node = new Node(STRING);
        node.str = s;
        return node;
    }
    
    
    private Node(int type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @return the left
     */
    public Node getLeft() {
        return left;
    }

    /**
     * @return the current
     */
    public Node getCurrent() {
        return current;
    }

    /**
     * @return the right
     */
    public Node getRight() {
        return right;
    }

    /**
     * @return the str
     */
    public String getStr() {
        return str;
    }

    /**
     * @return the strList
     */
    public List<String> getStrList() {
        return strList;
    }
}
