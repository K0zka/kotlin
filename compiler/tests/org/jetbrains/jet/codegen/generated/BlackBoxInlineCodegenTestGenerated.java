/*
 * Copyright 2010-2014 JetBrains s.r.o.
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

package org.jetbrains.jet.codegen.generated;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.regex.Pattern;
import org.jetbrains.jet.JetTestUtils;
import org.jetbrains.jet.test.InnerTestClasses;
import org.jetbrains.jet.test.TestMetadata;

import org.jetbrains.jet.codegen.generated.AbstractBlackBoxCodegenTest;

/** This class is generated by {@link org.jetbrains.jet.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/boxInline")
@InnerTestClasses({BlackBoxInlineCodegenTestGenerated.AnonymousObject.class, BlackBoxInlineCodegenTestGenerated.Builders.class, BlackBoxInlineCodegenTestGenerated.Capture.class, BlackBoxInlineCodegenTestGenerated.Complex.class, BlackBoxInlineCodegenTestGenerated.DefaultValues.class, BlackBoxInlineCodegenTestGenerated.LambdaClassClash.class, BlackBoxInlineCodegenTestGenerated.LambdaTransformation.class, BlackBoxInlineCodegenTestGenerated.LocalFunInLambda.class, BlackBoxInlineCodegenTestGenerated.NoInline.class, BlackBoxInlineCodegenTestGenerated.NonLocalReturns.class, BlackBoxInlineCodegenTestGenerated.Simple.class, BlackBoxInlineCodegenTestGenerated.Special.class, BlackBoxInlineCodegenTestGenerated.Trait.class, BlackBoxInlineCodegenTestGenerated.TryCatchFinally.class})
public class BlackBoxInlineCodegenTestGenerated extends AbstractBlackBoxCodegenTest {
    public void testAllFilesPresentInBoxInline() throws Exception {
        JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline"), Pattern.compile("^(.+)\\.1.kt$"), true);
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/anonymousObject")
    public static class AnonymousObject extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInAnonymousObject() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/anonymousObject"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("anonymousObjectOnCallSite.1.kt")
        public void testAnonymousObjectOnCallSite() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/anonymousObject/anonymousObjectOnCallSite.1.kt");
        }
        
        @TestMetadata("anonymousObjectOnCallSiteSuperParams.1.kt")
        public void testAnonymousObjectOnCallSiteSuperParams() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/anonymousObject/anonymousObjectOnCallSiteSuperParams.1.kt");
        }
        
        @TestMetadata("anonymousObjectOnDeclarationSite.1.kt")
        public void testAnonymousObjectOnDeclarationSite() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/anonymousObject/anonymousObjectOnDeclarationSite.1.kt");
        }
        
        @TestMetadata("anonymousObjectOnDeclarationSiteSuperParams.1.kt")
        public void testAnonymousObjectOnDeclarationSiteSuperParams() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/anonymousObject/anonymousObjectOnDeclarationSiteSuperParams.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/builders")
    public static class Builders extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInBuilders() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/builders"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("builders.1.kt")
        public void testBuilders() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/builders/builders.1.kt");
        }
        
        @TestMetadata("buildersAndLambdaCapturing.1.kt")
        public void testBuildersAndLambdaCapturing() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/builders/buildersAndLambdaCapturing.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/capture")
    public static class Capture extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInCapture() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/capture"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("captureInlinable.1.kt")
        public void testCaptureInlinable() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/captureInlinable.1.kt");
        }
        
        @TestMetadata("captureInlinableAndOther.1.kt")
        public void testCaptureInlinableAndOther() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/captureInlinableAndOther.1.kt");
        }
        
        @TestMetadata("captureThisAndReceiver.1.kt")
        public void testCaptureThisAndReceiver() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/captureThisAndReceiver.1.kt");
        }
        
        @TestMetadata("generics.1.kt")
        public void testGenerics() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/generics.1.kt");
        }
        
        @TestMetadata("simpleCapturingInClass.1.kt")
        public void testSimpleCapturingInClass() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/simpleCapturingInClass.1.kt");
        }
        
        @TestMetadata("simpleCapturingInPackage.1.kt")
        public void testSimpleCapturingInPackage() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/capture/simpleCapturingInPackage.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/complex")
    public static class Complex extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInComplex() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/complex"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("closureChain.1.kt")
        public void testClosureChain() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/complex/closureChain.1.kt");
        }
        
        @TestMetadata("forEachLine.1.kt")
        public void testForEachLine() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/complex/forEachLine.1.kt");
        }
        
        @TestMetadata("lambdaInLambda.1.kt")
        public void testLambdaInLambda() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/complex/lambdaInLambda.1.kt");
        }
        
        @TestMetadata("use.1.kt")
        public void testUse() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/complex/use.1.kt");
        }
        
        @TestMetadata("with.1.kt")
        public void testWith() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/complex/with.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/defaultValues")
    public static class DefaultValues extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInDefaultValues() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/defaultValues"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("defaultMethod.1.kt")
        public void testDefaultMethod() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/defaultValues/defaultMethod.1.kt");
        }
        
        @TestMetadata("inlineInDefaultParameter.1.kt")
        public void testInlineInDefaultParameter() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/defaultValues/inlineInDefaultParameter.1.kt");
        }
        
        @TestMetadata("simpleDefaultMethod.1.kt")
        public void testSimpleDefaultMethod() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/defaultValues/simpleDefaultMethod.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/lambdaClassClash")
    public static class LambdaClassClash extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInLambdaClassClash() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/lambdaClassClash"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("lambdaClassClash.1.kt")
        public void testLambdaClassClash() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaClassClash/lambdaClassClash.1.kt");
        }
        
        @TestMetadata("noInlineLambdaX2.1.kt")
        public void testNoInlineLambdaX2() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaClassClash/noInlineLambdaX2.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/lambdaTransformation")
    public static class LambdaTransformation extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInLambdaTransformation() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/lambdaTransformation"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("lambdaCloning.1.kt")
        public void testLambdaCloning() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaTransformation/lambdaCloning.1.kt");
        }
        
        @TestMetadata("lambdaInLambda2.1.kt")
        public void testLambdaInLambda2() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaTransformation/lambdaInLambda2.1.kt");
        }
        
        @TestMetadata("lambdaInLambdaNoInline.1.kt")
        public void testLambdaInLambdaNoInline() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaTransformation/lambdaInLambdaNoInline.1.kt");
        }
        
        @TestMetadata("regeneratedLambdaName.1.kt")
        public void testRegeneratedLambdaName() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaTransformation/regeneratedLambdaName.1.kt");
        }
        
        @TestMetadata("sameCaptured.1.kt")
        public void testSameCaptured() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/lambdaTransformation/sameCaptured.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/localFunInLambda")
    public static class LocalFunInLambda extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInLocalFunInLambda() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/localFunInLambda"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("localFunInLambda.1.kt")
        public void testLocalFunInLambda() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/localFunInLambda/localFunInLambda.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/noInline")
    public static class NoInline extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInNoInline() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/noInline"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("noInline.1.kt")
        public void testNoInline() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/noInline/noInline.1.kt");
        }
        
        @TestMetadata("noInlineLambdaChain.1.kt")
        public void testNoInlineLambdaChain() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/noInline/noInlineLambdaChain.1.kt");
        }
        
        @TestMetadata("noInlineLambdaChainWithCapturedInline.1.kt")
        public void testNoInlineLambdaChainWithCapturedInline() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/noInline/noInlineLambdaChainWithCapturedInline.1.kt");
        }
        
        @TestMetadata("withoutInline.1.kt")
        public void testWithoutInline() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/noInline/withoutInline.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/nonLocalReturns")
    @InnerTestClasses({NonLocalReturns.Deparenthesize.class, NonLocalReturns.TryFinally.class})
    public static class NonLocalReturns extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInNonLocalReturns() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/nonLocalReturns"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("explicitLocalReturn.1.kt")
        public void testExplicitLocalReturn() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/explicitLocalReturn.1.kt");
        }
        
        @TestMetadata("justReturnInLambda.1.kt")
        public void testJustReturnInLambda() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/justReturnInLambda.1.kt");
        }
        
        @TestMetadata("nestedNonLocals.1.kt")
        public void testNestedNonLocals() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/nestedNonLocals.1.kt");
        }
        
        @TestMetadata("noInlineLocalReturn.1.kt")
        public void testNoInlineLocalReturn() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/noInlineLocalReturn.1.kt");
        }
        
        @TestMetadata("propertyAccessors.1.kt")
        public void testPropertyAccessors() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/propertyAccessors.1.kt");
        }
        
        @TestMetadata("simple.1.kt")
        public void testSimple() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/simple.1.kt");
        }
        
        @TestMetadata("compiler/testData/codegen/boxInline/nonLocalReturns/deparenthesize")
        public static class Deparenthesize extends AbstractBlackBoxCodegenTest {
            public void testAllFilesPresentInDeparenthesize() throws Exception {
                JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/nonLocalReturns/deparenthesize"), Pattern.compile("^(.+)\\.1.kt$"), true);
            }
            
            @TestMetadata("bracket.1.kt")
            public void testBracket() throws Exception {
                doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/deparenthesize/bracket.1.kt");
            }
            
            @TestMetadata("labeled.1.kt")
            public void testLabeled() throws Exception {
                doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/deparenthesize/labeled.1.kt");
            }
            
        }
        
        @TestMetadata("compiler/testData/codegen/boxInline/nonLocalReturns/tryFinally")
        public static class TryFinally extends AbstractBlackBoxCodegenTest {
            public void testAllFilesPresentInTryFinally() throws Exception {
                JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/nonLocalReturns/tryFinally"), Pattern.compile("^(.+)\\.1.kt$"), true);
            }
            
            @TestMetadata("external.1.kt")
            public void testExternal() throws Exception {
                doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/nonLocalReturns/tryFinally/external.1.kt");
            }
            
        }
        
        public static Test innerSuite() {
            TestSuite suite = new TestSuite("NonLocalReturns");
            suite.addTestSuite(NonLocalReturns.class);
            suite.addTestSuite(Deparenthesize.class);
            suite.addTestSuite(TryFinally.class);
            return suite;
        }
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/simple")
    public static class Simple extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInSimple() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/simple"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("classObject.1.kt")
        public void testClassObject() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/classObject.1.kt");
        }
        
        @TestMetadata("extension.1.kt")
        public void testExtension() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/extension.1.kt");
        }
        
        @TestMetadata("params.1.kt")
        public void testParams() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/params.1.kt");
        }
        
        @TestMetadata("rootConstructor.1.kt")
        public void testRootConstructor() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/rootConstructor.1.kt");
        }
        
        @TestMetadata("severalClosures.1.kt")
        public void testSeveralClosures() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/severalClosures.1.kt");
        }
        
        @TestMetadata("severalUsage.1.kt")
        public void testSeveralUsage() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/severalUsage.1.kt");
        }
        
        @TestMetadata("simpleDouble.1.kt")
        public void testSimpleDouble() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleDouble.1.kt");
        }
        
        @TestMetadata("simpleEnum.1.kt")
        public void testSimpleEnum() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleEnum.1.kt");
        }
        
        @TestMetadata("simpleGenerics.1.kt")
        public void testSimpleGenerics() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleGenerics.1.kt");
        }
        
        @TestMetadata("simpleInt.1.kt")
        public void testSimpleInt() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleInt.1.kt");
        }
        
        @TestMetadata("simpleLambda.1.kt")
        public void testSimpleLambda() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleLambda.1.kt");
        }
        
        @TestMetadata("simpleObject.1.kt")
        public void testSimpleObject() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/simpleObject.1.kt");
        }
        
        @TestMetadata("vararg.1.kt")
        public void testVararg() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/simple/vararg.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/special")
    public static class Special extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInSpecial() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/special"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("identityCheck.1.kt")
        public void testIdentityCheck() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/identityCheck.1.kt");
        }
        
        @TestMetadata("ifBranches.1.kt")
        public void testIfBranches() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/ifBranches.1.kt");
        }
        
        @TestMetadata("iinc.1.kt")
        public void testIinc() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/iinc.1.kt");
        }
        
        @TestMetadata("inlineChain.1.kt")
        public void testInlineChain() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/inlineChain.1.kt");
        }
        
        @TestMetadata("plusAssign.1.kt")
        public void testPlusAssign() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/plusAssign.1.kt");
        }
        
        @TestMetadata("stackHeightBug.1.kt")
        public void testStackHeightBug() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/special/stackHeightBug.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/trait")
    public static class Trait extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInTrait() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/trait"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("trait.1.kt")
        public void testTrait() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/trait/trait.1.kt");
        }
        
    }
    
    @TestMetadata("compiler/testData/codegen/boxInline/tryCatchFinally")
    public static class TryCatchFinally extends AbstractBlackBoxCodegenTest {
        public void testAllFilesPresentInTryCatchFinally() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("compiler/testData/codegen/boxInline/tryCatchFinally"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }
        
        @TestMetadata("tryCatch.1.kt")
        public void testTryCatch() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/tryCatchFinally/tryCatch.1.kt");
        }
        
        @TestMetadata("tryCatch2.1.kt")
        public void testTryCatch2() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/tryCatchFinally/tryCatch2.1.kt");
        }
        
        @TestMetadata("tryCatchFinally.1.kt")
        public void testTryCatchFinally() throws Exception {
            doTestMultiFileWithInlineCheck("compiler/testData/codegen/boxInline/tryCatchFinally/tryCatchFinally.1.kt");
        }
        
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("BlackBoxInlineCodegenTestGenerated");
        suite.addTestSuite(BlackBoxInlineCodegenTestGenerated.class);
        suite.addTestSuite(AnonymousObject.class);
        suite.addTestSuite(Builders.class);
        suite.addTestSuite(Capture.class);
        suite.addTestSuite(Complex.class);
        suite.addTestSuite(DefaultValues.class);
        suite.addTestSuite(LambdaClassClash.class);
        suite.addTestSuite(LambdaTransformation.class);
        suite.addTestSuite(LocalFunInLambda.class);
        suite.addTestSuite(NoInline.class);
        suite.addTest(NonLocalReturns.innerSuite());
        suite.addTestSuite(Simple.class);
        suite.addTestSuite(Special.class);
        suite.addTestSuite(Trait.class);
        suite.addTestSuite(TryCatchFinally.class);
        return suite;
    }
}
