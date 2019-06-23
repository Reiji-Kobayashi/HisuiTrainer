package kobayashi.reiji.hisuitrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kobayashireiji on 2018/12/27.
 */

public class TitleActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }

    public void onclick_button1(View view){
        Intent intent = new Intent(TitleActivity.this,GameActivity.class);
        startActivity(intent);
    }

    public void onclick_button2(View view){
        Intent intent = new Intent(TitleActivity.this,ExplainActivity.class);
        startActivity(intent);
    }
}
