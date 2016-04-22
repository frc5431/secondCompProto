package org.usfirst.frc.team5431.robot;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import org.usfirst.frc.team5431.robot.driveBase;

public class autonPIDOutput implements PIDOutput{
	
	private double stall = 0.3;
	
	public void pidWrite(double output){
		
		if(driveBase.flag == driveBase.pidFlag.driving)
			driveBase.drive(output, driveBase.notPIDSideSpeed);
		else
		{
			if(output > 0.07 && output < stall)
			{
				driveBase.drive(stall, -stall);
			}
			else if(output < -0.07 && output > -stall)
			{
				driveBase.drive(-stall, stall);
			}
			else
				driveBase.drive(output, -output);
		}
			
		SmartDashboard.putNumber("PIDWriteOutput", output);
	}
}
