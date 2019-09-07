package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class LANCY_TeleOp extends LinearOpMode {
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;
    private DcMotor motorC = null;

    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("motorLeft"); // 0
        motorRight = hardwareMap.dcMotor.get("motorRight"); // 1
        motorC = hardwareMap.dcMotor.get("motorC"); // 2

        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorC.setDirection(DcMotor.Direction.REVERSE);

        boolean status = true;
        waitForStart();

        double armPower = 0.5;
        /*
        while (opModeIsActive()) {
            motorLeft.setPower(gamepad1.left_stick_y);
            motorRight.setPower(gamepad1.right_stick_y);
            if (gamepad2.a && status) {
                status = false;
                arm(armPower, -1120);
            }
            if (gamepad2.b && !status) {
                status = true;
                arm(armPower, 1120);
            }
         */
        while (opModeIsActive()) {
            motorLeft.setPower(gamepad1.left_stick_y / 2);
            motorRight.setPower(gamepad1.right_stick_y / 2);

            motorC.setPower(0);
            if (gamepad1.left_bumper) {
                motorC.setPower(-0.95);
            }
            if (gamepad1.right_bumper) {
                motorC.setPower(0.95);
            }
            idle();

            telemetry.addData("leftStick: ", gamepad1.left_stick_y);
            telemetry.addData("leftPower: ", motorLeft.getPower());
            telemetry.update();
        }
    }
    /*
    private void arm(double power, int distance) {
        motorC.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorC.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorC.setTargetPosition(distance);
        motorC.setPower(power);

        while (motorC.isBusy()) {
        }
        motorC.setPower(0);
        motorC.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
     */
}
