package re.neutrino.kanji_assist;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

class AssistStructureSaver {
    public AssistStructureSaver() {
    }

    void saveStructureForDebug(AnyAssistStructure structure) {
        if (!BuildConfig.DEBUG)
            return;

        final FakeAssistStructure fakeStructure =
                new FakeAssistStructure(structure);

        if (!isExternalStorageWritable()) {
            Log.w("saveStructureForDebug",
                    "External storage not writable, not saving assist structure");
            return;
        }

        // TODO Ask for permission interactively

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "assist.bin");
        if (!file.getParentFile().mkdirs()) {
            Log.e("saveStructureForDebug", "Directory not created, not saving assist structure");
        }

        try (FileOutputStream output = new FileOutputStream(file)) {
            new ObjectOutputStream(output).writeObject(fakeStructure);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
