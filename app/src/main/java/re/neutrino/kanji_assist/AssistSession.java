package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
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

        String selected = getSelectedTextToDisplay(structure);

        showPopup(selected, TextExtractor.x, TextExtractor.y);
    }

    @NonNull
    private String getSelectedTextToDisplay(AssistStructure structure) {
        String result = TextExtractor.getSelectedText(structure);
        if (result != null) {
            Toast.makeText(getContext(),
                    "Found selected text", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getContext(),
                    "Selected text not found", Toast.LENGTH_SHORT)
                    .show();
            result = "[No text selected]";
        }
        return result;
    }

    private void showPopup(String text, float x, float y) {
        Dialog dialog = getWindow();
        Log.d(getContext().getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(text);
        Button button = (Button) dialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closePopup();
            }});
        View view = dialog.findViewById(R.id.popup);
        Log.d(getContext().getPackageName(), String.valueOf(x));
        Log.d(getContext().getPackageName(), String.valueOf(y));
        view.setX(x);
        view.setY(y);
    }

    private void closePopup() {
        Dialog dialog = getWindow();
        dialog.dismiss();
    }
}
