package com.amaze.filemanager.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.filesystem.BaseFile;
import com.amaze.filemanager.filesystem.HFile;
import com.amaze.filemanager.utils.DataUtils;
import com.amaze.filemanager.utils.OpenMode;

import java.util.ArrayList;
import java.util.regex.Pattern;


/**
 * Created by vishal on 26/2/16.
 */
public class SearchAsyncHelper extends Fragment {

    private HelperCallbacks mCallbacks;
    private String mPath, mInput ;
    public SearchTask mSearchTask;
    private OpenMode mOpenMode;
    private boolean mRootMode, isRegexEnabled, isMatchesEnabled;
    public static boolean isItFirstSearch=true;



    public static final String KEY_PATH = "path";
    public static final String KEY_INPUT = "input";
    public static final String KEY_OPEN_MODE = "open_mode";
    public static final String KEY_ROOT_MODE = "root_mode";
    public static final String KEY_REGEX = "regex";
    public static final String KEY_REGEX_MATCHES = "matches";
    public static String lastSearch="";

    private MainActivity mainActivity;


    // interface for activity to communicate with asynctask
    public interface HelperCallbacks {
        void onPreExecute();
        void onPostExecute();
        void onProgressUpdate(BaseFile val);
        void onCancelled();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // hold instance of activity as there is a change in device configuration
        mCallbacks = (HelperCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mPath = getArguments().getString(KEY_PATH);
        mInput = getArguments().getString(KEY_INPUT);
        mOpenMode = OpenMode.getOpenMode(getArguments().getInt(KEY_OPEN_MODE));
        mRootMode = getArguments().getBoolean(KEY_ROOT_MODE);
        isRegexEnabled = getArguments().getBoolean(KEY_REGEX);
        isMatchesEnabled = getArguments().getBoolean(KEY_REGEX_MATCHES);
        mSearchTask = new SearchTask();
        mSearchTask.execute(mPath);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        // to avoid activity instance leak while changing activity configurations
        mCallbacks = null;
    }

    public class SearchTask extends AsyncTask<String, BaseFile, Void> {

        @Override
        protected void onPreExecute() {

            /*
            * Note that we need to check if the callbacks are null in each
            * method in case they are invoked after the Activity's and
            * Fragment's onDestroy() method have been called.
             */
            if (mCallbacks!=null) {

                mCallbacks.onPreExecute();
            }
        }

        // mCallbacks not checked for null because of possibility of
        // race conditions b/w worker thread main thread
        @Override
        protected Void doInBackground(String... params) {

            String path = params[0];
            HFile file=new HFile(mOpenMode, path);
            file.generateMode(getActivity());
            if(file.isSmb())return null;

            // level 1
            // if regex or not
            if (!isRegexEnabled){
                search(file, mInput);
            }
            else {

                // compile the regular expression in the input
                Pattern pattern = Pattern.compile(bashRegexToJava(mInput));
                // level 2
                if (!isMatchesEnabled) searchRegExFind(file, pattern);
                else searchRegExMatch(file, pattern);
            }
            return null;
        }

        @Override
        public void onPostExecute(Void c){
            if (mCallbacks!=null) {
                mCallbacks.onPostExecute();
            }
        }

        @Override
        protected void onCancelled() {
            if (mCallbacks!=null) mCallbacks.onCancelled();
        }

        @Override
        public void onProgressUpdate(BaseFile... val) {
            if (!isCancelled() && mCallbacks!=null) {
                mCallbacks.onProgressUpdate(val[0]);
            }
        }

