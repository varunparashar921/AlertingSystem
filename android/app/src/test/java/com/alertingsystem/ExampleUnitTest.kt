package com.alertingsystem

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        println(distance(51.5,176.8,51.6,176.8))
    }

    //returns distance in meters
    fun distance(lat1: Double, lng1: Double,
                 lat2: Double, lng2: Double): Double {
        val a = (lat1 - lat2) * distPerLat(lat1)
        val b = (lng1 - lng2) * distPerLng(lat1)
        return Math.sqrt(a * a + b * b)
    }

    private fun distPerLng(lat: Double): Double {
        return (0.0003121092 * Math.pow(lat, 4.0) + 0.0101182384 * Math.pow(lat, 3.0) - 17.2385140059 * lat * lat
                + 5.5485277537 * lat + 111301.967182595)
    }

    private fun distPerLat(lat: Double): Double {
        return -0.000000487305676 * Math.pow(lat, 4.0) - 0.0033668574 * Math.pow(lat, 3.0) + 0.4601181791 * lat * lat - 1.4558127346 * lat + 110579.25662316
    }

//    fun distance(lat1: Double, lat2: Double, lon1: Double,
//                 lon2: Double): Double {
//        val R = 6371 // Radius of the earth
//        val latDistance = Math.toRadians(lat2 - lat1)
//        val lonDistance = Math.toRadians(lon2 - lon1)
//        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
//                (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
//        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
//        var distance = R.toDouble() * c * 1000.0 // convert to meters
//        distance = Math.pow(distance, 2.0)
//        return Math.sqrt(distance)
//    }

}
