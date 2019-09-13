package org.firstinspires.ftc.teamcode;
/*
Heil!
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class LANCY_TeleOp extends LinearOpMode {
    private static final double ARM_RETRACTED_POSITION = 0.65;
    private static final double ARM_EXTENDED_POSITION = 1.0;

    public void runOpMode() {
        DcMotor motorLeft = hardwareMap.dcMotor.get("motorLeft"); // 0
        DcMotor motorRight = hardwareMap.dcMotor.get("motorRight"); // 1
        Servo armServo = hardwareMap.servo.get("armServo"); // Servo 0
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        armServo.setPosition(ARM_RETRACTED_POSITION);
        waitForStart();
        while (opModeIsActive()) {
            motorLeft.setPower(gamepad1.left_stick_y / 2);
            motorRight.setPower(gamepad1.right_stick_y / 2);
            if (gamepad1.left_bumper) {
                armServo.setPosition(ARM_EXTENDED_POSITION);
            }
            if (gamepad1.right_bumper) {
                armServo.setPosition(ARM_RETRACTED_POSITION);
            }
            idle();
        }
    }
}
