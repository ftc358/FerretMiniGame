package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
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
        setupVuforia();
        //setting robot location to origin
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);
        waitForStart();
        while (opModeIsActive()) {
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            // For the listener to not return null, checking for it to prevent errors
            if(latestLocation != null)
                lastKnownLocation = latestLocation;
            // Tell if the target is visible, and where the robot is
            telemetry.addData("Tracking " + target.getName(), listener.isVisible());
            telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));
            telemetry.update();
            idle();
        }
    }

    //To set up Vuforia
    private void setupVuforia() {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(); // Remove camera view from screen
        parameters.vuforiaLicenseKey = vuforiaKey;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset(""); //We need object file
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        // Setup the target to be tracked
        target = visionTargets.get(0); // 0 corresponds to the wheels target
        target.setName("Gold Mineral");
        target.setLocation(createMatrix(0, 0, 0, 0, 0, 0));

        // Set phone location on robot
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        // Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
    }

    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    private String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }

    /*
    Following is drive code
     */

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

}

// hi
    //Intake Arm Down
    public void ArmDown(double power) {
        motorC.setPower(-power);
    }

 */
