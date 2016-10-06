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
import java.util.Optional;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ASN1OpTypeExtern {

    public static Optional<ASN1OpTypeExtern> map(int tt) {
        return ASN1Op.map(tt)
                .filter(u -> u == ASN1Op.TYPE_EXTERN)
                .map(u -> opType(tt));
    }

    static ASN1OpTypeExtern opType(int tt) {
        List<ASN1Flag> flags = ASN1Flag.map(tt);
        return new ASN1OpTypeExtern(flags);
    }

    private final List<ASN1Flag> flags;

    public ASN1OpTypeExtern(List<ASN1Flag> flags) {
        this.flags = new ArrayList<>(flags);
    }

    public List<ASN1Flag> flags() {
        return new ArrayList<>(flags);
    }

    public ASN1Op op() {
        return ASN1Op.TYPE_EXTERN;
    }

    @Override
    public String toString() {
        return "ASN1OpTypeExtern{" + "flags=" + flags + '}';
    }
}
