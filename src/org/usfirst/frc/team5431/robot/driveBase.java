package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class declares variables/functions related specifically to the DriveBase
 * (and only the DriveBase).
 * 
 * @author Usaid Malik
 */
public class driveBase {
	public static final int wheelDiameter = 10; // In inches. Used to calculate
												// distancePerPulse
	private static final double distancePerPulse = wheelDiameter * Math.PI / 360;// Calculates
																					// distancePerPulse
																					// in
																					// inches
	private static final int samplesToAverage = 7; // How many pulses to do
													// before averaging them
													// (smoothes encoder count)
	private static final double minEncRate = 0.0; // Minimum Encoder Rate before
													// hardware thinks encoders
													// are stopped
	private static CANTalon frontright, frontleft, rearright, rearleft; // Declaration
																		// of
																		// CANTalons
																		// used
																		// in
																		// the
																		// drivebase
	Encoder rightBaseEncoder, leftBaseEncoder; // Declaration of encoders used
												// in the drivebase
	private static RobotDrive tankDriveBase;
	public CANTalon chopper;
	/**
	 * Constructor for driveBase. All CANTalons are set to coast. If you want to
	 * set the driveBase to brake, use driveBase(brakeMode).
	 */
	/*
	 * public driveBase(){ frontright = new CANTalon(RobotMap.frontright);
	 * frontleft = new CANTalon(RobotMap.frontleft); rearright = new
	 * CANTalon(RobotMap.rearright); rearleft = new CANTalon(RobotMap.rearleft);
	 * 
	 * frontright.setInverted(false); //Inverts(or doesn't) motors
	 * frontleft.setInverted(false); rearright.setInverted(false);
	 * rearleft.setInverted(false);
	 * 
	 * frontright.enableBrakeMode(true); //Default brake mode will be coast
	 * frontleft.enableBrakeMode(true); rearright.enableBrakeMode(true);
	 * rearleft.enableBrakeMode(true);
	 * 
	 * tankDriveBase = new RobotDrive(frontleft, rearleft, frontright,
	 * rearright);//Initializes RobotDrive to use tankDrive() rightBaseEncoder =
	 * new Encoder(RobotMap.rightBaseEnc1, RobotMap.rightBaseEnc2, false,
	 * EncodingType.k4X);//Using 4X encoding for encoders leftBaseEncoder = new
	 * Encoder(RobotMap.leftBaseEnc1, RobotMap.leftBaseEnc2, false,
	 * EncodingType.k4X);
	 * rightBaseEncoder.setDistancePerPulse(distancePerPulse); //Sets distance
	 * robot would travel every encoder pulse
	 * leftBaseEncoder.setDistancePerPulse(distancePerPulse);
	 * rightBaseEncoder.setSamplesToAverage(samplesToAverage); //Averages
	 * encoder count rate every samplesToAverage pulses
	 * leftBaseEncoder.setSamplesToAverage(samplesToAverage);
	 * rightBaseEncoder.setReverseDirection(false); //Reverses encoder direction
	 * based on position on robot leftBaseEncoder.setReverseDirection(false);
	 * rightBaseEncoder.setMinRate(minEncRate); //Sets minimum rate for encoder
	 * before hardware thinks it is stopped
	 * leftBaseEncoder.setMinRate(minEncRate); }
	 */

