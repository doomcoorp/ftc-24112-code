/*package org.firstinspires.ftc.teamcode.drive;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Scalar;

public class awd {
    public void init() {
        propProcessor = new PropProcessor();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.addProcessor(propProcessor);

        visionPortal = builder.build();
    }

    @Override
    public void init_loop() {
        telemetry.addData("satRectLeft", propProcessor.satRectLeft);
        telemetry.addData("satRectMiddle", propProcessor.satRectMiddle);
        telemetry.addData("satRectRight", propProcessor.satRectRight);
        telemetry.addData("selection", propProcessor.selection);
        selectedPosition = propProcessor.selection;
    }

    @Override
    public void end(boolean interrupted) {
        if (visionPortal != null) {
            visionPortal.stopStreaming();
        }
    }
}


class PropProcessor implements VisionProcessor {

    Rect rectLeft = new Rect(20, 210, 80, 80);
    Rect rectMiddle = new Rect(280, 160, 80, 80);
    Rect rectRight = new Rect(540, 210, 80, 80);
    Selected selection = Selected.NONE;
    Mat submat = new Mat();
    Mat hsvMat = new Mat();
    double satRectLeft = 0.0;
    double satRectMiddle = 0.0;
    double satRectRight = 0.0;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        satRectLeft = getAvgSaturation(hsvMat, rectLeft);
        satRectMiddle = getAvgSaturation(hsvMat, rectMiddle);
        satRectRight = getAvgSaturation(hsvMat, rectRight);

        if (satRectLeft > satRectMiddle && satRectLeft > satRectRight) {
            return Selected.LEFT;
        } else if (satRectMiddle > satRectLeft && satRectMiddle > satRectRight) {
            return Selected.MIDDLE;
        }
        return Selected.RIGHT;
    }

    protected double getAvgSaturation(Mat input, Rect rect) {
        submat = input.submat(rect);
        Core.MinMaxLocResult result = Core.minMaxLoc(submat);
        return result.maxVal;
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);
        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.RED);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(scaleCanvasDensity * 4);
        Paint nonSelectedPaint = new Paint(selectedPaint);
        nonSelectedPaint.setColor(Color.GREEN);
        android.graphics.Rect drawRectangleLeft = makeGraphicsRect(rectLeft, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleMiddle = makeGraphicsRect(rectMiddle, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleRight = makeGraphicsRect(rectRight, scaleBmpPxToCanvasPx);
        selection = (Selected) userContext;
        switch (selection) {
            case LEFT:
                canvas.drawRect(drawRectangleLeft, selectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case MIDDLE:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, selectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
            case RIGHT:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, selectedPaint);
                break;
            case NONE:
                canvas.drawRect(drawRectangleLeft, nonSelectedPaint);
                canvas.drawRect(drawRectangleMiddle, nonSelectedPaint);
                canvas.drawRect(drawRectangleRight, nonSelectedPaint);
                break;
        }
    }

    public enum Selected {
        NONE, LEFT, MIDDLE, RIGHT
    }
}
*/