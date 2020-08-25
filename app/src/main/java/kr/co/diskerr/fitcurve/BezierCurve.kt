package kr.co.diskerr.fitcurve

class BezierCurve(v0: Vector2, v1: Vector2) {
    val first = Vector2(v0)
    val cp0 = Vector2(v0)
    val cp1 = Vector2(v1)
    val last = Vector2(v1)

    operator fun get(index: Int) =
        when(index) {
            0 -> first
            1 -> cp0
            2 -> cp1
            3 -> last
            else -> throw IndexOutOfBoundsException("index: $index, size = 4")
        }

    fun i(tHat1: Vector2, tHat2: Vector2, d: Double) = i(tHat1, tHat2, d, d)

    fun i(tHat1: Vector2, tHat2: Vector2, d1: Double, d2: Double) {
        cp0.set(first + (tHat1 scale d1))
        cp1.set(last + (tHat2 scale d2))
    }

    fun q(u: Double): Vector2 {
        val a = first * CurveFitting.b0(u)
        val b = cp0 * CurveFitting.b1(u)
        val c = cp1 * CurveFitting.b2(u)
        val d = last * CurveFitting.b3(u)

        return a + b + c + d
    }

    fun qprime(u: Double): Vector2 {
        val ux = 1.0 - u

        val a = (cp0 - first) * (3 * ux * ux)
        val b = (cp1 - cp0) * (6 * ux * u)
        val c = (last - cp1) * (3 * u * u)

        return a + b + c
    }

    fun qprimeprime(u: Double): Vector2 {
        val ux = 1.0 - u

        val a = cp0 * 2.0
        val b = cp1 - a
        val c = b + first
        val d = c * (6 * ux)

        val e = cp1 * 2.0
        val f = last - e
        val g = f + cp0
        val h = g * (6 * u)

        return d + h
    }
}