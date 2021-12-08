package cnergee.sbbroadband.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import cnergee.sbbroadband.R;

/**
 * Created by Jyoti on 7/3/2017.
 */

public class MyCheckbox extends CheckBox {

    public MyCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MyCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MyCheckbox(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyCheckbox);
            String fontName = a.getString(R.styleable.MyCheckbox_fontName_check);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }


}
