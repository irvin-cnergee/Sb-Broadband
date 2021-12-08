package cnergee.sbbroadband.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

import cnergee.sbbroadband.R;

/**
 * Created by Jyoti on 6/15/2017.
 */

public class MyRadioButton extends RadioButton {

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyRadioButton(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
            String fontName = a.getString(R.styleable.MyRadioButton_fontName_radio);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
