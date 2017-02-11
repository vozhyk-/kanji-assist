/*
 * Copyright (C) 2016-2017 Witaut Bajaryn, Aleksander Mistewicz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package re.neutrino.kanji_assist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import java.io.IOException;

import re.neutrino.kanji_assist.assist_structure.AnyAssistStructure;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AssistStructureVisualizerActivity extends AppCompatActivity {
    public static final String STRUCTURE_KEY = "structure";
    public static final String FIXTURE_NAME_KEY = "fixture_name";

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler hideHandler = new Handler();
    private View contentView;
    private final Runnable hidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable showPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private AnyAssistStructure structure;

    private AssistStructureVisualizer visualizer;

    private AssistStructureDebugUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assist_structure_visualizer);

        contentView = findViewById(R.id.fullscreen_content);
        util = new AssistStructureDebugUtil(this);
        visualizer = (AssistStructureVisualizer)
                findViewById(R.id.visualizer_in_activity);

        loadFixture();
        visualizer.show(structure);
    }

    private void loadFixture() {
        final Bundle extras = getIntent().getExtras();

        if (loadStructureFromExtra(extras))
            return;

        if (loadFixtureFromNameExtra(extras))
            return;

        loadDefaultStructure();
    }

    private boolean loadStructureFromExtra(Bundle extras) {
        if (extras == null)
            return false;

        structure = (AnyAssistStructure) extras.get(STRUCTURE_KEY);
        return structure != null;
    }

    private boolean loadFixtureFromNameExtra(Bundle extras) {
        if (extras == null)
            return false;

        final String fixtureName = extras.getString(FIXTURE_NAME_KEY);
        //noinspection SimplifiableIfStatement
        if (fixtureName == null)
            return false;

        return loadFixtureFromName(fixtureName);
    }

    private boolean loadFixtureFromName(String name) {
        final int structureRes = getRawResourceByName(name);
        if (structureRes == 0)
            return false;

        structure = readStructure(structureRes);

        final int preScreenshotRes = getRawResourceByName(name + "_pre");
        if (preScreenshotRes != 0)
            loadPreScreenshot(preScreenshotRes);

        return true;
    }

    private int getRawResourceByName(String name) {
        return getResources().getIdentifier(
                name, "raw", getPackageName());
    }

    private void loadDefaultStructure() {
        structure = readStructure(R.raw.settings_with_baselines);
    }

    @Nullable
    private AnyAssistStructure readStructure(int res) {
        try {
            return util.readAssistStructure(res);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadPreScreenshot(int resource) {
        final Bitmap bitmap = BitmapFactory.decodeResource(
                getResources(), resource);
        Point size = getScreenSize();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                bitmap, size.x, size.y, true);
        contentView.setBackground(new BitmapDrawable(
                getResources(), scaledBitmap));
    }

    @NonNull
    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point result = new Point();
        display.getSize(result);
        return result;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        hide();
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable);
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        hideHandler.removeCallbacks(hideRunnable);
        hideHandler.postDelayed(hideRunnable, delayMillis);
    }
}
