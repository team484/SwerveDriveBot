
package org.usfirst.frc.team484.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team484.robot.commands.ExampleCommand;
import org.usfirst.frc.team484.robot.subsystems.ExampleSubsystem;

public class Robot extends IterativeRobot {
	public static BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();
	public static double posX = 0.0;
	public static double posY = 0.0;
	public static double velX = 0.0;
	public static double velY = 0.0;
	public static double accelX = 0.0;
	public static double accelY = 0.0;
	public static boolean isZeroing = false;
	public static double startTime = 0.0;
	public static int state = 1; //0 - test wheel rotation 1 - if wheels rotated couter-clockwise 2 - if wheels rotated clockwise
	public static boolean twistyStick = true; //Set to true if a stick with the ability to twist is plugged in
	public static double kP = 0.02;
	public static double kI = 0.0;
	public static double kD = 0.0;
	public static AnalogInput ir = new AnalogInput(2);
	public static boolean wasTrigger = false;
	public static double startAngle = 0.0;
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	public static Talon spFL = new Talon(4); 
	public static Talon spRL = new Talon(5);
	public static Talon spFR = new Talon(6);
	public static Talon spRR = new Talon(7);
	
	public static Talon iTransFL = new Talon(0);
	public static Talon iTransRL = new Talon(1);
	public static Talon iTransFR = new Talon(2);
	public static Talon iTransRR = new Talon(3);
	
	public static Encoder iEncFL = new Encoder(0, 1);
	public static Encoder iEncRL = new Encoder(2, 3);
	public static Encoder iEncFR = new Encoder(4, 5);
	public static Encoder iEncRR = new Encoder(6, 7);
	public static SwerveDrive sDrive;
	public static Joystick stick0 = new Joystick(0);
	public static Joystick stick1;
	
	public static AnalogGyro topGyro = new AnalogGyro(0);
	public static AnalogGyro bottomGyro = new AnalogGyro(1);
	
    Command autonomousCommand;
    double vel = 0.0;
    double dist = 0.0;
    public void robotInit() {
    	iEncFL.setDistancePerPulse(0.86694762);
    	iEncRL.setDistancePerPulse(0.86694762);
    	iEncFR.setDistancePerPulse(0.86694762);
    	iEncRR.setDistancePerPulse(0.86694762);
    	if (!twistyStick) {
    		stick1 = new Joystick(1);
    	}
		oi = new OI();
		if (state == 1) {
			sDrive = new SwerveDrive(kP, kI, kD, iEncFL, iEncRL, iEncFR, iEncRR, spFL, spRL, spFR, spRR, iTransFL, iTransRL, iTransFR, iTransRR, false);
		} else if (state == 2) {
			sDrive = new SwerveDrive(kP, kI, kD, iEncFL, iEncRL, iEncFR, iEncRR, spFL, spRL, spFR, spRR, iTransFL, iTransRL, iTransFR, iTransRR, true);
		}
		sDrive.setWheelbaseDimensions(21.0, 22.0);
		// instantiate the command used for the autonomous period
        autonomousCommand = new ExampleCommand();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        stage = 1;
    }

    /**
     * This function is called periodically during autonomous
     */
    public int stage = 1;
    public int counter = 0;
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        if (stage == 1) {
        	sDrive.drive(0, 0.2, 0);
        	if (ir.getAverageVoltage() > 1.7) {
        		stage = 2;
        		counter = 0;
        	}
        } else if (stage == 2 && counter < 100) {
        	counter++;
        	sDrive.drive(90, 0.2, 0);
        } else {
        	sDrive.drive(0, 0, 0);
        }
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        sDrive.enablePID();
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
    	if (!isZeroing && stick0.getRawButton(11)) {
    		isZeroing = true;
    		accelX = 0;
    		accelY = 0;
    		startTime = 0;
    		posX = 0;
    		posY = 0;
    		velX = 0;
    		velY = 0;
    	} else if (isZeroing && stick0.getRawButton(11)) {
    		accelX += accelerometer.getX();
    		accelY += accelerometer.getY();
    		startTime++;
    	} else if (isZeroing && !stick0.getRawButton(11)) {
    		accelX /= startTime;
    		accelY /= startTime;
    		isZeroing = false;
    	} else {
    		posX += velX / 50.0;
    		posY += velY / 50.0;
    		System.out.println("X: " + posX + " Y: " + posY);
    	}
        Scheduler.getInstance().run();
        if (stick0.getRawButton(2)) {
        	sDrive.setupWeels();
        } else {
        if (!wasTrigger && stick0.getTrigger()) {
        	wasTrigger = true;
        	startAngle = getBotAngle();
        	
        }
        if (!stick0.getTrigger()) {
        	wasTrigger = false;
        }
        if (state == 1 || state == 2) {
        	if (twistyStick) {
        			if (stick0.getTrigger()) {
        				sDrive.drive(stick0.getDirectionDegrees() + (startAngle - getBotAngle()), stick0.getMagnitude(), -stick0.getTwist()*stick0.getTwist()*stick0.getTwist()/Math.abs(stick0.getTwist()));
        			} else {
        				sDrive.drive(stick0.getDirectionDegrees(), stick0.getMagnitude(), -stick0.getTwist()*stick0.getTwist()*stick0.getTwist()/Math.abs(stick0.getTwist()));
        			}
        		} else {
        		sDrive.drive(stick0.getDirectionDegrees(), stick0.getMagnitude(), stick1.getX());
        	}
        } else {
        	spFL.set(0.5);
        	spRL.set(0.5);
        	spFR.set(0.5);
        	spRR.set(0.5);
        }
        }
       
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    public double getBotAngle() {
    	return (topGyro.getAngle() - bottomGyro.getAngle()) / 2.0;
    }
}
