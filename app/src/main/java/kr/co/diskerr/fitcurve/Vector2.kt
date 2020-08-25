package kr.co.diskerr.fitcurve

import android.graphics.PointF
import kotlin.math.hypot

class Vector2(var x: Double = 0.0, var y: Double = 0.0) {
    constructor(v: Vector2): this(v.x, v.y)
    constructor(p: PointF): this(p.x.toDouble(), p.y.toDouble())

    fun isZero() = x == 0.0 && y == 0.0
    fun length() = hypot(x, y)
    fun squaredLength() = x * x + y * y

    fun set(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    fun set(v: Vector2) = set(v.x, v.y)

    fun negate() {
        x = -x
        y = -y
    }

    fun normalize() =
        length().let {
            if (it != 0.0) {
                x /= it
                y /= it
            }
        }

    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
    operator fun times(c: Double) = Vector2(x * c, y * c)

    infix fun dot(rhs: Vector2) = x * rhs.x + y * rhs.y

    infix fun scale(newLength: Double) =
        length().let {
            Vector2(x, y).run {
                if (it != 0.0) times(newLength / it)
                else this
            }
        }


    fun distanceTo(v: Vector2) = (this - v).length()
}