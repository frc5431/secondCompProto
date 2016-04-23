package org.usfirst.frc.team5431.robot;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class autoAimPIDInput implements PIDSource{
	PIDSourceType filler = PIDSourceType.kDisplacement;
	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		SmartDashboard.putNumber("VisionFromCenter", -Vision.fromCenter);
		SmartDashboard.putNumber("VisionFromCenterAdjusted", -Vision.fromCenter + 32);
		//SmartDashboard.putNumber("Indicator", System.currentTimeMillis());
		return -Vision.fromCenter + 32;
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		filler = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return filler;
	}

}
