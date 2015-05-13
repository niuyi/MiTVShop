package com.xiaomi.mitv.shop.model;

import com.xiaomi.mitv.shop.network.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linniu on 2015/5/9.
 */
public class ProductDetail {
    public String name;
    public String price;
    public String[] images;

    public Map<String, String> goods_status;
    public Prop[] props_def;
    public Node[] props_tree;

    public static ProductDetail parse(String input){
        try{
            return JsonSerializer.getInstance().deserialize(input, ProductDetail.class);
        }catch (Exception e){

        }

        return null;
    }


    public static class Prop{
        public String name;
        public int level;
        public Option[] options;
    }

    public static class Option{
        public int id;
        public String name;
    }

    public static class Node{
        public String icon;
        public boolean valid;
        public int id;
        public String gid;
        public Node[] child;
    }

    public Node findNodeById(int id){
        return findNodeById(props_tree, id);
    }

    private static Node findNodeById(Node[] nodes, int id){
        if(nodes != null){
            for(Node node : nodes){
                if(node.id == id){
                    return node;
                }

                Node result = findNodeById(node.child, id);
                if(result != null && result.valid){
                    return result;
                }
            }
        }

        return null;
    }

    public static Node findNodeById(Node node, int id){
        if(node == null)
            return null;

        if(node.child == null)
            return null;

        return findNodeById(node.child, id);
    }
}
