package re.neutrino.kanji_assist;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class BasicTest {
    protected Instrumentation instr;
    protected Context globalContext;
    protected Context context;
    protected UiDevice device;

    protected String appName;

    @Before
    public void setUp() throws Exception {
        instr = getInstrumentation();
        globalContext = instr.getContext();
        context = instr.getTargetContext();
        device = UiDevice.getInstance(instr);

        appName = context.getString(R.string.app_name);
    }
}
