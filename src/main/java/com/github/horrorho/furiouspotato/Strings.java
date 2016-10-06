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

import java.util.List;
import net.jcip.annotations.Immutable;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class Strings {

    public static String integer(int i) {
        return String.format("%08x", i);
    }

//    public static String indent(int indent, List<String> strings) {
//        String joined = join(strings);
//        return Strings.indent(indent, joined);
//    }
//    public static String indent(int indent, String string) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < indent; i++) {
//            sb.append(' ');
//        }
//        sb.append(string);
//        return sb.toString();
//    }

    public static String indent(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String join(List<? extends Object> list) {
        return list.stream()
                .map(Object::toString)
                .filter(u -> !u.isEmpty())
                .collect(joining(" "));
    }
}
