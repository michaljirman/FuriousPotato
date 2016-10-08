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
import com.github.horrorho.furiouspotato.asn1template.ASN1Op;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpHeader;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpParse;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpTag;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpType;
import com.github.horrorho.furiouspotato.asn1template.ASN1OpTypeExtern;
import com.github.horrorho.furiouspotato.asn1template.ASN1Template;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Operations {

    public static final String HEADER = "LOCATION: < TT      , OFFSET  , PTR      >";

    private static final Logger logger = LoggerFactory.getLogger(Operations.class);

    private static final String PREFIX = "A1_OP_";

    public static String describe(ASN1Template template) {
        ASN1Op op = ASN1Op.map(template.tt())
                .orElseThrow(() -> new IllegalArgumentException("unsupported template op: " + template));
        logger.debug("describe() - op: {}", op);

        final String description;
        switch (op) {
            case HEADER:
                description = header(template);
                break;
            case TYPE:
                description = type(template);
                break;
            case TYPE_EXTERN:
                description = typeExtern(template);
                break;
            case TAG:
                description = tag(template);
                break;
            case PARSE:
                description = parse(template);
                break;
            case SEQOF:
                description = seqOf();
                break;
            case SETOF:
                description = setOf();
                break;
            case BMEMBER:
                description = bMember();
                break;
            case CHOICE:
                description = choice();
                break;
            default:
                throw new UnsupportedOperationException("unsupported op: " + op);
        }
        return PREFIX + description;
    }

    static String header(ASN1Template template) {
        ASN1OpHeader op = ASN1OpHeader.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.HEADER));

        return ASN1Op.HEADER + " ELEMENTS=" + template.ptr() + " " + Strings.join(op.flags());
    }

    static String type(ASN1Template template) {
        ASN1OpType op = ASN1OpType.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TYPE));

        return op.op() + " " + Strings.join(op.flags());
    }

    static String typeExtern(ASN1Template template) {
        ASN1OpTypeExtern op = ASN1OpTypeExtern.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TYPE_EXTERN));

        return op.op() + " " + Strings.join(op.flags());
    }

    static String tag(ASN1Template template) {
        ASN1OpTag op = ASN1OpTag.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.TAG));

        String universalTag = op.classType() == ASN1Class.UNIVERSAL
                ? "(" + ASN1Tag.map(op.tag()).map(Object::toString).orElse("UNKNOWN") + ")"
                : "";

        return op.op() + " TAG=" + op.tag() + universalTag + " " + op.method() + " " + op.classType() + " "
                + Strings.join(op.flags());
    }

    static String parse(ASN1Template template) {
        ASN1OpParse op = ASN1OpParse.map(template.tt())
                .orElseThrow(() -> new IllegalStateException("expected op: " + ASN1Op.PARSE));

        return op.op() + " " + op.type() + " " + Strings.join(op.flags());
    }

    static String seqOf() {
        return ASN1Op.SEQOF.toString();
    }

    static String setOf() {
        return ASN1Op.SETOF.toString();
    }

    static String bMember() {
        return ASN1Op.BMEMBER.toString();
    }

    static String choice() {
        return ASN1Op.CHOICE.toString();
    }
}
