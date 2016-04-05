package org.usfirst.frc.team5431.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.*;

public class ClimbChop {
//	static Compressor pcm;
	static DoubleSolenoid extendClimber, raiseClimber, choppers;
	static CANTalon winch;

	public ClimbChop() {

//		pcm = new Compressor(RobotMap.pcm);
//		pcm.setClosedLoopControl(false);
//		pcm.start();
//		if (!pcm.enabled()) {
//			SmartDashboard.putString("PCM State", "Not enabled");
//		} else {
//			SmartDashboard.putString("PCM State", "Enabled");
//		}
//		if (pcm.getCompressorNotConnectedFault()) {
//			SmartDashboard.putString("PCM State", "NOT CONNECTED");
//		}
		extendClimber = new DoubleSolenoid(RobotMap.climberExtend1, RobotMap.climberExtend2);
		raiseClimber = new DoubleSolenoid(RobotMap.climberRaise1, RobotMap.climberRaise2);
		choppers = new DoubleSolenoid(RobotMap.choppers1, RobotMap.choppers2);
		extendClimber.set(DoubleSolenoid.Value.kReverse);
		raiseClimber.set(DoubleSolenoid.Value.kReverse);
		choppers.set(DoubleSolenoid.Value.kForward);
		winch = new CANTalon(RobotMap.chopper);
	}
}
