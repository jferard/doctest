/* 
This file is part of Java DocTest Maven Plugin.
	Copyright (C) 2018 Julien Férard

Java DocTest Maven Plugin is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Java DocTest Maven Plugin is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Java DocTest Maven Plugin.  If not, see <https://www.gnu.org/licenses/>. 
*/
package com.github.jferard.doctest;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import java.nio.file.Paths;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;

/**
 * A DocTest Mojo. Bootstrap for a custom doclet.
 */
@org.apache.maven.plugins.annotations.Mojo(name = "doctest",
        requiresDependencyResolution = ResolutionScope.TEST)
public class DocTestMojo extends AbstractMojo {
    @Parameter(property = "sourceDirectory", defaultValue = "${project.build.sourceDirectory}",
            required = true)
    String sourceDirectory;

    @Parameter(property = "buildDirectory", defaultValue = "${project.build.directory}",
            required = true)
    String buildDirectory;

    @Parameter(property = "generatedTestsDirectory", defaultValue = "generated-sources",
            required = true)
    String generatedTestsDirectory;

    @Parameter(readonly = true, defaultValue = "${project}")
    private MavenProject mavenProject;

    @Parameter(readonly = true, defaultValue = "${session}")
    private MavenSession mavenSession;

    @Parameter(readonly = true, defaultValue = "${mojoExecution}")
    private MojoExecution mojoExecution;

    @Component
    private BuildPluginManager pluginManager;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        String outputDir = Paths.get(buildDirectory, generatedTestsDirectory).toAbsolutePath()
                .toString();
        this.mavenProject.addTestCompileSourceRoot(outputDir);
        getLog().info("DocTest output dir: " + outputDir);
        executeMojo(plugin(groupId("org.apache.maven.plugins"), artifactId("maven-javadoc-plugin"),
                version("3.0.1")), goal("javadoc"), getConf(),
                executionEnvironment(mavenProject, mavenSession, pluginManager));
    }

    private Xpp3Dom getConf() {
        Xpp3Dom configuration = configuration(
                MojoExecutor.element("doclet", ExtractDocTestDoclet.class.getName()), MojoExecutor
                        .element("docletArtifact",
                                MojoExecutor.element("groupId", "com.github.jferard"),
                                MojoExecutor.element("artifactId", "doctest-maven-plugin"),
                                MojoExecutor.element("version", "0.0.1-SNAPSHOT")),
                MojoExecutor.element("reportOutputDirectory", "${project.build.directory}"),
                MojoExecutor.element("destDir", "generated-sources"),
                MojoExecutor.element("useStandardDocletOptions", "false"),
                MojoExecutor.element("javadocExecutable", "${java.home}/bin/javadoc"));
        return configuration;
    }
}