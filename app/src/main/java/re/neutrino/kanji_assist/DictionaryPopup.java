package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
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
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.d("show", size.toString());
        float vertical_margin = context.getResources().getDimension(R.dimen.popup_vertical_margin);
        float height = context.getResources().getDimension(R.dimen.popup_height);
        if (position.y > size.y/2) {
            // bottom half
            Log.d("show", "bottom");
            position.y -= height + vertical_margin;
        } else {
            // top half
            Log.d("show", "top");
            position.y += vertical_margin;
        }
        view.setY(position.y);
        //view.setX(position.x);
        Log.d("show", String.valueOf(height));
        Log.d("show", position.toString());
    }

    private void close() {
        dialog.dismiss();
    }
}
