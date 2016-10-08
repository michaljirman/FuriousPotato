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
package com.github.horrorho.furiouspotato.asn1template;

import com.github.horrorho.furiouspotato.asn1.ASN1Flag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ASN1OpParse {
    // https://github.com/heimdal/heimdal/blob/master/lib/asn1/asn1-template.h
    // parse:
    //  0..11 type
    // 12..23 unused
    // 24..27 flags
    // 28..31 op

    public static Optional<ASN1OpParse> map(int tt) {
        return ASN1Op.map(tt)
                .filter(u -> u == ASN1Op.PARSE)
                .flatMap(u -> ASN1OpParse.opParse(tt));
    }

    static Optional<ASN1OpParse> opParse(int tt) {
        return ASN1OpParseType.map(tt).map(u -> opParse(tt, u));
    }

    static ASN1OpParse opParse(int tt, ASN1OpParseType type) {
        List<ASN1Flag> flags = ASN1Flag.map(tt);
        return new ASN1OpParse(type, flags);
    }

    private final ASN1OpParseType type;
    private final List<ASN1Flag> flags;

    public ASN1OpParse(ASN1OpParseType type, List<ASN1Flag> flags) {
        this.type = Objects.requireNonNull(type);
        this.flags = new ArrayList<>(flags);
    }

    public ASN1OpParseType type() {
        return type;
    }

    public List<ASN1Flag> flags() {
        return new ArrayList<>(flags);
    }

    public ASN1Op op() {
        return ASN1Op.PARSE;
    }
} 