package com.NEWROW.row.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.NEWROW.row.R;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenshotUtils {

    /*  Method which will return Bitmap after taking screenshot. We have to pass the view which we want to take screenshot.  */
    public static Bitmap getScreenShot(View screenView) {

//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
//        return bitmap;

        Bitmap bitmap = Bitmap.createBitmap(screenView.getWidth(),screenView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Drawable bgDrawable = screenView.getBackground();

        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        screenView.draw(canvas);

        return bitmap;

//        Bitmap b = Bitmap.createBitmap(screenView.getWidth() , screenView.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        screenView.layout(0, 0, screenView.getLayoutParams().width, screenView.getLayoutParams().height);
//        screenView.draw(c);
//        return b;
    }


    /*  Create Directory where screenshot will save for sharing screenshot  */
    public static File getMainDirectoryName(Context context) {
        File imagesFolder = new File(context.getCacheDir(), "images");
        return imagesFolder;
    }

    /*  Store taken screenshot into above created path  */
    public static File store(Bitmap bm, String fileName, File saveFilePath) {

        File dir = new File(saveFilePath.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(saveFilePath.getAbsolutePath(), fileName);

        try {

            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static Bitmap addWaterMark(Bitmap source,Context context,String category,String title) {

        Bitmap mergedBitmap = null, topBitmap=null, bottomBitmap=null;

        if(category.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_DT))){
            topBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.dt_header);
           // bottomBitmap =BitmapFactory.decodeResource(context.getResources(), R.drawable.dist_footer);
        } else if(category.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_CLUB))){
            topBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.club_header);
           // bottomBitmap =BitmapFactory.decodeResource(context.getResources(), R.drawable.club_footer);
        }else if (category.equalsIgnoreCase(String.valueOf(Constant.GROUP_CATEGORY_ADMIN))){
            //this is added by Gaurav
            topBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.riadmin_head);
        }

        // change by satish on 30-05-2019 as naren sir told
        bottomBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lib_footer_new);

        Bitmap patchBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.patch);

        int h=0;

        h = source.getHeight() + topBitmap.getHeight()+ bottomBitmap.getHeight()+patchBitmap.getHeight();

        mergedBitmap = Bitmap.createBitmap(source.getWidth(), h, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        canvas.drawColor(Color.WHITE);

        Matrix matrix = new Matrix();

        float scale = (float) (((float) source.getWidth() ) / (float) topBitmap.getWidth());
        matrix.postScale(scale,scale);
        RectF rectF =  new RectF(0, 0, topBitmap.getWidth(), topBitmap.getHeight());
        matrix.mapRect(rectF);
        canvas.drawBitmap(topBitmap, matrix,null);
        final float testTextSize = context.getResources().getDimensionPixelSize(R.dimen.font_size);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(testTextSize);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        int xOffset = getApproxXToCenterText(title, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), testTextSize, (int) rectF.width());

        canvas.drawText(title,xOffset,rectF.height()-25,paint);//rectF.height()-35,paint);

        canvas.drawBitmap(source, 0f, topBitmap.getHeight(), null);
       // canvas.drawBitmap(bottomBitmap, 0f,(topBitmap.getHeight()+source.getHeight()) , null);

        Matrix matrixB = new Matrix();
        float scaleB = (float) (((float) source.getWidth() ) / (float) bottomBitmap.getWidth());
        matrixB.postScale(scaleB,scaleB);
        RectF rectFB =  new RectF(0, 0, bottomBitmap.getWidth(), bottomBitmap.getHeight());
        matrixB.mapRect(rectFB);
        matrixB.postTranslate(source.getWidth() - rectFB.width(), h - rectFB.height());

        canvas.drawBitmap(bottomBitmap, matrixB,null);

        return mergedBitmap;
    }

    public static Bitmap addWaterMarkLib(Bitmap source,Context context,String title) {

        Bitmap mergedBitmap = null, topBitmap=null, bottomBitmap=null;

        topBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.library_head_new);
        bottomBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lib_footer_new);

        Bitmap patchBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.patch);

        int  h=0;

        h = source.getHeight() + topBitmap.getHeight()+ bottomBitmap.getHeight()+patchBitmap.getHeight();

        mergedBitmap = Bitmap.createBitmap(source.getWidth(), h, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mergedBitmap);

        canvas.drawColor(Color.WHITE);

        Matrix matrix = new Matrix();
        float scale = (float) (((float) source.getWidth() ) / (float) topBitmap.getWidth());
        matrix.postScale(scale,scale);
        RectF rectF =  new RectF(0, 0, topBitmap.getWidth(), topBitmap.getHeight());
        matrix.mapRect(rectF);
        canvas.drawBitmap(topBitmap, matrix,null);
        final float testTextSize = context.getResources().getDimensionPixelSize(R.dimen.font_size);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(testTextSize);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        int xOffset = getApproxXToCenterText(title, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), testTextSize, (int) rectF.width());

        canvas.drawText(title,xOffset,rectF.height()-25,paint);//rectF.height()-35,paint);

        canvas.drawBitmap(source, 0f, topBitmap.getHeight(), null);
        // canvas.drawBitmap(bottomBitmap, 0f,(topBitmap.getHeight()+source.getHeight()), null);
        Matrix matrixB = new Matrix();
        float scaleB = (float) (((float) source.getWidth() ) / (float) bottomBitmap.getWidth());
        matrixB.postScale(scaleB,scaleB);
        RectF rectFB =  new RectF(0, 0, bottomBitmap.getWidth(), bottomBitmap.getHeight());
        matrixB.mapRect(rectFB);
        matrixB.postTranslate(source.getWidth() - rectFB.width(), h - rectFB.height());

        canvas.drawBitmap(bottomBitmap, matrixB,null);

        return mergedBitmap;
    }

    public static int getApproxXToCenterText(String text, Typeface typeface, float fontSize, int widthToFitStringInto) {
        Paint p = new Paint();
        p.setTypeface(typeface);
        p.setTextSize(fontSize);
        float textWidth = p.measureText(text);
        int xOffset = (int)((widthToFitStringInto-textWidth)/2f) - (int)(fontSize/2f);
        return xOffset;
    }

