package ListPackage;

import java.util.ArrayList;
import org.apache.commons.net.ftp.FTPFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mth
 */
public class List<T> {

    private ArrayList<T> list;

    public List() {
        list = new ArrayList<>();
    }
    
    public int size(){
        int i = 0;
        
        i = list.size();
//        System.out.println("List size " + i);
        return i;
    }
    
    public void add(T object){
        list.add(object);
    }
    
    public void remove(int index){
        list.remove(index);
    }
    
    public void printList(){
        for (int i = 0; i < size(); i++){
            System.out.println((T) list.get(i));
        }
    }
    
    public String get(int index){
        String s = new String();
        
        for(int i = 0; i < index; i++){
            s = list.get(i).toString();
        }
        
        return s;
    }

}
