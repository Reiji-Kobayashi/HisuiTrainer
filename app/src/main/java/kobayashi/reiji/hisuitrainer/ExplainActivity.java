package kobayashi.reiji.hisuitrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by kobayashireiji on 2019/01/17.
 */

public class ExplainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
    }

    public void onclick_title(View view){
        Intent intent = new Intent(kobayashi.reiji.hisuitrainer.ExplainActivity.this, TitleActivity.class);
        startActivity(intent);
    }
}
