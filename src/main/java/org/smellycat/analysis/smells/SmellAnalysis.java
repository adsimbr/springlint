package org.smellycat.analysis.smells;

import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.smellycat.analysis.smells.springmvc.controller.PromiscuousController;
import org.smellycat.architecture.Architecture;
import org.smellycat.domain.Repository;
import org.smellycat.domain.SmellyClass;

public class SmellAnalysis {

	private String projectPath;
	private PrintStream output;
	private Repository repo;
	private Parser parser;
	
	private static Logger log = Logger.getLogger(SmellAnalysis.class);
	private Architecture arch;

	public SmellAnalysis(Architecture arch, String projectPath, PrintStream output, Repository repo) {
		this.arch = arch;
		this.projectPath = projectPath;
		this.output = output;
		this.repo = repo;
	}

	public void run() {

		log.info("Starting the parse engine");
		parser = new Parser(projectPath);
		
		identifyRoles();
		searchSmells();
		printAttributes();
		
	}

	private void printAttributes() {
		log.info("Saving the results...");
		for(SmellyClass clazz : repo.all()) {
			output.println(
				clazz.getFile() + "," +
				clazz.getName() + "," +
				clazz.getRole() + "," +
				clazz.getAttribute("number-of-routes") + "," +
				clazz.getAttribute("number-of-services-as-dependencies")
			);
		}
	}

	private void searchSmells() {
		log.info("Identifying smells...");
		parser.execute(new SmellsRequestor(repo, new PromiscuousController()));
	}

	private void identifyRoles() {
		log.info("Identifying roles...");
		// TODO: android here too
		parser.execute(new ArchitecturalRoleRequestor(arch, repo));
	}	
	

}