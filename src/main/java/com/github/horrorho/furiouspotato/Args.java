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

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Args {

    public static Optional<Args> parse(String[] args) {
        try {
            if (args.length == 0) {
                usage();
                tryHelp();
                return Optional.empty();
            }
            if (isHelp(args)) {
                help();
                return Optional.empty();
            }

            Args arguments = args(args);
            System.out.println("FILE    : " + arguments.file());
            System.out.println("DELTA   : 0x" + Hex.integer(arguments.delta()));
            System.out.println("LOCATION: 0x" + Hex.integer(arguments.address()));
            System.out.println("");
            return Optional.of(arguments);

        } catch (IllegalArgumentException ex) {
            System.out.println("Argument error: " + ex.getMessage());
            usage();
            tryHelp();
            return Optional.empty();
        }
    }

    static void tryHelp() {
        System.out.println("Try 'FuriousPotato --help' for more information.");
    }

    static void usage() {
        System.out.println("Usage: FuriousPotato FILE DELTA LOCATION");
    }

    static void help() {
        usage();
        System.out.println("HorrorHo's Furious Potato. Heimdal ASN1 template ripper.");
        System.out.println("");
        System.out.println("     --help     display this help and exit");
        System.out.println("");
        System.out.println("FILE<file>    input file");
        System.out.println("DELTA<hex>    the delta in bytes that translates the executables asn1_template");
        System.out.println("              data segment location to it's corresponding file offset");
        System.out.println("LOCATION<hex> the data segment location of the top asn1_template");
        System.out.println("");
        System.out.println("Example:");
        System.out.println("FuriousPotato PCS.dll -10001800 10068980");
        System.out.println("");
        System.out.println("Example details:");
        System.out.println("Rips KeySet from Apple's iCloud PCS.dll (Version: 15.0.0.10.1 CRC32: E7533650)");
        System.out.println("KeySet asn1_template executable location 0x10068980");
        System.out.println("KeySet asn1_template file offset 0x00067180");
        System.out.println("Delta = 0x00067180 - 0x10068980 = -0x10001800");
        System.out.println("FILE=PCS.dll DELTA=-10001800 LOCATION=10068980");
        System.out.println("");
        System.out.println("Project home:");
        System.out.println("https://github.com/horrorho/FuriousPotato");
    }

    static boolean isHelp(String[] args) {
        return Arrays.asList(args)
                .stream()
                .map(u -> u.toLowerCase(Locale.US))
                .anyMatch(u -> u.equals("--help"));
    }

    static Args args(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("missing arguments");
        }
        if (args.length > 3) {
            throw new IllegalArgumentException("too many arguments");
        }
        String file = args[0];
        int delta = integer(args[1]);
        int address = integer(args[2]);
        return new Args(file, delta, address);
    }

    static int integer(String hex) {
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException ex) {
            logger.debug("integer() - ex: {}", ex.getMessage());
            throw new IllegalArgumentException("bad hex: " + hex);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(Args.class);

    private final String file;
    private final int delta;
    private final int address;

    public Args(String file, int delta, int address) {
        this.file = Objects.requireNonNull(file);
        this.delta = delta;
        this.address = address;
    }

    public String file() {
        return file;
    }

    public int delta() {
        return delta;
    }

    public int address() {
        return address;
    }

    @Override
    public String toString() {
        return "Args{"
                + "file=" + file
                + ", delta=0x" + Hex.integer(delta)
                + ", address=0x" + Hex.integer(address)
                + '}';
    }
}
