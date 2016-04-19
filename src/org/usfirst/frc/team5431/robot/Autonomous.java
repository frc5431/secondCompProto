package org.usfirst.frc.team5431.robot;

import org.usfirst.frc.team5431.robot.Robot.AutoTask;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class for Autonomous commands. Uses switch-cases in order to acheive
 * multi-threading without creating multiple threads. Also much faster compared
 * to multi-threading and takes less space.
 * 
 * @author Usaid Malik
 */
public class Autonomous {

	public static int driveForwardState = 0;
	private int driveForwardGyroState = 0;
	private int touchForwardState = 0;
	private int moatForwardState = 0;
	private int navexLowbarShoot = 0;
	private static double forwardGyro_gyroAngle = 0.0;
	private static int forwardGyro_counter = 0;
	public static boolean autoAIMState = false;
	public static boolean seeOnlyTowerState = false;
	private static boolean runOnce = false;
	private static boolean forwardGyro_turn = false;
	private static boolean forwardGyro_turnTimed = false;
	private static boolean forwardGyro_shoot = false;
	private static boolean forwardGyro_intake = false;
	private static boolean forwardGyro_startFly = false;
	private static double forwardGyro_turnAngle = 0;
	private static long forwardGyro_neededTime = 0;
	private static long forwardGyro_turnTime = 0;
	public static int currAIM = 1;
	private long crossMoatTimer = 0;
	private static double[] driveDistance = { 0, 0 };
	private static double[] off = { 0.0, 0.0 };

	private final double[] speedToOuterWork = { 0.65, 0.65 }, speedToCrossMoat = { 1, 1 };

	private static final double distanceToOuterWork = 48, distanceToCrossWork = 135, // 128
			distanceToCrossRough = 130, distanceToSeeOnlyTower = 12, forwardGyro_barelyCross = 122;// 122

	public static enum FirstMove {
		StandStill, TouchOuterWork, Lowbar, RockWall, RoughTerrain, Portcullis, ChevalDeFrise, Moat;
	}

	public static enum AutoShoot {
		False, True;
	}

	/**
	 * Initializes the Autonomous class.
	 */
	public Autonomous() {

	}

	private void curveFix(double speeds[]) {
		/*
		 * final double toDrive[] = { 0, 0 }; driveDistance =
		 * Robot.drivebase.getEncDistance(); if(driveDistance[0] <
		 * driveDistance[1]) { toDrive[0] = speeds[0] - curveAmount; toDrive[1]
		 * = speeds[1] + curveAmount; } else if(driveDistance[0] >
		 * driveDistance[1]) { toDrive[0] = speeds[0] + curveAmount; toDrive[1]
		 * = speeds[1] - curveAmount; } else { toDrive[0] = speeds[0];
		 * toDrive[1] = speeds[1]; } Robot.drivebase.drive(-toDrive[0],
		 * -toDrive[1]);
		 */
		Robot.drivebase.drive(-speeds[0], -speeds[1]);
	}

	private void touchForward() {

		if ((driveDistance[0] < distanceToOuterWork || driveDistance[1] < distanceToOuterWork)
				&& driveForwardState == 0) {
			curveFix(speedToOuterWork);
		} else {
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}

		// touchForwardState = SwitchCase.driveForward(touchForwardState, 72);
	}

