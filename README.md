##ut-maven-plugin


[![Build Status](https://travis-ci.org/blueshen/ut-maven-plugin.png?branch=master)](https://travis-ci.org/blueshen/ut-maven-plugin)

Maven Plugin for Generate Unit Test Code Framework

###Usage:
    <plugin>
        <groupId>cn.shenyanchao.ut</groupId>
        <artifactId>ut-maven-plugin</artifactId>
        <version>0.2</version>
        <executions>
            <execution>
                <id>source2test</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>source2test</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
