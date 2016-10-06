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
package com.github.horrorho.furiouspotato.asn1;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Ahseya
 */
@Immutable
public enum ASN1Class {
    UNIVERSAL(0x00000000),
    APPLICATION(0x00400000),
    CONTEXT_SPECIFIC(0x00800000),
    PRIVATE(0x00C00000);

    public static ASN1Class map(int i) {
        return MAP.get(i & MASK);
    }

    public static final int MASK = 0x00C00000;

    private static final Map<Integer, ASN1Class> MAP
            = Stream.of(ASN1Class.values()).collect(toMap(ASN1Class::bits, Function.identity()));

    private final int bits;

    private ASN1Class(int bits) {
        this.bits = bits;
    }

    public int bits() {
        return bits;
    }
}
