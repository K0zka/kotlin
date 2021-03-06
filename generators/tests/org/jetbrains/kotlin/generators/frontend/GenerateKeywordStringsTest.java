/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.generators.frontend;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.testFramework.UsefulTestCase;

import java.io.IOException;

public class GenerateKeywordStringsTest extends UsefulTestCase {
    public void testGeneratedDataIsUpToDate() throws IOException {
        String text = GenerateKeywordStrings.generate();
        assertEquals("Contents differ. Regenerate " + GenerateKeywordStrings.class.getName(),
                     StringUtil.convertLineSeparators(text),
                     FileUtil.loadFile(GenerateKeywordStrings.DEST_FILE, true));
    }
}
