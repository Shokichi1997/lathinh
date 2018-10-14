package com.shokichi.gamelathinh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


public class GamePlay extends AppCompatActivity {
    private List<Drawable> images;
    private int COLLUM_PIC;
    private int ROW_PIC;
    private int[][] cards;
    private int level;
    private Drawable hinhUp;
    private Context context;
    private TableLayout tblCards;
    private Queue<Drawable> category  = null;
    private Button card1,card2;
    private int soCapDung = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        hinhUp = getResources().getDrawable(R.drawable.ic_launcher_background);
        tblCards = findViewById(R.id.tblCards);

        context = tblCards.getContext();
        category = new LinkedList<>();
        level = 1;
        newGame();
    }

    public void newGame(){
        loadLevel(level);
        loadPicture();
        tblCards.removeAllViews();
        for(int i=0;i<ROW_PIC;i++){
            tblCards.addView(addTableRow(i));
        }
        addCards();
    }
    /** set collum and row of picture block
     * @param level level of game*/
    public void loadLevel(int level){
        if(level == 1){
            COLLUM_PIC = 2;
            ROW_PIC = 2;
        } else{
            COLLUM_PIC = 4;
            ROW_PIC = level;
        }


    }

    /**
     * add rows into table*/
    public TableRow addTableRow(int row_index){
        TableRow tableRow = new TableRow(context);

        for(int i = 0;i<COLLUM_PIC;i++){
            tableRow.addView(createButton(row_index,i));
        }
        return tableRow;
    }

    /**
     * create a button (a card) that is button to process click
     * @param x is button location in row of buttons matrix
     * @param y is button location in column of buttons matrix*/
    public Button createButton(int x,int y){
        Button btnCard = new Button(context);
        btnCard.setBackground(hinhUp);
        btnCard.setId(100+x+y);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
                );
        params.setMargins(8,8,8,8);

        Card newCard = new Card(x,y,btnCard);
        btnCard.setLayoutParams(params);
        btnCard.setOnClickListener(new OnClickCardButton(newCard));
        return btnCard;
    }

    /**
     * Add a cards array to save image location => To match with images array*/
    public void addCards(){
        cards = new int[ROW_PIC][COLLUM_PIC];
        int size = ROW_PIC * COLLUM_PIC;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<size;i++){
            list.add(i);
        }
        Random ran = new Random();
        for(int i=size-1;i>=0;i--) {
            int t=0;

            if(i>0){
                t = ran.nextInt(i);
            }

            //Xóa số ở vị trí thứ t trong list đồng thời lấy số đó gán vào mảng cards
            //=> Lần lượt sẽ lấy hết các số trong list ra => Không bị trùng số
            t = list.remove(t);
            //lấy kết quả chia dư cho 2 => t được 1 cặp số
            cards[i/COLLUM_PIC][i%COLLUM_PIC] = t%(size/2);

        }
    }
    /**load hình vào list images*/
    public void loadPicture(){
        images = new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.apple));
        images.add(getResources().getDrawable(R.drawable.banana));
        images.add(getResources().getDrawable(R.drawable.cherry));
        images.add(getResources().getDrawable(R.drawable.custardapple));
        images.add(getResources().getDrawable(R.drawable.grape));
        images.add(getResources().getDrawable(R.drawable.lemon));
        images.add(getResources().getDrawable(R.drawable.mango));
        images.add(getResources().getDrawable(R.drawable.orange));
        images.add(getResources().getDrawable(R.drawable.papaya));
        images.add(getResources().getDrawable(R.drawable.peach));
        images.add(getResources().getDrawable(R.drawable.mangosteen));
        images.add(getResources().getDrawable(R.drawable.pineapple));
        images.add(getResources().getDrawable(R.drawable.strawberry));
        images.add(getResources().getDrawable(R.drawable.pumkim));
        images.add(getResources().getDrawable(R.drawable.watermelon));
        images.add(getResources().getDrawable(R.drawable.pear));
        images.add(getResources().getDrawable(R.drawable.apple));
    }

    Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() {
            thucHienSoSanh();
        }
    });

    private void thucHienSoSanh() {
        Drawable btnMot = category.poll();
        Drawable btnHai = category.poll();

        if (btnHai == btnMot) {
            soCapDung++;
            if (soCapDung == ((COLLUM_PIC * ROW_PIC) / 2)) {
                Toast.makeText(GamePlay.this, "Win", Toast.LENGTH_LONG).show();
                level++;
                soCapDung = 0;
                newGame();
            }
        } else {
            card1.setBackground(hinhUp);
            card2.setBackground(hinhUp);

        }
    }

    public class OnClickCardButton implements View.OnClickListener{


        private Card card;

        private Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what == 1){
                    card.getButton().setBackground(( Drawable ) msg.obj);
                }
                return false;
            }
        });



        public OnClickCardButton(Card card){
            this.card = card;

        }

        @Override
        public void onClick(View v) {
            //this.card = v;
            latHinh(v);
            soSanh();
        }

        /**images: Lưu danh sách hình (id của hình)
         * cards: Lưu chỉ số của hình (vị trí)
         * Khi click vào button => Card => x & y => cards[x][y] => index => images[index]*/
        private void soSanh() {

            if(category.size()==2){
                thucHienSoSanh();
            }
        }

        private void thucHienSoSanh() {
            Drawable btnMot = category.poll();
            Drawable btnHai = category.poll();

            if (btnHai == btnMot){
                soCapDung++;
                if(soCapDung==((COLLUM_PIC*ROW_PIC)/2)){
                    Toast.makeText(GamePlay.this,"Win",Toast.LENGTH_LONG).show();
                    level++;
                    soCapDung = 0;
                    newGame();
                }
            }
            else {
                card1.setBackground(hinhUp);
                card2.setBackground(hinhUp);

            }
        }

        private void latHinh(View v) {
            if(v.getId() == card.getButton().getId()) {
                int index = cards[card.getX()][card.getY()];
                Log.v("cate = ",category.size()+"");
                if (category.size() <= 2) {
                    category.add(images.get(index));


                    //Send message

                    Message message = handler.obtainMessage(1,images.get(index));
                    handler.sendMessage(message);

                    //card.getButton().setBackground(images.get(index)); //=> chưa xử lý được chỗ lật hình thứ 2
                    Log.v("Image: ",images.get(index).toString());
                    if(category.size() == 1){
                        card1 = new Button(context);
                        card1 = card.getButton();
                    }
                    else if(category.size() == 2){
                        card2 = new Button(context);
                        card2 = card.getButton();
                    }

                }
            }

        }
    }

}
