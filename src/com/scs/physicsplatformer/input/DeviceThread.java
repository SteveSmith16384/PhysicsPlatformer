package com.scs.physicsplatformer.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;


import org.gamepad4j.ButtonID;
import org.gamepad4j.Controllers;
import org.gamepad4j.IController;

import com.scs.physicsplatformer.Statics;

import ssmith.lang.Functions;

public final class DeviceThread extends Thread {

	public static boolean USE_CONTROLLERS = true;

	private Map<Integer, IInputDevice> createdDevices = new HashMap<>();
	private IInputDevice keyboard1, keyboard2;
	private List<NewControllerListener> listeners = new ArrayList<>();
	private JFrame window;
	
	public DeviceThread(JFrame _window) {
		super(DeviceThread.class.getSimpleName());

		window = _window;
		
		this.setDaemon(true);

		try {
			Controllers.initialize();
			Runtime.getRuntime().addShutdownHook(new DeviceShutdownHook());
		} catch (Throwable ex) {
			//ex.printStackTrace();
			USE_CONTROLLERS = false;
			Statics.p("NOT using controllers: " + ex.getMessage());
		}

		keyboard1 = new KeyboardInput(window, 1);
		keyboard2 = new KeyboardInput(window, 2);

	}


	public Collection<IInputDevice> getDevices() {
		return this.createdDevices.values();
	}

	public void run() {
		try {
			while (window.isVisible()) {
				if (USE_CONTROLLERS) {
					IController[] gamepads = null;
					Controllers.checkControllers();
					gamepads = Controllers.getControllers();

					for (IController gamepad : gamepads) {
						if (gamepad.isButtonPressed(ButtonID.FACE_DOWN)) {
							synchronized (createdDevices) {
								if (!createdDevices.containsKey(gamepad.getDeviceID())) {
									this.createController(gamepad.getDeviceID(), new PS4Controller(gamepad));
								}
							}
						}
					}

				}
				if (keyboard1.isJumpPressed()) {
					synchronized (createdDevices) {
						if (createdDevices.get(-1) == null) {
							this.createController(-1, keyboard1);
						}
					}
				}
				if (keyboard2.isJumpPressed()) {
					synchronized (createdDevices) {
						if (createdDevices.get(-2) == null) {
							this.createController(-2, keyboard2);
						}
					}
				}
				Functions.delay(500);
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private void createController(int id, IInputDevice input) {
		synchronized (createdDevices) {
			if (Statics.DEBUG) {
				Statics.p("Devices: " + this.createdDevices.keySet());
				Statics.p("Creating new device " + id);
			}
			createdDevices.put(id, input);
		}

		synchronized (listeners) {
			for (NewControllerListener l : this.listeners) {
				l.newController(input);
			}
		}
	}


	public void addListener(NewControllerListener l) {
		synchronized (listeners) {
			this.listeners.add(l);
		}		
	}
}
