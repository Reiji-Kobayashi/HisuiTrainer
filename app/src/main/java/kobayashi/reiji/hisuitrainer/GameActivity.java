package kobayashi.reiji.hisuitrainer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameView gV = new GameView(this);

        /*フィールドの大きさを画面サイズに設定*/
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        gV.fieldMaxX = size.x;
        gV.fieldMaxY = size.y;

        setContentView(gV);
    }

}