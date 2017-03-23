package com.amaze.filemanager.utils;

import com.amaze.filemanager.filesystem.BaseFile;
import com.amaze.filemanager.ui.drawer.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by arpitkh996 on 20-01-2016.
 */

//Central data being used across activity,fragments and classes
public class DataUtils {

    public static ArrayList<String> hiddenfiles=new ArrayList<>(), gridfiles=new ArrayList<>(), listfiles=new ArrayList<>(),history=new ArrayList<>()
    ,lock_array =new ArrayList<>();


    public static ArrayList<String>  favorites = new ArrayList<>();


   /* ,trash3 = new ArrayList<>();*/

   public static ArrayList<BaseFile> trash = new ArrayList<>();

    public static ArrayList<String> labelHistory = new ArrayList<>();
    //public static ArrayList<BaseFile> hiddenfiles = new ArrayList<>();



   // public static ArrayList<DataPackage> trash = new ArrayList<>();




    public static List<String> storages=new ArrayList<>();


    public static final int DELETE = 0, COPY = 1, MOVE = 2, NEW_FOLDER = 3, RENAME = 4, NEW_FILE = 5, EXTRACT = 6, COMPRESS = 7,POST=8,PRE=9,LOCK1=10,WITHPOST=11;



    public static final String FAVORITES = "favorites",DRIVE = "drive", SMB = "smb", BOOKS = "books", HISTORY = "Table1", HIDDEN = "Table2", LIST = "list", GRID = "grid"
    ,TRASH = "Table3" , LOCK = "Table4",LABELHISTORY="table5";




    public static ArrayList<Item> list=new ArrayList<>();
    public static ArrayList<String[]> servers=new ArrayList<>(),books=new ArrayList<>(),accounts=new ArrayList<>();

    static DataChangeListener dataChangeListener;
    public static int containsServer(String[] a){
        return contains(a,servers);
    }
    public static int containsServer(String path){
        if(servers==null)return -1;
        int i = 0;
        for (String[] x : servers) {
            if (x[1].equals(path)) return i;
            i++;

        }
        return -1;    }
    public static int containsBooks(String[] a){
        return contains(a,books);
    }
    public static int containsAccounts(String[] a){
        return contains(a,accounts);
    }
    public static int containsAccounts(String a){
        return contains(a,accounts);
    }
    public static void clear(){
        hiddenfiles=new ArrayList<>();
        gridfiles=new ArrayList<>();
        listfiles=new ArrayList<>();
        history=new ArrayList<>();
        storages=new ArrayList<>();
        servers=new ArrayList<>();
        books=new ArrayList<>();
        accounts=new ArrayList<>();
        favorites = new ArrayList<>();
        trash = new ArrayList<>();
        labelHistory=new ArrayList<>();

        // trash3 = new ArrayList<>();



         trash = new ArrayList<>();
        lock_array = new ArrayList<>();


    }
    public static void registerOnDataChangedListener(DataChangeListener dataChangeListener){
        DataUtils.dataChangeListener=dataChangeListener;
    }
    static int contains(String a, ArrayList<String[]> b) {
        int i = 0;
        for (String[] x : b) {
            if (x[1].equals(a)) return i;
            i++;

        }
        return -1;
    }
    static int contains(String[] a, ArrayList<String[]> b) {
        if(b==null)return -1;
        int i = 0;
        for (String[] x : b) {
            if (x[0].equals(a[0]) && x[1].equals(a[1])) return i;
            i++;

        }
        return -1;
    }
    public static void removeBook(int i){
        if(books.size()>i)
            books.remove(i);
    }
    public static void removeAcc(int i){
        if(accounts.size()>i)
            accounts.remove(i);
    }
    public static void removeServer(int i){
        if(servers.size()>i)
            servers.remove(i);
    }
    public static void addBook(String[] i){
            books.add(i);
    }
    public static void addBook(String[] i,boolean refreshdrawer){
        if(refreshdrawer && dataChangeListener!=null)dataChangeListener.onBookAdded(i,true);
        books.add(i);
    }
    public static void addAcc(String[] i){
            accounts.add(i);
    }
    public static void addServer(String[] i){
            servers.add(i);
    }


    public static void addLockFile(String i)
    {
        lock_array.add(i);
        if(dataChangeListener!=null)
            dataChangeListener.onLockedAdded(i);
    }


