
package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team484.robot.commands.ExampleCommand;
import org.usfirst.frc.team484.robot.subsystems.ExampleSubsystem;

public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	public static Jaguar spFL = new Jaguar(4); 
	public static Jaguar spRL = new Jaguar(5);
	public static Jaguar spFR = new Jaguar(6);
	public static Jaguar spRR = new Jaguar(7);
	
	public static Jaguar iTransFL = new Jaguar(0);
	public static Jaguar iTransRL = new Jaguar(1);
	public static Jaguar iTransFR = new Jaguar(2);
	public static Jaguar iTransRR = new Jaguar(3);
	
	public static Encoder iEncFL = new Encoder(0, 1);
	public static Encoder iEncRL = new Encoder(2, 3);
	public static Encoder iEncFR = new Encoder(4, 5);
	public static Encoder iEncRR = new Encoder(6, 7);
	public static SwerveDrive sDrive;
	public static Joystick stick0 = new Joystick(0);
	public static Joystick stick1 = new Joystick(1);

    Command autonomousCommand;

    public void robotInit() {
    	iEncFL.setDistancePerPulse(0.86694762);
    	iEncRL.setDistancePerPulse(0.86694762);
    	iEncFR.setDistancePerPulse(0.86694762);
    	iEncRR.setDistancePerPulse(0.86694762);
		oi = new OI();
		sDrive = new SwerveDrive(0.2, 0.0, 0.0, iEncFL, iEncRL, iEncFR, iEncRR, spFL, spRL, spFR, spRR, iTransFL, iTransRL, iTransFR, iTransRR, true);
		sDrive.setWheelbaseDimensions(15.0, 30.0);
		// instantiate the command used for the autonomous period
        autonomousCommand = new ExampleCommand();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        sDrive.drive(stick0.getDirectionDegrees(), stick0.getMagnitude(), -stick1.getX());
        System.out.println("FL: " + sDrive.getPIDError(SwerveDrive.MotorType.kFrontLeft));
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
