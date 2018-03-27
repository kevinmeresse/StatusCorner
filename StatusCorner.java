package com.km.myproject.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.km.myproject.R;

/**
 * Custom view displaying a corner overlay (default is top left) with an optional rotated text
 *
 * @author Kevin Meresse
 * @since 3/26/2018.
 */
public class StatusCorner extends View {

    // Text
    private CharSequence text;
    private StaticLayout textLayout;
    private TextPaint textPaint;
    private Point textOrigin;
    private boolean textAllCaps = false;
    private int textAngle;

    // Background
    private int cornerGravity;
    private Path bgPath;
    private Paint bgPaint;

    public StatusCorner(Context context) {
        super(context);
        init(null, 0);
    }

    public StatusCorner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StatusCorner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        // Corner background color
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);

        // Text color and origin coordinates
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textOrigin = new Point(0, 0);

        // Retrieve custom data from the layout
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StatusCorner, 0, defStyleAttr);
        try {
            bgPaint.setColor(a.getColor(R.styleable.StatusCorner_bgColor, 0));
            textPaint.setColor(a.getColor(R.styleable.StatusCorner_statusTextColor, 0));
            textPaint.setTextSize(a.getDimensionPixelSize(R.styleable.StatusCorner_statusTextSize, 10));
            textAllCaps = a.getBoolean(R.styleable.StatusCorner_statusTextAllCaps, false);
            cornerGravity = a.getInt(R.styleable.StatusCorner_cornerGravity, Gravity.TOP | Gravity.START);
            setText(a.getText(R.styleable.StatusCorner_statusText));
        } finally {
            a.recycle();
        }

        // Calculate the text angle
        switch (cornerGravity) {
            case Gravity.TOP | Gravity.END:
            case Gravity.BOTTOM | Gravity.START:
                textAngle = 45;
                break;
            default:
                textAngle = -45;
        }
    }

    public void setText(int resId) {
        setText(getResources().getText(resId));
    }

    public void setText(CharSequence text) {
        if (!TextUtils.equals(this.text, text)) {
            if (textAllCaps && text != null) text = text.toString().toUpperCase();
            this.text = text;
            updateContentBounds();
            invalidate();
        }
    }

    public void setStatusTextSize(float size) {
        textPaint.setTextSize(size);
        invalidate();
    }

    private void updateContentBounds() {
        if (text == null) {
            text = "";
        }

        // Create the text box
        if (textPaint != null) {
            float textWidth = textPaint.measureText(text, 0, text.length());
            textLayout = new StaticLayout(text, textPaint, (int) textWidth,
                    Layout.Alignment.ALIGN_CENTER, 1f, 0f, true);
        }

        // Calculate the text origin depending on the corner direction
        if (textLayout != null) {
            int left;
            int top;
            double diagonal = Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
            switch (cornerGravity) {
                case Gravity.BOTTOM | Gravity.START:
                    left = (int) ((diagonal - textLayout.getWidth()) / 2);
                    top = textLayout.getHeight() / 4;
                    break;
                case Gravity.TOP | Gravity.END:
                    left = (int) ((diagonal - textLayout.getWidth()) / 2);
                    top = -1 * (textLayout.getHeight() + (textLayout.getHeight() / 4));
                    break;
                case Gravity.BOTTOM | Gravity.END:
                    left = -1 * (textLayout.getWidth() / 2);
                    top = (int) ((diagonal / 2) + (textLayout.getHeight() / 4));
                    break;
                default:
                    left = -1 * (textLayout.getWidth() / 2);
                    top = (getHeight() - textLayout.getHeight()) / 2;
            }
            textOrigin.set(left, top);
        }
    }

    private void createTrianglePath(int w, int h) {
        bgPath = new Path();
        switch (cornerGravity) {
            case Gravity.TOP | Gravity.END:
                bgPath.moveTo(0, 0);
                bgPath.lineTo(w, h);
                bgPath.lineTo(w, 0);
                bgPath.lineTo(0, 0);
                break;
            case Gravity.BOTTOM | Gravity.START:
                bgPath.moveTo(0, 0);
                bgPath.lineTo(w, h);
                bgPath.lineTo(0, h);
                bgPath.lineTo(0, 0);
                break;
            case Gravity.BOTTOM | Gravity.END:
                bgPath.moveTo(0, h);
                bgPath.lineTo(w, 0);
                bgPath.lineTo(w, h);
                bgPath.lineTo(0, h);
                break;
            default:
                bgPath.moveTo(0, h);
                bgPath.lineTo(w, 0);
                bgPath.lineTo(0, 0);
                bgPath.lineTo(0, h);
        }
        bgPath.close();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Update background bounds
        createTrianglePath(w, h);

        if (w != oldw || h != oldh) {
            // Update content bounds
            updateContentBounds();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the triangle
        if (bgPath != null) {
            canvas.drawPath(bgPath, bgPaint);
        }

        // Draw the text
        if (textLayout != null) {
            canvas.save();
            canvas.rotate(textAngle);
            canvas.translate(textOrigin.x, textOrigin.y);
            textLayout.draw(canvas);
            canvas.restore();
        }
    }
}
