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

    public DictionaryPopup(Dialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    public void show(ScreenText screenText) {
        dialog.show();
        Log.d(context.getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(screenText.getText());
        Button button = (Button) dialog.findViewById(R.id.button);
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
