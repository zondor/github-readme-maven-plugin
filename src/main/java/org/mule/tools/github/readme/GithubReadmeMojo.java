/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.github.readme;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Generates a README.md for the project.
 *
 * @goal generate
 * @phase install
 */
public class GithubReadmeMojo
        extends AbstractMojo {

    /**
     * Project being built.
     *
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;

    /**
     * The path where the README.md will be generated.
     *
     * @parameter expression="${githubReadme.sourceDirectory}" default-value="${project.basedir}/README.md"
     * @required
     */
    protected File sourceDirectory;

    /**
     * README sections to include.
     *
     * @parameter expression="${githubReadme.sections}"
     */
    protected List<String> sections;

    public void execute()
            throws MojoExecutionException {
        if (sourceDirectory.exists()) {
            try {
                FileUtils.copyFile(sourceDirectory, new File(sourceDirectory.getAbsolutePath() + ".bak"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.setProperty("classpath.resource.loader.path", "/");
        ve.init();

        VelocityContext context = new VelocityContext();
        Properties projectProperties = new Properties();
        projectProperties.put("name", project.getName());
        projectProperties.put("description", project.getDescription() == null ?
                "" : project.getDescription());
        projectProperties.put("developers", project.getDevelopers());
        projectProperties.put("licenses", project.getLicenses());
        context.put("project", projectProperties);
        context.put("sections", sections);
        Template template;

        try {
            template = ve.getTemplate("README.md.tmpl");
        } catch (ResourceNotFoundException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (ParseErrorException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (MethodInvocationException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(sourceDirectory);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        template.merge(context, fileWriter);

        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

}
