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
	private int touchForwardState = 0;
	private int moatForwardState = 0;
	public static boolean autoAIMState = false;
	public static int currAIM = 1;
	private long crossMoatTimer = 0;
	private double[] driveDistance = { 0, 0 };

	private final double[] speedToOuterWork = { 0.65, 0.65 }, speedToCross = { 0.6, 0.6 }, speedToCrossMoat = { 1, 1 },
			speedToCrossRock = { 1, 1 };

	private static final double distanceToOuterWork = 48, distanceToCrossWork = 130, // 128
			distanceToCrossRough = 130, curveAmount = 0.3;

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

	private void LowbarShoot() {
		if ((driveDistance[0] < distanceToCrossWork && driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			// curveFix(speedToOuterWork);
			Robot.drivebase.drive(-0.7, -0.70);
		} else if (!autoAIMState) {
			driveForwardState = 1;
			for (int time = 0; time < 35; time++) {
				Robot.drivebase.drive(0.8, -0.8);
				Timer.delay(0.005);
			}
			for (int time2 = 0; time2 < 35; time2++) {
				Robot.drivebase.drive(-0.7, -0.73);
				Timer.delay(0.005);
			}
			autoAIMState = true;
			Timer.delay(0.44);
			SwitchCase.moveAmount = 0.43;
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

	private void crossForward() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossWork || driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-0.60, -0.60);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
	}

	private void crossRockWall() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossWork || driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
	}

	private void crossRoughTerrain() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossRough || driveDistance[1] < distanceToCrossRough)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
	}

	private void shootRockWall() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < (distanceToCrossWork / 2) || driveDistance[1] < (distanceToCrossWork / 2))
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			Timer.delay(0.5);
			Robot.drivebase.drive(0, 0);
			driveForwardState = 1;
			if (!autoAIMState) {
				autoAIMState = true;
				Timer.delay(0.75);
				SwitchCase.moveAmount = 0.43;
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
			SwitchCase.moveAmount = 0.43;
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

	private void encoderUpdate() {
		driveDistance = Robot.drivebase.getEncDistance();
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

		switch (currentAuto) {
		case TouchOuterWork:
			touchForward();
			break;
		case CrossOuter:
			crossForward();
			break;
		case Moat:
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
		case RockwallShoot:
			shootRockWall();
			break;
		case DoNothing:
		default:
			Timer.delay(0.1);
			break;
		}
		touchForwardState = SwitchCase.driveForward(touchForwardState, 72);
		moatForwardState = moatForward(moatForwardState);
		// driveForwardState = SwitchCase.driveForward(driveForwardState);
		// autoAIMState = SwitchCase.autoAim(autoAIMState);
	}

}
