package org.usfirst.frc.team5431.robot;
import org.usfirst.frc.team5431.robot.Robot.AutoTask;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SPI;
/**
 * Class for Autonomous commands. Uses switch-cases in order to acheive
 * multi-threading without creating multiple threads. Also much faster compared
 * to multi-threading and takes less space.
 * 
 * @author Andrew Goodman
 */
public class AutonomousNew{

	PIDController turnController;
	double rotateToAngleRate;
	static final double kP = 0.03;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	static final double kToleranceDegrees = 2.0f;
	AHRS ahrs;
	public static enum FirstMove {
		StandStill, TouchOuterWork, Lowbar, RockWall, RoughTerrain, Portcullis, ChevalDeFrise, Moat;
	}

	public static enum AutoShoot {
		False, True;
	}
	/**
	 * Initializes the Autonomous class.
	 */
	public AutonomousNew() {
		ahrs = new AHRS(SPI.Port.kMXP);
		turnController = new PIDController(kP, kI, kD, kF, ahrs, driveBase.frontleft);
	    turnController.setInputRange(-180.0f,  180.0f);
	    turnController.setOutputRange(-1.0, 1.0);
	    turnController.setAbsoluteTolerance(kToleranceDegrees);
	    turnController.setContinuous(true);
	}

	public void turn() {
		SmartDashboard.putNumber("AngleofNavX", ahrs.getAngle());
		turnController.setSetpoint(90.0f);
		turnController.enable();
		AllMottors();
	}
	public void AllMottors(){
		double Value = driveBase.frontleft.get();
		Robot.drivebase.drive(Value,-Value);
	}


}
//import edu.wpi.first.wpilibj.*;
//import edu.wpi.first.wpilibj.command.PIDSubsystem;
//import org.usfirst.frc.team5431.robot.RobotMap;
//
//
//public class Autonomous extends PIDSubsystem { // This system extends PIDSubsystem
//	static final double kToleranceDegrees = 2.0f;
//	
//	public Autonomous() {
//		super("Autonomous", 0.03, 0.0, 0.0);// The constructor passes a name for the subsystem and the P, I and D constants that are sueed when computing the motor output
//		setInputRange(-180.0f,  180.0f);
//		setOutputRange(-1.0, 1.0);
//		setAbsoluteTolerance(kToleranceDegrees);
//		getPIDController().setContinuous(true);
//	}
//	
//    public void initDefaultCommand() {
//    }
//
//    protected double returnPIDInput() {
//    	return Robot.gyro.getAngle(); // returns the sensor value that is providing the feedback for the system
//    }
//
//    protected void usePIDOutput(double output) {
//    	driveBase.frontleft.pidWrite(output);
//    	driveBase.rearleft.pidWrite(output);
//    	driveBase.frontright.pidWrite(-output);
//    	driveBase.rearright.pidWrite(-output);// this is where the computed output value fromthe PIDController is applied to the motor
//    }
//}
//
