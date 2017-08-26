package com.gnif.plugin.gzip_maven_plugin;

import java.util.Arrays;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.gnif.plugin.gzip_maven_plugin.utl.Util;

@Mojo(name="gzip")
public class Gzip extends AbstractMojo {

	@Parameter(defaultValue="")
	private String path;
	
	@Parameter(defaultValue="")
	private String pattern;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		String[] patterns = pattern.split(",");
		String[] resourcePaths = path.split(",");
		Util.gzipResource(Arrays.asList(resourcePaths), Arrays.asList(patterns));
	}

}
