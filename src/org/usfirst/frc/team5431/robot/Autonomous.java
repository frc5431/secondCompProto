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
	private static double[] driveDistance = { 0, 0 };

	private final double[] speedToOuterWork = { 0.65, 0.65 }, speedToCrossMoat = { 1, 1 };

	private static final double distanceToOuterWork = 48, distanceToCrossWork = 150, // 128
			distanceToCrossRough = 130;

	
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

	private void LowbarShoot() {
		if((driveDistance[0] < distanceToCrossWork && driveDistance[1] < distanceToCrossWork) && driveForwardState == 0) {
			//curveFix(speedToOuterWork);
			Robot.drivebase.drive(-0.70, -0.73);
		} else if(!autoAIMState) {
			driveForwardState = 1;
    		for(int time = 0; time < 35; time++) {
    			Robot.drivebase.drive(0.8, -0.8);
    			Timer.delay(0.005); 
    		}
    		for(int time2 = 0; time2 < 35; time2++) {
    			Robot.drivebase.drive(-0.6, -0.6);
    			Timer.delay(0.005); 
    		}
	    	autoAIMState = true;
	    	Timer.delay(0.75);
	    	SwitchCase.moveAmount = 0.43;
	    	SwitchCase.checkAmount = 1;
	    	SwitchCase.shotTheBall = false;
	    	currAIM = SwitchCase.autoAim(currAIM);
		}
		if(autoAIMState) {
			SmartDashboard.putString("READY READY READY", "Auto aiming");
			currAIM = SwitchCase.autoAim(currAIM);
			if((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) { currAIM = 1; }
		}
		
	}

	private void crossForward() {
		driveDistance = Robot.drivebase.getEncDistance();
		if((driveDistance[0] < distanceToCrossWork && driveDistance[1] < distanceToCrossWork) && driveForwardState == 0) {
			//curveFix(speedToOuterWork);
			Robot.drivebase.drive(-0.70, -0.73);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
		
		/*
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossWork || driveDistance[1] < distanceToCrossWork)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-0., -0.60);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}*/
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

	/*private void crossRoughTerrain() {
		driveDistance = Robot.drivebase.getEncDistance();
		if ((driveDistance[0] < distanceToCrossRough || driveDistance[1] < distanceToCrossRough)
				&& driveForwardState == 0) {
			Robot.drivebase.drive(-1, -1);
		} else {
			Robot.drivebase.drive(0, 0);
			Robot.drivebase.resetDrive();
			driveForwardState = 1;
		}
	}*/

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
	/*
	public static void processAutonSteps() {
		final double[] defaults = {0, 30, 0};
		final double[] stepAuton = new double[50];
		final double[] current = SmarterDashboard.getNumberArray("AUTO-STEP-SELECT", defaults);
		
		for(int am = 0; am < current.length; am++)
			stepAuton[am] = current[am];
		
		final FirstMove selectedCross = FirstMove.values()[(int) stepAuton[0]];
		//good joy msn
		final AutoShoot selectedShoot = AutoShoot.values()[(int) stepAuton[3]];
		
		switch(selectedCross) {
			default :
				break;
			case StandStill:
				drive(0, 0.1, 0.1, 1);
				return; //Just stop bro....
			case TouchOuterWork:
				drive(distanceToOuterWork, 0.75, 0.02, 10); //Just touch it dude
				return;
			case Lowbar:
				drive(distanceToCrossWork, 0.69, 0.02, 8); //Drive slowly
				break;
			case RockWall:
				drive(distanceToCrossWork, 1, 9, false); //Doesn't matter to fix
				break;
			case RoughTerrain:
				drive(distanceToCrossRough, 0.9, 0.02, 8);
				break;
			case Portcullis:
				fallChopper();
				Timer.delay(0.1);
				drive(distanceToCrossWork, 0.7, 0.02, 8);
				Timer.delay(0.1);
				liftChopper();
				break;
			case ChevalDeFrise:
				liftChopper();
				Timer.delay(0.01);
				drive(distanceToOuterWork, 0.6, 0.02, 6);
				Timer.delay(0.1);
				fallChopper();
				Timer.delay(1.4);
				drive(distanceToOuterWork - 10, 0.8, 0.02, 5);
				break;
			case Moat:
				liftChopper();
				Timer.delay(0.01);
				drive(distanceToCrossWork, -1, 6, false);
				turnPivot(180, 0.75, 3);
				break;
		}
		
		//Turn if you want
		turnPivot((int) stepAuton[1], 0.69, 5);
		
		//Drive if you want
		drive(stepAuton[2], 0.5, 0.05, 5);
		
		//Do you autoShoot?
		
		switch(selectedShoot) {
			default:
				break;
			case True:
				autoAim(9);
				break;
			case False:
				Robot.drivebase.drive(0, 0);
				break;
		}
		
		for(int amount = 0; amount < stepAuton.length; amount++) {
			if(stepAuton[amount] == 0) break;
		}
		
	}
	
	private static void liftChopper() {
		Robot.drivebase.chopperUp();
	}
	
	private static void fallChopper() {
		Robot.drivebase.chopperDown();
	}
	
	private static void autoAim(double timeoutSecs) {
		Robot.drivebase.enableBrakeMode();
		
		final double timeoutSystem = System.currentTimeMillis() + (timeoutSecs * 1000);
		SwitchCase.shotTheBall = false;
		
		while(System.currentTimeMillis() < timeoutSystem) {
			currAIM = SwitchCase.autoAim(currAIM);
			if ((currAIM == 0 || currAIM == -1) && !SwitchCase.shotTheBall) {
				currAIM = 1;
			}
			Timer.delay(0.005);
		}
		SwitchCase.shotTheBall = false;
		Robot.drivebase.drive(0, 0);
		Timer.delay(0.01);
		Robot.drivebase.disableBrakeMode();
	}
	
	private static void drive(double distance, double speeds, double timeoutSecs, boolean matter) {
		drive(distance, speeds, 0, timeoutSecs, matter);
	}
	
	private static void drive(double distance, double speeds, double curveFix,  double timeoutSecs) {
		drive(distance, speeds, curveFix, timeoutSecs, true);
	}
	
	private static void drive(double distance, double speeds, double curveFix, double timeoutSecs, boolean matter) {
		if(distance == 0) {Robot.drivebase.drive(0, 0); return;}
		final boolean direction = (distance < 0);
		Robot.drivebase.resetDrive();
		final double timeoutSystem = System.currentTimeMillis() + (timeoutSecs * 1000);
		Robot.drivebase.enableBrakeMode();
		Timer.delay(0.01);
		
		distance = direction ? -(distance) : distance;
		
		while(System.currentTimeMillis() < timeoutSystem && !matter) {
			encoderUpdate();
			if(driveDistance[0] < distance && driveDistance[1] < distance) {
				Robot.drivebase.drive(-speeds, -speeds);
			} else {
				Robot.drivebase.drive(0, 0);
				break;
			}
			Timer.delay(0.005);
		}
		
		while(System.currentTimeMillis() < timeoutSystem && matter) {
			encoderUpdate();
			
			final double toDrive[] = { 0, 0 };
			final double fixCurve = (curveFix * (driveDistance[0] - driveDistance[1]));
			toDrive[0] = speeds + fixCurve;
			toDrive[1] = speeds + fixCurve;
			toDrive[0] = (direction) ? -toDrive[0] : toDrive[0];
			toDrive[1] = (direction) ? -toDrive[1] : toDrive[1];
			if(driveDistance[0] < distance) {
				Robot.drivebase.drive(-toDrive[0], -toDrive[1]);
			} else {
				Robot.drivebase.drive(0, 0);
				break;
			}
			Timer.delay(0.005);
		}
		Robot.drivebase.drive(0, 0);
		Timer.delay(0.01);
		Robot.drivebase.disableBrakeMode();
	}
	
	private static void turnPivot(int degrees, double speed, double timeoutSecs) {
		Robot.drivebase.resetDrive();
		double distance = (23.75 * Math.PI * (degrees / 2)) / 360;//Calculate distance needed to turn
		final boolean direction = degrees < 0;//false is to the right, true is to the left
		if(degrees == 0) return;
		double timeoutSystem = System.currentTimeMillis() + (timeoutSecs * 1000);
		Robot.drivebase.enableBrakeMode();
		Timer.delay(0.01);
		//distance = direction ? -(distance) : distance;

		while(System.currentTimeMillis() < timeoutSystem){
			encoderUpdate(); //Update encoders
			
			boolean Lpass = false, Rpass = false;
						
			if(driveDistance[0] < distance) {
				Robot.drivebase.setLeft(-1);//(direction) ? speed : -speed); // negative is forward ps Usaid
			} else {
				Robot.drivebase.setLeft(0.01);
				Lpass = true;
			}
			
			if(driveDistance[1] > distance) {
				Robot.drivebase.setRight(1);//(direction) ? speed : -speed);
				Rpass = true;
			} else {
				Robot.drivebase.setLeft(-0.01);
			}
			
			if(Lpass && Rpass) {Timer.delay(0.005); break;}
			
			Timer.delay(0.005); //Motor update period (50Hz)
		}
		Robot.drivebase.drive(0, 0);
		Timer.delay(0.01);
		Robot.drivebase.disableBrakeMode();
	}*/

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

	private static void encoderUpdate() {
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
