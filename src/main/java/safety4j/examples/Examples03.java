/*
 * safety4j - Safety Library
 * Copyright (c) 2014-2017, David A. Bauer
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
package safety4j.examples;

import java.util.UUID;

import safety4j.ErrorHandler;
import safety4j.Method;
import safety4j.SafetyManager;
import safety4j.SafetyMethod;
import safety4j.SafetyThread;
import safety4j.TimeoutHandler;

public class Examples03 {
	
	public Examples03() {
		final SafetyManager safetyManager = new SafetyManager();
		
		final Method method = new Method() {
			@Override
			public void run(UUID uuid) {
				/*
				@SuppressWarnings("unused")
				int z = 67 / 0;
				*/
				boolean success = SafetyThread.run(safetyManager, "Method1.Block1", new Method() {
					@Override
					public void run(UUID uuid) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void error(Throwable t) {
					}

					@Override
					public void after() {
					}
				}, uuid, 1000);
				
				if (!success)
					System.out.println("Thread failed!");
			}

			@Override
			public void error(Throwable t) {
				System.out.println(t.getMessage());
			}
			
			@Override
			public void after() {
				System.out.println("Hello World!");
			}
		};
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SafetyMethod.run(safetyManager, "Methode 1", method, UUID.randomUUID()); 
			}
		});
		
		safetyManager.setErrorHandler(new ErrorHandler() {
			@Override
			public void handle(Throwable t, String message, UUID uuid) {
				System.out.println(String.format("ErrorHandler - Exception: %s - %s (UUID=%s)", t.toString(), message, uuid.toString()));
			}
		});
		safetyManager.setTimeoutHandler(new TimeoutHandler() {
			@Override
			public void handle(String message, UUID uuid) {
				System.out.println(String.format("TimeoutHandler - %s (UUID=%s)", message, uuid.toString()));
			}
		});
		
		thread.start();
	}
	
	public static void main(String[] args) {
		new Examples03();
	}
}
