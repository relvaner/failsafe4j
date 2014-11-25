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
package safety4j.examples;

import java.util.UUID;

import safety4j.ErrorHandler;
import safety4j.Method;
import safety4j.SafetyManager;
import safety4j.SafetyMethod;


public class Examples01 {

	public Examples01() {
		SafetyManager.getInstance().setErrorHandler(new ErrorHandler() {
			@Override
			public void handle(Exception e, String message, UUID uuid) {
				System.out.println("Exception: "+e.toString());
				System.out.println("Message: "+message);
				System.out.println("UUID: "+uuid.toString());
			}
		});
		
		SafetyMethod.run("Methode 1", new Method() {
			@Override
			public void run() {
				@SuppressWarnings("unused")
				int z = 67 / 0;
			}

			@Override
			public void error(Exception e) {
				System.out.println(e.getMessage());
			}
			
			@Override
			public void after() {
				System.out.println("Hello World!");
			}
		}, UUID.randomUUID());
		
		System.out.println("YES!");
	}
	
	public static void main(String[] args) {
		new Examples01();
	}
}
