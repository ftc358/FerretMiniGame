package org.firstinspires.ftc.teamcode;

/*
Heil!
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto Left")
@Disabled
public class LANCY_Auto_Left extends LinearOpMode {
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;

    @Override
    public void runOpMode() {

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        driveForwardDistance(0.75, 3209);
        turnLeft(0.5, 1190);
        driveForwardDistance(0.75, 3280);
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

    private void driveForwardDistance(double power, int distance) {
        rMResetEncoders();
        motorLeft.setTargetPosition(distance);
        motorRight.setTargetPosition(distance);
        rMRunToPosition();
        driveForward(power);
        while (motorLeft.isBusy() && motorRight.isBusy()) {
            if (motorLeft.getCurrentPosition() > 0.9 * distance
                    && motorRight.getCurrentPosition() > 0.9) {

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
