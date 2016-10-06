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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.joining;
import java.util.stream.IntStream;
import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Engine {

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    public static void execute(String file, int delta, int address) {
        try (FileChannel in = new RandomAccessFile(file, "r").getChannel()) {
            Mem mem = mem(in, delta);
            mem.address(address);
            execute(mem);

        } catch (IOException ex) {
            logger.debug("main () - IOException: {}", ex);
            System.out.println("IOError: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.debug("main () - IllegalArgumentException: {}", ex);
            System.out.println("Rip failed: " + ex.getMessage());
        } catch (RuntimeException ex) {
            logger.error("main() - error: {}", ex);
        }
    }

    static void execute(Mem mem) {
        peek(mem);
        System.out.println("");
        List<ASN1Template> templates = rip(mem);
        operations(templates);
        System.out.println("");
        inform(templates);
        System.out.println("");
        informOnly(templates);
    }

    static void peek(Mem mem) {
        int pos = mem.address();
        String bytes = IntStream.range(0, 4)
                .mapToObj(i -> mem.integer())
                .map(Hex::integer)
                .collect(joining(" "));
        mem.address(pos);

        System.out.println("ADDRESS PEEK:");
        System.out.println(Hex.integer(pos) + " : " + bytes);
    }

    static List<ASN1Template> rip(Mem mem) {
        List<ASN1Template> templates = new ArrayList<>();
        try {
            Ripper.apply(mem, templates);
            return templates;
        } catch (IllegalArgumentException ex) {
            System.out.println("*** FAILED ***");
            System.out.println("TRACE:");
            operations(templates);
            throw ex;
        }
    }

    static void operations(List<ASN1Template> templates) {
        System.out.println("TEMPLATES (asn1_template):");
        System.out.println(Operations.HEADER + " : DESCRIPTION");
        templates.stream()
                .map(t -> Templates.format(t) + " : " + Operations.describe(t))
                .forEach(System.out::println);
    }

    static void inform(List<ASN1Template> templates) {
        System.out.println(Informer.HEADER);
        Informer.apply(templates)
                .forEach(System.out::println);
    }

    static void informOnly(List<ASN1Template> templates) {
        // Potentially brittle, relies on FLOW : INFORM format;
        System.out.println(strip(Informer.HEADER));
        Informer.apply(templates)
                .stream()
                .map(Engine::strip)
                .forEach(System.out::println);
    }

    static String strip(String string) {
        int index = string.indexOf(":");
        if (index == -1) {
            logger.debug("strip() - bad format: {}", string);
        }
        return string.substring(index + 2, string.length());
    }

    static Mem mem(FileChannel in, int delta) throws IOException {
        MappedByteBuffer buffer = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        Mem mem = new Mem(buffer, delta);
        return mem;
    }
}