        /**
         * Recursively search for occurrences of a given text in file names and publish the result
         * @param file the current path
         * @param query the searched text
         */
        private void search(HFile file, String query) {
            query=query.trim();
            String filename="";


            /*
                isFirstSearch --> true if there are no search before it.
                                  false if there are a search before it.
                 (Looking if you just open the phone and searching something).
                 I need a control like that because , when printing 'Same Search' to screen ,
                 if isFirstSearch is true then lastSearch will be empty. Otherwise it will
                 not be empty.
                 lastSearch --> keeps the recently searched text.
                --Meriç BALGAMIŞ
             */
            //********************************************************************
                  /*
                    Son değiştirilme tarihi : 27.03.2017
                    Metot yazarı : Elif Aybike Aydemir
                    İssue : #14
                    Değişikliğin amacı/işlevi : Search metodu contains e göre çalışmaktadır. Fakat etiketlemeye göre arama seçeneği ile
                    o etikete sahip olan dosyaları yalnızca göstermek için yapılan değişikler //#19
                 */
            if(isItFirstSearch == true) {

                lastSearch = query;

                if (file.isDirectory()) {
                    ArrayList<BaseFile> f = file.listFiles(mRootMode);
                    // do you have permission to read this directory?
                    if (!isCancelled())

                        for (BaseFile x : f) {
                            if(x.getName().contains("."))
                                filename=x.getName().substring(0,x.getName().indexOf('.'));
                            if (!isCancelled()) {
                                if (x.isDirectory()) {


                                    if (query.contains("+")) {//#19
                                        //pre
                                        if (query.charAt(query.length() - 1) == '+') {
                                            if (x.getName().startsWith(query))
                                                publishProgress(x);
                                            if (!isCancelled()) search(x, query);
                                        }
                                        if (query.charAt(0) == '+') {//#19
                                            //post
                                            if (x.getName().contains(".") && filename.endsWith(query))
                                                publishProgress(x);
                                            if ((x.getName().endsWith(query) ) )
                                                publishProgress(x);

                                            if (!isCancelled()) search(x, query);
                                        }
                                    } else {//#19
                                        if (x.getName().toLowerCase().contains(query.toLowerCase())) {   // EGER DIRECTORY ISE ICINI DE GEZ
                                            publishProgress(x);
                                        }
                                        if (!isCancelled()) search(x, query);
                                    }


                                } else {


                                    if (query.contains("+")) {//#19
                                        //pre
                                        if (query.charAt(query.length() - 1) == '+') {
                                            if (x.getName().startsWith(query))
                                                publishProgress(x);
                                            if (!isCancelled()) search(x, query);
                                        }
                                        if (query.charAt(0) == '+') {//#19
                                            if (x.getName().contains(".") && filename.endsWith(query))
                                                publishProgress(x);
                                            if (x.getName().endsWith(query))
                                                publishProgress(x);

                                            if (!isCancelled()) search(x, query);
                                        }
                                    } else {//#19
                                        if (x.getName().toLowerCase().contains(query.toLowerCase())) {   // EGER DIRECTORY ISE ICINI DE GEZ
                                            publishProgress(x);
                                        }
                                        if (!isCancelled()) search(x, query);
                                    }

                                }
                            } else return;
                        }

                    else return;

                }
            }


            else{

                if(!query.equalsIgnoreCase(lastSearch)) {
                    lastSearch = query;
                }

                if (file.isDirectory()) {
                    ArrayList<BaseFile> f = file.listFiles(mRootMode);
                    // do you have permission to read this directory?
                    if (!isCancelled())

                        for (BaseFile x : f) {
                            if(x.getName().contains("."))
                                filename=x.getName().substring(0,x.getName().indexOf('.'));
                            if (!isCancelled()) {
                                if (x.isDirectory()) {


                                    if (query.contains("+")) {
                                        //pre
                                        if (query.charAt(query.length() - 1) == '+') {
                                            if (x.getName().startsWith(query))
                                                publishProgress(x);
                                            if (!isCancelled()) search(x, query);
                                        }
                                        if (query.charAt(0) == '+') {
                                            if (x.getName().contains(".") && filename.endsWith(query))
                                                publishProgress(x);
                                            if (x.getName().endsWith(query))
                                                publishProgress(x);
                                          ;
                                            if (!isCancelled()) search(x, query);
                                        }
                                    } else {
                                        if (x.getName().toLowerCase().contains(query.toLowerCase())) {   // EGER DIRECTORY ISE ICINI DE GEZ
                                            publishProgress(x);
                                        }
                                        if (!isCancelled()) search(x, query);
                                    }


                                } else {


                                    if (query.contains("+")) {
                                        //pre
                                        if (query.charAt(query.length() - 1) == '+') {
                                            if (x.getName().startsWith(query))
                                                publishProgress(x);
                                            if (!isCancelled()) search(x, query);
                                        }
                                        if (query.charAt(0) == '+') {
                                            if (x.getName().contains(".") && filename.endsWith(query))
                                                publishProgress(x);
                                            if (x.getName().endsWith(query))
                                                publishProgress(x);

                                            if (!isCancelled()) search(x, query);
                                        }
                                    } else {
                                        if (x.getName().toLowerCase().contains(query.toLowerCase())) {   // EGER DIRECTORY ISE ICINI DE GEZ
                                            publishProgress(x);
                                        }
                                        if (!isCancelled()) search(x, query);
                                    }

                                }
                            } else return;
                        }

                    else return;

                }







            /*
            If the code reaches here. Then the code block in Main.java (1791. row) will print to screen
            'Same Search'.
             */
            }

        }



        /**
         * Recursively find a java regex pattern {@link Pattern} in the file names and publish the result
         * @param file the current file
         * @param pattern the compiled java regex
         */
        private void searchRegExFind(HFile file, Pattern pattern) {

            if (file.isDirectory()) {
                ArrayList<BaseFile> f = file.listFiles(mRootMode);

                if (!isCancelled())
                    for (BaseFile x : f) {
                        if (!isCancelled()) {
                            if (x.isDirectory()) {
                                if (pattern.matcher(x.getName()).find()) publishProgress(x);
                                if (!isCancelled()) searchRegExFind(x, pattern);

                            } else {
                                if (pattern.matcher(x.getName()).find()) {
                                    publishProgress(x);
                                }
                            }
                        } else return;
                    }
                else return;
            } else {
                System.out
                        .println(file.getPath() + "Permission Denied");
            }
        }

        /**
         * Recursively match a java regex pattern {@link Pattern} with the file names and publish the result
         * @param file the current file
         * @param pattern the compiled java regex
         */
        private void searchRegExMatch(HFile file, Pattern pattern) {

            if (file.isDirectory()) {
                ArrayList<BaseFile> f = file.listFiles(mRootMode);

                if (!isCancelled())
                    for (BaseFile x : f) {
                        if (!isCancelled()) {
                            if (x.isDirectory()) {
                                if (pattern.matcher(x.getName()).matches()) publishProgress(x);
                                if (!isCancelled()) searchRegExMatch(x, pattern);

                            } else {
                                if (pattern.matcher(x.getName()).matches()) {
                                    publishProgress(x);
                                }
                            }
                        } else return;
                    }
                else return;
            } else {
                System.out
                        .println(file.getPath() + "Permission Denied");
            }
        }

        /**
         * method converts bash style regular expression to java. See {@link Pattern}
         * @param originalString
         * @return converted string
         */
        private String bashRegexToJava(String originalString) {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i=0; i<originalString.length(); i++) {
                switch (originalString.charAt(i) + "") {
                    case "*":
                        stringBuilder.append("\\w*");
                        break;
                    case "?":
                        stringBuilder.append("\\w");
                        break;
                    default:
                        stringBuilder.append(originalString.charAt(i));
                        break;
                }
            }

            Log.d(getClass().getSimpleName(), stringBuilder.toString());
            return stringBuilder.toString();
        }
    }
}