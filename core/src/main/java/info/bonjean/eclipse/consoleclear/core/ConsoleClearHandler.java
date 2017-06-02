/**
 * Copyright (C) 2017 Julien Bonjean <julien@bonjean.info>
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
package info.bonjean.eclipse.consoleclear.core;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.debug.internal.ui.views.console.ProcessConsole;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.TextConsole;

@SuppressWarnings("restriction")
public class ConsoleClearHandler extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IConsoleView consoleView = (IConsoleView) activePage.findView(IConsoleConstants.ID_CONSOLE_VIEW);
		if (consoleView == null)
			return null;

		IConsole console = consoleView.getConsole();
		if (console == null)
			return null;

		if (console instanceof ProcessConsole) {
			// if it is a process console, we might be able to close it.
			ProcessConsole processConsole = (ProcessConsole) console;
			if (processConsole.getProcess() == null || processConsole.getProcess().isTerminated()) {
				consoleManager.removeConsoles(new IConsole[] { processConsole });
				return null;
			}
		}

		if (!(console instanceof TextConsole))
			return null;

		TextConsole text = (TextConsole) console;
		text.clearConsole();

		return null;
	}
}
