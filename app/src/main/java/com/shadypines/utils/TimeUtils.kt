package com.shadypines.utils

import android.annotation.SuppressLint
import com.shadypines.radio.model.schecule.Day
import com.shadypines.radio.model.schecule.Schedule
import java.text.DateFormatSymbols
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * This is a class that contains utils for the time
 *  @author Al Mujahid Khan
 * */
class TimeUtils private constructor() {

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        /**
         * This method provides current system time in milliseconds
         *
         * @return [Long] current time
         * */
        fun currentTime(): Long {
            return System.currentTimeMillis()


        }

        fun getCurrentDayName(): String {
//            val calendar = Calendar.getInstance()
//            val date = calendar.time
//           return SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)
            val formatter = SimpleDateFormat("EE")
            formatter.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
            val cal = Calendar.getInstance()
            val timestamp = formatter.format(cal.time)
            return timestamp
        }


        @SuppressLint("SimpleDateFormat")
        fun getTimeFromTimeStamp(stamp: Long): String {
            val date = Date(stamp)
            val converterFormat = SimpleDateFormat("hh:mm a")
            //converterFormat.timeZone = TimeZone.getDefault()
            converterFormat.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
            return converterFormat.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun getDayNameByTimeStamp(stamp: Long): String {
            val date = Date(stamp)
            val converterFormat = SimpleDateFormat("EE")
            converterFormat.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
            return converterFormat.format(date)
        }

        /**
         * This method provides difference between current time in milliseconds and provided time
         *
         * @param time time in milliseconds
         * @return [Long] time difference in milliseconds
         */
        fun differ(time: Long): Long {
            return currentTime() - time
        }

        /**
         * This method converts milliseconds to days
         *
         * @param time time in milliseconds
         * @return [Int] count of days
         */
        fun convertMillisecondsToDays(time: Long): Int {
            return ((time.toDouble()) / (24 * 60 * 60 * 1000).toDouble()).toInt()
        }

        /**
         * This method provides a time ago text depending on the time difference
         *
         * @param newTime new time
         * @param oldTime old time
         * @return [String] time ago text
         * */
        fun getTimeAgo(newTime: Long, oldTime: Long): String {
            val diff = newTime - oldTime

            return when {
                diff < MINUTE_MILLIS -> "Just now"
                diff < 2 * MINUTE_MILLIS -> "A minute ago"
                diff < 50 * MINUTE_MILLIS -> (diff / MINUTE_MILLIS).toString() + " minutes ago"
                diff < 90 * MINUTE_MILLIS -> "An hour ago"
                diff < 24 * HOUR_MILLIS -> (diff / HOUR_MILLIS).toString() + " hours ago"
                diff < 48 * HOUR_MILLIS -> "Yesterday"
                else -> (diff / DAY_MILLIS).toString() + " days ago"
            }
        }

        /**
         * This method provides the year based on the time stamp
         *
         * @param timeStamp timestamp
         * @return [Int] year
         * */
        fun getYear(timeStamp: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            return calendar.get(Calendar.YEAR)
        }

        /**
         * This method provides the month based on the time stamp
         *
         * @param timeStamp timestamp
         * @return [Int] month
         * */
        fun getMonth(timeStamp: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            return calendar.get(Calendar.MONTH) + 1
        }

        /**
         * This method provides the month name based on the numeric month
         *
         * @param month month
         * @return [String] month in text
         * */
        fun getMonth(month: Int): String {
            return DateFormatSymbols.getInstance().months[month - 1]
        }

        /**
         * This method provides the day of month based on the time stamp
         *
         * @param timeStamp timestamp
         * @return [Int] day of month
         * */
        fun getDayOfMonth(timeStamp: Long): Int {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            return calendar.get(Calendar.DAY_OF_MONTH)
        }


        @SuppressLint("SimpleDateFormat")
        fun isConstrainedByTime(startTime: String, endTime: String): Boolean {
            return try {
                val date = Date()
                val dateFormatter = SimpleDateFormat("yyyy/MM/dd")
                dateFormatter.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
                val startDateWithTime = dateFormatter.format(date) + " " + startTime
                val endDateWithTime = dateFormatter.format(date) + " " + endTime
                val converterFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a")
                converterFormat.timeZone = TimeZone.getTimeZone("America/Los_Angeles")
                val currentTime = converterFormat.format(date)
                converterFormat.parse(currentTime)
                    .after(converterFormat.parse(startDateWithTime)) &&
                        converterFormat.parse(currentTime)
                            .before(converterFormat.parse(endDateWithTime))
            } catch (ex: ParseException) {
                false
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimeWithAMOrPM(hr: Int, min: Int): String? {
            val cal = Calendar.getInstance()
            cal[Calendar.HOUR_OF_DAY] = hr
            cal[Calendar.MINUTE] = min
            val formatter: Format
            formatter = SimpleDateFormat("hh:mm a")
            return formatter.format(cal.time)
        }

        fun selectDay(day: String, schedule: Schedule): List<Day> {
            if (day == Constants.Days.TUE){
                return schedule.data.tuesday
            }
            when (day) {
                Constants.Days.MON -> {
                    return schedule.data.monday
                }

                Constants.Days.WED -> {
                    return schedule.data.wednesday
                }

                Constants.Days.TUE -> {
                    return schedule.data.tuesday
                }

                Constants.Days.THU -> {
                    return schedule.data.thursday
                }

                Constants.Days.FRI -> {
                    return schedule.data.friday
                }

                Constants.Days.SAT -> {
                    return schedule.data.saturday
                }

                Constants.Days.SUN -> {
                    return schedule.data.sunday
                }
            }
            return emptyList()
        }

    }
}