	/**
	 * Constructor for the driveBase class. This specifies whether the CANTalons
	 * are/aren't in brake mode.
	 * 
	 * @param mode
	 *            A brakeMode enum. Can be brakeMode.Brake or brakeMode.NoBrake
	 */
	public driveBase(boolean mode) { // Constructor used if brakeMode is
										// specified (should be but probably
										// won't be used){
		frontright = new CANTalon(RobotMap.frontright); // Instantiates
														// CANTalons based on
														// mapping in RobotMap
		frontleft = new CANTalon(RobotMap.frontleft);
		rearright = new CANTalon(RobotMap.rearright);
		rearleft = new CANTalon(RobotMap.rearleft); // If in BrakeMode, set
													// CANTalons to brakeMode,
													// otherwise, don't

		frontright.enableBrakeMode(mode);
		frontleft.enableBrakeMode(mode);
		rearright.enableBrakeMode(mode);
		rearleft.enableBrakeMode(mode);

		tankDriveBase = new RobotDrive(frontleft, rearleft, frontright, rearright);// Initializes
																					// RobotDrive
																					// to
																					// use
																					// tankDrive()
		rightBaseEncoder = new Encoder(RobotMap.rightBaseEnc1, RobotMap.rightBaseEnc2, false, EncodingType.k4X);// Using
																												// 4X
																												// encoding
																												// for
																												// encoders
		leftBaseEncoder = new Encoder(RobotMap.leftBaseEnc1, RobotMap.leftBaseEnc2, false, EncodingType.k4X);
		rightBaseEncoder.setDistancePerPulse(distancePerPulse); // Sets distance
																// robot would
																// travel every
																// encoder pulse
		leftBaseEncoder.setDistancePerPulse(distancePerPulse);
		rightBaseEncoder.setSamplesToAverage(samplesToAverage); // Averages
																// encoder count
																// rate every
																// samplesToAverage
																// pulses
		leftBaseEncoder.setSamplesToAverage(samplesToAverage);
		rightBaseEncoder.setReverseDirection(false); // Reverses encoder
														// direction based on
														// position on robot
		leftBaseEncoder.setReverseDirection(true);
		rightBaseEncoder.setMinRate(minEncRate); // Sets minimum rate for
													// encoder before hardware
													// thinks it is stopped
		leftBaseEncoder.setMinRate(minEncRate);

		chopper = new CANTalon(RobotMap.chopper);
		chopper.setSafetyEnabled(false);
		chopper.enableBrakeMode(true);
	}

	/**
	 * Drive function to control motors. Control driveBase motors through this
	 * function.
	 * 
	 * @param left
	 *            Power to right motor. From -1 to 1.
	 * @param right
	 *            Power to left motor. From -1 to 1.
	 */
	public void drive(double left, double right) {
		tankDriveBase.tankDrive(left, right);
		SmarterDashboard.putNumber("LEFT-DRIVE", left);
		SmarterDashboard.putNumber("RIGHT-DRIVE", right);
	}

	/**
	 * Gets distance traveled by driveBase encoders since the last encoder
	 * reset.
	 * 
	 * @return double array where index 0 is left distance and 1 is right
	 *         distance.
	 */
	public double[] getEncDistance() {
		final double returnVals[] = { 0, 0 };
		returnVals[0] = -leftBaseEncoder.getDistance();// For some dumb reason,
														// the left never
														// inverses.
		returnVals[1] = rightBaseEncoder.getDistance();
		SmartDashboard.putNumber("LEFTENCODING", returnVals[0]);
		SmartDashboard.putNumber("RIGHTENCODING", returnVals[1]);
		return returnVals;
	}

	public void chopperUp() {
		chopper.set(1);
	}

	public void chopperDown() {
		chopper.set(-1);
	}

	public void resetDrive() {
		leftBaseEncoder.reset();
		rightBaseEncoder.reset();
	}
	
	public void setLeft(double speed) {
		frontleft.set(speed);
		rearleft.set(speed);
	}
	
	public void setRight(double speed) {
		frontright.set(speed);
		rearright.set(speed);
	}

	public void enableBrakeMode() {
		frontright.enableBrakeMode(true);
		rearright.enableBrakeMode(true);
		frontleft.enableBrakeMode(true);
		rearleft.enableBrakeMode(true);
	}

	public void disableBrakeMode() {
		frontright.enableBrakeMode(false);
		rearright.enableBrakeMode(false);
		frontleft.enableBrakeMode(false);
		rearleft.enableBrakeMode(false);
	}
}