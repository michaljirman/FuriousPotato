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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Ahseya
 */
@Immutable
public enum ASN1Tag {
    // Apple CoreCrypto: https://developer.apple.com/security/
    // ccasn1/corecrypto/ccasn1.h
    EOL(0x00),
    BOOLEAN(0x01),
    INTEGER(0x02),
    BIT_STRING(0x03),
    OCTET_STRING(0x04),
    NULL(0x05),
    OBJECT_IDENTIFIER(0x06),
    OBJECT_DESCRIPTOR(0x07),
    REAL(0x09),
    ENUMERATED(0x0A),
    EMBEDDED_PDV(0x0B),
    UTF8STRING(0x0C),
    SEQUENCE(0x10),
    SET(0x11),
    NUMERIC_STRING(0x12),
    PRINTABLE_STRING(0x13),
    T61_STRING(0x14), // TELETEX_STRING
    VIDEOTEX_STRING(0x15),
    IA5_STRING(0x16),
    UTC_TIME(0x17),
    GENERALIZED_TIME(0x18),
    GRAPHIC_STRING(0x19),
    VISIBLE_STRING(0x1A),
    GENERAL_STRING(0x1B),
    UNIVERSAL_STRING(0x1C),
    BMP_STRING(0x1E),
    HIGH_TAG_NUMBER(0x1F);

    public static Optional<ASN1Tag> map(int tt) {
        return Optional.ofNullable(MAP.get(tt & MASK));
    }

    public static final int MASK = 0x0000001F;

    private static final Map<Integer, ASN1Tag> MAP
            = Stream.of(ASN1Tag.values()).collect(toMap(ASN1Tag::bits, Function.identity()));

    private final int bits;

    private ASN1Tag(int bits) {
        this.bits = bits;
    }

    public int bits() {
        return bits;
    }
}
