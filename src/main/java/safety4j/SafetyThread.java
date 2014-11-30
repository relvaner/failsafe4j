/*
 * safety4j - Safety Library
 * Copyright (c) 2014, David A. Bauer
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package safety4j;

import java.util.UUID;

import safety4j.utils.TimeoutTimer;
import safety4j.utils.TimeoutTimerListener;
import tools4j.references.ReferenceBoolean;

public final class SafetyThread {

	public static boolean run(final String message, final Method method, final UUID uuid, int timeout) {
		final ReferenceBoolean result = new ReferenceBoolean(true);
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SafetyMethod.run(message, method, uuid);
			}
		});
		
		TimeoutTimer timeoutTimer = new TimeoutTimer(timeout, new TimeoutTimerListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void task() {
				result.setValue(false);
				
				if (message!=null)
					System.out.println(String.format("Method failed (timeout): %s (UUID: %s)", message, uuid.toString()));
				
				SafetyManager.getInstance().notifyTimeoutHandler(message, uuid);
				
				thread.stop();
			}
		});
		
		timeoutTimer.start();
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeoutTimer.interrupt();
		
		return result.getValue();
	}
	
	public static void run(final Method method, final UUID uuid, int timeout) {
		run(null, method, uuid, timeout);
	}
}
