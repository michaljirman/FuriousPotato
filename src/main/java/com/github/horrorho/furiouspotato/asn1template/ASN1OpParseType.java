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
import java.util.stream.Stream;
import net.jcip.annotations.Immutable;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author Ahseya
 */
@Immutable
public enum ASN1OpParseType {
    A1T_IMEMBER,
    A1T_HEIM_INTEGER,
    A1T_INTEGER,
    A1T_UNSIGNED,
    A1T_GENERAL_STRING,
    A1T_OCTET_STRING,
    A1T_OCTET_STRING_BER,
    A1T_IA5_STRING,
    A1T_BMP_STRING,
    A1T_UNIVERSAL_STRING,
    A1T_PRINTABLE_STRING,
    A1T_VISIBLE_STRING,
    A1T_UTF8_STRING,
    A1T_GENERALIZED_TIME,
    A1T_UTC_TIME,
    A1T_HEIM_BIT_STRING,
    A1T_BOOLEAN,
    A1T_OID,
    A1T_TELETEX_STRING;

    public static Optional<ASN1OpParseType> map(int tt) {
        return Optional.ofNullable(MAP.get(tt & MASK));
    }

    public static final int MASK = 0x00000FFF;

    private static final Map<Integer, ASN1OpParseType> MAP
            = Stream.of(ASN1OpParseType.values())
            .collect(toMap(u -> u.ordinal(), Function.identity()));
}
