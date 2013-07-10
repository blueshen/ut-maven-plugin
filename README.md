##ut-maven-plugin


[![Build Status](https://travis-ci.org/blueshen/ut-maven-plugin.png?branch=master)](https://travis-ci.org/blueshen/ut-maven-plugin)

Maven Plugin for Generate Unit Test Code Template

>This is not aim to solve the whole unit test,but simple generate the neccessary code.

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
