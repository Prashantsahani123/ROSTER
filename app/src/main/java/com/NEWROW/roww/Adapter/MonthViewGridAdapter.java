package com.NEWROW.row.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.NEWROW.row.Data.CalendarData;
import com.NEWROW.row.R;
import com.NEWROW.row.sql.CalendarMasterModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by user on 03-02-2017.
 */
public class MonthViewGridAdapter extends BaseAdapter {
    private static final String tag = "GridCellAdapter";
    private final Context _context;

    public final List<String> list;
    private static final int DAY_OFFSET = 1;
    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private final int[] daysOfMonth_NonLeapYear = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final int[] daysOfMonth_leapYear = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final int month, year;
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    private TextView selecteddate;
    private TextView tv_count;

    public boolean isLeapYear;
    public int currentMonth;
    long grpId;

    CalendarMasterModel calendarModel;
    int noOfeventscount = 0;

    EventsAdapter adapter;
    ArrayList<CalendarData> listOfEvents = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private View previousView;
    public String previousPositionCount;
    public String previousDay;

    // Days in Current Month
    public MonthViewGridAdapter(Context context, int textViewResourceId, int month, int year, long grpId) {
        super();
        this._context = context;
        this.list = new ArrayList<String>();
        this.month = month;
        this.year = year;
        this.grpId = grpId;
        calendarModel = new CalendarMasterModel(_context);
        adapter = new EventsAdapter(_context, listOfEvents);
        mRecyclerView = new RecyclerView(_context);

        Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
        Calendar calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
        Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
        Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
        Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

        checkisLeapYear(year);
        // Print Month
        printMonth(month, year);

    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private String getWeekDayAsString(int i) {
        return weekdays[i];
    }

    private int getNumberOfDaysOfMonth(int i) {
        if (isLeapYear) {
            return daysOfMonth_leapYear[i];
        } else {
            return daysOfMonth_NonLeapYear[i];
        }

    }

    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Prints Month
     *
     * @param mm
     * @param yy
     */
    private void printMonth(int mm, int yy) {
        Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
        // The number of days to leave blank at
        // the start of this month.
        int trailingSpaces = 0;
        int leadSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        currentMonth = mm - 1;
        String currentMonthName = getMonthAsString(currentMonth);

        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
        Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
            Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
            Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        }

        // Compute how much to leave before before the first day of the
        // month.
        // getDay() returns 0 for Sunday.
        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
        Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
        Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);


        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
            list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-WHITE" + "-" + prevMonth + "-" + prevYear + "-" + 0);
        }

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            String day = "";
            String month = String.valueOf(currentMonth + 1);
            int daylength = String.valueOf(i).length();
            int monthlength = month.length();
            if (daylength == 1) {
                day = "0" + String.valueOf(i);

            } else {
                day = String.valueOf(i);
            }
            if (monthlength == 1) {
                month = "0" + month;
            } else {
                month = month;
            }
            String date = yy + "-" + month + "-" + day;
            noOfeventscount = calendarModel.getCountForParticularDate(String.valueOf(grpId), date);
            Log.d(currentMonthName, String.valueOf(i) + " " + currentMonth + " " + yy);
            if (i == getCurrentDayOfMonth()) {
                list.add(String.valueOf(i) + "-BLUE" + "-" + currentMonth + "-" + yy + "-" + String.valueOf(noOfeventscount));

            } else {
                list.add(String.valueOf(i) + "-GRAY" + "-" + currentMonth + "-" + yy + "-" + String.valueOf(noOfeventscount));
            }

        }

        // Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
            list.add(String.valueOf(i + 1) + "-WHITE" + "-" + nextMonth + "-" + nextYear + "-" + String.valueOf(noOfeventscount));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.calendar_month_holder, parent, false);
        }

        // Get a reference to the Day gridcell
        selecteddate = (TextView) row.findViewById(R.id.date);
        tv_count = (TextView) row.findViewById(R.id.count);

        // ACCOUNT FOR SPACING

        Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
        String[] day_color = list.get(position).split("-");
        String theday = day_color[0];
        String themonth = day_color[2];
        String theyear = day_color[3];
        String count = day_color[4];


        // Set the Day GridCell
        selecteddate.setTag(theday + "-" + themonth + "-" + theyear);
        Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

        if (day_color[1].equals("WHITE")) {
            selecteddate.setTextColor(Color.LTGRAY);
            selecteddate.setText("");
            selecteddate.setClickable(false);
        }
        if (day_color[1].equals("GRAY")) {
            selecteddate.setText(theday);
            if (count.equalsIgnoreCase("0")) {
                selecteddate.setTextColor(Color.GRAY);
            } else {
                tv_count.setText(count);
                selecteddate.setTextColor(Color.parseColor("#1875D1"));
                selecteddate.setBackgroundResource(R.drawable.rounded_circle_blue_border);
                tv_count.setVisibility(View.VISIBLE);
            }

        }
        if (day_color[1].equals("BLUE")) {
            if (year == Calendar.getInstance().get(Calendar.YEAR) && currentMonth == Calendar.getInstance().get(Calendar.MONTH)) {
                selecteddate.setTextColor(Color.WHITE);
                selecteddate.setBackgroundResource(R.drawable.rounded_circle);
                if (count.equalsIgnoreCase("0")) {

                } else {
                    tv_count.setText(count);
                    tv_count.setVisibility(View.VISIBLE);
                }

            } else {
                selecteddate.setTextColor(Color.GRAY);
                if (count.equalsIgnoreCase("0")) {

                } else {
                    tv_count.setText(count);
                    selecteddate.setTextColor(Color.parseColor("#1875D1"));
                    selecteddate.setBackgroundResource(R.drawable.rounded_circle_blue_border);
                    tv_count.setVisibility(View.VISIBLE);
                }

            }
            selecteddate.setText(theday);

        }

        return row;
    }


    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }

    public boolean checkisLeapYear(int year) {
        if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
            isLeapYear = true;
            return true;
        } else {
            isLeapYear = false;
            return false;
        }
    }

    public View setSelected(View view, int pos) {
        if (previousView != null) {
            if (getPreviousPositionEventCount().equalsIgnoreCase("0")) {
                if (getPreviousDay().equalsIgnoreCase(String.valueOf(getCurrentDayOfMonth()))) {
                    if (year == Calendar.getInstance().get(Calendar.YEAR) && currentMonth == Calendar.getInstance().get(Calendar.MONTH)) {
                        previousView.setBackgroundResource(R.drawable.rounded_circle);
                    } else {
                        previousView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                } else {
                    selecteddate = (TextView) previousView.findViewById(R.id.date);
                    selecteddate.setTextColor(Color.GRAY);
                    previousView.setBackgroundColor(Color.parseColor("#00000000"));
                }
            } else {
                if (getPreviousDay().equalsIgnoreCase(String.valueOf(getCurrentDayOfMonth()))) {
                    if (year == Calendar.getInstance().get(Calendar.YEAR) && currentMonth == Calendar.getInstance().get(Calendar.MONTH)) {
                        previousView.setBackgroundResource(R.drawable.rounded_circle);
                    } else {
                        previousView.setBackgroundColor(Color.parseColor("#00000000"));
                    }
                } else {
                    selecteddate = (TextView) previousView.findViewById(R.id.date);
                    selecteddate.setTextColor(Color.parseColor("#1875D1"));
                    previousView.setBackgroundResource(R.drawable.rounded_circle_blue_border);
                }
            }
        }


        String[] newList = list.get(pos).split("-");
        String day = newList[0];
        String count = newList[4];

        if (newList[1].equals("WHITE")) {

        } else {
            previousView = view;
            setPreviousEventCount(count);
            setPreviousDay(day);
            previousView.setBackgroundResource(R.drawable.rounded_circle_gray_background);
            selecteddate = (TextView) previousView.findViewById(R.id.date);
            selecteddate.setTextColor(Color.WHITE);
        }


        return view;
    }

    public String getPreviousPositionEventCount() {
        return previousPositionCount;
    }

    public void setPreviousEventCount(String count) {
        this.previousPositionCount = count;
    }


    public String getPreviousDay() {
        return previousDay;
    }

    public void setPreviousDay(String day) {
        this.previousDay = day;
    }


}