    public static void removeLockFile(String i)
    {
        lock_array.remove(i);
        if(dataChangeListener!=null)
            dataChangeListener.onLockedRemoved(i);
    }

    public static void addHiddenFile(String i)
    {
        hiddenfiles.add(i);

        if(dataChangeListener!=null)
            dataChangeListener.onHiddenFileAdded(i);
    }
    public static void removeHiddenFile(String i)
    {
        hiddenfiles.remove(i);
        if(dataChangeListener!=null)
            dataChangeListener.onHiddenFileRemoved(i);
    }


    public static void addHistoryFile(String i)
    {
        history.add(i);
        if(dataChangeListener!=null)
            dataChangeListener.onHistoryAdded(i);
    }

    public static void addlabelHistory(String i)
    {
        labelHistory.add(i);
        if(dataChangeListener!=null)
            dataChangeListener.onHistoryAdded(i);
    }


    public static void addFavoritesFile(String i)
    {
        favorites.add(i);
        if(dataChangeListener!=null)
            dataChangeListener.onFavoritesAdded(i);
    }
    //*************************************

    public static void addTrashFile(/*String i*/ BaseFile i)
    {
        trash.add(i);

        if(dataChangeListener!=null)
            dataChangeListener.onTrashAdded(i);
    }
    //******************************



    public static void sortBook(){
        Collections.sort(books,new BookSorter());
    }
    public static void setServers(ArrayList<String[]> servers) {
        if(servers!=null)
        DataUtils.servers = servers;
    }

    public static void setBooks(ArrayList<String[]> books) {
        if(books!=null)
            DataUtils.books = books;
    }

    public static void setAccounts(ArrayList<String[]> accounts) {
        if(accounts!=null)
        DataUtils.accounts = accounts;
    }

    public static ArrayList<String[]> getServers() {
        return servers;
    }

    public static ArrayList<String[]> getBooks() {
        return books;
    }

    public static ArrayList<String[]> getAccounts() {
        return accounts;
    }

    public static ArrayList<String> getHiddenfiles() {
        return hiddenfiles;
    }// string

    public static void setHiddenfiles(ArrayList<String> hiddenfiles) {//Arraylist<String>
        if(hiddenfiles!=null)
        DataUtils.hiddenfiles = hiddenfiles;
    }



    public static void setGridfiles(ArrayList<String> gridfiles) {
        if(gridfiles!=null)
        DataUtils.gridfiles = gridfiles;
    }

    public static ArrayList<String> getListfiles() {
        return listfiles;
    }

    public static void setListfiles(ArrayList<String> listfiles) {
       if(listfiles!=null)
        DataUtils.listfiles = listfiles;
    }
    public static void clearHistory() {
        history=new ArrayList<>();
        if(dataChangeListener!=null)
            dataChangeListener.onHistoryCleared();
    }

    //******************************

    public static void clearTrash() {
        trash=new ArrayList<>();
        if(dataChangeListener!=null)
            dataChangeListener.onTrashCleared();

    }



    //********************************




    //******************************

    public static void clearHidden() {
        hiddenfiles=new ArrayList<>();
        if(dataChangeListener!=null)
            dataChangeListener.onHiddenCleared();

    }



    //********************************


    public static void clearFavorites() {
        favorites=new ArrayList<>();
        if(dataChangeListener!=null)
            dataChangeListener.onFavoritesCleared();
    }

    public static List<String> getStorages() {
        return storages;
    }

    public static void setStorages(List<String> storages) {
        DataUtils.storages = storages;
    }

    public static ArrayList<Item> getList() {
        return list;
    }




    public static void setList(ArrayList<Item> list) {
        DataUtils.list = list;
    }
    //Callbacks to do original changes in database (and ui if required)
    public interface DataChangeListener{
        void onLockedAdded(String path);
        void onLockedRemoved(String path);
        void onHiddenFileAdded(String path);
        void onHiddenFileRemoved(String path);
        void onHistoryAdded(String path);
        void onFavoritesAdded(String path);
        void onTrashAdded(String path);


         void onTrashAdded(BaseFile path);


        void onBookAdded(String path[],boolean refreshdrawer);
        void onHistoryCleared();
        void onFavoritesCleared();
        void onTrashCleared();


        void onHiddenCleared();
    }
}
