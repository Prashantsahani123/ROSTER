package com.NEWROW.row.Data;

/**
 * Created by user on 30-01-2017.
 */
public class CalendarViewOption {
      String text;
      int image;

    public CalendarViewOption(String text, int image){
        this.text = text;
        this.image = image;

    }

   public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
