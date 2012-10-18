# Github README Maven Plugin
Generates a README.md out of a Maven Project

## Build

    mvn clean install

## Usage

Add the following plugin to your pom.xml in the build -> plugins section:
```xml
 <plugin>
    <groupId>org.mule.tools</groupId>
    <artifactId>github-readme-maven-plugin</artifactId>
    <version>@project.version@</version>
    <executions>
        <execution>
            <phase>install</phase>
            <goals>
                <goal>generate-readme</goal>
            </goals>
            <configuration>
                <sections>
                    <section><![CDATA[# This a new section that will be added to the README.md]]</section>
                </sections>
            </configuration>
        </execution>
    </executions>
  <plugin>
```

 So that will generate the README.md in the root folder of the project with the information that can be extracted from the following tags:

  * name
  * description
  * licenses
  * developers

among others.

## Authors
  * Alberto Pose

## License
  * CPAL v1.0

Happy hacking!
