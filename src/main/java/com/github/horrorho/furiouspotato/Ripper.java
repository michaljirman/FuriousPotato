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

import com.github.horrorho.furiouspotato.asn1template.ASN1Template;
import com.github.horrorho.furiouspotato.asn1template.ASN1Op;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Ripper {

    private static final Logger logger = LoggerFactory.getLogger(Ripper.class);

    public static List<ASN1Template> apply(Mem mem) {
        ArrayList<ASN1Template> list = new ArrayList<>();
        apply(mem, list);
        return list;
    }

    public static void apply(Mem mem, List<ASN1Template> list) {
        header(mem, list::add);
    }

    static void header(Mem mem, Consumer<ASN1Template> templates) {
        ASN1Template header = template(mem);
        logger.debug("header() - header: {}", header);
        templates.accept(header);

        int base = mem.address();
        for (int i = 0, elements = header.ptr(); i < elements; i++) {
            mem.address(base + i * ASN1Template.SIZE);
            element(mem, templates);
        }
    }

    static void element(Mem mem, Consumer<ASN1Template> templates) {
        ASN1Template element = template(mem);
        logger.debug("element() - element: {}", element);
        templates.accept(element);

        ASN1Op op = ASN1Op.map(element.tt())
                .orElseThrow(() -> new IllegalArgumentException("unsupported template op: " + element));
        logger.debug("element() - op: {}", op);

        switch (op) {
            case PARSE:
            case TYPE_EXTERN:
                break;
            default:
                int ptr = element.ptr();
                if (ptr != 0) {
                    mem.address(ptr);
                    header(mem, templates);
                }
        }
    }

    static final ASN1Template template(Mem mem) {
        return new ASN1Template(mem.address(), mem.integer(), mem.integer(), mem.integer());
    }
}
