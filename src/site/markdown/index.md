##ut-maven-plugin


[![Build Status](https://travis-ci.org/blueshen/ut-maven-plugin.png?branch=master)](https://travis-ci.org/blueshen/ut-maven-plugin)

Maven Plugin for Generate Unit Test Code Template

>This tool is not designed to solve the problem of the whole unit testing, but simply to generate the necessary test code, and  improve the efficiency of write unit tests.

###Usage:

    <plugin>
        <groupId>cn.shenyanchao.ut</groupId>
        <artifactId>ut-maven-plugin</artifactId>
        <version>0.2.9</version>
        <executions>
            <execution>
                <id>source2test</id>
                <phase>process-test-sources</phase>
                <goals>
                    <goal>source2test</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

###Run from Source

    mvn clean install


###Debug from Source

    mvn clean integration-test