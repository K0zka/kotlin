/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.checkers;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/diagnostics/testsWithJava8")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class DiagnosticsWithJava8TestGenerated extends AbstractDiagnosticsWithFullJdkTest {
    public void testAllFilesPresentInTestsWithJava8() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8"), Pattern.compile("^(.+)\\.kt$"), true);
    }

    @TestMetadata("compiler/testData/diagnostics/testsWithJava8/annotations")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Annotations extends AbstractDiagnosticsWithFullJdkTest {
        public void testAllFilesPresentInAnnotations() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8/annotations"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("deprecatedRepeatable.kt")
        public void testDeprecatedRepeatable() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/annotations/deprecatedRepeatable.kt");
            doTest(fileName);
        }

        @TestMetadata("javaRepeatable.kt")
        public void testJavaRepeatable() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/annotations/javaRepeatable.kt");
            doTest(fileName);
        }

        @TestMetadata("javaRepeatableRetention.kt")
        public void testJavaRepeatableRetention() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/annotations/javaRepeatableRetention.kt");
            doTest(fileName);
        }

        @TestMetadata("javaUnrepeatable.kt")
        public void testJavaUnrepeatable() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/annotations/javaUnrepeatable.kt");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class DuplicateJvmSignature extends AbstractDiagnosticsWithFullJdkTest {
        public void testAllFilesPresentInDuplicateJvmSignature() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature/statics")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Statics extends AbstractDiagnosticsWithFullJdkTest {
            public void testAllFilesPresentInStatics() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature/statics"), Pattern.compile("^(.+)\\.kt$"), true);
            }

            @TestMetadata("jjk.kt")
            public void testJjk() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature/statics/jjk.kt");
                doTest(fileName);
            }

            @TestMetadata("jk.kt")
            public void testJk() throws Exception {
                String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/duplicateJvmSignature/statics/jk.kt");
                doTest(fileName);
            }
        }
    }

    @TestMetadata("compiler/testData/diagnostics/testsWithJava8/statics")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Statics extends AbstractDiagnosticsWithFullJdkTest {
        public void testAllFilesPresentInStatics() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8/statics"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("inheritanceStaticMethodFromInterface.kt")
        public void testInheritanceStaticMethodFromInterface() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/statics/inheritanceStaticMethodFromInterface.kt");
            doTest(fileName);
        }
    }

    @TestMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class TargetedBuiltIns extends AbstractDiagnosticsWithFullJdkTest {
        public void testAllFilesPresentInTargetedBuiltIns() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("concurrentMapRemove.kt")
        public void testConcurrentMapRemove() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns/concurrentMapRemove.kt");
            doTest(fileName);
        }

        @TestMetadata("forEachRemainingNullability.kt")
        public void testForEachRemainingNullability() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns/forEachRemainingNullability.kt");
            doTest(fileName);
        }

        @TestMetadata("mutableMapRemove.kt")
        public void testMutableMapRemove() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns/mutableMapRemove.kt");
            doTest(fileName);
        }

        @TestMetadata("removeIf.kt")
        public void testRemoveIf() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns/removeIf.kt");
            doTest(fileName);
        }

        @TestMetadata("stream.kt")
        public void testStream() throws Exception {
            String fileName = KotlinTestUtils.navigationMetadata("compiler/testData/diagnostics/testsWithJava8/targetedBuiltIns/stream.kt");
            doTest(fileName);
        }
    }
}
