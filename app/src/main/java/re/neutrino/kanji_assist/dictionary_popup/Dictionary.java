package re.neutrino.kanji_assist.dictionary_popup;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

class Dictionary {
    void get_definition(String text, TextView textView) {
        DictionaryBackgroundTask dictionaryBackgroundTask = new DictionaryBackgroundTask(textView);
        Log.d("dictionary", text);
        Log.d("dictionary", textView.getText().toString());
        dictionaryBackgroundTask.execute(text);
        textView.setText("fetch: in progress");
        textView.setVisibility(View.VISIBLE);
    }
}
