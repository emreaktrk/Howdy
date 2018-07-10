package istanbul.codify.muudy.utils;

/**
 * Created by egesert on 9.07.2018.
 */


import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordToSpan {
    private int colorTAG = -16776961;
    private int colorMENTION = -16776961;
    private int colorURL = -16776961;
    private int colorMAIL = -16776961;
    private int colorPHONE = -16776961;
    private int colorCUSTOM = -16776961;
    private int colorHIGHLIGHT = -1;
    private int backgroundHIGHLIGHT = -16776961;
    private String regexCUSTOM = null;
    private boolean underlineTAG = false;
    private boolean underlineMENTION = false;
    private boolean underlineURL = false;
    private boolean underlineMAIL = false;
    private boolean underlinePHONE = false;
    private boolean underlineCUSTOM = false;
    private WordToSpan.ClickListener clickListener;

    public WordToSpan() {
    }

    public void setRegexCUSTOM(String regexCUSTOM) {
        this.regexCUSTOM = regexCUSTOM;
    }

    public void setColorTAG(int colorTAG) {
        this.colorTAG = colorTAG;
    }

    public void setColorMENTION(int colorMENTION) {
        this.colorMENTION = colorMENTION;
    }

    public void setColorURL(int colorURL) {
        this.colorURL = colorURL;
    }

    public void setColorMAIL(int colorMAIL) {
        this.colorMAIL = colorMAIL;
    }

    public void setColorPHONE(int colorPHONE) {
        this.colorPHONE = colorPHONE;
    }

    public void setColorCUSTOM(int colorCUSTOM) {
        this.colorCUSTOM = colorCUSTOM;
    }

    public void setColorHIGHLIGHT(int colorHIGHLIGHT) {
        this.colorHIGHLIGHT = colorHIGHLIGHT;
    }

    public void setBackgroundHIGHLIGHT(int backgroundHIGHLIGHT) {
        this.backgroundHIGHLIGHT = backgroundHIGHLIGHT;
    }

    public void setUnderlineTAG(boolean underlineTAG) {
        this.underlineTAG = underlineTAG;
    }

    public void setUnderlineMENTION(boolean underlineMENTION) {
        this.underlineMENTION = underlineMENTION;
    }

    public void setUnderlineURL(boolean underlineURL) {
        this.underlineURL = underlineURL;
    }

    public void setUnderlineMAIL(boolean underlineMAIL) {
        this.underlineMAIL = underlineMAIL;
    }

    public void setUnderlinePHONE(boolean underlinePHONE) {
        this.underlinePHONE = underlinePHONE;
    }

    public void setUnderlineCUSTOM(boolean underlineCUSTOM) {
        this.underlineCUSTOM = underlineCUSTOM;
    }

    public void setClickListener(WordToSpan.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLink(String txt, View textView,String username) {
        Spannable ws = new SpannableString(txt);
        Matcher matcherTAG = Pattern.compile("(^|\\s+)#(\\w+)").matcher(txt);


        ws.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        while(matcherTAG.find()) {
            int st = matcherTAG.start();
            int en = st + matcherTAG.group(0).length();
            ws.setSpan(new WordToSpan.myClickableSpan(this.colorTAG, this.underlineTAG, "tag"), st, en, 33);
        }

        Matcher matcherMENTION = Pattern.compile("(^|\\s+)@(\\w+)").matcher(txt);


        while(matcherMENTION.find()) {
            int st = matcherMENTION.start();
            int en = st + matcherMENTION.group(0).length();
            ws.setSpan(new WordToSpan.myClickableSpan(this.colorMENTION, this.underlineMENTION, "mention"), st, en, 33);
            ws.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), st, en, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Matcher matcherURL = Pattern.compile("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]").matcher(txt);


        while(matcherURL.find()) {
            int st = matcherURL.start();
            int en = st + matcherURL.group(0).length();
            ws.setSpan(new WordToSpan.myClickableSpan(this.colorURL, this.underlineURL, "url"), st, en, 33);
        }

        Matcher matcherMAIL = Pattern.compile("([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9._-]+)").matcher(txt);


        while(matcherMAIL.find()) {
            int st = matcherMAIL.start();
            int en = st + matcherMAIL.group(0).length();
            ws.setSpan(new WordToSpan.myClickableSpan(this.colorMAIL, this.underlineMAIL, "mail"), st, en, 33);
        }

        Matcher matcherPHONE = Pattern.compile("\\d{13}|\\d{12}|\\d{11}|\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}").matcher(txt);


        while(matcherPHONE.find()) {
            int st = matcherPHONE.start();
            int en = st + matcherPHONE.group(0).length();
            ws.setSpan(new WordToSpan.myClickableSpan(this.colorPHONE, this.underlinePHONE, "phone"), st, en, 33);
        }

        if(this.regexCUSTOM != null && !this.regexCUSTOM.isEmpty()) {
            Matcher matcherCUSTOM = Pattern.compile(this.regexCUSTOM).matcher(txt);

            while(matcherCUSTOM.find()) {
                int st = matcherCUSTOM.start();
                int en = st + matcherCUSTOM.group(0).length();
                ws.setSpan(new WordToSpan.myClickableSpan(this.colorCUSTOM, this.underlineCUSTOM, "custom"), st, en, 33);
            }
        }

        TextView tv = (TextView)textView;
        tv.setText(ws);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(0);
    }

    public void setHighlight(String txt, String key, View textView) {
        Spannable ws = new SpannableString(txt);
        if(!key.isEmpty()) {
            Matcher matcherONE = Pattern.compile("(?i)" + key.trim()).matcher(txt);

            int en;
            while(matcherONE.find()) {
                int st = matcherONE.start();
                en = st + matcherONE.group(0).length();
                ws.setSpan(new ForegroundColorSpan(this.colorHIGHLIGHT), st, en, 33);
                ws.setSpan(new BackgroundColorSpan(this.backgroundHIGHLIGHT), st, en, 33);
            }

            String[] var14 = key.split(" ");
            en = var14.length;

            for(int var8 = 0; var8 < en; ++var8) {
                String retval = var14[var8];
                Matcher matcherALL = Pattern.compile("(?i)" + retval.trim()).matcher(txt);

                while(matcherALL.find()) {
                    int st = matcherALL.start();
                    en = st + matcherALL.group(0).length();
                    ws.setSpan(new ForegroundColorSpan(this.colorHIGHLIGHT), st, en, 33);
                    ws.setSpan(new BackgroundColorSpan(this.backgroundHIGHLIGHT), st, en, 33);
                }
            }
        }

        TextView tv = (TextView)textView;
        tv.setText(ws);
    }

    private class myClickableSpan extends ClickableSpan {
        int color;
        String type;
        boolean underline;

        myClickableSpan(int color, boolean underline, String type) {
            this.color = color;
            this.underline = underline;
            this.type = type;
        }

        public void onClick(View textView) {
            TextView tv = (TextView)textView;
            Spanned s = (Spanned)tv.getText();
            int start = s.getSpanStart(this);
            int end = s.getSpanEnd(this);
            WordToSpan.this.clickListener.onClick(this.type, s.subSequence(start, end).toString().trim());
        }

        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(this.color);
            ds.setUnderlineText(this.underline);
        }
    }

    public interface ClickListener {
        void onClick(String var1, String var2);
    }
}