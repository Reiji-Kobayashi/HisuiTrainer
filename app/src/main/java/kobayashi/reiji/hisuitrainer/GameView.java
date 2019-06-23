package kobayashi.reiji.hisuitrainer;

/**
 * Created by kobayashireiji on 2018/12/07.
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.lang.Math;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    //ゲームシステム全般
    public int mode=10 ,counter=0;
    public int startflag=0;

    //ステージ（描画範囲）
    public float fieldX=0, fieldY=0,fieldMaxX, fieldMaxY;

    //画像サイズ
    public int sizex=150, sizey=300;


    //プレイヤー
    public float pX, pY;
    int life=0;

    //スコア
    public int score=0;

    Random r = new Random();

    //パワー
    int powernum=0, powernum_max=30;
    float powerspeed;
    Power powers[] = new Power[powernum_max];


    //画像
    Resources res = this.getContext().getResources();

    Bitmap Imhisui0 = BitmapFactory.decodeResource(res, R.drawable.hisui0);
    Bitmap hisui0 = Bitmap.createScaledBitmap(Imhisui0,200,sizey,false);


    Bitmap Imcoin = BitmapFactory.decodeResource(res, R.drawable.coin);
    Bitmap coin = Bitmap.createScaledBitmap(Imcoin,sizex,sizex,false);

    Bitmap Impower0 = BitmapFactory.decodeResource(res, R.drawable.power0);
    Bitmap power0 = Bitmap.createScaledBitmap(Impower0,sizex,sizex,false);

    Bitmap Immana = BitmapFactory.decodeResource(res, R.drawable.mana);
    Bitmap mana = Bitmap.createScaledBitmap(Immana,100,100,false);

    /*Surfaceview*/
    private void init(){
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
    }

    public GameView(Context context){
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle){
        super(context, attributeSet, defStyle);
        init();
    }

    /*メインループ*/
    private void startExecutor(){
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                switch (mode){
                    case 0:
                        mode=10;
                        break;
                    case 10:
                        stageinit();
                        break;
                    case 20:
                        powermove();
                        OnDraw(getHolder());
                        if(score>=30){
                            mode=31;
                        }else if(life<=0){
                            mode=30;
                        }
                        break;
                    case 30:
                    case 31:
                        clear(getHolder());
                        break;
                    case 33:
                        getContext().startActivity(new Intent(getContext(), TitleActivity.class));
                        service.shutdown();
                        break;
                    default:
                        break;
                }
            }
        }, 30, 30, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        OnDraw(surfaceHolder);
        startExecutor();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3){}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){}


    @Override
    public boolean onTouchEvent(MotionEvent e){
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN: //タップ開始
                startflag=1;
                break;
            case MotionEvent.ACTION_MOVE: //タップ開始
                pX = e.getX();
                break;
        }
        return true;
    }

    /*描画*/
    public void OnDraw(SurfaceHolder holder){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.rgb(100,100,100));
        Paint p = new Paint();

        c.drawBitmap(hisui0, pX , pY, p);

        for (int i = 0; i < powernum; i++) {
            if (powers[i].flag == 1) {
                if (powers[i].kind == 0) {
                    c.drawBitmap(coin, powers[i].x, powers[i].y, p);
                } else {
                    c.drawBitmap(power0, powers[i].x, powers[i].y, p);
                }
            }
        }


        p.setColor(Color.BLACK);

        //c.drawBitmap(mana, 0, 0, p);

        p.setTextSize(100);
        c.drawText("マナ:"+life,0,100,p);
        c.drawText("得点:"+score,400,100,p);
        c.drawText("パワー:"+powernum,0,200,p);

        holder.unlockCanvasAndPost(c);
    }

    /*--------------------以下ゲームシステム-----------------------------------------------------*/


    /*ステージの初期化*/
    private void stageinit(){
        for(int i=0; i<powernum_max; i++){
            powers[i] = new Power(r.nextFloat() * fieldMaxX, 0);
        }

        pX=fieldMaxX/2;
        pY=fieldMaxY-sizey/2-200;
        mode=20;
        score=0;
        life=10;
        powernum=3;
        powerspeed=10;
    }

    /*力の移動と衝突*/
    private void powermove(){
        for(int i=0; i<powernum; i++) {
            if (powers[i].flag < 0) {
                powers[i].flag += 1;
            } else if (powers[i].flag == 0) {
                powers[i]= new Power(sizex+r.nextFloat() * (fieldMaxX-sizex*2), 100);
            } else if (powers[i].flag == 1) {
                powers[i].y += powerspeed;
                if (Math.abs(pX - powers[i].x) < 50 && powers[i].y > fieldMaxY-sizey-150 && powers[i].y < fieldMaxY) {
                    if(powers[i].kind==0){
                        life+=1;
                        powers[i].flag = -r.nextInt(100);
                    } else if(powers[i].kind==1) {
                        score += 1;
                        if (score % 5 == 0)
                            powerspeed += 0.5;
                        life -= 1;
                        powers[i].x=powers[powernum-1].x;
                        powers[i].y=powers[powernum-1].y;
                        powers[i].kind=powers[powernum-1].kind;
                        powers[i].flag=powers[powernum-1].flag;
                        powers[powernum-1].flag=-r.nextInt(100);
                        powernum-=1;
                        if(powernum<=0){
                            powernum = 0;
                            mode = 30;
                        }
                    }
                } else if (powers[i].y > fieldMaxY) {
                    if(powers[i].kind==1) {
                        powernum += 2;
                        if (powernum >= powernum_max) {
                            powernum = powernum_max - 1;
                            mode = 30;
                        }
                    }
                    powers[i].flag = -r.nextInt(100);
                }
            }
        }
    }

    /*ゲームクリア判定*/
    private void clear(SurfaceHolder holder){
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.rgb(255,255,0));
        Paint p = new Paint();

        p.setTextSize(150);
        p.setColor(Color.RED);

        if(mode==31) {
            p.setColor(Color.RED);
            c.drawText("GAME CLEAR",0, fieldMaxY/2, p);
        }
        if(mode==30){
            p.setColor(Color.BLUE);
            c.drawText("GAME OVER",0, fieldMaxY/2,p);
        }

        p.setTextSize(70);
        p.setColor(Color.BLACK);
        c.drawText("得点:"+score,0, fieldMaxY/2+100,p);

        counter++;
        if(counter>=100) {
            mode = 33;
            Log.d("d", "finish");
        }

        holder.unlockCanvasAndPost(c);

    }
}