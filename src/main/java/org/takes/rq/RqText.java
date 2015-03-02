/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.takes.Request;

/**
 * Request decorator, for plain text operations.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@EqualsAndHashCode(of = { "origin", "encoding" })
public final class RqText implements Request {

    /**
     * Original request.
     */
    private final transient Request origin;

    /**
     * Default encoding.
     */
    private final transient String encoding;

    /**
     * Ctor.
     * @param req Original request
     */
    public RqText(final Request req) {
        this(req, Charset.defaultCharset().name());
    }

    /**
     * Ctor.
     * @param req Original request
     * @param enc Default encoding
     */
    public RqText(final Request req, final String enc) {
        this.origin = req;
        this.encoding = enc;
    }

    @Override
    public List<String> head() throws IOException {
        return this.origin.head();
    }

    @Override
    public InputStream body() throws IOException {
        return this.origin.body();
    }

    /**
     * Read body as string.
     * @return Text body
     * @throws IOException If fails
     */
    public String text() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final InputStream input = this.body();
        try {
            while (true) {
                final int data = input.read();
                if (data < 0) {
                    break;
                }
                baos.write(data);
            }
            return new String(baos.toByteArray(), this.encoding);
        } finally {
            input.close();
        }
    }

}
