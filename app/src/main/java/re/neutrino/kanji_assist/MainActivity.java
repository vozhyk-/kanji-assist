package re.neutrino.kanji_assist;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ensureAssistantEnabled();
    }

    private void ensureAssistantEnabled() {
        if (!isAssistantEnabled()) {
            Toast.makeText(this,
                    R.string.please_enable_assistant, Toast.LENGTH_LONG);
            startActivity(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));
        }
    }

    private boolean isAssistantEnabled() {
        final String assistantComponent = Settings.Secure.getString(
                getContentResolver(),
                "voice_interaction_service");

        if (assistantComponent == null)
            return false;

        final ComponentName cn =
                ComponentName.unflattenFromString(assistantComponent);
        final String assistantPackage = cn.getPackageName();

        return assistantPackage.equals(getPackageName());
    }
}
