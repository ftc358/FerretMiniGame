package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TeleOp")
public class LANCY_TeleOp extends LinearOpMode {
    private DcMotor motorLeft = null;
    private DcMotor motorRight = null;
    private DcMotor motorC = null;

    @Override
    public void runOpMode() throws InterruptedException {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorC = hardwareMap.dcMotor.get("motorC");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorC.setDirection(DcMotor.Direction.REVERSE);
        int status = 1;
        waitForStart();
        while (opModeIsActive()) {
            motorLeft.setPower(gamepad1.left_stick_y);
            motorRight.setPower(gamepad1.right_stick_y);
            if (gamepad2.a && status == 1) {
                status = 0;
                arm(0.5, -1120);
            }
            if (gamepad2.b && status == 0) {
                status = 1;
                arm(0.5, 1120);
            }
            idle();
        }
    }

    private void arm(double power, int distance) {
        motorC.setMode(DcMotor.RunMode.RESET_ENCODERS);
        motorLeft.setTargetPosition(distance);
        motorC.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorC.setPower(power);
        while (motorRight.isBusy() && motorRight.isBusy()) {
        }
        motorC.setPower(0);
        motorC.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void driveForward(double power) {
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }

    public void brake() {
        driveForward(0);
    }

}
