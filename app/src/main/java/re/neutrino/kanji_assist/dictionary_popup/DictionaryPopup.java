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

package re.neutrino.kanji_assist.dictionary_popup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.text_extractor.ScreenText;

public class DictionaryPopup extends GridLayout {
    private String TAG = getClass().getName();

    private Dictionary dictionary;

    public DictionaryPopup(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.popup, this);

        this.dictionary = new Dictionary();
    }

    public DictionaryPopup(Context context) {
        this(context, null);
    }

    public void show(ScreenText screenText) {

        close();

        final Resources resources = getContext().getResources();
        float vertical_margin = resources.getDimension(
                R.dimen.popup_vertical_margin);
        float height = resources.getDimension(R.dimen.popup_height);

        PointF popupPosition = getPopupPosition(
                screenText, vertical_margin, height);

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, (int) height);
        params.setMargins((int) popupPosition.x, (int) popupPosition.y, 0, 0);

        setLayoutParams(params);

        TextView textSelected = (TextView) findViewById(R.id.textSelected);
        textSelected.setText(screenText.getText());

        dictionary.get_definition(this, screenText.getText());

        Button button = (Button) findViewById(R.id.buttonClose);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                close();
            }});

        show();
    }

    @NonNull
    private PointF getPopupPosition(ScreenText screenText, float vertical_margin, float height) {
        final String tag = TAG + ".show";

        Log.d(tag, String.valueOf(height));

        final Rect textRect = screenText.getRect();
        Log.d(tag, String.valueOf(textRect));

        WindowManager wm = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        Log.d(tag, displaySize.toString());

        PointF result = new PointF();
        if (textRect.bottom > displaySize.y/2) {
            // bottom half
            Log.d(tag, "bottom");
            result.y = textRect.bottom - height + vertical_margin;
        } else {
            // top half
            Log.d(tag, "top");
            result.y = textRect.bottom + vertical_margin;
        }
        return result;
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    private void close() {
        setVisibility(GONE);
    }
}