	private void PorticShoot() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < (distanceToCrossWork + 24) || driveDistance[1] < (distanceToCrossWork + 24))
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-0.7, -0.73);
			Robot.drivebase.chopperDown();
			SmartDashboard.putString("READY READY READY", "Auto driving");
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			Robot.drivebase.chopperUp();
			Timer.delay(0.25);
			Robot.drivebase.drive(0,
					0);/*
						 * 
						 * 
						 * for(int time = 0; time < 15; time++) {
						 * Robot.drivebase.drive(-0.78, 0.78);
						 * Timer.delay(0.005); } for(int time2 = 0; time2 < 30;
						 * time2++) { Robot.drivebase.drive(-0.76, -0.76);
						 * Timer.delay(0.005); }
						 */
			driveForwardState = 1;
			if (!autoAIMState) {
				autoAIMState = true;
				Timer.delay(0.75);
				// SwitchCase.moveAmount = 0.43;
				SwitchCase.checkAmount = 1;
				SwitchCase.shotTheBall = false;
				currAIM = SwitchCase.autoAim(currAIM);
			}
			if (autoAIMState) {

				SmartDashboard.putString("READY READY READY", "Auto aiming");
				currAIM = SwitchCase.autoAim(currAIM);
				if ((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) {
					currAIM = 1;
				}
			}
		}

	}

	private void LowbarShoot() {
		if ((driveDistance[0] < distanceToCrossWork || driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			// curveFix(speedToOuterWork);
			Robot.drivebase.drive(-0.80, -0.83);
		} else if (!autoAIMState && !seeOnlyTowerState) {
			driveForwardState = 1;
			Timer.delay(0.5);
			for (int time = 0; time < 30; time++) {
				Robot.drivebase.drive(0.78, -0.78);
				Timer.delay(0.005);
			}
			for (int time2 = 0; time2 < 35; time2++) {
				Robot.drivebase.drive(-0.76, -0.76);
				Timer.delay(0.005);
			}
			Robot.drivebase.resetDrive(); // Reset encoders for moving forward
			seeOnlyTowerState = true;
		} else if (!autoAIMState && seeOnlyTowerState) {
			if (driveDistance[0] < distanceToSeeOnlyTower && driveDistance[1] < distanceToSeeOnlyTower) {
				Robot.drivebase.drive(-.70, -.73);
			} else {
				autoAIMState = true;
				Timer.delay(0.15);
				// SwitchCase.moveAmount = 0.43;
				SwitchCase.checkAmount = 1;
				SwitchCase.shotTheBall = false;
				currAIM = SwitchCase.autoAim(currAIM);
			}
		}
		if (autoAIMState) {
			SmartDashboard.putString("READY READY READY", "Auto aiming");
			currAIM = SwitchCase.autoAim(currAIM);
			if ((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) {
				currAIM = 1;
			}
		}

	}

	private void crossForward() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossWork && driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			// curveFix(speedToOuterWork);
			Robot.drivebase.drive(-0.70, -0.73);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}

		/*
		 * driveDistance = Robot.drivebase.getEncDistance(); if
		 * ((driveDistance[0] < distanceToCrossWork || driveDistance[1] <
		 * distanceToCrossWork) && driveForwardState == 0) {
		 * Robot.drivebase.drive(-0., -0.60); } else { Robot.drivebase.drive(0,
		 * 0); Robot.drivebase.resetDrive(); driveForwardState = 1; }
		 */
	}

	private void crossRockWall() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossWork - 5 && driveDistance[1] < distanceToCrossWork - 5)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
	}

	/*
	 * private void crossRoughTerrain() { driveDistance =
	 * Robot.drivebase.getEncDistance(); if ((driveDistance[0] <
	 * distanceToCrossRough || driveDistance[1] < distanceToCrossRough) &&
	 * driveForwardState == 0) { Robot.drivebase.drive(-1, -1); } else {
	 * Robot.drivebase.drive(0, 0); Robot.drivebase.resetDrive();
	 * driveForwardState = 1; } }
	 */

	private void shootRockWall() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < (distanceToCrossWork - 24) || driveDistance[1] < (distanceToCrossWork - 24))
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
			SmartDashboard.putString("READY READY READY", "Auto driving");
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			Timer.delay(0.7);
			Robot.drivebase.drive(0, 0);

			driveForwardState = 1;
			if (!autoAIMState) {
				for (int time = 0; time < 10; time++) {
					Robot.drivebase.drive(-0.468, 0.468);
					Timer.delay(0.005);
				}
				Timer.delay(0.2);
				for (int time2 = 0; time2 < 70; time2++) {
					Robot.drivebase.drive(-0.468, -0.468);
					Timer.delay(0.005);
				}
				autoAIMState = true;
				// Timer.delay(0.2);
				SwitchCase.shotTheBall = false;
			}
		}

		if (autoAIMState) {
			SmartDashboard.putString("READY READY READY", "Auto aiming");
			currAIM = SwitchCase.autoAim(currAIM);
			if ((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) {
				currAIM = 1;
			}
		}

	}
	/*
	 * public static void processAutonSteps() { final double[] defaults = {0,
	 * 30, 0}; final double[] stepAuton = new double[50]; final double[] current
	 * = SmarterDashboard.getNumberArray("AUTO-STEP-SELECT", defaults);
	 * 
	 * for(int am = 0; am < current.length; am++) stepAuton[am] = current[am];
	 * 
	 * final FirstMove selectedCross = FirstMove.values()[(int) stepAuton[0]];
	 * //good joy msn final AutoShoot selectedShoot = AutoShoot.values()[(int)
	 * stepAuton[3]];
	 * 
	 * switch(selectedCross) { default : break; case StandStill: drive(0, 0.1,
	 * 0.1, 1); return; //Just stop bro.... case TouchOuterWork:
	 * drive(distanceToOuterWork, 0.75, 0.02, 10); //Just touch it dude return;
	 * case Lowbar: drive(distanceToCrossWork, 0.69, 0.02, 8); //Drive slowly
	 * break; case RockWall: drive(distanceToCrossWork, 1, 9, false); //Doesn't
	 * matter to fix break; case RoughTerrain: drive(distanceToCrossRough, 0.9,
	 * 0.02, 8); break; case Portcullis: fallChopper(); Timer.delay(0.1);
	 * drive(distanceToCrossWork, 0.7, 0.02, 8); Timer.delay(0.1);
	 * liftChopper(); break; case ChevalDeFrise: liftChopper();
	 * Timer.delay(0.01); drive(distanceToOuterWork, 0.6, 0.02, 6);
	 * Timer.delay(0.1); fallChopper(); Timer.delay(1.4);
	 * drive(distanceToOuterWork - 10, 0.8, 0.02, 5); break; case Moat:
	 * liftChopper(); Timer.delay(0.01); drive(distanceToCrossWork, -1, 6,
	 * false); turnPivot(180, 0.75, 3); break; }
	 * 
	 * //Turn if you want turnPivot((int) stepAuton[1], 0.69, 5);
	 * 
	 * //Drive if you want drive(stepAuton[2], 0.5, 0.05, 5);
	 * 
	 * //Do you autoShoot?
	 * 
	 * switch(selectedShoot) { default: break; case True: autoAim(9); break;
	 * case False: Robot.drivebase.drive(0, 0); break; }
	 * 
	 * for(int amount = 0; amount < stepAuton.length; amount++) {
	 * if(stepAuton[amount] == 0) break; }
	 * 
	 * }
	 * 
	 * private static void liftChopper() { Robot.drivebase.chopperUp(); }
	 * 
	 * private static void fallChopper() { Robot.drivebase.chopperDown(); }
	 * 
	 * private static void autoAim(double timeoutSecs) {
	 * Robot.drivebase.enableBrakeMode();
	 * 
	 * final double timeoutSystem = System.currentTimeMillis() + (timeoutSecs *
	 * 1000); SwitchCase.shotTheBall = false;
	 * 
	 * while(System.currentTimeMillis() < timeoutSystem) { currAIM =
	 * SwitchCase.autoAim(currAIM); if ((currAIM == 0 || currAIM == -1) &&
	 * !SwitchCase.shotTheBall) { currAIM = 1; } Timer.delay(0.005); }
	 * SwitchCase.shotTheBall = false; Robot.drivebase.drive(0, 0);
	 * Timer.delay(0.01); Robot.drivebase.disableBrakeMode(); }
	 * 
	 * private static void drive(double distance, double speeds, double
	 * timeoutSecs, boolean matter) { drive(distance, speeds, 0, timeoutSecs,
	 * matter); }
	 * 
	 * private static void drive(double distance, double speeds, double
	 * curveFix, double timeoutSecs) { drive(distance, speeds, curveFix,
	 * timeoutSecs, true); }
	 * 
	 * private static void drive(double distance, double speeds, double
	 * curveFix, double timeoutSecs, boolean matter) { if(distance == 0)
	 * {Robot.drivebase.drive(0, 0); return;} final boolean direction =
	 * (distance < 0); Robot.drivebase.resetDrive(); final double timeoutSystem
	 * = System.currentTimeMillis() + (timeoutSecs * 1000);
	 * Robot.drivebase.enableBrakeMode(); Timer.delay(0.01);
	 * 
	 * distance = direction ? -(distance) : distance;
	 * 
	 * while(System.currentTimeMillis() < timeoutSystem && !matter) {
	 * encoderUpdate(); if(driveDistance[0] < distance && driveDistance[1] <
	 * distance) { Robot.drivebase.drive(-speeds, -speeds); } else {
	 * Robot.drivebase.drive(0, 0); break; } Timer.delay(0.005); }
	 * 
	 * while(System.currentTimeMillis() < timeoutSystem && matter) {
	 * encoderUpdate();
	 * 
	 * final double toDrive[] = { 0, 0 }; final double fixCurve = (curveFix *
	 * (driveDistance[0] - driveDistance[1])); toDrive[0] = speeds + fixCurve;
	 * toDrive[1] = speeds + fixCurve; toDrive[0] = (direction) ? -toDrive[0] :
	 * toDrive[0]; toDrive[1] = (direction) ? -toDrive[1] : toDrive[1];
	 * if(driveDistance[0] < distance) { Robot.drivebase.drive(-toDrive[0],
	 * -toDrive[1]); } else { Robot.drivebase.drive(0, 0); break; }
	 * Timer.delay(0.005); } Robot.drivebase.drive(0, 0); Timer.delay(0.01);
	 * Robot.drivebase.disableBrakeMode(); }
	 * 
	 * private static void turnPivot(int degrees, double speed, double
	 * timeoutSecs) { Robot.drivebase.resetDrive(); double distance = (23.75 *
	 * Math.PI * (degrees / 2)) / 360;//Calculate distance needed to turn final
	 * boolean direction = degrees < 0;//false is to the right, true is to the
	 * left if(degrees == 0) return; double timeoutSystem =
	 * System.currentTimeMillis() + (timeoutSecs * 1000);
	 * Robot.drivebase.enableBrakeMode(); Timer.delay(0.01); //distance =
	 * direction ? -(distance) : distance;
	 * 
	 * while(System.currentTimeMillis() < timeoutSystem){ encoderUpdate();
	 * //Update encoders
	 * 
	 * boolean Lpass = false, Rpass = false;
	 * 
	 * if(driveDistance[0] < distance) {
	 * Robot.drivebase.setLeft(-1);//(direction) ? speed : -speed); // negative
	 * is forward ps Usaid } else { Robot.drivebase.setLeft(0.01); Lpass = true;
	 * }
	 * 
	 * if(driveDistance[1] > distance) {
	 * Robot.drivebase.setRight(1);//(direction) ? speed : -speed); Rpass =
	 * true; } else { Robot.drivebase.setLeft(-0.01); }
	 * 
	 * if(Lpass && Rpass) {Timer.delay(0.005); break;}
	 * 
	 * Timer.delay(0.005); //Motor update period (50Hz) }
	 * Robot.drivebase.drive(0, 0); Timer.delay(0.01);
	 * Robot.drivebase.disableBrakeMode(); }
	 */

	private int moatForward(int state) {
		switch (state) {
		case 0:
			break;
		default:
			break;
		case 1:
			curveFix(speedToCrossMoat);
			state = 2;
			break;
		case 2:
			if (!(driveDistance[0] < distanceToCrossWork || driveDistance[1] < distanceToCrossWork)
					&& driveForwardState == 0) {
				Robot.drivebase.resetDrive();
				driveForwardState = 1;
				state = 3;
			}
			break;
		case 3:
			crossMoatTimer = System.currentTimeMillis() + 3000;
			state = 4;
			break;
		case 4:
			if (System.currentTimeMillis() >= crossMoatTimer) {
				Robot.drivebase.drive(0, 0);
				state = 5;
			}
			break;
		case 5:
			state = 0;
			break;
		}
		/*
		 * if((driveDistance[0] < distanceToCrossWork || driveDistance[1] <
		 * distanceToCrossWork) && driveForwardState == 0) {
		 * curveFix(speedToCrossMoat); } else { Robot.drivebase.resetDrive();
		 * driveForwardState = 1; }
		 */
		return state;
	}

	/**
	 * Shoots from the spybox position.
	 */
	private void spyboxShoot() {
		for (int time = 0; time < 35; time++) {
			Robot.drivebase.drive(0.8, -0.8);
			Timer.delay(0.005);
		}
		for (int time2 = 0; time2 < 35; time2++) {
			Robot.drivebase.drive(-0.6, -0.6);
			Timer.delay(0.005);
		}
		if (!autoAIMState) {
			driveForwardState = 1;
			autoAIMState = true;
			Timer.delay(0.75);
			// SwitchCase.moveAmount = 0.43;
			SwitchCase.checkAmount = 1;
			SwitchCase.shotTheBall = false;
			currAIM = SwitchCase.autoAim(currAIM);
		}
		if (autoAIMState) {
			SmartDashboard.putString("READY READY READY", "Auto aiming");
			currAIM = SwitchCase.autoAim(currAIM);
			if ((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) {
				currAIM = 1;
			}
		}
	}

	private static void encoderUpdate() {
		driveDistance = Robot.drivebase.getEncDistance();
	}

	static double maximumError = 0, minimumError = 0, forwardGyro_previousAngle = 0;

	private static final double autoGyroDriveSpeed[] = { -0.70, -0.70 };
	private static int autoIterations = 0;

	private static void driveForwardGyro() {
		SmartDashboard.putBoolean("GYRO CODE ACTIVE", true);
		driveDistance = Robot.drivebase.getEncDistance();
		final double[] shootPower = { 3200.0, 3200.0 };
		SmartDashboard.putNumber("Starting Gyro Angle", Robot.startGyroAngle);
		if ((driveDistance[0] < (forwardGyro_barelyCross) || driveDistance[1] < (forwardGyro_barelyCross))
				&& driveForwardState == 0 && !forwardGyro_turn) {
			// if((driveDistance[0] < (0) || driveDistance[1] < (0)) &&
			// driveForwardState == 0 && !forwardGyro_turn) {
			// curveFix(speedToOuterWork);
			forwardGyro_previousAngle = forwardGyro_gyroAngle;
			autoIterations++;
			SmartDashboard.putNumber("GYRO CALCULATIONS", autoIterations);
			forwardGyro_gyroAngle = (Robot.gyro.getAngle() - Robot.startGyroAngle) * .025;
			if (forwardGyro_gyroAngle > 0.2) {

				forwardGyro_gyroAngle = 0.2;
			} else if (forwardGyro_gyroAngle < -0.2) {
				forwardGyro_gyroAngle = -0.2;
			}
			if (forwardGyro_gyroAngle < minimumError)
				minimumError = forwardGyro_gyroAngle;
			else if (forwardGyro_gyroAngle > maximumError)
				maximumError = forwardGyro_gyroAngle;
			SmartDashboard.putNumber("Maximum Error", maximumError);
			SmartDashboard.putNumber("Minimum Error", minimumError);
			if (forwardGyro_gyroAngle > 1.0 || forwardGyro_gyroAngle < -1.0) {
				Robot.drivebase.drive(autoGyroDriveSpeed[0] + forwardGyro_gyroAngle,
						autoGyroDriveSpeed[1] - forwardGyro_gyroAngle);
				SmartDashboard.putBoolean("Adjusting straightness", true);
			} else {
				SmartDashboard.putBoolean("Adjusting straightness", false);
				Robot.drivebase.drive(autoGyroDriveSpeed[0], autoGyroDriveSpeed[1]);
			}
		} else {
			if (!forwardGyro_turn) {
				forwardGyro_turn = true;
				Robot.drivebase.drive(0, 0);
				forwardGyro_turnAngle = -28.0;
				SmartDashboard.putNumber("Turn angle", forwardGyro_turnAngle);

			}
			// SmartDashboard.putNumber("Turn Counter", 0);
		}

		if (forwardGyro_turn && !forwardGyro_turnTimed) {
			forwardGyro_counter += 1;
			if (Robot.gyro.getAngle() > forwardGyro_turnAngle - 3) {
				Robot.drivebase.drive(-autoGyroDriveSpeed[0], autoGyroDriveSpeed[1]);
				SmartDashboard.putString("Angle Direction", "right");
			} else {
				// please murder me
				forwardGyro_turnTime = System.currentTimeMillis() + 1000;
				SmartDashboard.putNumber("Gyro afterTurn", Robot.gyro.getAngle());
				SmartDashboard.putNumber("timeAfterTurn", System.currentTimeMillis());
				SmartDashboard.putNumber("turnTime", forwardGyro_turnTime);
				forwardGyro_turnTimed = true;
				SwitchCase.shotTheBall = false;

			}
		}

		if (forwardGyro_turnTimed && !forwardGyro_shoot) {
			forwardGyro_counter++;
			SmartDashboard.putNumber("Turn Counter", forwardGyro_counter);

			if (System.currentTimeMillis() < forwardGyro_turnTime) {
				Robot.drivebase.drive(-0.90, 0.90);
				/*
				 * if(Robot.gyro.getAngle() > forwardGyro_turnAngle - 3){
				 * Robot.drivebase.drive(0.7, -0.7); SmartDashboard.putString(
				 * "Angle Direction", "right"); } else if(Robot.gyro.getAngle()
				 * < forwardGyro_turnAngle + 3){ Robot.drivebase.drive(-.7, .7);
				 * //Robot.drivebase.drive(0.0, 0.0); SmartDashboard.putString(
				 * "Angle Direction", "left"); } else{
				 * Robot.drivebase.drive(0.0, 0.0); SmartDashboard.putNumber(
				 * "Gyro afterTurn", Robot.gyro.getAngle());
				 * SmartDashboard.putNumber("timeAfterTurn",
				 * System.currentTimeMillis()); //forwardGyro_shoot = true;
				 * SwitchCase.shotTheBall = false; }
				 */
			} else {
				Robot.drivebase.drive(0.0, 0.0);
				forwardGyro_shoot = true;
			}
		}

		if (forwardGyro_turn && forwardGyro_shoot && !forwardGyro_intake && !forwardGyro_startFly) {

			// SmartDashboard.putNumber(key, value);
			Robot.flywheels.setFlywheelSpeed(shootPower);
			SmartDashboard.putNumber("timeAfterStart", System.currentTimeMillis());
			SmartDashboard.putNumber("Gyro beforeSpinUp", Robot.gyro.getAngle());
			forwardGyro_startFly = true;
		}

		if (forwardGyro_startFly && !forwardGyro_intake) {
			// currentRPM = {0.0, 0.0};
			double[] currentRPM = Robot.flywheels.getRPM();
			if ((currentRPM[0] <= shootPower[0] * 1.05 && currentRPM[0] >= shootPower[0] * .95)
					|| (currentRPM[1] <= shootPower[1] * 1.1 && currentRPM[1] >= shootPower[1] * .9)) {
				forwardGyro_neededTime = System.currentTimeMillis() + 750;
				forwardGyro_intake = true;
			}
		}

		if (forwardGyro_intake) {
			if (System.currentTimeMillis() >= forwardGyro_neededTime) {
				Robot.flywheels.setIntakeSpeed(0.0);
				Robot.flywheels.setFlywheelSpeed(off);
			} else
				Robot.flywheels.setIntakeSpeed(1.0);
		}
	}

	
	/**
	 * Updates the state of various autonomous functions. This must be called in
	 * <b>autonomousPeriodic()</b>.
	 * <ul>
	 * Currently updates
	 * </ul>
	 * :
	 * <li>driveForward()</li>
	 * <li>autoAim()</li>
	 */
	public void updateStates(AutoTask currentAuto) {

		encoderUpdate();

		SmartDashboard.putString("CURRENT SELECTED", currentAuto.name());

		switch (currentAuto) {
		case TouchOuterWork:
			touchForward();
			break;
		case CrossOuter:
			SmartDashboard.putNumber("navexState", navexLowbarShoot);
			double[] shootSpeed = {3300, 3300};
			switch(navexLowbarShoot)
			{
			default:
				break;
			case 0:
				Robot.drivebase.resetDrive();
				Robot.drivebase.enablePIDCDrive(-0.7, 0.2f, 0.7f);
				navexLowbarShoot = 1;
				break;
			case 1:
				driveDistance = Robot.drivebase.getEncDistance();
				if ((driveDistance[0] > (forwardGyro_barelyCross) || driveDistance[1] > (forwardGyro_barelyCross)))
				{
					//Robot.drivebase.disablePIDC();
					//Robot.drivebase.drive(0, 0);
					Robot.drivebase.enablePIDCTurn(-50);
					navexLowbarShoot = 2;
				}
				break;
			case 2:
				if(Math.abs(-50 - Robot.drivebase.ahrs.getYaw()) <= 1)
				{
					navexLowbarShoot = 3;
				}
				break;
			case 3:
				
				Robot.flywheels.setFlywheelSpeed(shootSpeed);
				navexLowbarShoot = 4;
				break;
			case 4:
				double[] currentRPM = Robot.flywheels.getRPM();
				if ((currentRPM[0] <= shootSpeed[0] * 1.05 && currentRPM[0] >= shootSpeed[0] * .95)
						|| (currentRPM[1] <= shootSpeed[1] * 1.1 && currentRPM[1] >= shootSpeed[1] * .9)) {
					forwardGyro_neededTime = System.currentTimeMillis() + 750;
					navexLowbarShoot = 5;
				}
				break;
			case 5:
				if (System.currentTimeMillis() >= forwardGyro_neededTime) {
					Robot.flywheels.setIntakeSpeed(0.0);
					Robot.flywheels.setFlywheelSpeed(off);
					Robot.drivebase.disablePIDC();
					navexLowbarShoot = 0;
				} else
					Robot.flywheels.setIntakeSpeed(1.0);
				break;
			}
			// crossForward();
			//driveForwardGyro();
			
			/*
			forwardGyro_turnAngle=-90;
			//forwardGyro_turnTimed=false;
//			final double rampRate = -0.005;
			if (!forwardGyro_turnTimed) {
				forwardGyro_counter += 1;
				final double percentageThere = Robot.gyro.getAngle()/forwardGyro_turnAngle;
				ramp[0] = (0.20-(0.20*percentageThere))+0.50;
				ramp[1]=-ramp[0];
				final double pid = motorPID(Robot.gyro.getAngle(), forwardGyro_turnAngle);
				Robot.drivebase.drive(pid, -pid);
//				ramp[0]-=rampRate;
//				ramp[1]+=rampRate;
				if (Robot.gyro.getAngle() > forwardGyro_turnAngle) {
					SmartDashboard.putNumber("RAMP 0", ramp[0]);
					SmartDashboard.putNumber("RAMP 1", ramp[1]);
					SmartDashboard.putString("Angle Direction", "right");
				} else {
//					Robot.drivebase.drive(0, 0);
					// please murder me
					forwardGyro_turnTime = System.currentTimeMillis() + 1000;
					SmartDashboard.putNumber("Gyro afterTurn", Robot.gyro.getAngle());
					SmartDashboard.putNumber("timeAfterTurn", System.currentTimeMillis());
					SmartDashboard.putNumber("turnTime", forwardGyro_turnTime);
					forwardGyro_turnTimed = true;
					SwitchCase.shotTheBall = false;

				}
			}

			if (forwardGyro_turnTimed) {
				//forwardGyro_counter++;
				Robot.drivebase.drive(0, 0);
				SmartDashboard.putNumber("Turn Counter", forwardGyro_counter);

				if (System.currentTimeMillis() < forwardGyro_turnTime) {
				//	Robot.drivebase.drive(-0.5, 0.5);
					/*
					 * if(Robot.gyro.getAngle() > forwardGyro_turnAngle - 3){
					 * Robot.drivebase.drive(0.7, -0.7); SmartDashboard.putString(
					 * "Angle Direction", "right"); } else if(Robot.gyro.getAngle()
					 * < forwardGyro_turnAngle + 3){ Robot.drivebase.drive(-.7, .7);
					 * //Robot.drivebase.drive(0.0, 0.0); SmartDashboard.putString(
					 * "Angle Direction", "left"); } else{
					 * Robot.drivebase.drive(0.0, 0.0); SmartDashboard.putNumber(
					 * "Gyro afterTurn", Robot.gyro.getAngle());
					 * SmartDashboard.putNumber("timeAfterTurn",
					 * System.currentTimeMillis()); //forwardGyro_shoot = true;
					 * SwitchCase.shotTheBall = false; }
					 *//*
				} else {
					Robot.drivebase.drive(0.0, 0.0);
					forwardGyro_shoot = true;
				}
			}
			*/
			break;
		case CrossMoatAndStop:
			// moatForward();
			moatForwardState = 1;
			break;
		case CrossRockWallAndStop:
			crossRockWall();
			break;
		case CrossLowbarAndShoot:
			LowbarShoot();
			break;
		case Spybox:
			spyboxShoot();
			break;
		case CrossRockwallAndShoot:
			shootRockWall();
			break;
		case CrossPortcullisAndShoot:
			PorticShoot();
			break;
		case DoNothing:
		default:
			Timer.delay(0.1);
			break;
		}
		touchForwardState = SwitchCase.driveForward(touchForwardState, 72);
		moatForwardState = moatForward(moatForwardState);
		driveForwardGyroState = SwitchCase.driveForwardGyro(driveForwardGyroState, distanceToCrossWork);
		// driveForwardState = SwitchCase.driveForward(driveForwardState);
		// autoAIMState = SwitchCase.autoAim(autoAIMState);

	}

}
