package org.firstinspires.ftc.teamcode;
/*
Heil!
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "LANCY_RIAFI")
public class LANCY_RIAFI extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AXzW9CD/////AAAAGTPAtr9HRUXZmowtd9p0AUwuXiBVONS/c5x1"
            + "q8OvjMrQ8/XJGxEp0TP9Kl8PvqSzeXOWIvVa3AeB6MyAQboyW/Pgd/c4a4U/VBs1ouUsVBkEdbaq1iY7RR0c"
            + "jYr3eLwEt6tmI37Ugbwrd5gmxYvOBQkGqzpbg2U2bVLycc5PkOixu7PqPqaINGZYSlvUzEMAenLOCxZFpsay"
            + "uCPRbWz6Z9UJfLeAbfAPmmDYoKNXRFll8/jp5Ie7iAhSQgfFggWwyiqMRCFA3GPTsOJS4H1tSiGlMjVzbJnk"
            + "usPKXfJ0dK3OH9u7ox9ESpi91T0MemXw3nn+/6QRvjGtgFH+wMDuQX7ta89+yW+wqdXX9ZQu8BzY";
    private static int direction;
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();
        if (tfod != null) {
            tfod.activate();
        }
        telemetry.addData("Recognition", "Initiate");
        telemetry.update();
        direction = getDirection();
        telemetry.addData("Recognition", "Complete");
        telemetry.update();
        if (direction == 0) { // Left
            driveForwardDistance(0.75, 3209);
            turnLeft(0.5, 1160);
            driveForwardDistance(0.75, 3280);
        } else if (direction == 1) { // Right
            driveForwardDistance(0.75, 3050);
            turnRight(0.5, 1350);
            driveForwardDistance(0.75, 3220);
        }
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    private int getDirection() {
        direction = -1;
        while (direction == -1) {
            if (tfod != null) { // Vuforia Recognition
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 2) {
                        int goldMineralX = -1;
                        int silverMineralX = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else {
                                silverMineralX = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineralX != -1) {
                            if (goldMineralX < silverMineralX) {
                                direction = 0;  //Left
                                telemetry.addData("Gold Mineral Position", "Left");
                            } else if (goldMineralX > silverMineralX) {
                                direction = 1;  //Right
                                telemetry.addData("Gold Mineral Position", "Right");
                            } else {
                                telemetry.addData("Error", "1");
                            }
                        } else {
                            telemetry.addData("Error", "0");
                        }
                    }
                    telemetry.update();
                }
            }
        }
        return direction;
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(
                tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private void turnLeft(double power, int distance) {
        rMResetEncoders();
        motorLeft.setTargetPosition(-distance);
        motorRight.setTargetPosition(distance);
        rMRunToPosition();
        driveForward(power);
        while (motorRight.isBusy() && motorRight.isBusy()) {
        }
        brake();
        rMRunUsingEncoders();
    }

    private void turnRight(double power, int distance) {
        rMResetEncoders();
        motorLeft.setTargetPosition(distance);
        motorRight.setTargetPosition(-distance);
        rMRunToPosition();
        driveForward(power);
        while (motorRight.isBusy() && motorRight.isBusy()) {
        }
        brake();
        rMRunUsingEncoders();
    }

    private void driveForwardDistance(double power, int distance) {
        rMResetEncoders();
        motorLeft.setTargetPosition(distance);
        motorRight.setTargetPosition(distance);
        rMRunToPosition();
        driveForward(power);
        while (motorLeft.isBusy() && motorRight.isBusy()) {
            if (motorLeft.getCurrentPosition() > 0.9 * distance
                    && motorRight.getCurrentPosition() > 0.9 * distance) {

                motorLeft.setPower(0.5 * power);
                motorRight.setPower(0.5 * power);
            }
        }
        brake();
        rMRunUsingEncoders();
    }

    private void driveForward(double power) {
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }

    private void brake() {
        driveForward(0);
    }

    private void rMRunUsingEncoders() {
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void rMRunToPosition() {
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void rMResetEncoders() {
        motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
