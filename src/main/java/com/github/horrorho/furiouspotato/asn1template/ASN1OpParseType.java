/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
