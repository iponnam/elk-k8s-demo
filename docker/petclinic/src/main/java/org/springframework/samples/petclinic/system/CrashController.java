/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import io.micrometer.core.annotation.Timed;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {

	@Timed
  @GetMapping("/oups")
  public String triggerException() {
      throw new RuntimeException("Expected: controller used to showcase what "
              + "happens when an exception is thrown");
  }

	@Timed
  @GetMapping("/oom")
	public String generateOOM() throws Exception {
		int iteratorValue = 20;
		// System.out.println("\n=================> OOM test started..\n");
		for (int outerIterator = 1; outerIterator < 20; outerIterator++) {
			// System.out.println("Iteration " + outerIterator + " Free Mem: " + Runtime.getRuntime().freeMemory());
			int loop1 = 2;
			int[] memoryFillIntVar = new int[iteratorValue];
			// feel memoryFillIntVar array in loop..
			do {
				memoryFillIntVar[loop1] = 0;
				loop1--;
			} while (loop1 > 0);
			iteratorValue = iteratorValue * 5;
			// System.out.println("\nRequired Memory for next loop: " + iteratorValue);
			Thread.sleep(1000);
		}

		return "/";
	}

  @ModelAttribute("transaction")
  public Transaction transaction() {
      return ElasticApm.currentTransaction();
  }

  @ModelAttribute("apmServer")
  public String apmServer() {
      return System.getenv("ELASTIC_APM_SERVER_URLS_FOR_RUM");
  }
}
