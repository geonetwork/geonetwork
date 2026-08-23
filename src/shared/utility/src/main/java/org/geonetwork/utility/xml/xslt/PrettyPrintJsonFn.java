/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.utility.xml.xslt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/** Extension function to pretty print JSON text. */
public class PrettyPrintJsonFn extends ExtensionFunctionDefinition {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName(XslFn.PREFIX, XslFn.URI, "prettyPrintJson");
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[] {SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 0;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 1;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {
            @Override
            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
                String value = "";
                if (arguments.length > 0) {
                    var item = arguments[0].head();
                    if (item != null) {
                        value = item.getStringValue();
                    }
                }

                if (value.isBlank()) {
                    return StringValue.makeStringValue("");
                }

                try {
                    JsonNode json = MAPPER.readTree(value);
                    return StringValue.makeStringValue(
                            MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json));
                } catch (JsonProcessingException e) {
                    throw new XPathException("Failed to pretty print JSON", e);
                }
            }
        };
    }
}
