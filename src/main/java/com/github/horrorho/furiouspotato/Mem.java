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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Objects;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author Ahseya
 */
@NotThreadSafe
public final class Mem {

    private final ByteBuffer buffer;
    private final int delta;

    public Mem(ByteBuffer buffer, int delta) {
        this.buffer = Objects.requireNonNull(buffer);
        this.delta = delta;
    }

    public int integer() {
        try {
            return buffer.getInt();
        } catch (BufferUnderflowException ex) {
            throw new IllegalArgumentException("address out of bounds: 0x" + Hex.integer(address()), ex);
        }
    }

    public int address() {
        return buffer.position() - delta;
    }

    public Mem address(int address) {
        try {
            buffer.position(address + delta);
            return this;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("address out of bounds: 0x" + Hex.integer(address), ex);
        }
    }

    @Override
    public String toString() {
        return "Mem{"
                + "buffer=" + buffer
                + ", delta=" + Integer.toHexString(delta)
                + '}';
    }
}
