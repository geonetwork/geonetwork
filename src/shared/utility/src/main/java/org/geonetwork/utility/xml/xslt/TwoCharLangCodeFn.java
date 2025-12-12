/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.utility.xml.xslt;

import com.neovisionaries.i18n.LanguageCode;
import java.util.Locale;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.geonetwork.constants.Geonet;
import org.testcontainers.shaded.com.google.common.base.Splitter;
import org.testcontainers.shaded.com.google.common.collect.Iterables;

public class TwoCharLangCodeFn extends ExtensionFunctionDefinition {
    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName(XslFn.PREFIX, XslFn.URI, "twoCharLangCode");
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        // TODO: Review optional parameter for default value
        return new SequenceType[] {SequenceType.SINGLE_STRING, SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {
            @Override
            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
                String iso3LangCode = arguments[0].head().getStringValue();
                String defaultValue =
                        (arguments.length > 1) ? arguments[1].head().getStringValue() : null;

                if (iso3LangCode == null || iso3LangCode.length() == 0) {
                    if (defaultValue != null) {
                        return StringValue.makeStringValue(defaultValue);
                    } else {
                        iso3LangCode = Geonet.DEFAULT_LANGUAGE;
                    }
                }
                String iso2LangCode = null;

                // Catch language entries longer than 3 characters with a semicolon
                if (iso3LangCode.length() > 3 && (iso3LangCode.indexOf(';') != -1)) {
                    // This will extract text similar to the following "fr;CAN", "fra;CAN", "fr ;CAN"
                    // In the case of "fr;CAN",  fr would be extracted even though it is not a 3 char code - but that is
                    // ok because LanguageCode.getByCode supports 2 and 3 char codes.
                    iso3LangCode = Iterables.get(Splitter.on(';').split(iso3LangCode), 0)
                            .trim();
                }

                LanguageCode languageCode = LanguageCode.getByCode(iso3LangCode.toLowerCase(Locale.getDefault()));
                if (languageCode != null) {
                    iso2LangCode = languageCode.name();
                }

                // Triggers when the language can't be matched to a code
                if (iso2LangCode == null) {
                    return StringValue.makeStringValue(iso3LangCode.substring(0, 2));
                } else {
                    return StringValue.makeStringValue(iso2LangCode);
                }
            }
        };
    }
}
