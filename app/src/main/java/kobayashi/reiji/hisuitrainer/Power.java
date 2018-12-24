package kobayashi.reiji.hisuitrainer;

import java.util.Random;

/**
 * Created by kobayashireiji on 2018/12/09.
 */

public class Power {
    public int flag,kind;
    public float x,y;

    Random r = new Random();


    public Power(){
        this.flag=0;
        this.kind=0;
        this.x=0;
        this.y=0;
    }


    public Power(float x, float y){
        this.flag= 1;
        this.kind= r.nextInt(2);
        this.x=x;
        this.y=y;
    }

}
