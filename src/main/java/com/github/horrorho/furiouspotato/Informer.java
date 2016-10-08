/*
 * The MIT License
 *
 * Copyright 2016 Ahseya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.horrorho.furiouspotato;

import com.github.horrorho.furiouspotato.asn1.ASN1Class;
import com.github.horrorho.furiouspotato.asn1.ASN1Tag;
import com.github.horrorho.furiouspotato.asn1template.ASN1Template;
import com.github.horrorho.furiouspotato.asn1template.ASN1Op;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpHeader;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpTag;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpType;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpTypeExtern;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpHeaderFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Informer {

    public static final String HEADER = "LOCATION > TARGET   : INFORM (experimental):";

    private static final Logger logger = LoggerFactory.getLogger(Informer.class);

    public static List<String> apply(List<ASN1Template> templates) {
        ArrayList<String> list = new ArrayList<>();
        header(templates.iterator(), list, "", 1);
        return list;
    }

    static void header(Iterator<ASN1Template> it, List<String> list, String suffix, int indentation) {
        if (!it.hasNext()) {
            return;
        }
        ASN1Template template = it.next();
        logger.debug("header() - template: {}", template);

        ASN1OpHeader op = ASN1OpHeader.map(template.tt())
                .orElseThrow(() -> new IllegalArgumentException("expected op: " + ASN1Op.HEADER));
        logger.debug("header() - op: {}", op);

        List<ASN1OpHeaderFlag> flags = op.flags();
        if (flags.contains(ASN1OpHeaderFlag.PRESERVE)) {
            logger.warn("header() - unsupported flag: {} template: {} description: {}",
                    ASN1OpHeaderFlag.PRESERVE, Templates.format(template), Operations.describe(template));
        }

        int elements = template.ptr();
        if (elements > 1) {
            String append = " (" + elements + ')';
            listAppendToLast(list, append);
        }

        for (int i = 0; i < elements; i++) {
            element(it, list, suffix, indentation + 1);
        }

        if (flags.contains(ASN1OpHeaderFlag.ELLIPSIS)) {
            listAddFormatted(list, indentation + 1, template, "...");
        }
    }

    static void element(Iterator<ASN1Template> it, List<String> list, String suffix, int indentation) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException("missing element");
        }
        ASN1Template template = it.next();
        logger.debug("element() - element: {}", template);

        ASN1Op op = ASN1Op.map(template.tt()).get();
        logger.debug("element() - op: {}", op);

        switch (op) {
            case HEADER:
                throw new IllegalArgumentException("unexpected op: " + ASN1Op.HEADER);
            case TYPE:
                header(it, list, type(template), indentation - 1);
                break;
            case PARSE:
                break;
            case TYPE_EXTERN:
                String typeExtern = typeExtern(template);
                listAddFormatted(list, indentation, template, typeExtern, suffix);
                break;
            case TAG:
                String tag = tag(template);
                listAddFormatted(list, indentation, template, tag, suffix);
                header(it, list, "", indentation);
                break;
            default:
                header(it, list, "", indentation - 1);
        }
    }

    static String type(ASN1Template template) {
        ASN1OpType op = ASN1OpType.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TYPE));

        return Strings.join(op.flags());
    }

    static String typeExtern(ASN1Template template) {
        ASN1OpTypeExtern op = ASN1OpTypeExtern.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TYPE_EXTERN));

        return "EXTERN 0x" + Hex.integer(template.ptr()) + Strings.join(op.flags());
    }

    static String tag(ASN1Template template) {
        ASN1OpTag opTag = ASN1OpTag.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TAG));

        String tag = tag(opTag);
        String flags = Strings.join(opTag.flags());
        List<String> list = Arrays.asList(tag, flags);
        return Strings.join(list);
    }

    static String tag(ASN1OpTag opTag) {
        return opTag.classType() == ASN1Class.UNIVERSAL
                ? tagUniversal(opTag)
                : tagNonUniversal(opTag);
    }

    static String tagUniversal(ASN1OpTag opTag) {
        return ASN1Tag.map(opTag.tag()).map(Object::toString).orElse("UNKNOWN_TAG");
    }

    static String tagNonUniversal(ASN1OpTag opTag) {
        return opTag.classType().toString().toLowerCase().substring(0, 4) + '[' + opTag.tag() + ']';
    }

    static void listAppendToLast(List<String> list, String string) {
        if (list.isEmpty()) {
            list.add("");
        }
        int i = list.size() - 1;
        list.set(i, list.get(i) + string);
    }

    static void listAddFormatted(List<String> list, int indentation, ASN1Template template, String... string) {
        List<String> strings = Arrays.asList(string);
        String join = Strings.join(strings);
        listAddFormatted(list, indentation, template, join);
    }

    static void listAddFormatted(List<String> list, int indentation, ASN1Template template, String string) {
        String indent = Strings.indent(indentation);
        String flow = flow(template);
        String line = flow + " : " + indent + string;
        list.add(line);
    }

    static String flow(ASN1Template template) {
        boolean isHeader = ASN1Op.map(template.tt()).get() == ASN1Op.HEADER;
        String address = Hex.integer(template.address());
        String ptr = isHeader ? "N/A     " : Hex.integer(template.ptr());
        return address + " > " + ptr;
    }
}
// TODO PRESERVE
