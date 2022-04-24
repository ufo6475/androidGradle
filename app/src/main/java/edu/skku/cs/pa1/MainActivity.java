package edu.skku.cs.pa1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> arraylist=new ArrayList();
    private  ListView1Adapter listviewapater;
    private ListView2Adapter listview2adapter;
    private ListView2Adapter listview3adapter;
    private ListView2Adapter listview4adapter;

    private ArrayList<items> itemlist;

    private ArrayList<String> listGray;
    private ArrayList<String> listYellow;
    private ArrayList<String> listGreen;

    private ListView upListView;
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;

    public String answer;

    public int Gray;
    public int Yellow;
    public int Green;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upListView=findViewById(R.id.listView1);
        itemlist=new ArrayList<items>();
        listviewapater= new ListView1Adapter(getApplicationContext(),itemlist);
        upListView.setAdapter(listviewapater);

        listView1=findViewById(R.id.listView2);
        listView2=findViewById(R.id.listView3);
        listView3=findViewById(R.id.listView4);

        listGray=new ArrayList<String>();
        listYellow=new ArrayList<String>();
        listGreen=new ArrayList<String>();

        listview2adapter=new ListView2Adapter(getApplicationContext(),listGray,Color.GRAY);
        listview3adapter=new ListView2Adapter(getApplicationContext(),listYellow,Color.YELLOW);
        listview4adapter=new ListView2Adapter(getApplicationContext(),listGreen,Color.GREEN);

        listView1.setAdapter(listview2adapter);
        listView2.setAdapter(listview3adapter);
        listView3.setAdapter(listview4adapter);



        try {
            InputStream is=getApplicationContext().getAssets().open("wordle_words.txt");
            BufferedReader bu =new BufferedReader(new InputStreamReader(is,"EUC_KR"));
            while(true){
                String st=bu.readLine();
                if(st!=null){
                    arraylist.add(st);
                }
                else{
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Random rm=new Random();
        int tmp=rm.nextInt(arraylist.size());
        answer=arraylist.get(tmp).toUpperCase();
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText input= findViewById(R.id.Input);
                String curinput=input.getText().toString();
                if(arraylist.contains(curinput.toLowerCase())) {

                    items nitem = new items(curinput.toUpperCase(), answer);
                    itemlist.add(nitem);
                    ListView1Adapter nadapter = (ListView1Adapter) upListView.getAdapter();
                    nadapter.notifyDataSetChanged();
                    upListView.setAdapter(nadapter);
                    input.setText("");

                    for (int i = 0; i < 5; i++) {
                        if (nitem.wordColor[i] == Color.GRAY) {
                            if(!listGray.contains(nitem.word.charAt(i)+""))
                                listGray.add(nitem.word.charAt(i) + "");
                        } else if (nitem.wordColor[i] == Color.YELLOW) {
                            if(!listYellow.contains(nitem.word.charAt(i)+"")&&!listGreen.contains(nitem.word.charAt(i)+""))
                                listYellow.add(nitem.word.charAt(i) + "");
                        } else {
                            if(!listGreen.contains(nitem.word.charAt(i)+"")) {
                                listYellow.remove(nitem.word.charAt(i) + "");
                                listGreen.add(nitem.word.charAt(i) + "");
                            }
                        }
                    }
                    ListView2Adapter n2adapter = (ListView2Adapter) listView1.getAdapter();
                    ListView2Adapter n3adapter = (ListView2Adapter) listView2.getAdapter();
                    ListView2Adapter n4adapter = (ListView2Adapter) listView3.getAdapter();
                    n2adapter.notifyDataSetChanged();
                    n3adapter.notifyDataSetChanged();
                    n4adapter.notifyDataSetChanged();
                    listView1.setAdapter(n2adapter);
                    listView2.setAdapter(n3adapter);
                    listView3.setAdapter(n4adapter);
                }
                else{
                    Toast.makeText(MainActivity.this, "Word '"+curinput+"' not in dictionary!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }



    public class items{
        public String word;
        public int[] wordColor=new int[5];

        items(String word,String answer){
            this.word=word;
            for(int i=0;i<5;i++){
                wordColor[i]=Color.GRAY;
            }

            for(int i=0;i<5;i++){
                if(answer.contains(word.charAt(i)+"")){
                    wordColor[i]= Color.YELLOW;
                }
            }
            for(int i=0;i<5;i++){
                if(answer.charAt(i)==word.charAt(i)){
                    wordColor[i]=Color.GREEN;
                }
            }
        }

    }


    private class ListView1Adapter extends BaseAdapter{

        private Context mContext;
        private ArrayList<items> itemlist;


        public ListView1Adapter(Context mContext,ArrayList<items>itemlist){
            this.mContext=mContext;
            this.itemlist=itemlist;
        }

        @Override
        public int getCount() {
            return itemlist.size();
        }

        @Override
        public Object getItem(int i) {
            return itemlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.wordle_word_list,viewGroup,false);
            }
            TextView word1=view.findViewById(R.id.wordText1);
            TextView word2=view.findViewById(R.id.wordText2);
            TextView word3=view.findViewById(R.id.wordText3);
            TextView word4=view.findViewById(R.id.wordText4);
            TextView word5=view.findViewById(R.id.wordText5);

            TextView[] wordlist={word1,word2,word3,word4,word5};

            for(int idx=0;idx<5;idx++){
                wordlist[idx].setText(itemlist.get(i).word.charAt(idx)+"");
                if(itemlist.get(i).wordColor[idx]==Color.GRAY)
                    wordlist[idx].setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                else if(itemlist.get(i).wordColor[idx]==Color.GREEN)
                    wordlist[idx].setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                else
                    wordlist[idx].setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow));
                if(itemlist.get(i).wordColor[idx]==Color.GRAY){
                    wordlist[idx].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                }
                else{
                    wordlist[idx].setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                }
            }
            return view;
        }
    }



    private class ListView2Adapter extends BaseAdapter{


        private Context mContext;
        private ArrayList<String> letterList;
        private int curColor;

        public ListView2Adapter(Context mContext,ArrayList<String> letterList,int curColor){
            this.mContext=mContext;
            this.letterList=letterList;
            this.curColor=curColor;
        }

        @Override
        public int getCount() {
            return letterList.size();
        }

        @Override
        public Object getItem(int i) {
            return letterList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                LayoutInflater layoutInflater =(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view=layoutInflater.inflate(R.layout.wordle_letter_list,viewGroup,false);
            }
            TextView letterView = view.findViewById(R.id.letterView);
            letterView.setText(letterList.get(i));
            if(curColor==Color.GRAY){
                letterView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.gray));
                letterView.setTextColor(Color.WHITE);
            }
            else if(curColor==Color.GREEN){
                letterView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                letterView.setTextColor(Color.BLACK);
            }
            else{
                letterView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.yellow));
                letterView.setTextColor(Color.BLACK);
            }
            return view;
        }
    }
}