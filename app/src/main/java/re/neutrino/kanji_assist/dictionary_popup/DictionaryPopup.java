package re.neutrino.kanji_assist.dictionary_popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.text_extractor.ScreenText;

public class DictionaryPopup {
    private Dialog dialog;
    private Context context;
    private RelativeLayout parent;

    private GridLayout popup;
    private Dictionary dictionary;

    public DictionaryPopup(Dialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
        this.dictionary = new Dictionary();
    }

    public void show(ScreenText screenText, RelativeLayout parent) {
        this.parent = parent;

        close();

        Log.d(context.getPackageName(), String.valueOf(dialog.isShowing()));

        float vertical_margin = context.getResources().getDimension(R.dimen.popup_vertical_margin);
        float height = context.getResources().getDimension(R.dimen.popup_height);

        PointF popupPosition = getPopupPosition(
                screenText, vertical_margin, height);

        popup = (GridLayout) dialog.getLayoutInflater()
                .inflate(R.layout.popup, parent, false);

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, (int) height);
        params.setMargins((int) popupPosition.x, (int) popupPosition.y, 0, 0);

        parent.addView(popup, params);

        TextView textSelected = (TextView) dialog.findViewById(R.id.textSelected);
        textSelected.setText(screenText.getText());

        TextView textDictionary = (TextView) dialog.findViewById(R.id.textDictionary);
        dictionary.get_definition(screenText.getText(), textDictionary);

        Button button = (Button) dialog.findViewById(R.id.buttonClose);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                close();
            }});
    }

    @NonNull
    private PointF getPopupPosition(ScreenText screenText, float vertical_margin, float height) {
        Log.d("show", String.valueOf(height));

        final Rect textRect = screenText.getRect();
        Log.d(context.getPackageName(), String.valueOf(textRect));

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        Log.d("show", displaySize.toString());

        PointF result = new PointF();
        if (textRect.bottom > displaySize.y/2) {
            // bottom half
            Log.d("show", "bottom");
            result.y = textRect.bottom - height + vertical_margin;
        } else {
            // top half
            Log.d("show", "top");
            result.y = textRect.bottom + vertical_margin;
        }
        return result;
    }

    private void close() {
        parent.removeView(popup);
    }
}
