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
import com.github.horrorho.furiouspotato.asn1.ASN1Class;
import com.github.horrorho.furiouspotato.asn1.ASN1Method;
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
public class ASN1OpTag {
    // https://github.com/heimdal/heimdal/blob/master/lib/asn1/asn1-template.h
    // tag:
    // 0..20 tag
    // 21     type
    // 22..23 class
    // 24..27 flags
    // 28..31 op

    public static Optional<ASN1OpTag> map(int tt) {
        return ASN1Op.map(tt)
                .filter(u -> u == ASN1Op.TAG)
                .map(u -> opTag(tt));
    }

    static ASN1OpTag opTag(int tt) {
        int tag = tt & TAG_MASK;
        ASN1Method method = ASN1Method.map(tt);
        ASN1Class classType = ASN1Class.map(tt);
        List<ASN1Flag> flags = ASN1Flag.map(tt);
        return new ASN1OpTag(tag, method, classType, flags);
    }

    private static final int TAG_MASK = 0x001FFFFF;

    private final int tag;
    private final ASN1Method method;
    private final ASN1Class classType;
    private final List<ASN1Flag> flags;

    public ASN1OpTag(int tag, ASN1Method method, ASN1Class classType, List<ASN1Flag> flags) {
        this.tag = tag;
        this.method = Objects.requireNonNull(method);
        this.classType = Objects.requireNonNull(classType);
        this.flags = new ArrayList<>(flags);
    }

    public int tag() {
        return tag;
    }

    public ASN1Method method() {
        return method;
    }

    public ASN1Class classType() {
        return classType;
    }

    public List<ASN1Flag> flags() {
        return new ArrayList<>(flags);
    }

    public ASN1Op op() {
        return ASN1Op.TAG;
    }

    @Override
    public String toString() {
        return "ASN1OpTag{" + "tag=" + tag + ", type=" + method + ", classType=" + classType + ", flags=" + flags + '}';
    }
}
