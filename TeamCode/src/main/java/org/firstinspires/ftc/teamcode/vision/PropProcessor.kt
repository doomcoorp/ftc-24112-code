package org.firstinspires.ftc.teamcode.vision

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration
import org.firstinspires.ftc.vision.VisionProcessor
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc

 class PropProcessor : VisionProcessor {
      var rectLeft = Rect(10, 360, 100, 80)
      var rectMiddle = Rect(285, 310, 100, 80)
      var rectRight = Rect(530, 360, 100, 80)
     var selection = Selected.NONE
    var submat = Mat()
    var hsvMat = Mat()
    var satRectLeft : Double = 0.0
    var satRectMiddle : Double = 0.0
    var satRectRight : Double = 0.0
    override fun init(width: Int, height: Int, calibration: CameraCalibration) {}
    override fun processFrame(frame: Mat, captureTimeNanos: Long): Any {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV)
        satRectLeft = getAvgSaturation(hsvMat, rectLeft)
        satRectMiddle = getAvgSaturation(hsvMat, rectMiddle)
        satRectRight = getAvgSaturation(hsvMat, rectRight)

        if (satRectLeft > satRectMiddle && satRectLeft > satRectRight) {
            return Selected.LEFT
        } else if (satRectMiddle > satRectLeft && satRectMiddle > satRectRight) {
            return Selected.MIDDLE
        }
        return Selected.RIGHT

    }

     fun getAvgSaturation(input: Mat, rect: Rect?): Double {
        submat = input.submat(rect)
        val color = Core.mean(submat)
        return color.`val`[1]
    }

    private fun makeGraphicsRect(rect: Rect, scaleBmpPxToCanvasPx: Float): android.graphics.Rect {
        val left = Math.round(rect.x * scaleBmpPxToCanvasPx)
        val top = Math.round(rect.y * scaleBmpPxToCanvasPx)
        val right = left + Math.round(rect.width * scaleBmpPxToCanvasPx)
        val bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx)
        return android.graphics.Rect(left, top, right, bottom)
    }

    override fun onDrawFrame(
        canvas: Canvas,
        onscreenWidth: Int,
        onscreenHeight: Int,
        scaleBmpPxToCanvasPx: Float,
        scaleCanvasDensity: Float,
        userContext: Any
    ) {
        val selectedPaint = Paint()
        selectedPaint.color = Color.RED
        selectedPaint.style = Paint.Style.STROKE
        selectedPaint.strokeWidth = scaleCanvasDensity * 4
        val nonSelectedPaint = Paint(selectedPaint)
        nonSelectedPaint.color = Color.GREEN
        val drawRectangleLeft = makeGraphicsRect(rectLeft, scaleBmpPxToCanvasPx)
        val drawRectangleMiddle = makeGraphicsRect(rectMiddle, scaleBmpPxToCanvasPx)
        val drawRectangleRight = makeGraphicsRect(rectRight, scaleBmpPxToCanvasPx)
        selection = userContext as Selected
        when (selection) {
            Selected.LEFT -> {
                canvas.drawRect(drawRectangleLeft, selectedPaint)
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint)
                canvas.drawRect(drawRectangleRight, nonSelectedPaint)
            }

            Selected.MIDDLE -> {
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint)
                canvas.drawRect(drawRectangleMiddle, selectedPaint)
                canvas.drawRect(drawRectangleRight, nonSelectedPaint)
            }

            Selected.RIGHT -> {
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint)
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint)
                canvas.drawRect(drawRectangleRight, selectedPaint)
            }

            Selected.NONE -> {
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint)
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint)
                canvas.drawRect(drawRectangleRight, nonSelectedPaint)
            }
        }
    }

    public enum class Selected {
        NONE, LEFT, MIDDLE, RIGHT
    }
     public fun getSelected () : Selected {
         return selection;
     }
     public fun getsatRectMiddle () : Double {
        return satRectMiddle
     }
     public fun getsatRectLeft () : Double {
         return satRectLeft
     }
     public fun getsatRectRight () : Double {
         return satRectRight
     }
         //public
//    public fun getSelection () : Selected {
//        return selection
//    }
//    public fun getSatRectLeft () : Double {
//        return satRectLeft
//    }
//    public fun getSatRectMiddle () : Double {
//        return satRectMiddle
//    }
   //public fun getSatRectRight () : Double {
  //   return satRectRight    }
}