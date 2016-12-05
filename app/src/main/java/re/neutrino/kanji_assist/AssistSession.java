package re.neutrino.kanji_assist;

import android.app.Dialog;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AssistSession extends VoiceInteractionSession {
    public AssistSession(Context context) {
        super(context);
    }

    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);
        Toast.makeText(getContext(), "Begin scan", Toast.LENGTH_SHORT).show();
        Log.d(getContext().getPackageName(),
                ((Integer)structure.getWindowNodeCount()).toString());
        Dialog dialog = getWindow();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Log.d("set_pos (pre), x", String.valueOf(layoutParams.x));
        Log.d("set_pos (pre), y", String.valueOf(layoutParams.y));
        layoutParams.gravity = Gravity.NO_GRAVITY;
        layoutParams.x = 100;
        layoutParams.y = 100;
        window.setAttributes(layoutParams);
        Log.d(getContext().getPackageName(), String.valueOf(dialog.isShowing()));
        dialog.setContentView(R.layout.popup);
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText("Example popup");
        layoutParams = window.getAttributes();
        Log.d("set_pos (post), x", String.valueOf(layoutParams.x));
        Log.d("set_pos (post), y", String.valueOf(layoutParams.y));
        Toast.makeText(getContext(), "End scan", Toast.LENGTH_SHORT).show();
    }
}
