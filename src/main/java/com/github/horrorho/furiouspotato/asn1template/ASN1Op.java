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

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public enum ASN1Op {
    HEADER(0x00000000),
    TYPE(0x10000000),
    TYPE_EXTERN(0x20000000),
    TAG(0x30000000),
    PARSE(0x40000000),
    SEQOF(0x50000000),
    SETOF(0x60000000),
    BMEMBER(0x70000000),
    CHOICE(0x80000000);

    public static Optional<ASN1Op> map(int tt) {
        return Optional.ofNullable(MAP.get(tt & MASK));
    }

    public static final int MASK = 0xF0000000;

    private static final Map<Integer, ASN1Op> MAP
            = Stream.of(ASN1Op.values())
            .collect(Collectors.toMap(ASN1Op::bits, Function.identity()));

    private final int bits;

    private ASN1Op(int bits) {
        this.bits = bits;
    }

    public int bits() {
        return bits;
    }
}