//    public static Bitmap addWatermark( Bitmap source,Context context,String name) {
//        int w, h;
//        Canvas c;
//        Paint paint;
//        Bitmap bmp, watermark;
//        Matrix matrix;
//        float scale;
//        RectF r;
//        w = source.getWidth();
//        h = source.getHeight();
//        // Create the new bitmap
//        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
//        // Copy the original bitmap into the new one
//        c = new Canvas(bmp);
//       // paint.setAlpha(40);
//        c.drawBitmap(source, 0, 0, paint);
//        // Load the watermark
//        watermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.shareimage);
//        // Scale the watermark to be approximately 40% of the source image height
//        scale = (float) (((float) w ) / (float) watermark.getWidth());
//        // Create the matrix
//        matrix = new Matrix();
//        matrix.postScale(scale, scale);
//        // Determine the post-scaled size of the watermark
//        r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
//        matrix.mapRect(r);
//        // Move the watermark to the bottom right corner
//        matrix.postTranslate(w - r.width(), h - r.height());
//        // Draw the watermark
//
//        paint.setAlpha(50);
//        c.drawBitmap(watermark, matrix, paint);
//
////        Paint txtpaint = new Paint();
////        //apply color
////        txtpaint.setColor(Color.BLACK);
////
////        //set text size
////        txtpaint.setTextSize(40);
////        txtpaint.setAntiAlias(true);
////        //set should be underlined or not
////        txtpaint.setTextAlign(Paint.Align.CENTER);
////        txtpaint.setUnderlineText(false);
////        //draw text on given location
////        c.drawText(name,w-100 ,h-watermark.getHeight(), txtpaint);
////
////        // Free up the bitmap memory
////        watermark.recycle();
//        return bmp;
//    }


}
