package re.neutrino.kanji_assist.text_extractor;

import android.graphics.Point;

public class ScreenText {
    private String text;
    private Point position;

    public ScreenText(String text, Point position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public String toString() {
        return getText() + " @ " + getPosition();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScreenText) {
            final ScreenText other = (ScreenText) obj;
            return getText().equals(other.getText()) &&
                    getPosition().equals(other.getPosition());
        }
        return super.equals(obj);
    }

    public String getText() {
        return text;
    }

    public Point getPosition() {
        return position;
    }
}
