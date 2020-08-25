package kr.co.diskerr.fitcurve

import android.graphics.PointF
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("kr.co.diskerr.fitcurve", appContext.packageName)
    }

    @Test
    fun fitCurve_test() {
        // expected0 is generated from JavaScript implementation https://github.com/soswow/fit-curve
        val expected0 = doubleArrayOf(
            877.79297, 379.6875, 846.5014478112415, 408.3550956959401, 813.5386837304789, 433.8269436240937, 785.6468, 466.17654,
            785.6468, 466.17654, 783.1164365778868, 469.11130898802173, 776.1200802944647, 478.27222200938246, 779.07556, 475.76608,
            779.07556, 475.76608, 806.0295563253632, 452.91004671876294, 827.691439776845, 431.2924732908021, 860.5365, 415.75217,
            860.5365, 415.75217, 864.5233929349508, 413.86581250891487, 891.5891884503292, 402.7549920363754, 896.21204, 405.37982,
            896.21204, 405.37982, 898.2175716100658, 406.51854907132576, 893.7797281216713, 409.3965183713407, 892.0125, 410.87833,
            892.0125, 410.87833, 887.0086686950823, 415.07401663748107, 881.2743127366853, 418.319156404506, 876.0725, 422.26672,
            876.0725, 422.26672, 847.5324037235858, 443.9252938041118, 820.1643079559689, 467.23142419067096, 797.63586, 495.27945,
            797.63586, 495.27945, 793.8990010851642, 499.93185728051867, 805.7367866398083, 486.5012508771956, 810.08844, 482.4181,
            810.08844, 482.4181, 830.1081934768663, 463.6335904804489, 851.6908075816947, 447.6354585667566, 877.98395, 438.6982,
            877.98395, 438.6982, 880.176021401913, 437.9530966341106, 885.257270867866, 435.89989652651593, 884.911, 438.1891,
            884.911, 438.1891, 884.4924768743701, 440.9559645613462, 859.1934914046966, 464.02893886820425, 858.93066, 464.28708,
            858.93066, 464.28708, 853.4951626651997, 469.6255799235457, 814.7158839164729, 512.3793734587241, 811.0358, 513.4607,
            811.0358, 513.4607, 808.842778943472, 514.105079836934, 812.9919656190675, 509.2998243246054, 814.3297, 507.44644,
            814.3297, 507.44644, 828.8983623198133, 487.26206651085545, 869.5313755439734, 447.9679946065542, 896.68225, 449.46478,
            896.68225, 449.46478, 904.8804442836471, 449.9167336737672, 887.8665027126968, 463.51558966378417, 881.9394, 469.1975,
            881.9394, 469.1975, 862.6919863920721, 487.64868662952045, 843.1424549981643, 505.66014181370565, 824.53125, 524.77295
        )

        // original C code produces artifacts on this input
        val input0 = arrayListOf(
            PointF(877.79297f, 379.6875f), PointF(866.8231f, 389.7375f), PointF(846.3178f, 406.5857f),
            PointF(823.81757f, 426.23526f), PointF(800.9987f, 449.08826f), PointF(785.6468f, 466.17654f),
            PointF(778.7467f, 474.8966f), PointF(779.07556f, 475.76608f), PointF(785.8434f, 468.87885f),
            PointF(803.9256f, 452.25775f), PointF(828.1095f, 433.57962f), PointF(860.5365f, 415.75217f),
            PointF(884.775f, 406.76892f), PointF(896.21204f, 405.37982f), PointF(892.0125f, 410.87833f),
            PointF(876.0725f, 422.26672f), PointF(848.4607f, 443.92902f), PointF(823.1768f, 467.18665f),
            PointF(804.97375f, 486.6408f), PointF(797.63586f, 495.27945f), PointF(799.54504f, 493.39957f),
            PointF(810.08844f, 482.4181f), PointF(830.83014f, 464.0448f), PointF(857.307f, 447.57193f),
            PointF(877.98395f, 438.6982f), PointF(884.911f, 438.1891f), PointF(876.64923f, 447.52206f),
            PointF(858.93066f, 464.28708f), PointF(834.3717f, 489.04514f), PointF(817.95245f, 506.38196f),
            PointF(811.0358f, 513.4607f), PointF(814.3297f, 507.44644f), PointF(826.6651f, 491.80685f),
            PointF(854.6969f, 468.51706f), PointF(881.4818f, 453.1156f), PointF(896.68225f, 449.46478f),
            PointF(897.2127f, 453.98282f), PointF(881.9394f, 469.1975f), PointF(859.61383f, 490.0263f),
            PointF(835.4174f, 513.5934f), PointF(824.53125f, 524.77295f)
        )

        fitCurve_testImpl(expected0, input0, 4.0)

        // test cases from https://github.com/soswow/fit-curve/blob/master/test/smoke.test.js
        val expected1 = doubleArrayOf(
            0.0, 0.0, 20.27317402, 20.27317402, -1.24665147, 0.0, 20.0, 0.0
        )

        val input1 = arrayListOf(
            PointF(0f, 0f), PointF(10f, 10f), PointF(10f, 0f), PointF(20f, 0f)
        )

        fitCurve_testImpl(expected1, input1, 50.0)

        val expected2 = doubleArrayOf(
            0.0, 0.0, 3.333333333333333, 3.333333333333333, 5.285954792089683, 10.0, 10.0, 10.0,
            10.0, 10.0, 13.333333333333334, 10.0, 7.6429773960448415, 2.3570226039551585, 10.0, 0.0,
            10.0, 0.0, 12.3570226, -2.3570226, 16.66666667, 0.0, 20.0, 0.0
        )

        val input2 = arrayListOf(
            PointF(0f, 0f), PointF(10f, 10f), PointF(10f, 0f), PointF(20f, 0f)
        )

        fitCurve_testImpl(expected2, input2, 1.0)
    }

    private fun fitCurve_testImpl(expected: DoubleArray, input: List<PointF>, error: Double) {
        val output = CurveFitting.fitCurve(input, error)

        assertEquals(output.size * 8, expected.size)

        for ((count, bez) in output.withIndex()) {
            for (i in 0 until 4) {
                assertEquals(bez[i].x, expected[count * 8 + i * 2], 0.0001)
                assertEquals(bez[i].y, expected[count * 8 + i * 2 + 1], 0.0001)
            }
        }
    }
}