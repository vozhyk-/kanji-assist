package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class DictionaryPopup {
    Dialog dialog;
    Context context;
    Dictionary dictionary;

    public DictionaryPopup(Dialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
        this.dictionary = new Dictionary();
    }

    public void show(ScreenText screenText) {
        dialog.show();
        Log.d(context.getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);

        TextView textSelected = (TextView) dialog.findViewById(R.id.textSelected);
        textSelected.setText(screenText.getText());

        TextView textDictionary = (TextView) dialog.findViewById(R.id.textDictionary);
        dictionary.get_definition(screenText.getText(), textDictionary);

        Button button = (Button) dialog.findViewById(R.id.buttonClose);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                close();
            }});

        View view = dialog.findViewById(R.id.popup);

        final Point position = screenText.getPosition();
        Log.d(context.getPackageName(), String.valueOf(position));
        view.setX(position.x);
        view.setY(position.y);
    }

    private void close() {
        dialog.dismiss();
    }
}
