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

import net.jcip.annotations.Immutable;

/**
 *
 * @author Ahseya
 */
@Immutable
public final class ASN1Template {

    public static final int SIZE = 0x0c;

    private final int address;
    private final int tt;
    private final int offset;
    private final int ptr;

    public ASN1Template(int address, int tt, int offset, int ptr) {
        this.address = address;
        this.tt = tt;
        this.offset = offset;
        this.ptr = ptr;
    }

    public int address() {
        return address;
    }

    public int tt() {
        return tt;
    }

    public int offset() {
        return offset;
    }

    public int ptr() {
        return ptr;
    }

    @Override
    public String toString() {
        return "ASN1Template{"
                + "address=0x" + Integer.toHexString(address)
                + ", tt=0x" + Integer.toHexString(tt)
                + ", offset=0x" + Integer.toHexString(offset)
                + ", ptr=0x" + Integer.toHexString(ptr)
                + '}';
    }
}
