/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.utility.xml.xslt;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.geonetwork.utility.ApplicationContextProvider;
import org.geonetwork.utility.schemas.CodeListTranslator;

/** Extension function to get the translation of a code from a codelist. */
public class GetCodeListTranslationFn extends ExtensionFunctionDefinition {
    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName(XslFn.PREFIX, XslFn.URI, "getCodelistTranslation");
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] {
            SequenceType.SINGLE_STRING,
            SequenceType.SINGLE_STRING,
            SequenceType.SINGLE_STRING,
            SequenceType.SINGLE_STRING
        };
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {
            @Override
            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
                String schema = arguments[0].head().getStringValue();
                String codelist = arguments[1].head().getStringValue();
                String value = arguments[2].head().getStringValue();
                String language = arguments[3].head().getStringValue();

                return StringValue.makeStringValue(ApplicationContextProvider.getApplicationContext()
                        .getBean(CodeListTranslator.class)
                        .getTranslation(schema, codelist, value, language));
            }
        };
    }
}
