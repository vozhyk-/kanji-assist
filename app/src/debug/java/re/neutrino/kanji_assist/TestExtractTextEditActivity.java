package re.neutrino.kanji_assist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TestExtractTextEditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_extract_text_edit);
    }

    public void requestAssist(View view) {
        showAssist(null);
    }
}
