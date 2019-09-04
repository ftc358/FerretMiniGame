package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/*
This is the Autonomous code for Group LANCY (Liz, Andrew, Nathan, Cristian, and Yoyo)
 */
@Autonomous(name = "Autonomous")
public class LANCY_Auto extends LinearOpMode {

    //Vuforia Key
    private static final String vuforiaKey = "";
    //Declare motors
    DcMotor motorLeft = null;
    DcMotor motorRight = null;
    //Set drive power
    double drivePower = 0.5;
    //Vuforia variables
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable target;
    private VuforiaTrackableDefaultListener listener;
    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {
            telemetry.update();
        }
    }

    //Drive Forwards
    public void DriveForward(double power) {
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }

    //Drive Backwards
    public void DriveReverse(double power) {
        motorLeft.setPower(-power);
        motorRight.setPower(-power);
    }

    //Turn Left
    public void TurnLeft(double power) {
        motorLeft.setPower(-power);
        motorRight.setPower(power);
    }

    //Turn Right
    public void TurnRight(double power) {
        motorLeft.setPower(power);
        motorRight.setPower(-power);
    }

    //StopDriving
    public void Brake() {
        DriveForward(0);
    }

}

/* Unused Code

    DcMotor motorC = null;

    //Intake Arm Lift
    public void ArmLift(double power) {
        motorC.setPower(power);
    }

    //Intake Arm Down
    public void ArmDown(double power) {
        motorC.setPower(-power);
    }

 */