package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwitchCase {
	private static double[] encodersDistance = { 0, 0 };
	// private double[] encodersRPM = {0};
	private static final double wheelCircum = driveBase.wheelDiameter * Math.PI;
	private static double driveForwardDistance = 0;
	static Vision cameraVision = new Vision();
	private static boolean pass = false;
	public final static int abortAutoAim = -42; // Get the joke, anyone?
	private static long autoAimTimer = 0;
	private static long autoAimIntakeTimer = 0;
	private static long autoAimManualTimer = 0;
	// private static int autoAimRemoteState = 0; //Used for the shoot()
	// function within autoAim()
	private static final int[] off = { 0, 0 };
	private static boolean inAuto = false;
	public static final double moveAmount = 0.48;
	public static int checkAmount = 3;
	private static int timesCount = 0;
	public static boolean shotTheBall = false;
	private static final double percentRange = 0.02;
	
	private static final int minVal = -80, maxVal = 80;
	public SwitchCase() {

	}

	/**
	 * Function that uses switch-case autonomous to allow the robot to drive
	 * forward at full speed until the driveForwardDistance specified in
	 * Autonomous.java. <b>Should only be called within updateStates()</b> Won't
	 * work otherwise.
	 * 
	 * @param state
	 * @return state
	 */
	public static int driveForward(int state, int distance) {
		switch (state) {
		default:
			break;
		case 0:
			break;
		case 1:
			driveForwardDistance = wheelCircum * distance;
			Robot.drivebase.drive(.75, .75);
			state = 2;
			break;
		case 2:
			encodersDistance = Robot.drivebase.getEncDistance();
			if (encodersDistance[0] > driveForwardDistance || encodersDistance[1] > driveForwardDistance) {
				Robot.drivebase.drive(0.0, 0.0);
				state = 3;
			}
			break;
		case 3:
			state = 0;
			break;
		}
		return state;
	}

	/**
	 * Function that uses switch-case autonomous to allow the robot to
	 * constantly look for a target to do auto-aim then fires.. <b>Should only
	 * be called within updateStates().</b> Won't work otherwise.
	 * 
	 * @param state
	 * @return state
	 */
	public static int autoAim(int state) {
		SmartDashboard.putNumber("STATE STATE STATE", state);
		SmarterDashboard.putBoolean("AUTO", state > 0);

		switch (state) {
		default:
			break;
		case 0:
			cameraVision.Update();
			inAuto = false;
			//timesCount = 0;
			break;
		case 1:
			//SmartDashboard.putNumber("STATE STATE STATE", state);
			//SmartDashboard.putNumber("COUNTINGDUDE", timesCount);
			//SmartDashboard.putBoolean("PASSED?", false);
			//timesCount += 1;
			// If David's autoAim code says to shoot
			cameraVision.Update();

			if (Vision.distance > 80 && Vision.distance < 94) {
				pass = true;
			} else if (Vision.distance < 94) {
				Robot.drivebase.drive(0.7, 0.7);
				state = 1;
				pass = false;
			} else if (Vision.distance > 80) {
				Robot.drivebase.drive(-0.7, -0.7);
				state = 1;
				pass = false;
			} else {
				SmartDashboard.putNumber("CRAPSTUFF", Vision.manVals[1]);
			} /*
				 * else if (Vision.manVals[0] == 5) {// || Vision.manVals[1] ==
				 * 5){ } SmarterDashboard.putString("ERROR",
				 * "It's too close and too far"); Robot.drivebase.drive(0, 0);
				 * state = abortAutoAim;
				 * 
				 * }
				 */

			// You get it now, right?
			if (pass) {
				//SmartDashboard.putBoolean("PASSED?", true);

				Robot.drivebase.enableBrakeMode();
				if (Vision.manVals[0] == 0) {
					state = 4;
					/*
					 * if (timesCount > checkAmount) { state = 4;// Change when
					 * you want f/backward } else { state = 1; timesCount += 1;
					 * Timer.delay(0.1); }
					 */
				} else if (Vision.manVals[0] == 1) {
					Robot.drivebase.drive(-moveAmount, moveAmount);
					state = 1;
				} else if (Vision.manVals[0] == 2) {
					Robot.drivebase.drive(moveAmount, -moveAmount);
					state = 1;
				} else if (Vision.manVals[0] == 5) {// || Vision.manVals[1] ==
													// 5){
					SmarterDashboard.putString("ERROR", "It's too close and too far");
					Robot.drivebase.drive(0, 0);
					state = abortAutoAim;

				} else {
					state = abortAutoAim; // manVals[0] should only be 0-2.
											// Nothing else. Somethign is wrong.
				}
			}
			break;
		/*
		 * case 3: cameraVision.Update(); SmartDashboard.putNumber(
		 * "STATE STATE STATE", state); if(Vision.manVals[1] == 0){
		 * if(Vision.manVals[0] != 0)//Make sure turn left + right is alright
		 * state = 2; else{ state = 4; //autoAimRemoteState = 1;
		 * SmartDashboard.putNumber("remoteBugIn", autoAimRemoteState); } } else
		 * if(Vision.manVals[1] == 1){ Robot.drivebase.drive(-0.55, -0.55);
		 * state = 3; } else if(Vision.manVals[1] == 2){
		 * Robot.drivebase.drive(.55, .55); state = 3; } else state =
		 * abortAutoAim; break;
		 */
		case 4:
			Robot.drivebase.disableBrakeMode();
			SmartDashboard.putNumber("STATE STATE STATE", state);
			// SmartDashboard.putNumber("remoteBug", autoAimRemoteState);
			// autoAimRemoteState =
			// if(autoAimRemoteState == 4)
			// state = 0;
			inAuto = true;
			state = 5;
			break;
		case 5:
			autoAimTimer = System.currentTimeMillis() + 3000;
			state = 6;
			// have to make this better, made into 4 states:
			// state 5 is to spin the flywheels
			// state 6 checks if the rpm is correct, and if so, go to state 7
			// state 7 intakes it
			// state 8 checks if a second passes, and if so, go to state 0
		case 6:
			//this used to do something, but we removed it, and i dont want to change all the numbers again
			state = 7;
			break;
		case 7:

			// state = 5;
			// shootStates = shoot(shootStates, 0.8);
			// if(shootStates == 0) {state = -1;}
			final double[] currentRPM = Robot.flywheels.getRPM();
			final int[] speeds3 = { 4250, 4250 };
			final int marginOfError = (int) (speeds3[0] * percentRange);
			Robot.flywheels.setPIDSpeed(speeds3);
			if (System.currentTimeMillis() >= autoAimTimer && ((currentRPM[0] <= speeds3[0] + marginOfError
					&& currentRPM[0] >= speeds3[0] - marginOfError)
					|| (currentRPM[1] <= speeds3[1] + marginOfError && currentRPM[1] >= speeds3[1] - marginOfError))) {
				state = 8;
			}
			// Teleop.currentShootState=1;

			break;
		case 8:
			autoAimIntakeTimer = System.currentTimeMillis() + 1000;
			state = 9;
			break;
		case 9:
			Robot.flywheels.setIntakeSpeed(1.0);
			SmartDashboard.putNumber("Auto Aim Timer", autoAimIntakeTimer);
			if (System.currentTimeMillis() >= autoAimIntakeTimer) {
				state = 10;
			}
			break;
		case 10:
			shotTheBall = true;
			Robot.flywheels.setPIDSpeed(off);
			state = -1;
			break;
		case abortAutoAim:
			SmartDashboard.putString("Bug", "Failed to AutoAim");
			state = 1;
			break;
		case -1:
			Robot.drivebase.disableBrakeMode();
			SmartDashboard.putNumber("STATE STATE STATE", -1);
			Robot.flywheels.setPIDSpeed(off);
			Robot.flywheels.setIntakeSpeed(0);
			state = 0;
			break;
		}
		return state;
	}

	public static int shoot(int state, double shootSpeed) {// shootSpeed is temp
															// since there is no
															// camera at the
															// time of coding
		double toSetSpeed = shootSpeed + SmarterDashboard.getNumber("OVERDRIVE", 0.0);
		cameraVision.Update();
		SmartDashboard.putNumber("SysTime", System.currentTimeMillis());
		/*
		 * if(Vision.manVals[1] == 1) { SmarterDashboard.addDebugString(
		 * "You're too close"); } else if(Vision.manVals[1] == 2) {
		 * SmarterDashboard.addDebugString("You're too far"); } else {
		 * SmarterDashboard.addDebugString("You're good"); }
		 */
		switch (state) {
		default:
			break;
		case 0:
			break;
		case 1:
			SmarterDashboard.putBoolean("AUTO", true);
			//double[] speeds = { toSetSpeed, toSetSpeed };
			final int toShoot = (int) cameraVision.getRPMS();
			final int[] woav = {toShoot, toShoot};
			Robot.flywheels.setPIDSpeed(woav);
			autoAimTimer = System.currentTimeMillis() + 2500;
			//Robot.flywheels.leftFW.setVoltageRampRate(1);
			//Robot.flywheels.rightFW.setVoltageRampRate(1);

			state = 2;
		case 2:
			SmartDashboard.putNumber("shootBug", System.currentTimeMillis());
			SmartDashboard.putNumber("shootBug2", autoAimTimer);

			final double[] curRPM = Robot.flywheels.getRPM();
			final int speedsTwo = (int) cameraVision.getRPMS();
			final int[] speedToo = {speedsTwo, speedsTwo};
			/*
			SmartDashboard.putString("LEFTRIGHT", String.valueOf(speedsTwo[2]) + ":" + String.valueOf(speedsTwo[3]));
			SmartDashboard.putString("NEEDEDLEFTRIGHT",
					String.valueOf(speedsTwo[4]) + ":" + String.valueOf(speedsTwo[5]));
			double leftPower = Robot.flywheels.getLeftPower(); // SmarterDashboard.getNumber("OVERDRIVE",
																// 0.0);;
			double rightPower = Robot.flywheels.getRightPower(); // +
																	// SmarterDashboard.getNumber("OVERDRIVE",
																	// 0.0);;
			double newPower[] = { leftPower + (speedsTwo[0] / 3.5), rightPower + (speedsTwo[1] / 3.5) };
			Robot.flywheels.setFlywheelSpeed(newPower);*/
			Robot.flywheels.setPIDSpeed(speedToo);
			if (cameraVision.withIn(curRPM[0], speedsTwo + minVal, speedsTwo + maxVal) && cameraVision.withIn(curRPM[1], speedsTwo + minVal, speedsTwo + maxVal)) {
				/*
				 * if(passedTimes < 4) { Timer.delay(0.1); passedTimes += 1; }
				 * else {
				 */
				// if(System.currentTimeMillis() >= autoAimTimer){]
				/*final double newSpeed[] = { newPower[0] - 0.1, // + 0.009 +
																// (SmarterDashboard.getNumber("OVERDRIVE",
																// 0.0) /10),
						newPower[1] - 0.1 };// + 0.009 +
											// (SmarterDashboard.getNumber("OVERDRIVE",
											// 0.0) /10)};
				Robot.flywheels.setFlywheelSpeed(newSpeed);
				// Timer.delay(0.2);
				// SmartDashboard.putNumber("autoAimIntakebug",
				// System.currentTimeMillis());*/
				Robot.flywheels.setIntakeSpeed(1);
				Robot.flywheels.setPIDSpeed(speedToo);
				if (inAuto) {
					for (int a = 0; a < 200; a++) {
						Robot.flywheels.setIntakeSpeed(1);
						Timer.delay(0.005);
					}
					shotTheBall = true;
					Robot.flywheels.setPIDSpeed(off);
				}
				autoAimIntakeTimer = System.currentTimeMillis() + 1750;
				SmartDashboard.putNumber("autoAimIntakeBug2", autoAimIntakeTimer);
				state = 3;
				// }
			} else
				state = 2;
			break;
		case 3:
			if (System.currentTimeMillis() >= autoAimIntakeTimer) {
				SmarterDashboard.putBoolean("AUTO", false);
				Robot.flywheels.setIntakeSpeed(0);
				Robot.flywheels.setPIDSpeed(off);
//				Robot.flywheels.leftFW.setVoltageRampRate(10);
//				Robot.flywheels.rightFW.setVoltageRampRate(10);
				state = 4;
			} else
				state = 3;
			break;
		case 4: // This is to allow remoteStates to know when program is done
			state = 0;
			break;
		case -1:// You are aborting
			Robot.flywheels.setIntakeSpeed(0);
			Robot.flywheels.setPIDSpeed(off);
//			Robot.flywheels.leftFW.setVoltageRampRate(10);
//			Robot.flywheels.rightFW.setVoltageRampRate(10);
			state = 0;
		}
		return state;
	}

	public static int shootManual(int state) {
		switch (state) {
		default:
			break;
		case 0:
			break;
		case 1:
			autoAimManualTimer = System.currentTimeMillis() + 750;
			Robot.flywheels.setIntakeSpeed(1);
			state = 2;
			break;
		case 2:
			SmartDashboard.putNumber("autoAimManualTimer", autoAimManualTimer);
			if (autoAimManualTimer >= System.currentTimeMillis()) {
				Robot.flywheels.setIntakeSpeed(0);
				state = 3;
			}
			break;
		case 3:// In case you want to know that it is done
			state = 0;
			break;
		case -1:
			Robot.flywheels.setIntakeSpeed(0);
			Robot.flywheels.setPIDSpeed(off);
			state = 0;
			break;
		}
		return state;
	}
}

	/*
	public static int climb(int state) {
		switch (state) {
		default:
			break;
		case 0:
			break;
		case 1:
			Robot.pneumatics.extendClimber.set(DoubleSolenoid.Value.kForward);
			extendClimberTimer = System.currentTimeMillis() + 2000;
			state = 2;
			break;
		case 2:
			if (System.currentTimeMillis() >= extendClimberTimer) {
				state = 3;
			}
			break;
		case 3:
			raiseClimberTimer = System.currentTimeMillis() + 2000;
			Robot.pneumatics.raiseClimber.set(DoubleSolenoid.Value.kForward);
			state = 4;
			break;
		case 4:
			if (System.currentTimeMillis() >= raiseClimberTimer) {
				state = 5;
			}
			break;
		case 5:
			Robot.pneumatics.extendClimber.set(DoubleSolenoid.Value.kReverse);
			extendClimberTimer = System.currentTimeMillis() + 500;
			state = 6;
			break;
		case 6:
			if (System.currentTimeMillis() >= extendClimberTimer) {
				state = 7;
			}
			break;
		case 7: // Done, do winch now.
			state = 0;
			break;
		// For reverse-climbing. Make sure to update the delays for BOTH
		// climbing/letting go
		case 8:
			Robot.pneumatics.extendClimber.set(DoubleSolenoid.Value.kForward);
			extendClimberTimer = System.currentTimeMillis() + 500;
			state = 9;
			break;
		case 9:
			if (System.currentTimeMillis() >= extendClimberTimer) {
				state = 10;
			}
			break;
		case 10:
			Robot.pneumatics.raiseClimber.set(DoubleSolenoid.Value.kReverse);
			raiseClimberTimer = System.currentTimeMillis() + 2000;
			state = 11;
			break;
		case 11:
			if (System.currentTimeMillis() >= raiseClimberTimer) {
				state = 12;
			}
			break;
		case 12:
			Robot.pneumatics.extendClimber.set(DoubleSolenoid.Value.kReverse);
			extendClimberTimer = System.currentTimeMillis() + 2000;
			state = 13;
			break;
		case 13:
			Robot.pneumatics.extendClimber.set(DoubleSolenoid.Value.kReverse);
			Robot.pneumatics.raiseClimber.set(DoubleSolenoid.Value.kReverse);
			state = 14;
			break;
		case 14:
			state = 0;
			break;
		case -1:
			state = 13;
			break;
		}
		return state;
	}
}*/
