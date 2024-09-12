package com.shadypines.utils

import com.shadypines.radio.model.schecule.Day

/**
 * This is a class that contains all the needed constants
 * @author Al Mujahid Khan
 * */
class Constants {

    class Default {
        companion object {
            const val OPEN_COUNT: String = "OPEN_COUNT"
            const val DEFAULT_INTEGER: Int = 0
            val DEFAULT_STRING: String? = null
            val CURRENT_SHOW: Day? = null
            var CAME_FROM_LOGIN_Schedule = false
            var CAME_FROM_LOGIN_Home = false
//            val showWeb:Int = 0


        }
    }

    class Preferences {
        companion object {
            var valued = 10
            var showValued = 10
            const val EMAIL = "email"
            const val IS_REGISTERED = "is_registered"


        }
    }

    class Days {
        companion object {
            const val MON: String = "MON"
            const val TUE: String = "TUE"
            const val WED: String = "WED"
            const val THU: String = "THU"
            const val FRI: String = "FRI"
            const val SAT: String = "SAT"
            const val SUN: String = "SUN"
        }
    }

}

class DefalutConstant{
    val CURRENT_SHOW: Day? = null

}

