package kr.co.diskerr.fitcurve

import android.graphics.PointF

object CurveFitting {
    fun fitCurve(points: List<PointF>, error: Double): List<BezierCurve> {
        val tHat1 = computeTangent(points[1], points[0])
        val tHat2 = computeTangent(points[points.lastIndex - 1], points.last())

        return fitCubic(points, tHat1, tHat2, error)
    }

    private fun fitCubic(pts: List<PointF>, tHat1: Vector2, tHat2: Vector2, error: Double): List<BezierCurve> {
        if (pts.size == 2) {
            val first = Vector2(pts[0])
            val last = Vector2(pts[1])
            val d = first.distanceTo(last) / 3.0

            return arrayListOf(BezierCurve(first, last).apply { i(tHat1, tHat2, d) })
        }

        val u = chordLengthParameterize(pts)
        var bezierCurve = generateBezier(pts, u, tHat1, tHat2)
        var e = computeMaxError(pts, bezierCurve, u)

        if (e.first < error) return arrayListOf(bezierCurve)

        if (e.first < error * error) {
            val maxIterations = 20

            var uPrime = DoubleArray(u.size) { u[it] }
            var prevError = e.first
            var prevSplitPoint = e.second

            for (i in 0 until maxIterations) {
                uPrime = reparameterize(pts, uPrime, bezierCurve)

                bezierCurve = generateBezier(pts, uPrime, tHat1, tHat2)
                e = computeMaxError(pts, bezierCurve, u)

                if (e.first < error) return arrayListOf(bezierCurve)
                else if (e.second == prevSplitPoint) {
                    val errChange = e.first / prevError
                    if (errChange > .9999 && errChange < 1.0001) break
                }

                prevError = e.first
                prevSplitPoint = e.second
            }
        }

        return ArrayList<BezierCurve>().apply {
            val left = Vector2(pts[e.second - 1])
            val center = Vector2(pts[e.second])
            val right = Vector2(pts[e.second + 1])

            val tHatCenter = (left - right).apply {
                if (isZero()) (left - center).let { set(-it.y, it.x) }
                normalize()
            }

            addAll(fitCubic(pts.subList(0, e.second + 1), tHat1, tHatCenter, error))
            tHatCenter.negate()
            addAll(fitCubic(pts.subList(e.second, pts.size), tHatCenter, tHat2, error))
        }
    }

    private fun generateBezier(pts: List<PointF>, uPrime: DoubleArray, tHat1: Vector2, tHat2: Vector2): BezierCurve {
        val a = Array(pts.size) { i ->
            Array(2) { j -> if (j == 0) tHat1 scale b1(uPrime[i]) else tHat2 scale b2(uPrime[i]) }
        }

        val c = Array(2) { DoubleArray(2) }
        val x = DoubleArray(2)

        val first = Vector2(pts.first())
        val last = Vector2(pts.last())
        val bezierCurve = BezierCurve(first, last)

        for (i in pts.indices) {
            c[0][0] += a[i][0] dot a[i][0]
            c[0][1] += a[i][0] dot a[i][1]
            c[1][0] = c[0][1]
            c[1][1] += a[i][1] dot a[i][1]

            val tmp = Vector2(pts[i]) - bezierCurve.q(uPrime[i])

            x[0] += a[i][0] dot tmp
            x[1] += a[i][1] dot tmp
        }

        val detC0C1 = c[0][0] * c[1][1] - c[1][0] * c[0][1]
        val detC0X = c[0][0] * x[1] - c[1][0] * x[0]
        val detXC1 = x[0] * c[1][1] - x[1] * c[0][1]

        val alphaL = if (detC0C1 == 0.0) 0.0 else detXC1 / detC0C1
        val alphaR = if (detC0C1 == 0.0) 0.0 else detC0X / detC0C1

        val segmentLength = first.distanceTo(last)
        val epsilon = 1.0e-6 * segmentLength

        if (alphaL < epsilon || alphaR < epsilon)
            return bezierCurve.apply { i(tHat1, tHat2, segmentLength / 3) }

        return bezierCurve.apply { i(tHat1, tHat2, alphaL, alphaR) }
    }

    private fun reparameterize(pts: List<PointF>, u: DoubleArray, bezierCurve: BezierCurve) =
        DoubleArray(pts.size) { newtonRaphsonRootFind(bezierCurve, Vector2(pts[it]), u[it]) }

    private fun newtonRaphsonRootFind(bezierCurve: BezierCurve, p: Vector2, u: Double): Double {
        val d = bezierCurve.q(u) - p
        val qp = bezierCurve.qprime(u)
        val qpp = bezierCurve.qprimeprime(u)
        val numerator = d dot qp
        val denominator = (qp dot qp) + 2.0 * (d dot qpp)

        return if (denominator == 0.0) u else u - numerator / denominator
    }

    private fun computeTangent(a: PointF, b: PointF) = (Vector2(a) - Vector2(b)).apply { normalize() }

    private fun chordLengthParameterize(pts: List<PointF>) =
        DoubleArray(pts.size).also { u ->
            u[0] = 0.0

            for (i in 1 until pts.size)
                u[i] = u[i - 1] + Vector2(pts[i]).distanceTo(Vector2(pts[i - 1]))

            for (i in 1 until pts.size) u[i] = u[i] / u.last()
        }

    private const val B_PARTS = 10
    private const val B_PARTS_D = B_PARTS.toDouble()

    private fun computeMaxError(pts: List<PointF>, bezierCurve: BezierCurve, u: DoubleArray): Pair<Double, Int> {
        var maxDist = 0.0
        var splitPoint = pts.size / 2

        val tDistMap = mapTtoRelativeDistances(bezierCurve)

        for (i in pts.indices) {
            val p = Vector2(pts[i])
            val t = findT(u[i], tDistMap)

            val v = bezierCurve.q(t) - p
            val dist = v.squaredLength()

            if (dist > maxDist) {
                maxDist = dist
                splitPoint = i
            }
        }

        return Pair(maxDist, splitPoint)
    }

    private fun findT(u: Double, tDistMap: List<Double>) =
        when {
            u < 0 -> 0.0
            u > 1 -> 1.0
            else -> {
                var t = 0.0

                for (i in 1 .. B_PARTS) {
                    if (u <= tDistMap[i]) {
                        val tMin = (i - 1) / B_PARTS_D
                        val tMax = i / B_PARTS_D
                        val lenMin = tDistMap[i - 1]
                        val lenMax = tDistMap[i]

                        t = (u - lenMin) / (lenMax - lenMin) * (tMax - tMin) + tMin

                        break
                    }
                }

                t
            }
        }

    private fun mapTtoRelativeDistances(bezierCurve: BezierCurve): List<Double> {
        var btCurr: Vector2
        var btPrev = bezierCurve[0]
        val btDist = arrayListOf(0.0)

        var sumLen = 0.0

        for (i in 1 .. B_PARTS) {
            btCurr = bezierCurve.q(i / B_PARTS_D)
            sumLen += (btCurr - btPrev).length()

            btDist.add(sumLen)

            btPrev = btCurr
        }

        return btDist.map { x -> x / sumLen }
    }

    fun b0(u: Double) = (1.0 - u) * (1.0 - u) * (1.0 - u)
    fun b1(u: Double) = 3 * u * (1.0 - u) * (1.0 - u)
    fun b2(u: Double) = 3 * u * u * (1.0 - u)
    fun b3(u: Double) = u * u * u
}