package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

/*
This is the Autonomous code for Group LANCY (Liz, Andrew, Nathan, Cristian, and Yoyo)
 */
@Autonomous(name = "Autonomous")
public class LANCY_Auto_Scheiße extends LinearOpMode {

    //Vuforia Key
    private static final String VUFORIA_KEY =
            "AXzW9CD/////AAAAGTPAtr9HRUXZmowtd9p0AUwuXiBVONS/c5x1q8OvjMrQ8"
                    + "/XJGxEp0TP9Kl8PvqSzeXOWIvVa3AeB6MyAQboyW/Pgd/c4a4U"
                    +
                    "/VBs1ouUsVBkEdbaq1iY7RR0cjYr3eLwEt6tmI37Ugbwrd5gmxYvOBQkGqzpbg2U2bVLycc5PkOixu7PqPqaINGZYSlvUzEMAenLOCxZFpsayuCPRbWz6Z9UJfLeAbfAPmmDYoKNXRFll8/jp5Ie7iAhSQgfFggWwyiqMRCFA3GPTsOJS4H1tSiGlMjVzbJnkusPKXfJ0dK3OH9u7ox9ESpi91T0MemXw3nn+/6QRvjGtgFH+wMDuQX7ta89+yW+wqdXX9ZQu8BzY";
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    //Declare motors
    DcMotor motorLeft = null;
    DcMotor motorRight = null;
    //Set drive power
    double drivePower = 0.5;
    VuforiaLocalizer vuforia;
    //Vuforia variables
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable target;
    private VuforiaTrackableDefaultListener listener;
    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;
    private TFObjectDetector tfod;

    public void runOpMode() throws InterruptedException {
        initVuforia();
        runUsingEncoders();
        //setting robot location to origin
        // lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);
        int turnDist = 1;
        waitForStart();
        OpenGLMatrix originalLocation = listener.getUpdatedRobotLocation();
        //Monitor position using Vuforia (most likely obsolete)
        /*
        while (opModeIsActive()) {
            OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();
            // For the listener to not return null, checking for it to prevent errors
            if (latestLocation != null) {
                lastKnownLocation = latestLocation;
            }
            // Tell if the target is visible, and where the robot is
            telemetry.addData("Tracking " + target.getName(), listener.isVisible());
            telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));
            telemetry.update();
            idle();
        }
         */
        if (originalLocation.get(0, 1) > originalLocation.get(0, 0)) {
            turnDist = -1;
        }

    }

    //To set up Vuforia image-based tracking
    /*
    private void setupVuforia() {
        // Setup parameters to create localizer
        parameters = new VuforiaLocalizer.Parameters(); // Remove camera view from screen
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("Gold Mineral");

        // Setup the target to be tracked
        target = visionTargets.get(0); // 0 corresponds to the wheels target
        target.setName("Gold Mineral");
        target.setLocation(createMatrix(0, 0, 0, 0, 0, 0));

        // Set phone location on robot
        phoneLocation = createMatrix(-127, 51, 0, 90, 0, 0);

        // Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);

    }
     */


    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    private String formatMatrix(OpenGLMatrix matrix) {
        return matrix.formatAsTransform();
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(
                tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset("Gold Mineral");
    }

    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
    }

    public void deactivateVuforia() {
        tfod.deactivate();
        tfod.shutdown();
    }

    /*
    Following is drive code (w/o encoders)
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

    // Run Using Encoders
    public void runUsingEncoders() {
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Run Without Encoders
    public void runWithoutEncoders() {
        motorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

}

/*

Unused Drive Code

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
