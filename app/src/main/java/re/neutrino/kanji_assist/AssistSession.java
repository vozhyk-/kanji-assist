package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

class AssistSession extends VoiceInteractionSession {
    AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);

        ScreenText selected = getSelectedTextToDisplay(structure);

        showPopup(selected);
    }

    @NonNull
    private ScreenText getSelectedTextToDisplay(AssistStructure structure) {
        ScreenText result = TextExtractor.getSelectedText(structure);
        if (result != null) {
            Toast.makeText(getContext(),
                    "Found selected text", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(),
                    "Selected text not found", Toast.LENGTH_SHORT)
                    .show();
            result = new ScreenText("[No text selected]", new Point(500, 500));
        }
        return result;
    }

    private void showPopup(ScreenText screenText) {
        Dialog dialog = getWindow();
        Log.d(getContext().getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(screenText.getText());
        Button button = (Button) dialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closePopup();
            }});
        View view = dialog.findViewById(R.id.popup);

        final Point position = screenText.getPosition();
        Log.d(getContext().getPackageName(), String.valueOf(position));
        view.setX(position.x);
        view.setY(position.y);
    }

    private void closePopup() {
        Dialog dialog = getWindow();
        dialog.dismiss();
    }
}
