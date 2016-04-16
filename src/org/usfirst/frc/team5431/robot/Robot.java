
package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team5431.robot.driveBase;
import org.usfirst.frc.team5431.robot.Shooter;
/**
 * This is the second version of competition code (sans David as per request). This competition code is intended to be a functional
 * version without the bells and whistles that are added to the first version of the competition code. 
 */
public class Robot extends IterativeRobot {
    SendableChooser chooser;
    static driveBase drivebase;
    static Shooter flywheels;
    static Teleop teleop;
    static Autonomous auton;
    static OI oiInput;    
	enum AutoTask{ CrossRockWallAndStop, CrossMoatAndStop, TouchOuterWork, CrossLowbarAndStop, CrossLowbarAndShoot, DoNothing, CrossOuter, Spybox,CrossPortcullisAndShoot, CrossRockwallAndShoot};
	static AutoTask currentAuto;
	static AnalogGyro gyro;
	public static final boolean brakeMode = false;    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        drivebase = new driveBase(brakeMode);
        flywheels = new Shooter();
        teleop = new Teleop();
        auton = new Autonomous();
        oiInput = new OI(0, 1);
        gyro = new AnalogGyro(0);
        
        //gyro.initGyro();
        gyro.setSensitivity(0.001666);
        //gyro.setSensitivity(.0016594);
        //gyro.calibrate();
        
        //SmarterDashboard.addDebugString("Robot started");
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	drivebase.enableBrakeMode();
    	SmarterDashboard.putBoolean("AUTO", true);
    	currentAuto = AutoTask.valueOf(SmarterDashboard.getString("AUTO-SELECTED", "AutoShoot"));
 		SmartDashboard.putString("Auto Selected: ", currentAuto.toString());
 		drivebase.resetDrive();
 		gyro.reset();
    }
    
    public void disabledPeriodic(){
    	SmarterDashboard.putBoolean("AUTO", false);
    	SmarterDashboard.putBoolean("ENABLED", false);
    	SmarterDashboard.putBoolean("connection", true);
    	SmarterDashboard.periodic();
    	drivebase.resetDrive();
    	//SwitchCase.moveAmount = 0.468;
    	Autonomous.autoAIMState = false;
    	Autonomous.currAIM = 0;
    	Autonomous.driveForwardState = 0;
    	SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
    	Timer.delay(0.1); //Sleep a little for little overhead time
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    	auton.updateStates(currentAuto);
    	SmarterDashboard.putBoolean("connection", true);
    	SmarterDashboard.putBoolean("AUTO", true);
    	SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
    	//Update RPM for fly wheels
        final double[] rpms = flywheels.getRPM();
		SmarterDashboard.putNumber("FLY-LEFT", rpms[0]);
		SmarterDashboard.putNumber("FLY-RIGHT", rpms[1]);
    	//Timer.delay(0.005); // Wait 50 Hz
    	//SmarterDashboard.periodic();
    	
    }
    public void teleopInit(){
    	drivebase.disableBrakeMode();
    	gyro.reset();
    }
    /**
     * This function is called periodically during operator control.
     * Calls the update functions for the OI and the Teleop classes.
     */
    public void teleopPeriodic() {
    	//SwitchCase.moveAmount = 0.468;
        oiInput.updateVals();
        teleop.Update(oiInput);
        SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
        //Update connection
        SmarterDashboard.putBoolean("ENABLED", true);
        SmarterDashboard.putBoolean("connection", true);
        
        //Update RPM for fly wheels
        final double[] rpms = flywheels.getRPM();
		SmarterDashboard.putNumber("FLY-LEFT", rpms[0]);
		SmarterDashboard.putNumber("FLY-RIGHT", rpms[1]);
		
		//Update drivetrain distance
		final double[] driverpm = drivebase.getEncDistance();
		SmarterDashboard.putNumber("DRIVE-DISTANCE-LEFT", driverpm[0]);
		SmarterDashboard.putNumber("DRIVE-DISTANCE-RIGHT", driverpm[1]);
    	SmarterDashboard.periodic();
    }

	public void testPeriodic() {
	} // Not used

}
