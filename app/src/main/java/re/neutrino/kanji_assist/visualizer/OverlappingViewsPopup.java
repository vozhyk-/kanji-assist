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

package re.neutrino.kanji_assist.visualizer;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import re.neutrino.kanji_assist.R;
import re.neutrino.kanji_assist.text_extractor.AssistStructureWalker
        .AbsoluteViewNode;


class OverlappingViewsPopup extends LinearLayout {

    private LayoutInflater inflater;

    public OverlappingViewsPopup(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.overlapping_views_popup, this);
    }

    public OverlappingViewsPopup(Context context) {
        this(context, null);
    }

    public void show(ArrayAdapter<AbsoluteViewNode> adapter) {
        final ListView container = (ListView)
                findViewById(R.id.overlapping_views_list);
        container.setAdapter(adapter);

        final Button closeButton = (Button)
                findViewById(R.id.overlapping_views_popup_close);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });

        setLayoutParams(makeLayoutParams());

        show();
    }

    @NonNull
    private RelativeLayout.LayoutParams makeLayoutParams() {
        final Resources resources = getContext().getResources();
        int verticalMargin = (int) resources.getDimension(
                R.dimen.popup_vertical_margin);
        float height = resources.getDimension(R.dimen.popup_height);

        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, (int) height);
        params.setMargins(0, verticalMargin, 0, verticalMargin);
        return params;
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    private void close() {
        setVisibility(GONE);
    }
}
