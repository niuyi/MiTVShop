package com.xiaomi.mitv.shop.model;

import android.text.TextUtils;
import com.xiaomi.mitv.shop.network.JsonSerializer;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linniu on 2015/5/9.
 */
public class ProductDetail {
    public static final String PID_KEY = "PID";

    public String id;
    public int status;
    public String name;
    public Price price;
    public String[] images;

    public GoodStatus[] goods_status;
    public Intro[] introduce;

    public Prop[] props_def;
    public Node[] props_tree;

    public static ProductDetail parse(String input){
        if(TextUtils.isEmpty(input)){
            return null;
        }

        try{
            return JsonSerializer.getInstance().deserialize(input, ProductDetail.class);
        }catch (Exception e){

        }

        return null;
    }

    public GoodStatus getGoodStatus(String id){
        for(GoodStatus g : goods_status){
            if(g.id.equals(id)){
                return g;
            }
        }

        return null;
    }

    public boolean check(){
        return true;
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

    public static class Intro{
        public String name;
        public String[] album;
    }

    public static class Node{
        public String icon;
        public boolean valid;
        public int id;
        public String gid;
        public Node[] child;
    }

    public static class GoodStatus{
        public String id;
        public String name;
        public int status;
        public String price;
    }

    public static class Price{
        public String max;
        public String min;
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
